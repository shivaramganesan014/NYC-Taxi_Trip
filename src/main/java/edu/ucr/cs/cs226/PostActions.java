package edu.ucr.cs.cs226;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.python.util.PythonInterpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostActions {

    public static void updateStatus(String relation, String from, String to, int status){

        WriterUtil.createProcess(relation, from, to, 1);


    }


    public static void callPython(){
        try{
            String command = "python3 scripts/index.py";
            Runtime.getRuntime().exec(command);
        }
        catch (Exception e){
            System.out.println("Error executing python file " + e.getLocalizedMessage());
        }
    }
}
