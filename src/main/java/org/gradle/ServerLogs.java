package org.gradle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class ServerLogs {
	final static Logger LOG = LoggerFactory.getLogger(ServerLogs.class);
	static String line;
	static String line1;
	static Long timestamp_started;
	static Long timestamp_finished;
		public static void main(String[] args)    throws IOException {
			LOG.info("Main Method:");
			ArrayList<JSONObject> json=new ArrayList<JSONObject>();
            	 JSONObject obj,obj1 = null;
            	    // The name of the file to open.
            	    String fileName = "/Users/a212604433/Documents/test/ServerLogsCreditSuisse/src/main/resources/org/gradle/Logs.json";
            	    //String fileName1 = "/Users/a212604433/Documents/test/ServerLogsCreditSuisse/src/main/resources/org/gradle/Logs.json";
            	    // This will reference one line at a time
            	    // FileReader reads text files in the default encoding.
            	        FileReader fileReader = new FileReader(fileName);
            	        FileReader fileReader1 = new FileReader(fileName);
            	        // Always wrap FileReader in BufferedReader.
            	        BufferedReader bufferedReader = new BufferedReader(fileReader);
            	        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            	        String id,state;
            	        try {
            	        while((line = bufferedReader.readLine()) != null) {
            	            obj = (JSONObject) new JSONParser().parse(line);
            	            json.add(obj);
            	            id=(String)obj.get("id");
            	            state=(String)obj.get("state");
            	            timestamp_started = (Long)obj.get("timestamp");
            	            LOG.info("Main Loop Values");
            	            LOG.info(id +","+ state + "," + timestamp_started);
            	            			while((line1 = bufferedReader1.readLine()) != null) {
            	            				getInnerLoopValues(obj1 , id ,timestamp_started, line1);  //for innerloop which will check for the id 
		            	   }
            	          }
            	        // Always close files.
            	        bufferedReader.close();   
            	        bufferedReader1.close();
            	        }
            	    catch(FileNotFoundException ex) {
            	        System.out.println("Unable to open file '" + fileName + "'");                
            	    }
            	    catch(IOException ex) {
            	        System.out.println("Error reading file '" + fileName + "'");                  
            	        // Or we could just do this: 
            	        // ex.printStackTrace();
            	    } catch (ParseException e) {
            	        // TODO Auto-generated catch block
            	        e.printStackTrace();
            	    }
            }

	private static void getInnerLoopValues(JSONObject obj1, String id, Long timestamp_started,String line1) {
			// TODO Auto-generated method stub
		 LOG.info("Inside Inner Loop - Inner Loop Values");
			 ArrayList<JSONObject> json1=new ArrayList<JSONObject>();
			try {
				obj1 = (JSONObject) new JSONParser().parse(line1);
				json1.add(obj1);
				LOG.info((String)obj1.get("id") +","+ (String)obj1.get("state")+ "," + (Long)obj1.get("timestamp"));
            	 	if(id.equalsIgnoreCase((String)obj1.get("id")) && ("FINISHED").equalsIgnoreCase((String)obj1.get("state"))) 
            	 		{
            	 			timestamp_finished = (Long)(obj1.get("timestamp"));
            	 			long duration = timestamp_finished.longValue() - timestamp_started.longValue();
            	 				if(duration> 4) {
            	 					LOG.info("Alert :greater than 4");
            	 					String type=(String) obj1.get("type");
            	 					String host= (String) obj1.get("host");
            	 					checkforValues(id,type,host,duration);  //will check whether type and host is not null
            	 					  //save the data is a  location
            	 								}
            	 		}
            	 	
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	private static void checkforValues(String id, String type, String host, long duration) {
			// TODO Auto-generated method stub
			if(type!=null && host!=null) {
				try {
					saveLogData(id,type,host,duration);  //save the data in table where duration is greater than 4
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}

	private static void saveLogData(String id, String type, String host, long duration) throws IOException {
			// TODO Auto-generated method stub
			Calendar cal = Calendar.getInstance();
			LOG.info("table saved in /..../..../Documents/test/ServerLogsCreditSuisse/Logs_" +cal.getTime()+ ".docx"); 
			LOG.info("Logs for Date - >" +cal.getTime());
			XWPFDocument document = new XWPFDocument();
			XWPFTable tableOne = document.createTable();
	        XWPFTableRow tableOneRowOne = tableOne.getRow(0);
	        tableOneRowOne.getCell(0).setText("ID:");
	        tableOneRowOne.addNewTableCell().setText("Duration");
	        tableOneRowOne.addNewTableCell().setText("Host");
	        tableOneRowOne.addNewTableCell().setText("Type");
	        XWPFTableRow tableOneRowTwo = tableOne.createRow();
	        tableOneRowTwo.getCell(0).setText(id);
	        tableOneRowTwo.getCell(1).setText(Long.toString(duration) + "(Alert)");
	        tableOneRowTwo.getCell(2).setText(type);
	        tableOneRowTwo.getCell(3).setText(host);
	        FileOutputStream outStream = new FileOutputStream("Logs_"+cal.getTime()+ ".docx");
	        document.write(outStream);
	        outStream.close();
	 
		}
}
		
	

