import sys
import os
import pandas as pd

def update_coordinates(filepath):

	for f in os.listdir(filepath):
		print(f)
		if(f.endswith(".csv")):
			print(filepath)
			df1 = pd.read_csv(filepath+f)
			df2 = pd.read_csv('./coordinates.csv')
			df = pd.merge(df1, df2, left_on='locationid', right_on='LocationID', how='left').drop("LocationID", axis=1)
			df.to_csv(path+"/"+"busiest_t.csv")
			break
	# df = pd.merge(df1, df2, left_on='pulocationid', right_on='LocationID', how='left').drop("LocationID", axis=1)

path = sys.argv[1]
df = update_coordinates(path)

