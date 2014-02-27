#coding=utf-8
'''
Created on 2013-3-11

@author: Enix Yu
'''
from config import logconfig
from datetime import datetime
import logging
import os
import sys

def currentTime():
    return str(datetime.now().strftime("%Y-%m-%d %H:%M:%S"))

class Logger():

    logger = None
    hdlr = None
    
    @classmethod
    def getLog(self, log_file_name):         
        #remove the handler if exist                      
        if Logger.logger is not None and Logger.hdlr is not None:            
            Logger.hdlr.close()
            Logger.logger.removeHandler(Logger.hdlr)
        
        logger = logging.getLogger()            
        logger.setLevel(logging.INFO)  
        logfile = os.path.join(sys.path[0], logconfig['LOG_FOLDER'], log_file_name)
        Logger.hdlr = logging.FileHandler(os.path.normpath(logfile))
        formatter = logging.Formatter(logconfig['LOG_FORMAT'])
        Logger.hdlr.setFormatter(formatter)
        logger.addHandler(Logger.hdlr)
        
        Logger.logger = logger                    
            
        return Logger.logger
        