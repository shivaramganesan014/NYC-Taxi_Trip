package edu.ucr.cs.cs226;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
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

    public static void insert(Dataset<Row> df, SaveMode saveMode){
        Properties props = new Properties();
        props.setProperty("connectionURL", POSTGRESQL_URL);
        props.setProperty("driver", "org.postgresql.Driver");
        props.setProperty("user", "shiva");
        props.setProperty("password", "shiva");

        df.write().option("url", POSTGRESQL_URL)
                .option("connectionURL", POSTGRESQL_URL)
                .option("driver", "org.postgresql.Driver")
                .mode(saveMode)
                .option("user", "shiva")
                .option("password", "shiva")
                .jdbc(POSTGRESQL_URL, "process", props);

    }

    public static void update(Dataset<Row> df){
        Properties props = new Properties();
        props.setProperty("connectionURL", POSTGRESQL_URL);
        props.setProperty("driver", "org.postgresql.Driver");
        props.setProperty("user", "shiva");
        props.setProperty("password", "shiva");

        df.write().option("url", POSTGRESQL_URL)
                .option("connectionURL", POSTGRESQL_URL)
                .option("driver", "org.postgresql.Driver")
                .mode(SaveMode.Overwrite)
                .option("user", "shiva")
                .option("password", "shiva")
                .jdbc(POSTGRESQL_URL, "process", props);

    }
}
