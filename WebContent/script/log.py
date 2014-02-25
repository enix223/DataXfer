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
    
    @classmethod
    def getLog(self):        
        if Logger.logger is None:
            logger = logging.getLogger()
            logfile = os.path.join(sys.path[0], logconfig['LOG_FOLDER'], logconfig['LOG_FILENAME_FMT'])
            hdlr = logging.FileHandler(os.path.normpath(logfile))
            formatter = logging.Formatter(logconfig['LOG_FORMAT'])
            hdlr.setFormatter(formatter)
            logger.addHandler(hdlr)
            logger.setLevel(logging.INFO)
            Logger.logger = logger 
                           
        return Logger.logger