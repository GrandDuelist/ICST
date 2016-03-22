import os
import os.path
import csv
import sys
def modifyDuplicateForUnion(union_path,duplicate_path):
    fileNames = os.listdir(union_path)
    duplicateHandle = open(duplicate_path,'r')
    reader = csv.reader(duplicateHandle,delimiter='\t')
    for record in reader.iteritem():
        print record

    
if __name__ == '__main__':
    if len(sys.argv) !=3:
        print "Usage: python union_cluser_dir duplicate_path"
    modifyDuplicateForUnion(sys.argv[1],sys.argv[2])

