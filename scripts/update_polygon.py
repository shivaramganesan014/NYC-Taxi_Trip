import sys
import os
import pandas as pd

def convert(val):
	val = val.split(" ")
	try:
		return [float(val[0]), float(val[1])]
	except:
		return []	
	

def transform(x):
	x = str(x)
	x = x[13:]
	
	x = x[3:]
	x = x[:-3]
	x = x.split(", ")
	# map(convert, x)
	res = []
	for y in x:
		res.append(convert(y))
	return res
	
	
	# return res

def update_polygon(filepath):

	for f in os.listdir(filepath):
		print(f)
		if os.path.isdir(filepath+f):
			for ff in os.listdir(filepath+f):
				if(ff.endswith(".csv") and "busiest_t" not in ff):
					retpath = filepath+f+"/"
					df1 = pd.read_csv(retpath+ff)
					df2 = pd.read_csv('./polygons.csv')
					df = pd.merge(df1, df2, left_on='locationid', right_on='OBJECTID', how='left').drop("OBJECTID", axis=1)
					df["the_geom"] = df["the_geom"].apply(transform)
					df.to_csv(retpath+"busiest_t.csv")
					break
		else:
			print("not dir")
	# df = pd.merge(df1, df2, left_on='pulocationid', right_on='LocationID', how='left').drop("LocationID", axis=1)

path = sys.argv[1]
df = update_polygon(path)

