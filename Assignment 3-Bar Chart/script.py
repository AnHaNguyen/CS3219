import json
import numpy as np

class WeekData(object):
	def __init__(self, week, addition, deletion):
		self.period = week
		self.addition = int(addition)
		self.deletion = int(deletion)

with open('contributors.json') as data_file:    
    data = json.load(data_file)

size = 10
unix_arr = []

#Convert the weeks in the range from ISO to Unix timestamps
unix_arr.append([1469318400,"24/07"])
unix_arr.append([1469923200,"31/07"])
unix_arr.append([1470528000,"07/08"])
unix_arr.append([1471132800,"14/08"])
unix_arr.append([1471737600,"21/08"])
unix_arr.append([1472342400,"28/08"])
unix_arr.append([1472947200,"04/09"])
unix_arr.append([1473552000,"11/09"])
unix_arr.append([1474156800,"18/09"])
unix_arr.append([1474761600,"25/09"])



#Supposedly, the json result from Git API are sorted by week from old to new
additions = np.zeros(size)
deletions = np.zeros(size)
for j in range(len(data)):
	weeks_arr = data[j]['weeks']
	for i in range(len(weeks_arr)):
		for k in range(size):
			if weeks_arr[i]['w'] == unix_arr[k][0]:
				additions[k] += weeks_arr[i]['a']
				deletions[k] += weeks_arr[i]['d']

data = list()
for i in range(size):
	weekData = WeekData(unix_arr[i][1],additions[i],deletions[i])
	data.append(weekData)

jsonStr = json.dumps(data, default=lambda o: o.__dict__)
print(jsonStr)

output_file = open("data.json", "w")
output_file.write(jsonStr)
output_file.close()
