package daemon_process;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.*;

public class ForecastSncAPI {

	public static JSONObject forecastSyncAPI(String datasetSynID, String refTimestamp, String metric, String method) throws Exception {
		
		DataOutputStream dataOut = null;
		BufferedReader in =null;
		JSONObject jobj =new JSONObject();
		
		
	//	try {

		String url = Constants.urlForecast;
		
		URL urlObj = new URL(url);
		  HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		  //setting request method
		  connection.setRequestMethod("POST");

		  //adding headers
		  connection.setRequestProperty("Content-Type","application/json");
		  connection.setRequestProperty("Accept","application/json");
		  //convert username:password to base64
		  connection.setRequestProperty("Authorization",Constants.authDetails);

		  connection.setDoInput(true);

		  //sending POST request
		  connection.setDoOutput(true);
		  dataOut = new DataOutputStream(connection.getOutputStream());
		  if(method.equals("linear regression"))
			  dataOut.writeBytes("{\"datasetID\":"+ datasetSynID +",   \"targetColumn\": \""+ metric +"\", \"forecastMethod\": \"" + method + "\",\"smoothingCycleLength\": 2,   \"dateColumn\": \""+ Constants.metricTimestamp +"\",   \"numberOfForecasts\": 1,     \"referenceDate\" : \"" + refTimestamp + "\" }");
		  else 
			  dataOut.writeBytes("{\"datasetID\":"+ datasetSynID +",   \"targetColumn\": \""+ metric +"\",\"dateColumn\": \""+ Constants.metricTimestamp +"\",   \"numberOfForecasts\": 1,     \"referenceDate\" : \"" + refTimestamp + "\" }");
		  dataOut.flush();

		  int responseCode = connection.getResponseCode();
		  
		  in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		  
		  String inputLine;
		  StringBuffer response = new StringBuffer();
		  while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		  }

		  JSONParser parser = new JSONParser();
		  JSONObject json = (JSONObject) parser.parse(response.toString());
		  
		  JSONArray jarray=(JSONArray) json.get("forecasts");
		  
			  JSONObject j = (JSONObject) jarray.get(0);
			  String forecastValue  = j.get("forecastValue").toString();
			  String forecastTime   = j.get("date").toString();
		
			  jobj = new JSONObject();	  
		  
			  jobj.put("forecastValue", forecastValue);
			  jobj.put("forecastTime", forecastTime);
			  jobj.put("server", "WEBI Server");
			  jobj.put("metric", metric);
			  jobj.put("referenceTime", refTimestamp);
			  
	//	} catch (Exception e) {
		  //do something with exception
		//  e.printStackTrace();
		//} finally {
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
		//}
		return jobj;
		
		
	}

}
