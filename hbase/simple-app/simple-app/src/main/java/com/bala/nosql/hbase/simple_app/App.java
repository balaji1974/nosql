package com.bala.nosql.hbase.simple_app;

import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException, ServiceException
    {
       // System.out.println( "Hello World!" );
       // new App().connect();
    	
    	Connection conn = null;
    	try {
    	    Configuration conf = HBaseConfiguration.create();
    	    conf.set("hbase.zookeeper.quorum", "127.0.0.1");
    	    conf.set("hbase.zookeeper.property.clientPort", "2181"); //2181 is the default port
    	    conn = ConnectionFactory.createConnection(conf);
    	    Table table = null;
    	    try {
    	        table = conn.getTable(TableName.valueOf("tableName"));
    	        Get g = new Get(Bytes.toBytes("rowId"));
    	        Result result = table.get(g);
    	        byte [] name = result.getValue(Bytes.toBytes("columnType"),Bytes.toBytes("columnName"));
    	        System.out.println("name: " + Bytes.toString(name));
    	    } catch (TableNotFoundException tnfe) {
    	        throw new RuntimeException("HBase table not found.", tnfe);
    	    } catch (IOException ioe) {
    	        throw new RuntimeException("Cannot create connection to HBase.", ioe);
    	    }
    	    finally {
    	        if (table != null) {
    	            try {
    	                table.close();
    	            } catch (IOException e) {
    	               e.printStackTrace();;
    	            }
    	        }
    	    }

    	} 
    	finally {
    	    if (conn != null) {
    	        try {
    	            conn.close();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	    }
    	}
    	
    	
    }
    
    private void connect() throws IOException, ServiceException {
        Configuration config = HBaseConfiguration.create();
        System.out.println("Here1");
        String path = this.getClass().getClassLoader().getResource("hbase-site.xml").getPath();
        System.out.println("Here2");
        
        config.addResource(new Path(path));

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (MasterNotRunningException e) {
            System.out.println("HBase is not running." + e.getMessage());
            return;
        }

        HBaseClientOperations HBaseClientOperations = new HBaseClientOperations();
        HBaseClientOperations.run(config);
    }
}
