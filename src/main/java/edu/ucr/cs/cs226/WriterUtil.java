package edu.ucr.cs.cs226;

import org.apache.hadoop.util.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.collection.Seq;

import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WriterUtil {

    public static final int PROCESS_INPROGRESS = 0;
    public static final int PROCESS_COMPLETED = 1;

    private static final String ANALYSIS_ROOT_DIR = "analysis";

    public static void writeToFile(Dataset<Row> rows, String path, String from, String to){
        String analysis_id = from + "@" + to;
        String writePath = ANALYSIS_ROOT_DIR+"/"+path+"/"+analysis_id;
        deleteDir(new File(writePath));
        long x = rows.count();
        rows.coalesce(5).write().option("header", true).csv(writePath);
        PostActions.updateStatus(path, from, to, 1);
        PostActions.callPython();
    }

    private static boolean deleteDir(File f){
        if(f.isDirectory()){
            String[] child = f.list();
            for(String c : child){
                boolean success = deleteDir(new File(f, c));
                if(!success){
                    return false;
                }
            }
        }
        return f.delete();
    }

    public static Dataset<Row> getProcess(String relation, String from, String to){
        Query q = new Query("process");
        q.setColumns(new String[]{"status", "start_time", "end_time"});
        q.addFilter("relation = \'"+relation+"\'", "and");
        q.addFilter("relation_range_start = \'"+from+"\'", "and");
        q.addFilter("relation_range_end = \'"+to+"\'", "and");
        Dataset<Row> df = DBManager.getDataset(q.toString());
        df.show();
        return df;
    }

    public static void createProcess(String relation, String r_start, String r_end, int status){
        StructType schema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("relation", DataTypes.StringType, false),
                DataTypes.createStructField("relation_range_start", DataTypes.StringType, false),
                DataTypes.createStructField("relation_range_end", DataTypes.StringType, false),
                DataTypes.createStructField("status", DataTypes.IntegerType, false),
                DataTypes.createStructField("start_time", DataTypes.StringType, true),
                DataTypes.createStructField("end_time", DataTypes.StringType, true)
        });
        List<ProcessPOJO> process = new ArrayList();
        String endTime = null;
        String startTime = null;
        if(status == 1){
            endTime = System.currentTimeMillis()+"";
        }
        else{
            startTime = System.currentTimeMillis()+"";
        }
        process.add(new ProcessPOJO(relation, r_start, r_end, status, startTime, endTime));
        List<Row> lr = process.stream().map(p -> {
                    try {
                        return new ProcessToRowMapper().call(p);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        Dataset<Row> df = App.spark.newSession().createDataFrame(lr, schema);
        DBManager.insert(df, SaveMode.Append);
    }
}
