package edu.ucr.cs.cs226;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.connector.write.Write;

import javax.xml.crypto.Data;
import java.net.URI;
import java.util.List;

public class TimeSeriesUtil {
//    Total distance in each month
//    select sum(trip_distance) as total_distance, extract(month from to_timestamp(tpep_pickup_datetime, 'yyyy-mm-dd HH24:mi:ss')) as month from tripdata group by month limit 10;
//    Finding busiest time/month
    /*
    select
    genseries + INTERVAL '1 min' as end_time,
    count(*) as pickupcount
    from generate_series('2023-01-01 00:00:00', '2023-01-01 05:00:00', '1 min'::interval) genseries
    left join (select * from tripdata where tpep_pickup_datetime >= '2023-01-01 00:00:00' and tpep_pickup_datetime <= '2023-01-01 24:00:00' limit 100) as tempTable on to_timestamp(tempTable.tpep_pickup_datetime, 'yyyy-mm-dd HH24:mi:ss') >= genseries
    and
    to_timestamp(tempTable.tpep_pickup_datetime, 'yyyy-mm-dd HH24:mi:ss') < genseries + INTERVAL '1 min'
    where tempTable.tpep_pickup_datetime is not null
    group by genseries
    order by genseries;
    */

    //average speed of each driver - gorup by vendor id

    // group by location to find hotspot regions - pickup and dropoff

    public static final String DATE_TIME_FORMAT = "yyyy-mm-dd HH24:mi:ss";

    public static Dataset<Row> getTripDistanceVsAmount(String from, String to, Integer limit){
        Query q = new Query("tripdata");
        q.setColumns(new String[]{"trip_distance", "fare_amount", "tip_amount", "extra", "tolls_amount", "total_amount", "airport_fee", "improvement_surcharge", "congestion_surcharge", "mta_tax"});
        q.addFilter("tpep_pickup_datetime >= \'"+ from + "\'", "and");
        q.addFilter("tpep_pickup_datetime <= \'" + to + "\'", "and");
        q.setLimit(limit);
//        StringBuilder query = new StringBuilder("select trip_distance, fare_amount, tip_amount, extra, tolls_amount, total_amount, airport_fee from tripdata limit 10");
        Dataset<Row> ret = DBManager.getDataset(q.toString());
        WriterUtil.createProcess(Constants.DISTANCE_AMOUNT_RELATION, from, to, 0);
        return ret;

    }
    public static Dataset<Row> getPassengerCountVsTipAmount(String from, String to, Integer limit){
        Query q = new Query("tripdata");
        q.setColumns(new String[]{"passenger_count", "tip_amount", "total_amount"});
        q.addFilter("tpep_pickup_datetime >= \'"+ from + "\'", "and");
        q.addFilter("tpep_pickup_datetime <= \'" + to + "\'", "and");
        q.setLimit(limit);
//        StringBuilder query = new StringBuilder("select passenger_count, tip_amount from tripdata where to_timestamp(tpep_pickup_datetime, \'yyyy-mm-dd HH24:mi:ss\') >= \'2023-06-01 00:00:00\' limit 10");
        WriterUtil.createProcess(Constants.PASSENGER_TIP_RELATION, from, to, 0);
        return DBManager.getDataset(q.toString());
    }

    public static Dataset<Row> getDistanceWithNoPassenger(){
        //change group by column for PU and DO location id
        StringBuilder query = new StringBuilder("select sum(case when passenger_count = 0 then trip_distance else 0 end) as distance, pulocationid as pickuplocation from tripdata group by pulocationid limit 10");
        return DBManager.getDataset(query.toString());
    }

