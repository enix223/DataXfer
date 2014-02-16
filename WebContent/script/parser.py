#coding=utf-8
'''
Created on 2013-3-29

@author: Enix Yu
'''

#------------------------------------------------------
#Possible value 
#1. d: discrete series: 1,3,5
#2. c: continuous series: 1-15
#3. w: wildcard *
#  
#schedule{
#    day:       <d> | <c> | <w>  [Range: 1-31]
#    month:     <d> | <c> | <w>  [Range: 1-12]
#    year:      <d> | <c> | <w>  [format: 2013]
#    weekday:   <d> | <c> | <w>  [Range: 0-6, 0 for Monday]
#    specified: <d>              [format: YYYYMMDD] 
#}
#
#priority:
#specified >  weekday | day
#------------------------------------------------------
#from log import Logger
from datetime import datetime
import re

class Parser(object):
    
    __schedule__ = ''
    __WILDCARD__ = '*'
    
    def __init__(self, schedule):
        self.__schedule__ = schedule        
        #self.logger = Logger.getLog();
    
    def rangeToList(self, value):
        # continuous series
        a = []
        if(value.find('-') > -1):
            st = int(value.split('-')[0])
            end = int(value.split('-')[1]) + 1
            for i in range(st, end):
                a.append(i) 
        else:
            a.append(int(value))
            
        return a
        
    def parseToList(self, value):
        arr = []
        # wildcard
        if(value == self.__WILDCARD__):
            arr = [self.__WILDCARD__]
            return arr
            
        match = re.match(r'^(\d+-?,?)+$', value)
        if(match):    
            try:
                if(re.match(r'^\d+$',value)):
                    arr.append(int(value))                      
                #discrete series as tuple
                elif(re.match(r'^(\d+,?)+$', value)):
                    arr = list(eval(value))
                else:
                    s = value.split(',')
                    for i in s: 
                        arr += self.rangeToList(i)           
            except Exception, e:
                #self.logger.error('Parser->parseToList: convert error %s', str(e))
                print str(e)
        
        return arr
        
        
    def isRun(self):
        r = datetime.now()
        year, month, day, weekday = r.year, r.month, r.day, r.weekday()
        today = r.strftime('%Y%m%d')
        
        #Check specified dates
        if(self.__schedule__['specified'] != ''): #specified the run date
            if(self.__schedule__['specified'].find(today) > -1):
                return True
            else:
                return False
        
        if(self.__schedule__['year'] == self.__WILDCARD__  or (year in self.parseToList(self.__schedule__['year']))):
            pass
        else:
            return False
            
        if(self.__schedule__['month'] == self.__WILDCARD__ or (month in self.parseToList(self.__schedule__['month']))):
            pass
        else:
            return False

        if(self.__schedule__['day'] == self.__WILDCARD__):
            if(self.__schedule__['weekday'] == self.__WILDCARD__):
                return True
            elif(weekday in self.parseToList(self.__schedule__['weekday'])):
                return True
            else:
                return False
        elif(day in self.parseToList(self.__schedule__['day'])):
            return True
        else:
            return False 
            
            
            
        