import csv

if __name__=="__main__":
    d = {'age':1,'name': 'Zhihan', 'education': 'Rutgers University', 'region': 'New Jersey'}
    header = ['name','education','region','age']
    csv_file = open('test.csv','w')
    writer = csv.DictWriter(csv_file,fieldnames=d.keys())
    writer.writeheader()
    writer.writerow(d)
