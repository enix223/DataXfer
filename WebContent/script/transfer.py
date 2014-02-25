#!/usr/bin/env jython
#coding=utf-8
'''
Created on 2013-3-8

@author: Enix Yu
'''

from com.cel.dataxfer.jython import TransferType
import sys
import os
from config import mainconfig
from config import dbconfig
from db import DBUtils
from log import Logger
from time import time
from parser import Parser

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
        self.logger = Logger.getLog()
        self.batch = False
    
    def loopTables(self):
        
        dbType = self.config["DB_TYPE"]
        
        try:            
            config_db = DBUtils(dbconfig)     
            
            sql = "select * from DataXfer_v_config"
            if self.package != "":
                sql += " where pkg='" + self.package + "'"                    
                
            rows = config_db.sQuery(sql)
            for row in rows:                
                
                srcTable = row['source']
                srcSQL = row['sql'] 
                tgrTable = row['target']
                truncate = row['truncate_flag']
                procInd = row['process']
                
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
                    
                    if(row['source_driver'] == None or row['target_driver'] == None):
                        self.logger.error("Source Table %s data source not set" % srcTable)
                        continue
                        
                    
                    db_source = DBUtils({"DRIVER": row['source_driver'], 
                                           "URL": row['source_url'], 
                                           "USER": row['source_username'], 
                                           "PASSWD": row['source_passwd']})
                
                    db_target = DBUtils({"DRIVER": row['target_driver'], 
                                           "URL": row['target_url'], 
                                           "USER": row['target_username'], 
                                           "PASSWD": row['target_passwd']})
                    # Check whether needed to create table or not
                    if( not db_target.is_tbl_exist(tgrTable)): #not exist
                        cols_def = db_source.get_tbl_cols(srcTable)
                        if len(cols_def) != 0 :
                            db_target.createTable(tgrTable, cols_def, dbType)
                            
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
                            db_target.truncateTbl(tgrTable)
                    self.transfer(db_source, db_target, srcSQL, tgrTable) #batch insert
                    
                    db_source.close() #close on each record finished
                    db_target.close() #close on each record finished
        
            
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
        #check the transfer.pid file exist or not
        #if(os.path.exists("transfer.pid")):
        #    f = open("transfer.pid")
        #    pid = f.read()
        #    f.close()
        #    self.logger.info("Transfer job is running, pid: %s" % pid)    
        #    return -1             
        #else:
            #create pid file if not exist
        #    pid = os.getpid()
        #    f = open("transfer.pid", "wb+")
        #    f.write(str(pid))
        #    f.close()
        
        self.msgs.append("Transfer start...")
        self.logger.info("-----------------------Transfer start-----------------------")   
        rc = self.loopTables()    
        self.msgs.append("Transfer successful ended.")
        self.logger.info("---------------------Transfer successful.----------------")
        self.logger.info(" ")
        
        #clear the pid
        #os.remove("transfer.pid")
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
    
    
    
    
    
    
    
    
