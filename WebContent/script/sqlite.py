'''
Created on 2014-2-8

@author: Enix
'''

import sqlite3
import sys
import os

class SQLiteUtil(object):
    
    def __init__(self): 
        self.conn = None     
        self.cursor = None  
        self.connect()
        
    def connect(self):
        if self.conn is None:              
            root = os.path.dirname(sys.argv[0])
            db_path = os.path.abspath(os.path.join(root, "../database/data.db"))
            self.conn = sqlite3.connect(db_path)
        return self.conn
    
    def close(self):
        try:
            self.cursor.close()
            self.conn.close()
        except:
            pass
        
    def query(self, sql):
        self.connect()
        if self.cursor is None:
            self.cursor = self.conn.cursor()
                        
        self.cursor = self.conn.cursor()
        self.cursor.execute(sql)
        return self.cursor.fetchone()
    
    def next(self):
        self.connect()
        if self.cursor:
            return self.cursor.fetchone()
        else:
            return None
        
    def execute(self, sql):
        self.connect()
        if self.cursor is None:
            self.cursor = self.conn.cursor()
            
        self.cursor.execute(sql)        
        
    def commit(self):
        self.connect()
        self.conn.commit()
        
    
        
    