    public static Dataset<Row> getTripDistanceVsDuration(String from, String to, Integer limit){
        Query q = new Query("tripdata");
        q.setColumns(new String[]{"trip_distance", "to_timestamp(tpep_dropoff_datetime, \'"+DATE_TIME_FORMAT+"\') - to_timestamp(tpep_pickup_datetime, \'"+DATE_TIME_FORMAT+"\') as duration", "total_amount"});
        q.addFilter("tpep_pickup_datetime >= \'"+ from + "\'", "and");
        q.addFilter("tpep_pickup_datetime <= \'" + to + "\'", "and");
        q.setLimit(limit);
//        StringBuilder query = new StringBuilder("select trip_distance, to_timestamp(tpep_dropoff_datetime, \'yyyy-mm-dd HH24:mi:ss\') - to_timestamp(tpep_pickup_datetime, \'yyyy-mm-dd HH24:mi:ss\') as duration from tripdata limit 10");
        WriterUtil.createProcess(Constants.DISTANCE_DURATION_RELATION, from, to, 0);
        return DBManager.getDataset(q.toString());
    }

//    public static Dataset<Row> getInRange(String col, String from, String to){
//        //date format: yyyy-mm-dd hh:mm:ss
//        int fromYear = getYear(from);
//        int toYear = getYear(to);
//        Dataset<Row> rows = null;
//        try{
//            FileSystem fs = FileSystem.get(new URI(App.ROOT_DOMAIN), App.config);
//            String filePath = App.ROOT_DOMAIN+"/input/NYC";
//            RemoteIterator<LocatedFileStatus> rit = fs.listFiles(new Path(filePath), true);
//            while(rit.hasNext()){
//                LocatedFileStatus fileStatus = rit.next();
//
////                String name = fileStatus.getPath().getName();
//                String name = fileStatus.getPath().getParent().getName();
////                System.out.println("Current file :: " + name);
//                int currYear = 0;
//                try{
//                    currYear = Integer.parseInt(name);
//                }
//                catch (Exception e){
//                    System.out.println("error while reading file :: " + name + " :: " + "skipping");
//                    continue;
//                }
////                System.out.println("shiva :: curr year :: " + currYear);
////                System.out.println("shiva :: from and to year :: " + fromYear + " :: " + toYear);
//                if(currYear >= fromYear && currYear <= toYear){
//                    String currPath = filePath+"/"+name+"/";
////                    System.out.println("shiva :: reading " + currPath);
//                    RemoteIterator<LocatedFileStatus> monthlyData = fs.listFiles(new Path(currPath), false);
//                    while(monthlyData.hasNext()){
//                        String fName = monthlyData.next().getPath().getName();
////                        System.out.println("shiva :: reading :: " + fName);
//                        Query q = new Query();
//                        q.setFilter(col+" >= \'"+from + "\' and "+col+" <= \'" + to + "\'");
//                        Dataset<Row> yearRows = getDataset(currPath+fName, q);
//                        if(rows == null){
//                            rows = yearRows;
//                        }
//                        else{
//                            rows = rows.union(yearRows);
//                        }
//
//                    }
//                }
//            }
//        }
//        catch (Exception e){
//            System.out.println("Exception while getting file names ::: " + e.getLocalizedMessage());
//        }
//
//        return rows;
//
//    }

    private static Dataset<Row> getDataset(String filePath){
        return getDataset(filePath, (String[])null);
    }

    private static Dataset<Row> getDataset(String filePath, String[] columns){
        Query q = new Query();
        q.setColumns(columns);
        return getDataset(filePath, q);
    }

    private static Dataset<Row> getDataset(String filePath, Query q){
        Dataset<Row> parquetFile = App.spark.read().parquet(filePath);
        parquetFile.createOrReplaceTempView(q.getTableName());
        System.out.println("curr query :: " + q.toString());
        return App.spark.sql(q.toString());
//        return App.spark.sql("select * from "+q.getTableName()+" where ")
    }


    private static int getYear(String datetime){
        //date format: yyyy-mm-dd hh:mm:ss
        return Integer.parseInt(datetime.split(" ")[0].split("-")[0]);
    }
}
