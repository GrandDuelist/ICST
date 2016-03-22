import urllib2
import re
import bs4
import os
from os import listdir
import os.path
import csv
testFilePath = './lda_input/litmus_20/show_test.cgi?id=1709'
testVersionPath='./lda_input/litmus_20'
testDataPath = './lda_input'
class LitmusAnalysis():
    def __init__(self):
        self.cleaner = CleanHtml()

    def analyzeOneHtml(self,htmlFile):
        # result = {}
        handle = open(htmlFile)
        content = handle.read()
        soup = bs4.BeautifulSoup(content,'lxml')
        testContent = soup.find('div','testcase-content')
        # testContent = soup.find('table',class_='tcm')
        meta = None
        #get summary
        if testContent != None:
            meta = self.extract_meta(testContent)
        # result['summary'] = meta
        # get test content
        step_to_perform = None
        return meta
        
    # def extractAllVersion(self,versionPath):
        # pass

    def extractAllVersion(self,dataPath):
        all_test_case = []
        for versionName in listdir(dataPath):
            versionPath = os.path.join(dataPath,versionName)
            for fileName in listdir(versionPath):
                filePath = os.path.join(versionPath,fileName)
                oneStructureResult = self.analyzeOneHtml(filePath)
                if oneStructureResult != None:
                    # print oneStructureResult
                    all_test_case.append(oneStructureResult)
        return all_test_case

    def outputToCSV(self, filePath,testCases):
        fileHandle = open(filePath,'w')
        writer = csv.DictWriter(fileHandle,fieldnames=['Testcase ID','Summary','Product','Branch','step_to_perform'])
        writer.writerows(testCases)

    def uniqueSummaries(self,testCases,filePath):
        uniqueSums = {}
        for testCase in testCases:
            uniqueSums[testCase['Summary']] = testCase['Testcase ID']

        outputResult = []
        for summary in uniqueSums:
            outputResult.append({"summary":summary})
        fileHandle = open(filePath,'w')
        writer = csv.DictWriter(fileHandle,fieldnames=['summary'])
        writer.writerows(outputResult)

    def extract_meta(self,content):
        summaryMeta = {}
        # summaryHtml = content.find('table',class_="tcm")
        summaryHtml = content
        if summaryHtml.find('td',id='testcase_id_display') != None:
            summaryMeta['Testcase ID'] = summaryHtml.find('td',id='testcase_id_display').text.encode('utf-8')
        if summaryHtml.find('div',id='summary_text') != None:
            summaryMeta['Summary'] = summaryHtml.find('div',id='summary_text').text.encode('utf-8')

        if summaryHtml.find('div',id='product_text') != None:
            summaryMeta['Product'] = summaryHtml.find('div',id='product_text').text.encode('utf-8')

        if summaryHtml.find('div',id='branch_text') != None:
            summaryMeta['Branch'] = summaryHtml.find('div',id='branch_text').text.encode('utf-8')

        if summaryHtml.find('div',id='steps_text') != None:
           summaryMeta['step_to_perform'] = summaryHtml.find('div',id='steps_text').text.encode('utf-8')
        return summaryMeta
    
    def extract_step_to_perform(self,content):
        soup = bs4.BeautifulSoup(content)
        content = soup.find()

class CleanHtml():
    def cleanSummary(self):
        print "clean summary"

if __name__ == "__main__":
    litmus = LitmusAnalysis()
    allTestCases = litmus.extractAllVersion(testDataPath)
    litmus.outputToCSV('testCaseContent.csv',allTestCases)
    # litmus.uniqueSummaries(allTestCases,'testCaseContent.csv')
    # litmus.analyzeOneHtml(testFilePath)

