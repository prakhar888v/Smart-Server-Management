package daemon_process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class KeyInfluencers {
	
	static String allCounts[][] = new String[6][5];
	
	public static String[] getKeyInfluencers(String datasetSynID) {
		
		for(int i=0;i<6;i++){
			allCounts[i]=keyInfluncerAPI(i,datasetSynID);
		}
		
		return fiveMostCommonInfluencers();
	}

	private static String[] fiveMostCommonInfluencers() {
		
		Map<String, Integer> elementsCounts = new HashMap<>();
		for (int i = 0; i < allCounts.length; i++) {
		    for (int j = 0; j < allCounts[i].length; j++) {
		        Integer count = elementsCounts.get(allCounts[i][j]);
		        if(count == null){
		          count = 0;
		        }
		        elementsCounts.put(allCounts[i][j], count+1);
		    }
		}
		
		String [] news =new String[5];
		for(int i=0;i<5;i++){
		Map.Entry<String, Integer> maxEntry = null;

		for (Map.Entry<String, Integer> entry : elementsCounts.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		    {
		        maxEntry = entry;
		    }
		}
		news[i]=maxEntry.getKey();
		elementsCounts.remove(maxEntry.getKey());
		}
		return news;

	}

	private static String[] keyInfluncerAPI(int i, String datasetSynID) {
		
		DataOutputStream dataOut = null;
		BufferedReader in =null;
		JSONObject jobj =new JSONObject();
		String result[]=new String[5];
		try {

		  String url = Constants.urlKeyInfluencer;

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
		  dataOut.writeBytes("{\"datasetID\":\""+ datasetSynID +"\",    \"targetColumn\": \"" + Constants.parentMetrics[i] + "\",    \"numberOfInfluencers\" : 5,    \"skippedVariables\" : [\"DataId\", \"TotalDiskSp\",\"Timestamp\"],    \"autoSelection\" : true  }");
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
		  
		  JSONArray jarray=(JSONArray) json.get("influencers");
		  
		  for(int j=0;j<5;j++){
			  JSONObject temp = (JSONObject) jarray.get(j);
			  result[j]=temp.get("variable").toString();
		  }
		  
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
		return result;
		
		
	}

}
