package edu.ucr.cs.cs226;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.net.URI;
import java.util.List;

public class TimeSeriesUtil {

    public static Dataset<Row> getInRange(String col, String from, String to){
        //date format: yyyy-mm-dd hh:mm:ss
        int fromYear = getYear(from);
        int toYear = getYear(to);
        Dataset<Row> rows = null;
        try{
            FileSystem fs = FileSystem.get(new URI(App.ROOT_DOMAIN), App.config);
            String filePath = App.ROOT_DOMAIN+"/input/NYC";
            RemoteIterator<LocatedFileStatus> rit = fs.listFiles(new Path(filePath), true);
            while(rit.hasNext()){
                LocatedFileStatus fileStatus = rit.next();

//                String name = fileStatus.getPath().getName();
                String name = fileStatus.getPath().getParent().getName();
//                System.out.println("Current file :: " + name);
                int currYear = 0;
                try{
                    currYear = Integer.parseInt(name);
                }
                catch (Exception e){
                    System.out.println("error while reading file :: " + name + " :: " + "skipping");
                    continue;
                }
//                System.out.println("shiva :: curr year :: " + currYear);
//                System.out.println("shiva :: from and to year :: " + fromYear + " :: " + toYear);
                if(currYear >= fromYear && currYear <= toYear){
                    String currPath = filePath+"/"+name+"/";
//                    System.out.println("shiva :: reading " + currPath);
                    RemoteIterator<LocatedFileStatus> monthlyData = fs.listFiles(new Path(currPath), false);
                    while(monthlyData.hasNext()){
                        String fName = monthlyData.next().getPath().getName();
//                        System.out.println("shiva :: reading :: " + fName);
                        Query q = new Query();
                        q.setFilter(col+" >= \'"+from + "\' and "+col+" <= \'" + to + "\'");
                        Dataset<Row> yearRows = getDataset(currPath+fName, q);
                        if(rows == null){
                            rows = yearRows;
                        }
                        else{
                            rows = rows.union(yearRows);
                        }

                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Exception while getting file names ::: " + e.getLocalizedMessage());
        }

        return rows;

    }

    public static Dataset<Row> getDataset(String filePath){
        return getDataset(filePath, (String[])null);
    }

    public static Dataset<Row> getDataset(String filePath, String[] columns){
        Query q = new Query();
        q.setColumns(columns);
        return getDataset(filePath, q);
    }

    private static Dataset<Row> getDataset(String filePath, Query q){
        Dataset<Row> parquetFile = App.spark.read().parquet(filePath);
        parquetFile.createOrReplaceTempView(q.getTableName());
        System.out.println("curr query :: " + q.toString());
        return App.spark.sql(q.toString());
//        return App.spark.sql("select * from "+q.getTableName()+" where ")
    }


    private static int getYear(String datetime){
        //date format: yyyy-mm-dd hh:mm:ss
        return Integer.parseInt(datetime.split(" ")[0].split("-")[0]);
    }
}
