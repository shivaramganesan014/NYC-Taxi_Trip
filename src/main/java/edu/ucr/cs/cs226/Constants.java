package edu.ucr.cs.cs226;

import java.util.HashMap;

public class Constants {

    public static final String PASSENGER_TIP_RELATION = "passenger_tip_relation";
    public static final String DISTANCE_AMOUNT_RELATION = "distance_amount_relation";
    public static final String DISTANCE_DURATION_RELATION = "distance_duration_relation";

    public static final HashMap<String, String> FILE_PATH_MAP = new HashMap(){{
        put(PASSENGER_TIP_RELATION, PASSENGER_TIP_RELATION);
        put(DISTANCE_AMOUNT_RELATION, DISTANCE_AMOUNT_RELATION);
        put(DISTANCE_DURATION_RELATION, DISTANCE_DURATION_RELATION);
    }};
}
