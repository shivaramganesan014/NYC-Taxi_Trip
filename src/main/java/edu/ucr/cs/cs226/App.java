package edu.ucr.cs.cs226;

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
    public static void main( String[] args )
    {
        SparkConf conf = new SparkConf().setAppName("NYC_Trips").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        SparkSession spark = SparkSession.builder().getOrCreate();
        String filePath = "hdfs://localhost:9000/input/2023/2023/jan.parquet";
//
        Dataset<Row> parquetFile = spark.read().parquet(filePath);
//
        parquetFile.createOrReplaceTempView("tempView");
        Dataset<Row> rows = spark.sql("select * from tempView");
        rows.show();

//        JavaRDD<String> lines = sc.textFile("hdfs://localhost:9000/input/2023/jan.parquet");
//        System.out.println("Test Reading done here :: " + lines.count());
//        JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
//        int totalLength = lineLengths.reduce((a, b) -> a + b);
//        System.out.printf("Total length is %d\n", totalLength);

    }
}
