'''
Created on 2014-2-14

@author: Enix
'''

import os, sys
from org.apache.log4j import Logger
from org.apache.log4j import FileAppender
from org.apache.log4j import PatternLayout
from org.apache.log4j import Level
from config import logconfig


fileName = os.path.join(sys.path[0], logconfig['LOG_FOLDER'], logconfig['LOG_FILENAME_FMT'])     
fa = FileAppender()
fa.setName("FileLogger")
fa.setFile(fileName)
fa.setLayout(PatternLayout("%d %-5p [%c{1}] %m%n"))
fa.setThreshold(Level.DEBUG)
fa.setAppend(True)
fa.activateOptions()
Logger.getRootLogger().addAppender(fa)
        
        