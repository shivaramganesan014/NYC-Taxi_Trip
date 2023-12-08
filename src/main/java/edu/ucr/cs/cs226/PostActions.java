package edu.ucr.cs.cs226;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostActions {

    public static void updateStatus(String relation, String from, String to, int status){

        WriterUtil.createProcess(relation, from, to, 1);

    }

    public static void updateCoordinates(String path){
        try{
            String command = "python3 ./scripts/update_coordinates.py "+path;
            System.out.println(command);
            Runtime.getRuntime().exec(command);
        }
        catch (Exception e){
            System.out.println("Error executing python file " + e.getLocalizedMessage());
        }
    }

    public static void performTipDistanceAnalysis(String path){
        try{
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "scripts/tip_dist_analysis.py");
            processBuilder.redirectErrorStream(true);

            Process p = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = p.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

//            String command = "python3 scripts/tip_dist_analysis.py";
//            Runtime.getRuntime().exec(command);
        }
        catch (Exception e){
            System.out.println("Error executing python file " + e.getLocalizedMessage());
        }
    }


    public static void callPython(String key){
        try{
            List<String> execList = Constants.EXEC_LIST.get(key);
            for(String script : execList){
                String command = "python3 scripts/"+script;
                Runtime.getRuntime().exec(command);
            }
//            String command = "python3 scripts/index.py";
//            Runtime.getRuntime().exec(command);
        }
        catch (Exception e){
            System.out.println("Error executing python file " + e.getLocalizedMessage());
        }
    }
}
