import sys
import os 
import shutil

folder_path =  "/home/zhihan/workspace/ICST/data/lda_input_steps_to_perform/litmus_rrr"
version_list = ['V20','V30','V35','V36','V36_complete_web','V40','V50','V60','V70','V80','V90','V10','V11','V12','V13','V14']

vdersion_list_index= {}
def read_version_duplicates(file_path):
    result = {}
    if not os.path.exists(file_path):
        exit(1)
    else:
        duplicates = open(file_path,"r")
        for line in duplicates:
            title_file_version = line.split("\t")
            version = title_file_version[2].replace('\n','')
            one_summary  = {'file_path': title_file_version[1], 'version':version, 'version_index':
                    version_list_index[version]}
            if title_file_version[0] not in result:
                result[title_file_version[0]] = []
            result[title_file_version[0]].append(one_summary)
    return result

def remove_files_list(duplicates,folder_path,version_list=None):
    result = []
    for summary_name, one_summary_group in duplicates.items():
        one_summary= one_summary_remove_list(one_summary_group,version_list)
        if one_summary is not None:
            result = result + one_summary
    return result

def one_summary_remove_list(one_summary_group, version_list):
    is_removed = False
    smallest_target_version = smallest_version_to_keep(one_summary_group,'V50')
    if smallest_target_version > len(version_list):
        return None
    return [ one_summary['file_path'] for one_summary in one_summary_group if
            one_summary['version_index']>smallest_target_version]


def smallest_version_to_keep(one_summary_group, smallest_version):
    smallest = version_list_index[smallest_version]
    result = 100
    for one_summary in one_summary_group:
        if one_summary['version_index'] < result and one_summary['version_index'] >= smallest:
            result = one_summary['version_index']
    return result


def version_list_map(version_list):
    result = {}
    version_index = 1
    for ii in version_list:
        result[ii] = version_index
        version_index = version_index+1
    return result;

def remove_file_in_the_list(folder_path, remove_list,file_name_suffix=''):
    if not os.path.exists(folder_path):
        exit(1)
    for file_name in remove_list:
        file_path = os.path.join(folder_path,file_name)
        file_path = file_path+file_name_suffix
        if(os.path.exists(file_path)):
            os.remove(file_path)
    
if __name__=="__main__":
    if len(sys.argv)!=2:
        print "Usage: python file path"
        exit(1)
    version_list_index= version_list_map(version_list)
    duplicates = read_version_duplicates(sys.argv[1])
    # prcoess_duplicates(folder_path,duplicates)
    remove_list = remove_files_list(duplicates,folder_path,version_list)
    remove_file_in_the_list(folder_path,remove_list,'.txt')
