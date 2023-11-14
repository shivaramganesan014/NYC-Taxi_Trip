package edu.ucr.cs.cs226;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriterUtil {

    private static final String ANALYSIS_ROOT_DIR = "analysis";

    public static void writeToFile(Dataset<Row> rows, String path, String analysis_id){
        String writePath = ANALYSIS_ROOT_DIR+"/"+path+"/"+analysis_id;
        deleteDir(new File(writePath));
        long x = rows.count();
        rows.coalesce(5).write().option("header", true).csv(writePath);
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
}
