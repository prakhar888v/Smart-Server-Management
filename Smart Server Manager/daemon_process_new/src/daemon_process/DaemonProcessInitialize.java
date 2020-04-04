package daemon_process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.xml.internal.fastinfoset.util.ValueArray;

public class DaemonProcessInitialize {
	
	public String[] metrics = {Constants.metric1,Constants.metric2,Constants.metric3,Constants.metric4,Constants.metric5};

	class DaemonMethods extends TimerTask{
		JSONArray forecastArray1 = new JSONArray();
		JSONArray forecastArray2 = new JSONArray();
		
	@Override
		public void run() {
		
		String datasetSynID,refTimestamp,method="smoothing";
		refTimestamp = RefTimeStamp.getRefTimestamp();
		datasetSynID = DatasetSyncID.datasetSynAPI();
		UpdateVariables.updateVariables(datasetSynID,metrics);
	
		int i=0;
		for(String metric:metrics){
				JSONObject forecastObject=null;
				try {
					System.out.println("Running forecast");
					forecastObject = ForecastSncAPI.forecastSyncAPI(datasetSynID,refTimestamp,metric,method);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					
				}
				forecastArray1.add(forecastObject);
				i++;
			}
		
		try {
			forecastArray1 = ServerAction.takeServerActionOnArray(forecastArray1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		for(int j=0;j<5;j++){
			System.out.println("Smoothing");
			InsertForecast.insertForecastInHANALogs((JSONObject) forecastArray1.get(j),Constants.urlInsertLogsSmoothing);
			
		}
			
		//Regression
		method = "linear regression";
		i=0;
		for(String metric:metrics){
				JSONObject forecastObject=null;
				try {
					forecastObject = ForecastSncAPI.forecastSyncAPI(datasetSynID,refTimestamp,metric,method);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
				forecastArray2.add(forecastObject);
				i++;
			}
		
		try {
			forecastArray2 = ServerAction.takeServerActionOnArray(forecastArray2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
		for(int j=0;j<5;j++){
			System.out.println("Regression");
			InsertForecast.insertForecastInHANALogs((JSONObject) forecastArray2.get(j),Constants.urlInsertLogsRegression);
		}
		forecastArray1.clear();
		forecastArray2.clear();
		DeleteDatasetSyncID.deleteDatasetSyncID(datasetSynID);
	}
	}
	
	class DaemonMethodsForKeyInfluencer extends TimerTask{

		@Override
		public void run() {
			String datasetSynID = DatasetSyncIDKeyInf.datasetSynAPI();
			metrics=KeyInfluencers.getKeyInfluencers(datasetSynID);
			DeleteDatasetSyncID.deleteDatasetSyncID(datasetSynID);
		}
		
	}

	/*class DaemonMethodsRegression extends TimerTask{
		
		JSONArray forecastArray = new JSONArray();
		public void run() {
			
			String method = "linear regression";
			String datasetSynID,refTimestamp;
			refTimestamp = RefTimeStamp.getRefTimestamp();
			datasetSynID = DatasetSyncID.datasetSynAPI();
			UpdateVariables.updateVariables(datasetSynID,metrics);
		
			int i=0;
			for(String metric:metrics){
					JSONObject forecastObject = ForecastSncAPI.forecastSyncAPI(datasetSynID,refTimestamp,metric,method);
					forecastArray.add(forecastObject);
					i++;
				}
			
			try {
				forecastArray = ServerAction.takeServerActionOnArray(forecastArray);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int j=0;j<5;j++){
				System.out.println("Regression");
				InsertForecast.insertForecastInHANALogs((JSONObject) forecastArray.get(j),Constants.urlInsertLogsRegression);
			}
			
			forecastArray.clear();
			DeleteDatasetSyncID.deleteDatasetSyncID(datasetSynID);
		}
	}*/
	
	public void daemonSart(){
		
	Timer timer1 = new Timer();
	timer1.schedule(new DaemonMethods(), 0, 25000);
	
	//Timer timer2 = new Timer();
	//timer2.schedule(new DaemonMethodsForKeyInfluencer(),0,10000);
	
	//Timer timer3 = new Timer();
	//timer3.schedule(new DaemonMethodsRegression(),0,25000);
	
	}
}
