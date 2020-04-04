package daemon_process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UpdateVariables {

	public static void updateVariables(String datasetSynID, String[] metrics) {
		DataOutputStream dataOut = null;
		BufferedReader in =null;

		try {

		  String url = Constants.urlUpdateVariable + datasetSynID +"/variables/update";

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
		  dataOut.writeBytes("[{\"name\" :\"" + metrics[0] + "\", \"value\" : \"continuous\"  }, { \"name\" :\"" + metrics[1] + "\", \"value\" : \"continuous\" },  { \"name\" :\"" + metrics[2] + "\", \"value\" : \"continuous\" },{ \"name\" :\"" + metrics[3] + "\", \"value\" : \"continuous\" },{ \"name\" :\"" + metrics[4] + "\", \"value\" : \"continuous\" }]");
		  dataOut.flush();

		  int responseCode = connection.getResponseCode();
		  in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		  String inputLine;
		  StringBuffer response = new StringBuffer();
		  while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
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

		
	}

}
