import json
import numpy as np

class WeekData(object):
	def __init__(self, week, addition, deletion):
		self.period = week
		self.addition = int(addition)
		self.deletion = int(deletion)

with open('contributors.json') as data_file:    
    data = json.load(data_file)


unix_arr = np.zeros(4)

#Convert the weeks in the range from ISO to Unix timestamps
unix_arr[0] = 1469923200 #start week 31/07
unix_arr[1] = 1470528000
unix_arr[2] = 1471132800
unix_arr[3] = 1471737600 #end week 21-27/08
size = 4

#Supposedly, the json result from Git API are sorted by week from old to new
additions = np.zeros(4)
deletions = np.zeros(4)
for j in range(len(data)):
	weeks_arr = data[j]['weeks']
	for i in range(len(weeks_arr)):
		for k in range(size):
			if weeks_arr[i]['w'] == unix_arr[k]:
				additions[k] += weeks_arr[i]['a']
				deletions[k] += weeks_arr[i]['d']

data = list()
for i in range(size):
	weekData = WeekData(i,additions[i],deletions[i])
	data.append(weekData)

jsonStr = json.dumps(data, default=lambda o: o.__dict__)
print(jsonStr)

output_file = open("data.json", "w")
output_file.write(jsonStr)
output_file.close()
