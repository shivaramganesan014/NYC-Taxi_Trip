package edu.ucr.cs.cs226;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriterUtil {

    public static void writeToFile(Dataset<Row> rows, String path){
        deleteDir(new File(path));
        rows.coalesce(1).write().option("header", true).csv(path);
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
