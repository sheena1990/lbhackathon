package com.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.model.beans.Location;
import com.model.beans.User;
import com.model.dao.DownStreamMessage;
import com.model.dao.GeneralDao;

public class ProcessInformation {
	GeneralDao generalDao = new GeneralDao();
	
	public List<String> findNearestUsers(String registrationId){
		List<String> usersRegIds = new ArrayList<String>();
		//db select
		usersRegIds = generalDao.findNearbyUsers(registrationId);
		return usersRegIds;
	}
	
	public void registerUser(User user){
		//db insert
		generalDao.registerUser(user);
	}
	
	public void updateUserLocation(String registrationId, Location location){
		//db update
	}
	
	public  User createUserFromInput(JsonObject jData) {
		User user = new User();
		user.setFirstName(jData.get("first_name").getAsString());
		user.setLastName(jData.get("last_name").getAsString());
		user.setImeiNum(jData.get("imeiNo").getAsString());
		user.setPhoneNumber(jData.get("phoneNo").getAsString());
		user.setUserToken(jData.get("token").getAsString());
	/*	Location location = new Location();
		location.setLatitude(jData.get("latitude").getAsDouble());
		location.setLongitude(jData.get("longitude").getAsDouble());
		user.setLocation(location);*/
		return user;
	}
	
	public void sendHelp(JsonObject jData){
		System.out.println("Send Help");
		String token = jData.get("token").getAsString();
		Location location = new Location();
		location.setLatitude(jData.get("latitude").getAsDouble());
		location.setLongitude(jData.get("longitude").getAsDouble());
		DownStreamMessage ds = new DownStreamMessage();
		ds.sendDownStreamMessage(token,location.getLatitude(),location.getLongitude());
		
	}
	
	public void doConnect(String pUrl)throws Exception{
		String lRespMesg = "";
		
		   HttpURLConnection urlconn = null;
		   URL url = new URL(pUrl);	    	   
		
		   urlconn = (HttpURLConnection) url.openConnection();
		   urlconn.setInstanceFollowRedirects(true);
		   urlconn.setRequestMethod("GET");
		   urlconn.setDoOutput(true);
		   urlconn.connect();
		   int respCode = urlconn.getResponseCode();
		   System.out.println("Response code :: "+respCode);
		   if(respCode!=HttpURLConnection.HTTP_CLIENT_TIMEOUT && respCode==HttpURLConnection.HTTP_OK){
		   
		   String lStream = "";
		   BufferedReader inp = new BufferedReader(new InputStreamReader(
		     urlconn.getInputStream()));
		   while ((lStream = inp.readLine()) != null) {
		    lRespMesg = lRespMesg + lStream;
		   }
		   System.out.println("reply msg :: "+lRespMesg);
		   inp.close();
		   urlconn.disconnect();
		 	{
		 		//log.debug("Response :: "+lRespMesg);
		 	}
		   }
		   
		   System.out.println(lRespMesg);
		   JSONObject json = new JSONObject(lRespMesg);
		   Iterator<?> keys = json.keys();
			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    System.out.println(key);
			    System.out.println(json.get(key));
			}
			JSONArray rowsArray = (JSONArray) json.get("rows");
			System.out.println(rowsArray);
			JSONObject data1 = (JSONObject)rowsArray.get(0);
			JSONArray elementsArray = (JSONArray)data1.get("elements");
			JSONObject data2 = (JSONObject)elementsArray.get(0);
			System.out.println(data2);
			JSONObject distanceObj = (JSONObject)data2.get("distance");
			System.out.println(distanceObj);
			String distance = (String)distanceObj.get("text");
			System.out.println(distance);
		   //return lRespMesg; 
	}
	
	public void callDistanceMatrixApi() throws Exception{
		 
			String apiKey = "AIzaSyBPaaPOnkKaTtKHpRvp0WDDkBo2OAN8A_M";
			//String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/output?location=-33.8670522,151.1957362&rankby=distance&types=food&key="+apiKey;
			String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&mode=walking&origins=40.6655101,-73.89188969999998&destinations=40.6905615,-73.9976592&key="+apiKey;
			System.out.println(url);
			ProcessInformation pf = new ProcessInformation();
			pf.doConnect(url);
			//List<Place> nearbyPlaces = getNearbyPlacesRankedByDistance(50, 50, 5, );
	}
}
