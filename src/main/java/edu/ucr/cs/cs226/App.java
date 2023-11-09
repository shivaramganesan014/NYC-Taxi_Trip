package edu.ucr.cs.cs226;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public final static String ROOT_DOMAIN = "hdfs://localhost:9000";
    public final static String ROOT_DIR = ROOT_DOMAIN+"/input/NYC/";

    public static SparkConf conf = new SparkConf().setAppName("NYC_Trips").setMaster("local");
    public static SparkContext context = new SparkContext(conf);
    public static Configuration config = context.hadoopConfiguration();

    public static SparkSession spark = SparkSession.builder().getOrCreate();

    public static Properties props = new Properties();
    public static void main( String[] args )
    {
//        Dataset<Row> parquetFile = spark.read().parquet(filePath);
////
//        parquetFile.createOrReplaceTempView("tempView");
//        Dataset<Row> rows = spark.sql("select * from tempView");
//        rows.show();

        props.setProperty("Driver", "org.postgresql.Driver");

        Dataset<Row> rows = DBManager.getDataset("select * from \"TripData\" limit 2");
        rows.show();

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
