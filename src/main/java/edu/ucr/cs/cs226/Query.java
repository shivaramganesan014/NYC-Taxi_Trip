package edu.ucr.cs.cs226;

public class Query {

    private String[] columns;
    private boolean selectAll;
    private String filter;

    private String tableName;

    private static final String TABLE_NAME = "TEMP_VIEW";

    public Query(){
        this.selectAll = true;
        this.tableName = TABLE_NAME;
    }
    public Query(String[] columns, String filter){
        this.columns = columns;
        this.filter = filter;
        this.tableName = TABLE_NAME;
    }
    public Query(String[] columns){
        this(columns, null);
    }

    public void setTableName(String name){
        this.tableName = name;
    }

    public String toString(){
        StringBuilder query = new StringBuilder("select ");
        if(selectAll || columns==null){
            query.append("* ");
        }
        else{
            for(String c : columns){
                query.append(c+", ");
            }
        }
        query.append(" from "+tableName);
        if(filter != null){
            query.append(" where " + filter);
        }
        return query.toString();
    }

    public String getTableName(){
        return this.tableName;
    }

    public void setColumns(String[] col){
        if(col == null){
            this.selectAll = true;
        }
        this.columns = col;
    }

    public void setFilter(String filter){
        this.filter = filter;
    }
}
