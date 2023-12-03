import geopandas as gpd
from shapely.geometry import Point, Polygon
import pandas as pd
from shapely.wkt import loads
import ast
import matplotlib.pyplot as plt


def poly(val):
  points_list = ast.literal_eval(val)
  try:
    return str(Polygon(points_list))
  except:
    return None

poi_data = gpd.read_file("../../../NYC/Point_Of_Interest.csv")
busiest_loc_data = gpd.read_file("../analysis/pu_busiest_location/2021@4 month/busiest_t.csv")
shpfile = gpd.read_file("../../../NYC/taxi_zones/taxi_zones.shp")

poi_data.geometry = poi_data["the_geom"].apply(loads)

poi_data.set_crs(epsg=4326, inplace=True)

busiest_loc_data["total"] = busiest_loc_data["total"].astype('int')
busiest_loc_data = busiest_loc_data[busiest_loc_data['total'] != 0]
busiest_loc_data = busiest_loc_data[busiest_loc_data["Shape_Leng"] != '']

busiest_loc_data["the_geom"] = busiest_loc_data["the_geom"].apply(poly)
busiest_loc_data.geometry = busiest_loc_data["the_geom"].apply(loads)

busiest_loc_data["locationid"] = busiest_loc_data["locationid"].astype(float).astype(int)
busiest_loc_data["total"] = busiest_loc_data["total"].astype(int)
location_counts = busiest_loc_data["locationid"].value_counts()

topk = busiest_loc_data.nlargest(10, "total")



shapes = shpfile[shpfile["LocationID"].isin(topk["locationid"])]

topk["LocationID"] = topk["LocationID"].astype(float).astype(int)

topk_with_names = pd.merge(topk, busiest_loc_data, on="locationid", how="left")

top_k_with_names = topk_with_names.sort_values(by="locationid")

fig, ax = plt.subplots(figsize=(10, 10))
shpfile.to_crs(epsg=4326).plot(ax = ax)
shapes.to_crs(epsg=4326).plot(ax = ax, color='burlywood')
imp_points = gpd.sjoin(poi_data, shapes.to_crs(epsg=4326), op='within')
ax.text(x=0.5, y=1.02, s='Busiest Dropoff Regions with POI: '+ str(len(imp_points)) + " in top " + str(len(shapes)) + " regions", fontsize=14, ha='center', transform=ax.transAxes)
imp_points.to_crs(epsg=4326).plot(ax = ax, color = 'red', marker='o', aspect = 1, markersize=15 ,ec='black', linewidth=1)

# details_text = 'Top K Regions:\n\n{}'.format(",\n".join(map(str, topk["LocationID"])))

details_text = 'Top K Regions:\n\n{}'.format("\n".join(top_k_with_names[top_k_with_names["locationid"].isin(topk["LocationID"])]['zone_x'].unique()))

ax.text(-74.08, 40.77, details_text, fontsize=10, bbox=dict(facecolor='white', alpha=0.8))

ax.set_xlim(-74.1, -73.7)
ax.set_ylim(40.55, 40.89)

plt.show()

