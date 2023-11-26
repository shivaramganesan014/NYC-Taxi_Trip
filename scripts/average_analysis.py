import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import os
import seaborn as sns

path = "analysis/avg_tip_distance_analysis/"

def getValues(path):
	dist = []
	tip = []
	files = []
	for f in os.listdir(path):
		if(not os.path.isdir(path+f)):
			continue
		for ff in os.listdir(path+f):
			if(".csv" in ff and ".crc" not in ff):
				df = pd.read_csv(path+f+"/"+ff, encoding='utf-8')
				dist.append(list(df['avg_trip_distance']))
				tip.append(list(df["avg_tip_amount"]))
				files.append(f)
				break
	return (files, dist, tip)


def analyse(filepath):
	files, dist, tip = getValues(path)
	hours = [i for i in range(0,24)]


	x = np.arange(len(hours))
	width = 0.25
	multiplier = 0

	fig, ax = plt.subplots(layout='constrained')

	fig.set_figheight(15)
	fig.set_figwidth(30)

	# maxV = 0
	# for xx in dist:
	# 	v = max(xx)
	# 	maxV = max(v, maxV)
	# print(maxV)


	for i in range(0, len(files)):
		attribute = files[i]
		values = dist[i]
		offset = width * multiplier
		rects = ax.bar(x + offset, values, width, label=attribute)
		ax.bar_label(rects, padding=3)
		multiplier += 1

	ax.set_ylabel("Average Trip Distance")
	ax.set_title("Average Trip Distance analysis")
	ax.set_xticks(x + width, hours)
	ax.legend(loc = 'upper left', ncols = 3)
	ax.set_ylim(0, 16)

	if not os.path.exists("./analysis/plot/"):
		print("creating dir")
		os.makedirs('./analysis/plot/')
	print("saving file")
	print(os.getcwd())
	fig.savefig("./analysis/plot/average_dist_hour.png")

df = analyse(path)