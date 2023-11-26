
import sys
import os
import numpy as np
import matplotlib.pyplot as plt 
import pandas as pd

rootDir = "./analysis/avg_tip_distance"

validFiles = ['2021@2021', '2022@2022', '2023@2023']
colors = ['red', 'green', 'blue', 'maroon', 'orange']

def getValues(filepath):
	li = {}
	li2 = {}
	for f in os.listdir(filepath):
		if(f in validFiles):
			for fi in os.listdir(filepath+"/"+f):
				if(fi.endswith(".csv")):
					df = pd.read_csv(filepath+"/"+f+"/"+fi)
					li[f] = list(df["average_distance"])
					li2[f] = list(df["average_tip"])
					# li.append({f: list(df["average_distance"])})
					# files.append(f)
					break
	# return (li,files)
	return (li, li2)

def save(data, name):
	days = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"]
	x = np.arange(len(days))
	width = 0.25
	multiplier = 0
	fig, ax = plt.subplots(layout='constrained')

	yLim = 10

	fig.set_figheight(5)
	fig.set_figwidth(10)
	for attribute, values in sorted(data.items()):
		offset = width * multiplier
		yLim = max(yLim, max(values))
		rects = ax.bar(x + offset, values, width, label=attribute)
		ax.bar_label(rects, padding=3)
		multiplier += 1

	yLabel = "tip amount percentage"
	title = "Tip amount analysis"
	if(name == "avg_dist"):
		yLabel = "distance in miles"
		title = "Distance analysis"

	ax.set_ylabel(yLabel)
	ax.set_title(title)
	ax.set_xticks(x + width, days)

	ax.legend(loc = 'upper left', ncols = 3)
	ax.set_ylim(0, yLim+2)

	if not os.path.exists("./analysis/plot"):
		print("creating dir")
		os.makedirs('./analysis/plot')
	print("saving file")
	print(os.getcwd())
	fig.savefig("./analysis/plot/"+name+".png")

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
	avgDist, avgTip = getValues(rootDir)
	# print(li)
	# print(files)
	# print(len(li))

	# plt.figure().set_figheight(25)
	# plt.figure().set_figwidth(100)

	save(avgDist, "avg_dist")
	save(avgTip, "avg_tip")

	# for attribute, values in sorted(avgDist.items()):
	# 	offset = width * multiplier
	# 	rects = ax.bar(x + offset, values, width, label=attribute)
	# 	ax.bar_label(rects, padding=3)
	# 	multiplier += 1

	# ax.set_ylabel("tip amount percentage")
	# ax.set_title("Tip amount analysis")
	# ax.set_xticks(x + width, days)
	# ax.legend(loc = 'upper left', ncols = 3)
	# ax.set_ylim(0, 10)


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


	# if not os.path.exists("./analysis/plot"):
	# 	print("creating dir")
	# 	os.makedirs('./analysis/plot')
	# print("saving file")
	# print(os.getcwd())
	# fig.savefig("./analysis/plot/tip_distance.png")



	# fig = plt.figure(figsize = (10, 5))
	# plt.bar(days, tip, color ='maroon', width = 0.4)
	# plt.xlabel("Days of the week")
	# plt.ylabel("Average Tip Amount")
	# plt.title("Tip analysis")
	# plt.show()
	# df = pd.merge(df1, df2, left_on='pulocationid', right_on='LocationID', how='left').drop("LocationID", axis=1)

# path = sys.argv[1]

# os.system('say "your program has started"')
df = analyse(rootDir)