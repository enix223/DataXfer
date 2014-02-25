#coding=utf-8
'''
Created on 2013-3-29

@author: Enix Yu
'''
from datetime import datetime
import os, sys

dbconfig = { 
        "DRIVER": "net.sourceforge.jtds.jdbc.Driver", 
        "URL": "jdbc:jtds:sqlserver://172.29.25.25:1433;DatabaseName=J12_DBS_DATA", 
        "USER": "saleslink", 
        "PASSWD": "saleslink@db"  
} 

logconfig = {    
    "LOG_FOLDER" : os.path.abspath(os.path.join(os.path.dirname(sys.argv[0]), "../log/")),
    "LOG_FILENAME_FMT": "history_%s.log" % datetime.now().strftime("%Y%m%d"),
    "LOG_FORMAT": '[%(asctime)s](%(levelname)s)%(name)s : %(message)s'      
}

mainconfig = {
    "CONFIG_DB": "SHAREPOINT",
    "EXCEL_PATH": '',
    "EXCEL_SHEET_NAME": "sheet1",
    "DB_SOURCE": "PRODUCTION",    
    "DB_TARGET": "SHAREPOINT",    
    "DB_TYPE": "MSSQL",
    "FETCHSIZE": 500,
    "BATCH_INSERT_SIZE": 200
} 

mailconfig = {
    'RECEIVER' : ['13826022357@139.com']
}

syntax = {
    'MSSQL.char' : "[%s] [NVARCHAR](%d)",
    'MSSQL.int' : "[%s] [INT]",
    'MSSQL.decimal' : "[%s] [DECIMAL](%d, %d)",
    'MSSQL.primary' : "",
    'MSSQL.date' : "[%s] [DATE]",
    'MYSQL.char' : "`%s` VARCHAR(%d)",
    'MYSQL.int' : "`%s` INT",
    'MYSQL.decimal' : "`%s` DECIMAL(%d, %d)",
    'MYSQL.primary' : "PRIMARY KEY (`dbs_rowid`)",
    'MYSQL.date' : "`%s` `DATE`"
}
