import sys
from os import listdir
import os
import os.path
import csv

PREFIX = 'show_test.cgi?id='
PREFIX2 = 'show_test.cgi@id='
SUFFIX = '.txt'

def removeDuplictes(dirPath, duplicates):
    fileNames = os.listdir(dirPath)
    fileHandle = open(duplicates)
    reader = csv.DictReader(fileHandle)
    summaryMap = {}
    for record in reader:
        if not summaryMap.has_key(record['Summary']):
            summaryMap[record['Summary']] = []
        summaryMap[record['Summary']].append(PREFIX + str(record['Testcase ID'])+SUFFIX)
        summaryMap[record['Summary']].append(PREFIX2 + str(record['Testcase ID'])+SUFFIX)


    for (summary, files) in summaryMap.iteritems():
        pos = 0
        for fileName in files:
            if fileName in fileNames:
                removeLeftFiles(pos,dirPath,files)
                # break
            pos += 1
    # print summaryMap

def removeLeftFiles(pos,dirPath, fileList):
    print fileList
    print pos
    for i in range((pos+1),len(fileList)):
        print i
        filePath = os.path.join(dirPath,fileList[i])
        if os.path.exists(filePath):
            # os.remove(os.path.join(dirPath,fileList[i]))
            os.remove(filePath)
    

if __name__=='__main__':
    if len(sys.argv) !=3:
        print "Usage: python dir duplication file"
    removeDuplictes(sys.argv[1],sys.argv[2])


