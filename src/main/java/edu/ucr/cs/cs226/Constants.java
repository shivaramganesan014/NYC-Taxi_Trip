package edu.ucr.cs.cs226;

import java.util.HashMap;

public class Constants {

    public static final String PASSENGER_TIP_RELATION = "passenger_tip_relation";
    public static final String DISTANCE_AMOUNT_RELATION = "distance_amount_relation";
    public static final String DISTANCE_DURATION_RELATION = "distance_duration_relation";
    public static final String PU_BUSIEST_LOCATION = "pu_busiest_location";
    public static final String DO_BUSIEST_LOCATION = "do_busiest_location";
    public static final String AVG_TIP_DISTANCE = "avg_tip_distance";
    public static final String AVG_ANALYSIS = "avg_tip_distance_analysis";

    public static final HashMap<String, String> FILE_PATH_MAP = new HashMap(){{
        put(PASSENGER_TIP_RELATION, PASSENGER_TIP_RELATION);
        put(DISTANCE_AMOUNT_RELATION, DISTANCE_AMOUNT_RELATION);
        put(DISTANCE_DURATION_RELATION, DISTANCE_DURATION_RELATION);
        put(PU_BUSIEST_LOCATION, PU_BUSIEST_LOCATION);
        put(DO_BUSIEST_LOCATION, DO_BUSIEST_LOCATION);
        put(AVG_TIP_DISTANCE, AVG_TIP_DISTANCE);
        put(AVG_ANALYSIS, AVG_ANALYSIS);
    }};
}
