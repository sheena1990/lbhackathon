package com.model.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;

public class DownStreamMessage {
	
	public static void main(String args[]){
		//sendDownStreamMessage("1");
	}
	
	public  void sendDownStreamMessage(String toUser, double lat , double lon){
		//toUser = "c_PhTI0TYPY:APA91bFR1oq4CWK6ZS9EnaU1ahyztVdSPSq2H0Yzj8ipWBHyAjekoH1oMgqC_rzXFsrpR1PvcyVTy4yF4Bj8NYzCDIWPfY4RetI2u48HGOWtZ7pkQhZM_9u_1DLZFs1myjTvqe8EcrPL";
		String key = "AIzaSyAFP_1zVBUoBiUVTDJQLNerheX3VjBIkOw";
		try{
		/*URL url  = new URL("https://gcm-http.googleapis.com/gcm/send"); */
			URL url  = new URL("https://android.googleapis.com/gcm/send"); 
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		/*conn.setRequestProperty("Authorization", "key=AIzaSyD-b2qrtF7YFeBZgPgytGN4-gXezAL8MU0");*/
		conn.setRequestProperty("Authorization", "key=" +key);
		conn.setRequestMethod("POST");
		/*String input = "{ \"data\": {\"score\": \"5x1\",\"time\": \"15:10\"},"
				+ "\"to\" : \"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1\"}";*/
		/*String input = "{ \"data\": {\"score\": \"5x1\",\"time\": \"15:10\"},"
				+ "\"to\" : \" "+ toUser +"\"}";*/
		JSONObject obj = new JSONObject();
		JSONObject j1 = new JSONObject();
		j1.put("score", "5");
		j1.put("message", "pls help");
		j1.put("latitude", lat);
		j1.put("longitude", lon);
		obj.put("to", toUser);
		obj.put("data", j1);
	
		OutputStream os = conn.getOutputStream();
		/*os.write(input.getBytes());*/
		os.write(obj.toString().getBytes());
		System.out.println("sent");
		os.flush();
		
/*		InputStream inputStream = conn.getInputStream();
        //String resp = IOUtils.toString(inputStream);

        //System.out.println(resp);
        
        String lStream = "";
        String lRespMesg = "";
		   BufferedReader inp = new BufferedReader(new InputStreamReader(
				   conn.getInputStream()));
		   while ((lStream = inp.readLine()) != null) {
		    lRespMesg = lRespMesg + lStream;
		   }
		   System.out.println("**************" + lRespMesg);*/
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		}
	
}
