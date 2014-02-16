#!/usr/bin/env jython
#coding=utf-8
'''
Created on 2013-3-8

@author: Enix Yu
'''
from utils.db import DBUtils
from utils.excel import Excel
import sys, os
from utils.log import Logger
from time import time
from config import mainconfig
from utils.parser import Parser

## --------------------------
## Return code
## 0 : normal end
## -2: args not enough
## -1: transfer failed
## --------------------------

class Transfer:
    
    config = mainconfig
    
    def __init__(self, config_file_path):
        self.logger = Logger().getLog()
        self.config['EXCEL_PATH'] = os.path.normpath(sys.path[0] + config_file_path)
    
    def loopTables(self):
        
        try:
            as400 = DBUtils(self.config["DB_SOURCE"])
            mssql = DBUtils(self.config["DB_TARGET"])
            
            xl = Excel(self.config["EXCEL_PATH"], self.config["EXCEL_SHEET_NAME"])
            c_rows = xl.get_row_count()
            for r in range(1, c_rows):
                row = xl.get_row_list(r)
                srcTable = row[0]
                srcSQL = row[1]
                tgrTable = row[2]
                truncate = row[3]
                procInd = row[5]
                
                run = {}
                run['year'], run['month'], run['day'], run['weekday'], run['specified'] = row[6], row[7], row[8], row[9], row[10]
                parser = Parser(run)
            
                if(procInd == "Y" and parser.isRun()):
                    # Check whether needed to create table or not
                    if( not mssql.is_tbl_exist(tgrTable)): #not exist
                        cols_def = as400.get_tbl_cols(srcTable)
                        if len(cols_def) != 0 :
                            mssql.createTable(tgrTable, cols_def)
                            self.logger.info("Table %s created" % tgrTable)
                        else:
                            self.logger.error("Source Table %s not exist" % srcTable)
                            continue
                    else: # does exist
                        if(truncate == "Y"):
                            mssql.truncateTbl(tgrTable)
                    self.transfer(as400, mssql, srcSQL, tgrTable) #batch insert
        
            as400.close()
            mssql.close()

            return 0
            
        except Exception, e:
            self.logger.error("batch=>loopTables error \n %s" % str(e))
            return -1
    
    def transfer(self, sdb, tdb, srcSQL, tgrTable):
        try:
            t1 = time()   
            self.logger.info("Table %s transfer begin" % tgrTable )     
            # ---- fetch 500 records at a time
            tSQL = "INSERT INTO %s(%s) VALUES(%s)" 
            result = sdb.tQuery(srcSQL, self.config["FETCHSIZE"]) 
            rows = []
            result = sdb.next()
            count = 0
            while(result):
                r = result[0]
                columns = r.keys()
                sqlv, row  = [], []            
                for c in columns:
                    sqlv.append("?")
                    row.append(r[c].replace("'", "''"))
                rows.append(row)
                result = sdb.next()
                count += 1
                isql = tSQL % (tgrTable, ",".join(columns), ",".join(sqlv))
                if (count % self.config["BATCH_INSERT_SIZE"] == 0) or (not result): 
                    tdb.executeMany(isql, rows)
                    rows = []
            t2 = time()
            self.logger.info("Table %s transfer end \n Total row read: %d\n Transfer Rate: %f rec/s\n" % (tgrTable, count, count/(t2-t1)))
            # ---------------------------------------
        except Exception ,e:
            self.logger.critical("Transfer crupted \n %s" % str(e))
            
    def begin(self):
        self.logger.info("-----------------------Transfer start-----------------------")   
        rc = self.loopTables()    
        self.logger.info("---------------------Transfer successful.---------------- \n\n")
        return rc
    
if __name__ == '__main__':
    if(len(sys.argv) != 2):
        print "Usage: jython transfer.py /config/setting.daily"
        sys.exit(-2)
    else:
        config_file = sys.argv[1]
        t = Transfer(config_file)
        rc = t.begin()
        sys.exit(rc)
    
    
    
    
    
    
    
    
