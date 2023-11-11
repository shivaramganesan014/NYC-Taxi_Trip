package edu.ucr.cs.cs226;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONObject;

import java.util.Properties;

public class DBManager {

    public static final String POSTGRESQL_URL = "jdbc:postgresql://localhost:5432/demo2";

    public static Dataset<Row> getDataset(String query){
//        return App.spark.read().jdbc(POSTGRESQL_URL, query, App.props);
        return App.spark.read()
                .format("jdbc")
                .option("url", POSTGRESQL_URL)
//                .option("dbtable", "\"TripData\"")
                .option("user", "shiva")
                .option("password", "shiva")
                .option("query", query)
                .load();
    }

    public static String toJSON(Row r){
//        JSONObject json = new JSONObject();
        return r.toString();
//        return json;
    }


}
