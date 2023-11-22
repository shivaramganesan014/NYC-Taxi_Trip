import numpy as np
import matplotlib.pyplot as plt 
import pandas as pd
import sys
import os

rootDir = "./analysis/avg_tip_distance"

validFiles = ['2021@2021', '2022@2022', '2023@2023']
colors = ['red', 'green', 'blue', 'maroon', 'orange']

def getValues(filepath):
	li = []
	files = []
	for f in os.listdir(filepath):
		if(f in validFiles):
			for fi in os.listdir(filepath+"/"+f):
				if(fi.endswith(".csv")):
					df = pd.read_csv(filepath+"/"+f+"/"+fi)
					li.append(list(df["average_distance"]))
					files.append(f)
					break
	return (li,files)

def analyse(filepath):

	# for f in os.listdir(filepath):
	# 	print(f)
	# 	if(f.endswith(".csv")):
	# 		print(filepath)
	# 		df = pd.read_csv(filepath+f)
	# 		days = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"]
	# 		tip = list(df["average_tip"])
	# 		dist = list(df["average_distance"])
	# 		fig = plt.figure(figsize = (10, 5))
	# 		plt.bar(days, tip, color ='maroon', width = 0.4)
	# 		plt.xlabel("Days of the week")
	# 		plt.ylabel("Average Tip Amount")
	# 		plt.title("Tip analysis")
	# 		plt.show()
	# 		break
	log("analysis started")
	li, files = getValues(rootDir)
	log("values fetched")
	# print(files)
	# print(len(li))
	days = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"]

	# print(li)


	x = np.arange(len(days))
	width = 0.25
	multiplier = 0

	# plt.figure().set_figheight(25)
	# plt.figure().set_figwidth(100)

	fig, ax = plt.subplots(layout='constrained')

	fig.set_figheight(5)
	fig.set_figwidth(10)

	for i in range(0, len(files)):
		attribute = files[i]
		values = li[i]
		offset = width * multiplier
		rects = ax.bar(x + offset, values, width, label=attribute)
		ax.bar_label(rects, padding=3)
		multiplier += 1

	ax.set_ylabel("tip amount percentage")
	ax.set_title("Tip amount analysis")
	ax.set_xticks(x + width, days)
	ax.legend(loc = 'upper left', ncols = 3)
	ax.set_ylim(0, 10)
	# plt.show()

	# plt.figure(figsize=(20,10)) 

	# width = 0.25

	# ind = np.arange(len(days))
	# bars = ()
	# for i in range(0, len(li)):
	# 	bars = bars + (plt.bar(ind + (width * i), li[i], width))

    	

	# plt.xlabel("Days") 
	# plt.ylabel('Average Tip Amount') 
	# plt.title("Tip Days Relation")


	# plt.xticks(ind+width,tuple(days)) 
	# plt.legend(bars, tuple(files))
	# # plt.show()
	# # plt.plot()
	if not os.path.exists("../analysis/plot"):
		print("creating dir")
		os.makedirs('./analysis/plot')
	print("saving file")
	print(os.getcwd())
	fig.savefig("./analysis/plot/tip_distance.png")



	# fig = plt.figure(figsize = (10, 5))
	# plt.bar(days, tip, color ='maroon', width = 0.4)
	# plt.xlabel("Days of the week")
	# plt.ylabel("Average Tip Amount")
	# plt.title("Tip analysis")
	# plt.show()
	# df = pd.merge(df1, df2, left_on='pulocationid', right_on='LocationID', how='left').drop("LocationID", axis=1)

# path = sys.argv[1]
def log(content):
	f = open("errorlog.txt", "a")
	f.write(content+"\n")
	f.close()
try:
	log("program started")
	df = analyse(rootDir)

except:
	log("Error at start")



