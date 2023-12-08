
import pandas as pd
import pyarrow
import time
import os

starttime = time.time()
years = ["2021", "2022", "2023"]
data = {}
for year in years:
  filepath = "drive/MyDrive/CS226-Final-Project/year-"+year+"/"
  files = os.listdir(filepath)
  map_data = {}
  for f in files:
    try:
      if "parquet" in f:
        mydf = pd.read_parquet(filepath+f, engine='pyarrow')
        month = f[:-8]
        mydf = mydf[mydf["passenger_count"] != 0]
        total = len(mydf)
        map_data[month] = total
    except:
      print("error")
    # map["month"] = {"distance": totaldistance, "cost": totalcost}
  endtime = time.time()
  totaltime = endtime - starttime
  data[year] = map_data
  print(totaltime)

import matplotlib.pyplot as plt
import numpy as np

years = list(data.keys())
months = list(data[years[0]].keys())

plt.figure(figsize=(12, 6))

colors = ['skyblue', 'salmon', 'lightgreen', 'orange', 'lightcoral']

bar_width = 0.35
bar_positions = np.arange(len(months))

for i, year in enumerate(years):
    trip_counts = [data[year].get(month, 0) for month in months]
    plt.bar(bar_positions + i * bar_width, trip_counts, bar_width, label=str(year), color=colors[i % len(colors)])

plt.xlabel('Month')
plt.ylabel('Number of Trips')
plt.title('Monthly Total Trips by Year')
plt.xticks(bar_positions + bar_width * (len(years) - 1) / 2, months)
plt.legend(title='Year')
plt.show()
