#coding=utf-8
'''
Created on 2013-3-8

@author: Enix Yu
'''

from java.lang import *
from java.sql import *
from org.apache.log4j import Logger
#from log import Logger
import mylog
import config
import traceback
import sys

class DBUtils:
    
    dbconfig = config.dbconfig 
        
    def getStatement(self):
        if not self.__stmt__:
            self.__stmt__ = self.conn.createStatement()
    
    def __init__(self, dbType):
        self.logger = Logger.getRootLogger()
        self.conn = None
        self.__stmt__ = None
        self.__rs__ = None
        config = self.dbconfig[dbType]        
        if config:
            try:                
                Class.forName(config["DRIVER"]).newInstance()
                self.conn = DriverManager.getConnection(config["URL"], config["USER"], config["PASSWD"])
            except SQLException ,e:
                self.logger.error("(db.py => __init__)Get %s connection error! \n %s" % (dbType,str(e)))
                raise Exception
            
    def close(self):
        try:
            self.__rs__.close()
        except:
            pass
        try:
            self.__stmt__.close()
        except:
            pass        
        try:
            self.conn.close()
        except:
            pass    
    
    # Query for small table    
    def sQuery(self, sql):        
        self.getStatement()
        self.__rs__ = self.__stmt__.executeQuery(sql)        
        result = []
        while(self.__rs__.next()):
            row ={}
            rsmd = self.__rs__.getMetaData()
            for i in range(rsmd.getColumnCount()):
                row[rsmd.getColumnLabel(i+1)] = self.__rs__.getString(i+1)
            result.append(row)
                
        return result
    
    # Restrict the fetch size for big table
    def tQuery(self, sql, count):
        self.getStatement()
        self.__stmt__.setFetchSize(count)
        self.__rs__ = self.__stmt__.executeQuery(sql)  
    
    # Query for big table
    def bQuery(self, sql):
        self.tQuery(sql, 0)
    
    # Query for big table, should be run after bQuery()
    def next(self):
        result = []
        if(self.__rs__.next()):
            row ={}
            rsmd = self.__rs__.getMetaData()
            for i in range(rsmd.getColumnCount()):
                row[rsmd.getColumnLabel(i+1)] = self.__rs__.getString(i+1)
            result.append(row)
        
        return result
    
    # Perform insert/update/delete for one sql
    def execute(self, sql):
        try:
            self.getStatement()
            self.__stmt__.executeUpdate(sql)
            return True
        except Exception , e:
            self.logger.error("(db.py => execute)Execute SQL %s error! \n %s" % (sql, str(e)))
            return False
    
    #===============================
    # sql : insert/update template
    # param: list [{r1}, {r2}, ...]
    #===============================
    def executeMany(self, sql, param):    
        try:
            prestmt = self.conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY)
            for r in param:
                i = 1
                for c in r:                     
                    prestmt.setString(i, c)                    
                    i += 1
                prestmt.addBatch()
            prestmt.executeBatch()
            prestmt.close()
            return True
        except Exception, e:
            self.logger.error("(db.py => executeMany)Execute sql error! \n %s"  % str(e))
            self.logger.error("sql: %s; params %s" % (sql, str(r)))
            #je = sys.exc_info()[1]            
            #je.printStackTrace()  # java part 
            return False
    
    # Truncate table        
    def truncateTbl(self, tableName):
        try:
            self.execute("TRUNCATE TABLE %s" % tableName)
        except:
            pass         
        
    def is_tbl_exist(self, tableName):
        try:
            meta = self.conn.getMetaData()
            __scheme__ = tableName.split(".")[0] if len(tableName.split(".")) == 2 else None
            __table__ = tableName.split(".")[1] if len(tableName.split(".")) == 2 else tableName
            rs = meta.getTables(None, __scheme__, __table__, ["TABLE"])
            if (rs.next()):
                return True
            else:
                return False
        except Exception, e:
            self.logger.error("(db.py => is_tbl_exist) Check if table %s exist failed \n %s" % (tableName, str(e)))
            return False        
        
    def get_tbl_cols(self, tableName):
        cols = []
        __scheme__ = tableName.split(".")[0] if len(tableName.split(".")) == 2 else None
        __table__ = tableName.split(".")[1] if len(tableName.split(".")) == 2 else None
        try:
            if(self.is_tbl_exist(tableName)):
                rs = self.conn.getMetaData().getColumns(None, __scheme__, __table__, "%")
                while(rs.next()):
                    col = {}
                    col["col_name"] = rs.getString("COLUMN_NAME")
                    col["col_type"] = rs.getInt("DATA_TYPE")
                    col["col_size"] = rs.getInt("COLUMN_SIZE")
                    col["col_digit"] = rs.getInt("DECIMAL_DIGITS")
                    cols.append(col)
        except Exception, e:
            self.logger.error("(db.py => get_tbl_cols) Get table %s  column definition failed \n %s" % (tableName, str(e)))
        
        return cols
                
    def createTable(self, tableName, cols_def, dbType):
        flag = False
        sql = ""
        if(dbType == 'MSSQL'):
            sql = "CREATE TABLE [dbo].[%s] (dbs_rowid int IDENTITY(1,1) NOT NULL," % tableName
        elif(dbType == 'MYSQL'):
            sql = "CREATE TABLE `%s` (`dbs_rowid` BIGINT NOT NULL AUTO_INCREMENT," % tableName
        else:
            return flag
        
        if(not self.is_tbl_exist(tableName)):
            arr_cols = []
            for col in cols_def:                                  
                if(col["col_type"] == 1): #Char
                    #s_cols = "[%s] [NVARCHAR](%d)"
                    s_cols = config.syntax[dbType + ".char"]
                    arr_cols.append(s_cols % (col["col_name"], col["col_size"]))
                elif((col["col_type"] in (12,2,3,4)) and col["col_digit"] == 0): #integer
                    #s_cols = "[%s] [INT]"
                    s_cols = config.syntax[dbType + ".int"]
                    arr_cols.append(s_cols % col["col_name"])
                elif((col["col_type"] in (12,2,3,4)) and col["col_digit"] != 0): #decimal
                    #s_cols = "[%s] [DECIMAL](%d, %d)"
                    s_cols = config.syntax[dbType + ".decimal"]
                    arr_cols.append(s_cols % (col["col_name"], col["col_size"], col["col_digit"]))
                elif(col["col_type"] == 91): #Date
                    s_cols = config.syntax[dbType + ".date"]
                    arr_cols.append(s_cols % col["col_name"])
                else:
                    break
            arr_cols.append(config.syntax[dbType + ".primary"])
            sql += ",".join(arr_cols) + ")"
            try:
                flag = self.execute(sql)
                #self.logger.info("db.py => Table def %s" % str(cols_def)) #For debug use only
                self.logger.info("db.py => createTable %s \n %s" % (tableName, sql))
            except Exception, e:
                self.logger.error("(db.py => createTable) Create table %s failed \n %s" % (tableName, str(e)))
                flag = False
                
        return flag
                                
                
        
        
        
        
        
        
        
        
        
        
        
        
        
        
