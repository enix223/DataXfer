#coding=utf-8
'''
Created on 2013-3-29

@author: Enix Yu
'''
from datetime import datetime
import os, sys

dbconfig = { 
    "QATEST":{ 
        "DRIVER": "com.ibm.as400.access.AS400JDBCDriver", 
        "URL": "jdbc:as400://192.168.231.144/LIBJ12COG", 
        "USER": "NOELT", 
        "PASSWD": "NOEL123"
    },         
    "PRODUCTION":{ 
        "DRIVER": "com.ibm.as400.access.AS400JDBCDriver", 
        "URL": "jdbc:as400://192.168.231.215/LIBJ12COG", 
        "USER": "CRYSTALDLD", 
        "PASSWD": "dj7whj4q"
    }, 
    "MSSQL":{
        "DRIVER": "net.sourceforge.jtds.jdbc.Driver", 
        "URL": "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=J12_DBS_DATA",
        #"URL": "jdbc:jtds:sqlserver://172.29.25.156:1433;instance=SQLEXPRESS;DatabaseName=DBS", 
        "USER": "sa", 
        "PASSWD": "enixyuabc@123"
        #"PASSWD": "simedarbycn"      
    },
    "SHAREPOINT":{
        "DRIVER": "net.sourceforge.jtds.jdbc.Driver", 
        "URL": "jdbc:jtds:sqlserver://172.29.25.25:1433;DatabaseName=J12_DBS_DATA", 
        "USER": "saleslink", 
        "PASSWD": "saleslink@db"
    },
    "SQLITE3":{
        "DRIVER": "org.sqlite.JDBC",
        "URL": "jdbc:sqlite://" + (os.path.abspath(os.path.join(os.path.dirname(sys.argv[0]), "../database/data.db"))).replace("\\","/"),
        "USER": None, 
        "PASSWD": None
    }           
} 

logconfig = {    
    "LOG_FOLDER" : os.path.abspath(os.path.join(os.path.dirname(sys.argv[0]), "../log/")),
    "LOG_FILENAME_FMT": "history_%s.log" % datetime.now().strftime("%Y%m%d"),
    "LOG_FORMAT": '[%(asctime)s](%(levelname)s)%(name)s : %(message)s'      
}

mainconfig = {
    "CONFIG_DB": "SQLITE3",
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
