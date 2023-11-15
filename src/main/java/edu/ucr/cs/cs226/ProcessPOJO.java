package edu.ucr.cs.cs226;

import java.sql.Timestamp;

public class ProcessPOJO {

    String relation;
    String relation_range_start;
    String relation_range_end;
    int status;
    String start_time;
    String end_time;

    public ProcessPOJO(String relation,String r_start, String r_end, int status, String start_time, String end_time){
        this.relation = relation;
        this.start_time = start_time;
        this.end_time = end_time;
        this.status = status;
        this.relation_range_start = r_start;
        this.relation_range_end = r_end;
    }



}
