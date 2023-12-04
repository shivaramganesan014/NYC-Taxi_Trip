import geopandas as gpd
import pandas as pd
from shapely.wkt import loads
from shapely.geometry import Polygon
import ast
import matplotlib.pyplot as plt
from matplotlib.colors import Normalize, LogNorm
from mpl_toolkits.axes_grid1 import make_axes_locatable


locdata = gpd.read_file('../analysis/do_busiest_location/2022@4 month/busiest_t.csv')
shpfile = gpd.read_file("../additional_data/taxi_zones/taxi_zones.shp")

def poly(val):
  points_list = ast.literal_eval(val)
  try:
    return str(Polygon(points_list))
  except:
    return None
locdata["the_geom"] = locdata["the_geom"].apply(poly)

locdata["total"] = locdata["total"].astype('int')
locdata = locdata[locdata['total'] != 0]
locdata = locdata[locdata["Shape_Leng"] != '']
locdata.geometry =  locdata['the_geom'].apply(loads)

shpfile["LocationID"] = shpfile["LocationID"].astype(int).astype(str)
locdata["LocationID"] = locdata["LocationID"].astype(float).astype(int).astype(str)


fig, ax = plt.subplots(1, 1, figsize=(10, 8))
shpfile["LocationID"] = shpfile["LocationID"].astype(int)
locdata["LocationID"] = locdata["LocationID"].astype(int)
shpfile.to_crs(epsg=4326).plot(ax = ax, color = 'lightgreen', aspect = 1)
locdata.set_crs(epsg=4326, inplace=True)
norm = Normalize(vmin=locdata["total"].min(), vmax=locdata["total"].max())
 
norm = LogNorm(vmin=locdata['total'].min(), vmax=locdata['total'].max())
sc = locdata.to_crs(epsg=4326).plot(ax=ax, column='total', cmap='YlOrRd', legend=True, norm=norm, ec='black')

plt.show()