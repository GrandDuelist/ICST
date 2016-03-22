import sys
import os
import shutil
import csv
import statistics as st

duplicate_file_path = '../data layerize/duplicated.txt'
folder_path =  "/home/zhihan/workspace/ICST/data/lda_input_steps_to_perform/"
version_list = ['V20','V30','V35','V36','V36_complete_web','V40','V50','V60','V70','V80','V90','V10','V11','V12','V13','V14']

version_list_index= {}

def read_version_duplicates(file_path):
    result = {}
    if not os.path.exists(file_path):
        exit(1)
    else:
        duplicates = open(file_path,"r")
        for line in duplicates:
            title_file_version = line.split("\t")
            version = title_file_version[2].replace('\n','')
            one_summary  = {'summary':title_file_version[0],'file_path': title_file_version[1], 'version':version, 'version_index': version_list_index[version]}
            if title_file_version[0] not in result:
                result[title_file_version[0]] = []
            result[title_file_version[0]].append(one_summary)
    return result

def version_list_map(version_list):
    result = {}
    version_index = 1
    for ii in version_list:
        result[ii] = version_index
        version_index = version_index+1
    return result;

def write_life_cycle(file_path,output_path):
    duplicates = read_version_duplicates(file_path)
    output_file = open(output_path,'w')
    writer = csv.DictWriter(output_file,fieldnames=['summary','version','version_index','life_cycle','file_path'])
    writer.writeheader()
    for summary_name, one_summary in duplicates.items():
        process_one_summary_cycle(one_summary)
        writer.writerows(one_summary)

def calculate_life_cycle(file_path):
    result = {}
    for version in version_list:
        result[version] = []
    duplicates = read_version_duplicates(file_path)
    for summary_name, one_summary in duplicates.items():
        process_one_summary_cycle(one_summary)
        for test_case in one_summary:
            result[test_case['version']].append(test_case['life_cycle'])
    return result

def life_cycle_statistics(version_cluster_list):
    result = {}
    version_iteration = version_list
    for version in version_iteration:
        result[version] = {}
    for version_cluster_key in version_cluster_list.keys():
        version_cluster_content = version_cluster_list[version_cluster_key]
        result[version_cluster_key]["version"] = version_cluster_key 
        result[version_cluster_key]["No of total tests"] = len(version_cluster_content)
        result[version_cluster_key]["No of new tests"] = version_cluster_content.count(0)
        #remove the test cases with age = 0
        content_without_zero = [value for value in version_cluster_content if value != 0]
        if content_without_zero != []:
            result[version_cluster_key]["Min"] = min(content_without_zero)
            result[version_cluster_key]["Max"] = max(content_without_zero)
            result[version_cluster_key]["Average"] = st.mean(content_without_zero)
            result[version_cluster_key]["Median"] = st.median(content_without_zero)
        else:
            result[version_cluster_key]["Min"] = 0
            result[version_cluster_key]["Max"] = 0
            result[version_cluster_key]["Average"] = 0
            result[version_cluster_key]["Median"] = 0
    return result

    
def output_csv(version_cluster_statistics,output_file):
    output_file_handle = open(output_file,'w')
    writer = csv.DictWriter(output_file_handle,fieldnames = ['version','No of total tests','No of new tests','Min','Max','Average','Median'])
    writer.writeheader()
    print version_cluster_statistics.values()
    writer.writerows(version_cluster_statistics.values())

def process_one_summary_cycle(one_summary_group):
    smallest = initial_version(one_summary_group)
    for one_summary in one_summary_group:
        one_summary['life_cycle'] = one_summary['version_index']-smallest

def initial_version(one_summary_group):
    result = 1000
    for one_summary in one_summary_group:
        if one_summary['version_index'] < result:
            result = one_summary['version_index']
    return result

output_path = 'life_cycle_statistics.csv'

def life_cycle_statistics_main(dupliate_file_path,output_path):
    version_cluster_list = calculate_life_cycle(duplicate_file_path)
    result = life_cycle_statistics(version_cluster_list)
    output_csv(result,output_path)

def summary_statistics_main(duplicate_file_path,output_path):
    write_life_cycle(duplicate_file_path,output_path)

    
if __name__=="__main__":
    version_list_index= version_list_map(version_list)
    life_cycle_statistics_main(duplicate_file_path,output_path)
    # summary_statistics_main(duplicate_file_path,output_path)

