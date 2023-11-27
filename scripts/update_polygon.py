import sys
import os
import pandas as pd
from shapely.geometry import MultiPoint

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

def get_lat_lng(points, zone):
	# print(zone+"::"+str(len(points)))
	res = []
	count = 0
	for p in points:
		if(len(p) > 1):
			t = (p[0], p[1])
			res.append(t)	
	pts = MultiPoint(res)
	return pts.centroid


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
					df['lat'] = pd.Series(dtype='float')
					df['lng'] = pd.Series(dtype='float')
					for i in range(0, len(df["the_geom"])):
						coords = get_lat_lng(df["the_geom"][i], df["zone"])
						# if(len(coords) > 0):
							# print(coords[0])
						if(coords):
							# print(coords.x)
							# print(coords.y)
							df.loc[i, "lat"] = coords.y
							df.loc[i, "lng"] = coords.x
							# df['lat'][i] = coords.x
							# df['lng'][i] = coords.y
							
					# df["lng"] = df["the_geom"].apply(get_lat_lng)
					# pts = get_lat_lng(df["the_geom"].apply)
					df.to_csv(retpath+"busiest_t.csv")

					break
		else:
			print("not dir")
	# df = pd.merge(df1, df2, left_on='pulocationid', right_on='LocationID', how='left').drop("LocationID", axis=1)

path = sys.argv[1]
df = update_polygon(path)

