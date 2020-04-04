package daemon_process;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import daemon_process.Constants;

public class ServerAction {

	public static JSONArray takeServerActionOnArray(JSONArray forecastArray) {
 		String defaultString = "{\"isThresholdExceeded\":\"No\"}";
 		JSONParser parser = new JSONParser();
 		 try {
			JSONObject defaultJson = (JSONObject) parser.parse(defaultString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
 		   
 		for(int i=0;i<5;i++){
 			JSONObject temp = (JSONObject) forecastArray.get(0);
 			temp.put("isThresholdExceeded", "No");
 			temp.put("action", "Everything Fine!");
 			forecastArray.remove(0);
 			forecastArray.add(temp);
 			}
 		
 		boolean startFlag = false;
 		boolean stopFlag = false;
 		for(int i=0;i<5;i++){
 			JSONObject j = (JSONObject) forecastArray.get(i);
 	 		String metric = j.get("metric").toString();
 	 		float value = Float.parseFloat(j.get("forecastValue").toString());
 	 		
 	 		for(int k=0;k<6;k++){
 	 			if(metric == Constants.parentMetrics[k]){
 	 				if(value > Constants.maxthresholds[k])
 	 				{
 	 					if(!startFlag)
 	 					{
 	 						startFlag = true;
	 	 	 				String newString = "{\"server\":\"WEBI Server\",\"metric\":\""+ metric +"\",\"forecastValue\":\""+ j.get("forecastValue").toString() + "\",\"forecastTime\":\""+j.get("forecastTime").toString()+"\",\"isThresholdExceeded\":\"Yes\",\"action\":\"Create new Server\"}";
	 						JSONObject newObject = null;
	 						try {
	 							newObject = (JSONObject) parser.parse(newString);
	 						} catch (Exception e) {
	 							// TODO Auto-generated catch block
	 							System.out.println(e.getMessage());
	 						}
	 						
	 						forecastArray.remove(k);
	 						forecastArray.add(k,newObject);
	 	 				}
 	 					else
 	 					{
 	 	 					if(value > Constants.maxthresholds[k]){
 		 	 					String newString = "{\"server\":\"WEBI Server\",\"metric\":\""+ metric +"\",\"forecastValue\":\""+ j.get("forecastValue").toString() + "\",\"forecastTime\":\""+j.get("forecastTime").toString()+"\",\"isThresholdExceeded\":\"Yes\",\"action\":\"No Action Required\"}";
 		 						JSONObject newObject = null;
 		 						try {
 		 							newObject = (JSONObject) parser.parse(newString);
 		 						} catch (Exception e) {
 		 							// TODO Auto-generated catch block
 		 							System.out.println(e.getMessage());
 		 						}
 	 						
 		 						forecastArray.remove(k);
 		 						forecastArray.add(k,newObject);
 	 	 					}
 	 					}
 	 				}
 	 				if(value < Constants.minthresholds[k])
 	 				{
 	 					if(!stopFlag)
 	 					{
 	 						stopFlag = true;
	 	 	 				String newString = "{\"server\":\"WEBI Server\",\"metric\":\""+ metric +"\",\"forecastValue\":\""+ j.get("forecastValue").toString() + "\",\"forecastTime\":\""+j.get("forecastTime").toString()+"\",\"isThresholdExceeded\":\"Yes\",\"action\":\"Stop Server\"}";
	 						JSONObject newObject = null;
	 						try {
	 							newObject = (JSONObject) parser.parse(newString);
	 						} catch (Exception e) {
	 							// TODO Auto-generated catch block
	 							System.out.println(e.getMessage());
	 						}
	 						
	 						forecastArray.remove(k);
	 						forecastArray.add(k,newObject);
	 	 				}
 	 					else
 	 					{
 	 	 					if(value < Constants.minthresholds[k]){
 		 	 					String newString = "{\"server\":\"WEBI Server\",\"metric\":\""+ metric +"\",\"forecastValue\":\""+ j.get("forecastValue").toString() + "\",\"forecastTime\":\""+j.get("forecastTime").toString()+"\",\"isThresholdExceeded\":\"Yes\",\"action\":\"No Action Required\"}";
 		 						JSONObject newObject = null;
 		 						try {
 		 							newObject = (JSONObject) parser.parse(newString);
 		 						} catch (Exception e) {
 		 							// TODO Auto-generated catch block
 		 							System.out.println(e.getMessage());
 		 						}
 	 						
 		 						forecastArray.remove(k);
 		 						forecastArray.add(k,newObject);
 	 	 					}
 	 					}
 	 				}
 	 				break;
 	 			}

 	 		}
 	 			
 	 		/*for(int k=0;k<10;k++){
 	 			if(metric == Constants.parentMetrics[k] && value < Constants.minthresholds[k]){
 	 				String newString = "{\"server\":\"WEBI Server\",\"metric\":\""+ metric +"\",\"forecastValue\":\""+ j.get("forecastValue").toString() + "\",\"forecastTime\":\""+j.get("forecastTime").toString()+"\",\"isThresholdExceeded\":\"Lower than Min Threshold\",\"action\":\"Stopping This Server\"}";
					JSONObject newObject = (JSONObject) parser.parse(newString);
					forecastArray.remove(k);
					forecastArray.add(newObject);
					return forecastArray;
 	 			}
 	 		}*/
 	 		
 	 	}
 		
 	return forecastArray;
 		
 	}
	
	
 }
