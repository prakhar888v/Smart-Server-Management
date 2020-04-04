package daemon_process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteDatasetSyncID {

	public static void deleteDatasetSyncID(String datasetSynID) {
		DataOutputStream dataOut = null;
		BufferedReader in =null;

		try {

		  String url = Constants.urlDeleteDataset + datasetSynID;

		  URL urlObj = new URL(url);
		  HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		  //setting request method
		  connection.setRequestMethod("DELETE");

		  //adding headers
		  connection.setRequestProperty("Content-Type","application/json");
		  connection.setRequestProperty("Accept","application/json");
		  //convert username:password to base64
		  connection.setRequestProperty("Authorization",Constants.authDetails);

		  connection.setDoInput(true);

		  //sending POST request
		  connection.setDoOutput(true);
		  dataOut = new DataOutputStream(connection.getOutputStream());
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
