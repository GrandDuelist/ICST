import os 
import shutil 
from os import listdir
import os.path
import sys

omit_sub_dirs = ["litmus_10","litmus_30","litmus_35","litmus_36","litmus_40","litmus_14"]

def copy_files_in_folder(from_dir, to_dir):
    if not os.path.exists(to_dir):
        os.mkdir(to_dir)
        print to_dir
    dir_names = [ filepath for filepath in listdir(from_dir) if os.path.isdir (os.path.join(from_dir,filepath))]
    for dir_name in dir_names:
        if(dir_name not in omit_sub_dirs):
            print dir_name
            from_sub_dir= os.path.join(from_dir,dir_name)
            for sub_file_name in listdir(from_sub_dir):
                from_file_path = os.path.join(from_sub_dir,sub_file_name)
                to_file_path = os.path.join(to_dir,sub_file_name)
                shutil.copyfile(from_file_path,to_file_path)
    
if __name__=="__main__":
    if len(sys.argv)!=3:
        print "Usring: python from_dir to_dir"
        exit(1)
    copy_files_in_folder(sys.argv[1],sys.argv[2])
