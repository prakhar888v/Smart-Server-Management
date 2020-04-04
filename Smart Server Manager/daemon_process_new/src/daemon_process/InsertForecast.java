package daemon_process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;

public class InsertForecast {

	public static void insertForecastInHANALogs(JSONObject forecastObj, String url) {
		
		
		DataOutputStream dataOut=null;
		BufferedReader in=null;
		String metric=null;
		String forecastValue=null;
		String forecastTime=null;
		String isThresholdExceeded=null;
		String action=null;
		try {
			dataOut = null;
			in = null;
			metric = forecastObj.get("metric").toString();
			forecastValue = forecastObj.get("forecastValue").toString();
			forecastTime = forecastObj.get("forecastTime").toString();
			isThresholdExceeded = forecastObj.get("isThresholdExceeded").toString();
			action = forecastObj.get("action").toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		
		
		try{

		  URL urlObj = new URL(url);
		  HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		  //setting request method
		  connection.setRequestMethod("POST");

		  //adding headers
		  connection.setRequestProperty("Content-Type","application/json");
		  connection.setRequestProperty("Accept","application/json");
		  //convert username:password to base64
		  connection.setRequestProperty("Authorization","Basic U0hTOlBhc3N3b3JkMSM=");

		  connection.setDoInput(true);

		  //sending POST request
		  connection.setDoOutput(true);
		  dataOut = new DataOutputStream(connection.getOutputStream());
		  dataOut.writeBytes("{\"server\" : \"Webi Server\",\"metric\" : \"" + metric + "\",\"forecastvalue\" : \""+ forecastValue +"\",\"forecasttime\" : \""+ forecastTime +"\",\"exceedsthreshold\" : \""+ isThresholdExceeded +"\",\"action\" : \""+ action +"\"}");
		  dataOut.flush();

		  int responseCode = connection.getResponseCode();
		  in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		  String inputLine;
		  StringBuffer response = new StringBuffer();
		  while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		  }

		  System.out.println(forecastObj);
		 } catch (Exception e) {
		  //do something with exception
		  System.out.println(e.getMessage());
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

		
	}

}
