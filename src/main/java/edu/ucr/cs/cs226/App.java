package edu.ucr.cs.cs226;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

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
    public static void main( String[] args )
    {
//        Dataset<Row> parquetFile = spark.read().parquet(filePath);
////
//        parquetFile.createOrReplaceTempView("tempView");
//        Dataset<Row> rows = spark.sql("select * from tempView");
//        rows.show();

        Dataset<Row> rows = TimeSeriesUtil.getInRange("2022-06-12 2:00:00", "2022-07-12 1:00:00");
        if(rows != null){
            rows.show();
        }
        else {
            System.out.println("Empty Dataset returned");
        }


//        JavaRDD<String> lines = sc.textFile("hdfs://localhost:9000/input/2023/jan.parquet");
//        System.out.println("Test Reading done here :: " + lines.count());
//        JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
//        int totalLength = lineLengths.reduce((a, b) -> a + b);
//        System.out.printf("Total length is %d\n", totalLength);

    }
}
