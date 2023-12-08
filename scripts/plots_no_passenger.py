
import pandas as pd
import pyarrow
import time
import os


starttime = time.time()
year = "2023"
filepath = "drive/MyDrive/CS226-Final-Project/year-"+year+"/"
files = os.listdir(filepath)
map_data = {"month": [], "distance": [], "cost": []}
for f in files:
  try:
    if "parquet" in f:
      mydf = pd.read_parquet(filepath+f, engine='pyarrow')
      month = f[:-8]
      mydf = mydf[mydf["passenger_count"] == 0]
      mydf = mydf[mydf["trip_distance"] > 0]
      totaldistance = mydf.trip_distance.sum()
      totalcost = mydf.fare_amount.sum()
      map_data["month"].append(month)
      map_data["distance"].append(totaldistance)
      map_data["cost"].append(totalcost)
  except:
    print("error")
  # map["month"] = {"distance": totaldistance, "cost": totalcost}
endtime = time.time()
totaltime = endtime - starttime
print(totaltime)

df = pd.DataFrame(map_data)

df.head(15)

max(df["cost"])

def barPlot():
  map_df.plot(x='month', y=['distance', 'cost'], kind='bar', stacked=True)
  plt.title('Total Distance and Cost for Each Month' + "("+year+")")
  plt.xlabel('Month')
  plt.ylabel('Cost')
  plt.show()

def linePlot():
  map_df.plot(x='month', y=['distance', 'cost'], marker='o')
  plt.title('Trend of Distance and Cost Over Months' + "("+year+")")
  plt.xlabel('Month')
  plt.ylabel('Value')
  plt.show()

def scatterPlot():
  plt.scatter(map_df['distance'], map_df['cost'], color='blue', marker='o')
  plt.title('Distance vs. Cost' + "("+year+")")
  plt.xlabel('Total Distance')
  plt.ylabel('Total Cost')
  plt.grid(True)
  plt.show()

def piePlot():
  plt.pie(map_df['distance'], labels=map_df['month'], autopct='%1.1f%%', startangle=90)
  plt.title('Proportion of Total Distance by Month' + "("+year+")")
  plt.show()

import seaborn as sns
def boxPlot():
  sns.boxplot(x='variable', y='value', data=pd.melt(map_df[['distance', 'cost']]))
  plt.title('Distribution of Distance and Cost' + "("+year+")")
  plt.xlabel('Variable')
  plt.ylabel('Value')
  plt.show()

import matplotlib.pyplot as plt
import numpy as np
from sklearn.preprocessing import MinMaxScaler


map_df = pd.DataFrame(map_data)

plt.figure(figsize=(10, 6))
barPlot()
linePlot()
scatterPlot()
piePlot()
boxPlot()