package edu.ucr.cs.cs226;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

public class ProcessToRowMapper implements MapFunction<ProcessPOJO, Row> {

    @Override
    public Row call(ProcessPOJO processPOJO) throws Exception {
        Row r = RowFactory.create(
                processPOJO.relation,
                processPOJO.relation_range_start,
                processPOJO.relation_range_end,
                processPOJO.status,
                processPOJO.start_time,
                processPOJO.end_time
        );
        return r;
    }
}
