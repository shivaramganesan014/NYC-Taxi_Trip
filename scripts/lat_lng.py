import shapefile
import pandas as pd

def get_lat_lng():

	sf = shapefile.Reader("./taxi_zones/taxi_zones.shp")
	fields_name = [field[0] for field in sf.fields[1:]]
	shp_dic = dict(zip(fields_name, list(range(len(fields_name)))))
	content = []
	for sr in sf.shapeRecords():
		shape = sr.shape
		rec = sr.record
		loc_id = rec[shp_dic['LocationID']]

		x = (shape.bbox[0]+shape.bbox[2])/2
        y = (shape.bbox[1]+shape.bbox[3])/2
        content.append((loc_id, x, y))
    df = pd.DataFrame(content, columns=["LocationID", "longitude", "latitude"])
    return df

df = get_lat_lng()
df.to_csv("coordinates.csv")
