package com.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServletCode {
	
	
	public void connectApi(){
		try{
		String requestInitUrl=requestInitUrl+"/?MSISDN="+msisdn+"&channelName="+channel;
		String response="";
		response=doConnect(requestInitUrl);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	private static String doConnect(String pUrl) throws IOException{
	String lRespMesg = "";
	
	   HttpURLConnection urlconn = null;
	   URL url = new URL(pUrl);	    	   
	
	   urlconn = (HttpURLConnection) url.openConnection();
	   urlconn.setInstanceFollowRedirects(true);
	   urlconn.setRequestMethod("GET");
	   urlconn.setDoOutput(true);
	   urlconn.connect();
	   int respCode = urlconn.getResponseCode();
	   
	   if(respCode!=HttpURLConnection.HTTP_CLIENT_TIMEOUT && respCode==HttpURLConnection.HTTP_OK){
	   
	   String lStream = "";
	   BufferedReader inp = new BufferedReader(new InputStreamReader(
	     urlconn.getInputStream()));
	   while ((lStream = inp.readLine()) != null) {
	    lRespMesg = lRespMesg + lStream;
	   }
	   inp.close();
	   urlconn.disconnect();
	 	System.out.println("Response msg :: "+ lRespMesg);
	   }else{
		   {
			   System.out.println("Error in connection");
		   }
	   }
	   return lRespMesg;
}
}
