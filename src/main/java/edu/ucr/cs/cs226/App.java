package edu.ucr.cs.cs226;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public final static String ROOT_DOMAIN = "hdfs://localhost:9000";
    public final static String ROOT_DIR = ROOT_DOMAIN+"/input/NYC/";

    public static final int MAX_ITERATIONS = 100;
    public static final long BATCH_SIZE = 100000;

//    public static SparkConf conf = new SparkConf().setAppName("NYC_Trips").setMaster("local");
//    public static SparkContext context = new SparkContext(conf);

//    public static Configuration config = context.hadoopConfiguration();

    public static SparkSession spark = SparkSession.builder()
            .appName("NYC_Analysis")
            .config("spark.dynamicAllocation.enabled", true)
            .config("spark.dynamicAllocation.minExecutors","1")
            .config("spark.dynamicAllocation.maxExecutors","5")
            .config("spark.master", "local")
            .config("spark.driver.memory", "5g")
            .config("spark.executor.instances", "4")
            .config("spark.executor.cores", "4")
            .config("spark.executor.memory", "3g")
            .getOrCreate();

    public static Properties props = new Properties();
    public static void main( String[] args )
    {
//        Dataset<Row> parquetFile = spark.read().parquet(filePath);
////
//        parquetFile.createOrReplaceTempView("tempView");
//        Dataset<Row> rows = spark.sql("select * from tempView");
//        rows.show();

        props.setProperty("Driver", "org.postgresql.Driver");

        post("/avg_tip_distance_analysis", (request, response) -> {
            JSONObject obj = new JSONObject(request.body());
            String year = obj.optString("year");
            Integer inYear = year.isEmpty() ? null : Integer.parseInt(year);
            String tempAnalysisId = inYear == null ? "full" : year;
            Dataset<Row> exdf = WriterUtil.getProcess(Constants.AVG_ANALYSIS, tempAnalysisId, tempAnalysisId);
            if(exdf.count() == 1){
                return "An existing analysis for the given relation is running";
            }

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Dataset<Row> df = TimeSeriesUtil.getAverageAnalysis(inYear);
                    WriterUtil.writeToFile(df, Constants.AVG_ANALYSIS, year==null ? "full" : year+"", year==null ? "full" : year+"", 1);
                    PostActions.updateStatus(Constants.DISTANCE_AMOUNT_RELATION, year==null ? "full" : year+"", year==null ? "full" : year+"", 1);
                    PostActions.callPython();
                }
            });
            t.start();

            return "analysis started";

        });


        post("/distance_amount_analysis", (request, response) -> {
            JSONObject obj = new JSONObject(request.body());
            String from = obj.optString("from");
            String to = obj.optString("to");
            Dataset<Row> exdf = WriterUtil.getProcess(Constants.DISTANCE_AMOUNT_RELATION, from, to);
            if(exdf.count() == 1){
                return "An exisiting analysis is running for the relation for the given time range";
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    long offset = 0;
                    long currItr = 0;

                    while(currItr < MAX_ITERATIONS){
                        Dataset<Row> rows = TimeSeriesUtil.getTripDistanceVsAmount(from, to, BATCH_SIZE, offset).coalesce(10);
                        offset += BATCH_SIZE;

                        if(rows.isEmpty()){
                            System.out.println("=====Breaking the loop as the rows is empty=====");
                            break;
                        }
                        WriterUtil.writeToFile(rows, Constants.DISTANCE_AMOUNT_RELATION, from, to, currItr);
                        currItr+=1;
                        System.gc();
                    }
                    PostActions.updateStatus(Constants.DISTANCE_AMOUNT_RELATION, from, to, 1);
                    PostActions.callPython();
                }
            });
            t.start();
            return "content will be written to "+Constants.FILE_PATH_MAP.get(Constants.DISTANCE_AMOUNT_RELATION);
        });

        post("/count_tip_analysis", (request, response) -> {
            JSONObject obj = new JSONObject(request.body());
            String from = obj.optString("from");
            String to = obj.optString("to");
            Dataset<Row> exdf = WriterUtil.getProcess(Constants.PASSENGER_TIP_RELATION, from, to);
            if(exdf.count() == 1){
                return "An exisiting analysis is running for the relation for the given time range";
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    long offset = 0;
                    long currItr = 0;

                    while(currItr < MAX_ITERATIONS){
                        Dataset<Row> rows = TimeSeriesUtil.getPassengerCountVsTipAmount(from, to, BATCH_SIZE, offset).coalesce(10);
                        offset += BATCH_SIZE;

                        if(rows.isEmpty()){
                            System.out.println("=====Breaking the loop as the rows is empty=====");
                            break;
                        }
                        WriterUtil.writeToFile(rows, Constants.PASSENGER_TIP_RELATION, from, to, currItr);
                        currItr+=1;
                        System.gc();

                    }
                    PostActions.updateStatus(Constants.PASSENGER_TIP_RELATION, from, to, 1);
                    PostActions.callPython();
//                    Dataset<Row> rows = TimeSeriesUtil.getPassengerCountVsTipAmount(from, to, null, null);
////                    WriterUtil.writeToFile(rows, Constants.PASSENGER_TIP_RELATION, from, to);
                }
            });
            t.start();
            return "content will be written to "+Constants.FILE_PATH_MAP.get(Constants.PASSENGER_TIP_RELATION);
        });

        post("/distance_duration_relation", (request, response) -> {
            JSONObject obj = new JSONObject(request.body());
            String from = obj.optString("from");
            String to = obj.optString("to");
            Dataset<Row> exdf = WriterUtil.getProcess(Constants.DISTANCE_DURATION_RELATION, from, to);
            if(exdf.count() == 1){
                return "An exisiting analysis is running for the relation for the given time range";
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    long offset = 0;
                    long currItr = 0;

                    while(currItr < MAX_ITERATIONS){
                        Dataset<Row> rows = TimeSeriesUtil.getTripDistanceVsDuration(from, to, BATCH_SIZE, offset).coalesce(10);
                        offset += BATCH_SIZE;

                        if(rows.isEmpty()){
                            System.out.println("=====Breaking the loop as the rows is empty=====");
                            break;
                        }
                        WriterUtil.writeToFile(rows, Constants.DISTANCE_DURATION_RELATION, from, to, currItr);
                        currItr+=1;
                        System.gc();

                    }
                    PostActions.updateStatus(Constants.DISTANCE_DURATION_RELATION, from, to, 1);
                    PostActions.callPython();
//                    Dataset<Row> rows = TimeSeriesUtil.getTripDistanceVsDuration(from, to, null, null);
////                    WriterUtil.writeToFile(rows, Constants.DISTANCE_DURATION_RELATION, from, to);
                }
            });
            t.start();
            return "content will be written to "+Constants.FILE_PATH_MAP.get(Constants.PASSENGER_TIP_RELATION);
        });

        post("/busiest_location", (request, response) -> {

            JSONObject obj = new JSONObject(request.body());
            String pickup = obj.optString("pickup");
            boolean pu = Boolean.valueOf(pickup);
            String year = obj.optString("year");
            String interval = obj.optString("interval");
            Dataset<Row> exdf = WriterUtil.getProcess(pu ? Constants.PU_BUSIEST_LOCATION : Constants.DO_BUSIEST_LOCATION, year, interval);
            if(exdf.count() == 1){
                return "An exisiting analysis is running for the relation for the given time range";
            }

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Dataset<Row> df = TimeSeriesUtil.getBusiestLocation(pu, Integer.parseInt(year), interval);
                    String analysisId = pu ? Constants.PU_BUSIEST_LOCATION : Constants.DO_BUSIEST_LOCATION;
                    WriterUtil.writeToFile(df, analysisId, year, interval, 1);
                    String dirName = year+"@"+interval;
                    System.gc();
                    PostActions.updateStatus(analysisId, year, interval, 1);
                    if(pu){
                        PostActions.updateCoordinates("analysis/"+analysisId+"/"+dirName+"/");
                    }
                    else{
                        PostActions.updateCoordinates("analysis/"+analysisId+"/"+dirName+"/");
                    }
                    PostActions.callPython();
                }
            });
            t.start();

            return "Analysis started.";

        });

        post("/avgTipDistance", (request, response) -> {
            JSONObject obj = new JSONObject(request.body());
            String year = obj.optString("year");
            Integer inYear = year.isEmpty() ? null : Integer.parseInt(year);
            String tempAnalysisId = inYear == null ? "full" : year;
            Dataset<Row> exdf = WriterUtil.getProcess(Constants.AVG_TIP_DISTANCE, tempAnalysisId, tempAnalysisId);
            if(exdf.count() == 1){
                return "An exisiting analysis is running for the relation for the given time range";
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Dataset<Row> df = TimeSeriesUtil.getAverageTipDistanceByYear(inYear);
                    WriterUtil.writeToFile(df, Constants.AVG_TIP_DISTANCE, inYear == null ? "full" : year, inYear == null ? "full" : year, 1);
                    String dirName = tempAnalysisId+"@"+tempAnalysisId;
                    System.gc();
                    PostActions.updateStatus(Constants.AVG_TIP_DISTANCE, tempAnalysisId, tempAnalysisId, 1);
//                    PostActions.performTipDistanceAnalysis(dirName);
//                    if(pu){
//                        PostActions.updateCoordinates("analysis/"+analysisId+"/"+dirName+"/");
//                    }
//                    else{
//                        PostActions.updateCoordinates("analysis/"+analysisId+"/"+dirName+"/");
//                    }
                    PostActions.callPython();
                }
            });
            t.start();

            return "Analysis started";
        });





//        Dataset<Row> rows = DBManager.getDataset("select * from \"TripData\" limit 2");
//        Dataset<Row> rows = TimeSeriesUtil.getTripDistanceVsDuration();
//        Dataset<Row> rows = TimeSeriesUtil.getDistanceWithNoPassenger();
//        rows.toString();

//        rows.show();



//        Dataset<Row> rows = TimeSeriesUtil.getInRange("tpep_pickup_datetime", "2022-06-12 2:00:00", "2022-07-12 1:00:00");
//        if(rows != null){
//            rows = rows.filter("passenger_count = 0");
//            rows.show();
//        }
//        else {
//            System.out.println("Empty Dataset returned");
//        }



//        JavaRDD<String> lines = sc.textFile("hdfs://localhost:9000/input/2023/jan.parquet");
//        System.out.println("Test Reading done here :: " + lines.count());
//        JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
//        int totalLength = lineLengths.reduce((a, b) -> a + b);
//        System.out.printf("Total length is %d\n", totalLength);

    }
}
