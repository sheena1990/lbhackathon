package com.model.dao;

import com.google.gson.JsonObject;

public class UpStreamReceiver {
	
	public static void main(String args[]){
		String apiKey = "AIzaSyAFP_1zVBUoBiUVTDJQLNerheX3VjBIkOw";
		String senderKey = "1034234491032";
		String serviceName = "HelpMeServer";
		
		UpStreamNew upst = new UpStreamNew(apiKey,senderKey,serviceName) {
			
			@Override
			public void onMessage(String from, JsonObject jData) {
				// TODO Auto-generated method stub
				System.out.println("From inp :: "+from);
				System.out.println("Json data inp ::"+jData);
			/*	String action = jData.get("action").getAsString();
				System.out.println(action);
				 String toToken = jData.get("to").getAsString();
				 System.out.println(toToken);
		    */      
			}
		};
	
	
	System.out.println("Ended");
	}
}
