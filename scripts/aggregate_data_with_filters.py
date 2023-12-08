import os
import time
import pandas as pd

start_time = time.time()

months = ["jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"]
years = ["2021", "2022", "2023"]
columns=['tpep_pickup_datetime', 'tpep_dropoff_datetime', 'passenger_count', 'trip_distance', 'PULocationID', 'DOLocationID', 'total_amount']

df = pd.DataFrame()

for year in years:
    dir = os.path.join('./all/', year)
    
    for month in months:
        file = os.path.join(dir, f"{month}.parquet")
        
        if os.path.exists(file):
            # read parquet in temporary df
            temp = pd.read_parquet(file, engine='pyarrow', columns=columns)

            # calculate datetime
            temp['pickup_datetime'] = pd.to_datetime(temp['tpep_pickup_datetime'])
            temp['dropoff_datetime'] = pd.to_datetime(temp['tpep_dropoff_datetime'])

            # determine day of week
            temp['is_weekend'] = temp.pickup_datetime.dt.dayofweek.isin([5, 6]).astype(int)

            # determine time of day
            temp['hour'] = temp.pickup_datetime.dt.hour
            choices = ['morning', 'afternoon', 'evening', 'night']
            temp['time_of_day'] = pd.cut(temp['hour'], bins=[0, 6, 12, 18, 24], labels=choices, include_lowest=True)

            # drop temporary columns
            temp = temp.drop(['tpep_pickup_datetime', 'tpep_dropoff_datetime', 'pickup_datetime', 'dropoff_datetime', 'hour'], axis=1)

            # add year column
            temp['year'] = int(year)

            # finally, add the temporary df in main df
            df = pd.concat([df, temp], ignore_index=True)

# replace null values with 0
df = df.dropna()

end_time = time.time()
total = end_time - start_time

# logs
print(f'total time to combine data: {total}')
print(f'dataframe column list: {df.columns.tolist()}') # attributes
print(f'dataframe size: {df.shape}') # size

grouped_pickup_df = df.groupby(['PULocationID', 'time_of_day', 'is_weekend', 'year']).agg(
    average_passenger_count=('passenger_count', 'mean'),
    average_total_amount=('total_amount', 'mean'),
    average_trip_distance=('trip_distance', 'mean'),
    total_trips=('passenger_count', 'size')
).reset_index()

print(f'grouped pickup aggregate data: {grouped_pickup_df}')

grouped_dropoff_df = df.groupby(['DOLocationID', 'time_of_day', 'is_weekend', 'year']).agg(
    average_passenger_count=('passenger_count', 'mean'),
    average_total_amount=('total_amount', 'mean'),
    average_trip_distance=('trip_distance', 'mean'),
    total_trips=('passenger_count', 'size')
).reset_index()

print(f'grouped dropoff aggregate data: {grouped_dropoff_df}')

# save the pickup and dropoff data
grouped_pickup_df.to_json('./all/pickups.json', orient='records')
grouped_dropoff_df.to_json('./all/dropoffs.json', orient='records')
