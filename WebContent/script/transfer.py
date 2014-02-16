#!/usr/bin/env jython
#coding=utf-8
'''
Created on 2013-3-8

@author: Enix Yu
'''
from java.lang import *
from com.cel.dataxfer.jython import TransferType
from org.apache.log4j import Logger
import sys
from config import mainconfig
from db import DBUtils
#from log import Logger
import mylog
from time import time
from utils.parser import Parser

## --------------------------
## Return code
## 0 : normal end
## -2: args not enough
## -1: transfer failed
## --------------------------

class Transfer(TransferType):
    
    config = mainconfig

    delimiter = {
        'MSSQL' : '[%s]',
        'MYSQL' : '`%s`'
    }
        
    
    def __init__(self, package):
        self.msgs = []
        self.package = package
        self.logger = Logger.getRootLogger()
        self.batch = False
    
    def loopTables(self):
        
        dbType = self.config["DB_TYPE"]
        
        try:
            as400 = DBUtils(self.config["DB_SOURCE"])
            mssql = DBUtils(self.config["DB_TARGET"]) 
            config_db = DBUtils(self.config["CONFIG_DB"])     
            
            sql = "select * from config"
            if self.package != "":
                sql += " where pkg='" + self.package + "'"
                
            #row = config_db.s.execute(sql)
            rows = config_db.sQuery(sql)
            for row in rows:
                srcTable = row['source']#row[0]
                srcSQL = row['sql']#row[1]
                tgrTable = row['target']#row[2]
                truncate = row['truncate']#row[3]
                procInd = row['process']#row[4]
                
                #schedule
                yr = row['year']
                month = row['month']
                day = row['day']
                weekday = row['weekday']
                specify = row['specify']
                
                run = {}
                run['year'], run['month'], run['day'], run['weekday'], run['specified'] = yr, month, day, weekday, specify
                parser = Parser(run)
                
                runflag = False
                # Run condition:
                # 1. Not in batch mode
                # 2. In batch mode and hit schedule
                if((not self.batch) or (self.batch and parser.isRun())):
                    runflag = True                
                
                if(procInd == "Y" and runflag):
                    # Check whether needed to create table or not
                    if( not mssql.is_tbl_exist(tgrTable)): #not exist
                        cols_def = as400.get_tbl_cols(srcTable)
                        if len(cols_def) != 0 :
                            mssql.createTable(tgrTable, cols_def, dbType)
                            
                            #for message return
                            self.msgs.append("Table %s created" % tgrTable)
                            self.logger.info("Table %s created" % tgrTable)
                        else:
                            #for message return
                            self.msgs.append("Source Table %s not exist" % srcTable)
                            self.logger.error("Source Table %s not exist" % srcTable)
                            continue
                    else: # does exist
                        if(truncate == "Y"):
                            mssql.truncateTbl(tgrTable)
                    self.transfer(as400, mssql, srcSQL, tgrTable) #batch insert
        
            as400.close()
            mssql.close()
            config_db.close()

            return 0
            
        except Exception ,e:
            self.msgs.append("Transfer crupted \n %s" % str(e))
            self.logger.error("Transfer crupted \n %s" % str(e))
            return -1

    def transfer(self, sdb, tdb, srcSQL, tgrTable):
        try:
            dbType = self.config["DB_TYPE"]
            t1 = time()   
            self.msgs.append("Table %s transfer begin" % tgrTable)
            self.logger.info("Table %s transfer begin" % tgrTable )     
            # ---- fetch 500 records at a time
            tSQL = "INSERT INTO " + self.delimiter[dbType] + "(%s) VALUES(%s)"
            result = sdb.tQuery(srcSQL, self.config["FETCHSIZE"]) 
            rows = []
            result = sdb.next()
            count = 0
            while(result):
                r = result[0]
                columns = r.keys()
                columnsFmt = []
                sqlv, row  = [], []            
                for c in columns:
                    sqlv.append("?")
                    row.append(r[c].replace("'", "''"))
                    columnsFmt.append(self.delimiter[dbType] % c) #Add the column delimeter
                    #columnsFmt.append("[" + c + "]") #Add the column delimeter
                rows.append(row)
                result = sdb.next()
                count += 1
                isql = tSQL % (tgrTable, ",".join(columnsFmt), ",".join(sqlv))                
                
                if (count % self.config["BATCH_INSERT_SIZE"] == 0) or (not result): 
                    tdb.executeMany(isql, rows)
                    rows = []
            t2 = time()
            
            self.msgs.append("Table %s transfer end." % tgrTable)
            self.msgs.append("Total row read: %d. Transfer Rate: %f rec/s." % (count, count/(t2-t1)))
            self.logger.info("Table %s transfer end." % tgrTable)
            self.logger.info("Total row read: %d. Transfer Rate: %f rec/s." % (count, count/(t2-t1)))
            # ---------------------------------------
        except Exception ,e:
            self.msgs.append("Transfer crupted. %s" % str(e))
            self.logger.error("Transfer crupted. %s" % str(e))
            
    def begin(self):
        self.msgs.append("Transfer start...")
        self.logger.info("-----------------------Transfer start-----------------------")   
        rc = self.loopTables()    
        self.msgs.append("Transfer successful ended.")
        self.logger.info("---------------------Transfer successful.----------------")
        self.logger.info(" ")
        return rc
    
    def getMsg(self):
        return self.msgs
    
    def setBatchMode(self, flag):
        self.batch = flag;
    
if __name__ == '__main__':
    if(len(sys.argv) != 1):
        print "Usage: jython transfer.py"
        sys.exit(-2)
    else:
        t = Transfer("")
        rc = t.begin()
        sys.exit(rc)
    
    
    
    
    
    
    
    
