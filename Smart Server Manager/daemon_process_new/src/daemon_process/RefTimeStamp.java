package daemon_process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RefTimeStamp {

	public static String getRefTimestamp() {
		
		DataOutputStream dataOut = null;
		BufferedReader in =null;
		JSONArray jarr = new JSONArray();
		String timestamp = null;
		
		try {

		  URL urlObj = new URL("https://hanai331963trial.hanatrial.ondemand.com/SRVMANAGER/SERVICES/UpdMonDBGetRefDate.xsjs");
		  HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		  //setting request method
		  connection.setRequestMethod("GET");

		  //adding headers
		  connection.setRequestProperty("Accept","application/json");
		  
		  //convert username:password to base64
		  connection.setRequestProperty("Authorization","Basic U0hTOlBhc3N3b3JkMSM=");

		  connection.setDoInput(true);

		  //sending POST request
		  connection.setDoOutput(true);
		 
		  int responseCode = connection.getResponseCode();
		  in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		  String inputLine;
		  StringBuffer response = new StringBuffer();
		  while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		  }
		  
		  JSONParser parser = new JSONParser();
		  JSONObject json = (JSONObject) parser.parse(response.toString());
		  
		  JSONArray jarray = (JSONArray) json.get("results");
		  JSONObject j = (JSONObject) jarray.get(0);
		  timestamp = j.get("timestamp").toString();
		  	  
		} catch (Exception e) {
		  //do something with exception
		  e.printStackTrace();
		} finally {
		  try {
		    if(dataOut != null) {
		      dataOut.close();
		    }
		    if(in != null) {
		      in.close();
		    }

		  } catch (IOException e) {
		    //do something with exception
		    e.printStackTrace();
		  }
		}

		return timestamp;
	}

}
