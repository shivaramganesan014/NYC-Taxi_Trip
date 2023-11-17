package edu.ucr.cs.cs226;

public class Query {

    private String[] columns;
    private boolean selectAll;
    private String filter;

    private String tableName;

    private Long limit;
    private Long offset;

    private String groupByCol;

    private String sortByCol;

    private static final String TABLE_NAME = "TEMP_VIEW";

    public Query(){
        this((String) null);
    }
    public Query(String tableName){
        this.selectAll = true;
        this.tableName = TABLE_NAME;
        if(tableName != null){
            this.tableName = tableName;
        }
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
            query.delete(query.length()-2 ,query.length()-1);
        }
        query.append(" from "+tableName);
        if(filter != null){
            query.append(" where " + filter);
        }

        if(this.groupByCol!=null){
            query.append(" group by " + this.groupByCol);
        }

        if(this.sortByCol!=null){
            query.append(" sort by " + this.sortByCol);
        }

        if(this.limit != null){
            query.append(" limit " + this.limit);
        }
        if(this.offset != null){
            query.append(" offset " + this.offset);
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
        this.selectAll = false;
    }

    public void addFilter(String filter, String op){
        if(this.filter == null || this.filter.isEmpty()){
            this.filter = filter;
        }
        else{this.filter += " " + op + " " + filter;}
    }

    public void setLimit(Long limit){
        this.limit = limit;
    }

    public void setOffset(Long offset){
        this.offset = offset;
    }

    public void setGroupByCol(String groupBy){
        this.groupByCol = groupBy;
    }

    public void setSortByCol(String sortBy){
        this.sortByCol = sortBy;
    }

    public void setFilter(String filter){
        this.filter = filter;
    }
}
