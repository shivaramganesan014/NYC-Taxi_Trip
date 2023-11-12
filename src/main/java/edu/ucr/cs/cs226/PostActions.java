package edu.ucr.cs.cs226;

import org.python.util.PythonInterpreter;

public class PostActions {

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
