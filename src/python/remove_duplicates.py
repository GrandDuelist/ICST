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
        summaryMap[record['Summary']].append({'fileName': PREFIX + str(record['Testcase ID'])+SUFFIX,
            'branch':record["Branch"]})

        summaryMap[record['Summary']].append({'fileName': PREFIX + str(record['Testcase ID'])+SUFFIX,
            'branch':record["Branch"]})

    print summaryMap
    

    for (summary, files) in summaryMap.iteritems():
        pos = 0
        files.sort(lambda x,y : cmp(x['branch'],y['branch']))
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
        filePath = os.path.join(dirPath,fileList[i]['fileName'])
        if os.path.exists(filePath):
            # os.remove(os.path.join(dirPath,fileList[i]))
            os.remove(filePath)
    

if __name__=='__main__':
    if len(sys.argv) !=3:
        print "Usage: python dir duplication file"
    removeDuplictes(sys.argv[1],sys.argv[2])


