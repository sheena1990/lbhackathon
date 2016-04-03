package com.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.model.beans.Location;
import com.model.beans.User;
import com.util.ConnectionUtil;
import com.util.DBConnection;

public class GeneralDao {
	DBConnection dbConnection = new DBConnection();
	Connection conn = dbConnection.getConnection();
	
	public List<User> test(){
		System.out.println("inside findnearbyusers");
	      Transaction tx = null;
	      List<User> nearbyUserList = new ArrayList<User>();
	      try{
	         //tx = session.beginTransaction();
	    	  Statement s = conn.createStatement();
	    	  String sql = "select * from Help_user";
	    	  ResultSet res = s.executeQuery(sql);
	    	  if(res != null){
	    		  while(res.next()){
	    			  System.out.println("*********");
	    			  System.out.println(res.getString("user_id"));
	    		  }
	    	  }
	         /*nearbyUserList = session.createQuery("from User").list();
	         if(nearbyUserList != null && nearbyUserList.size() > 0)
	         System.out.println(nearbyUserList.get(0).getUserId());
	         else
	        	 System.out.println(" empty ");*/
	         //tx.commit(); 
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	         //session.close(); 
	      }
	      return nearbyUserList;
	}

	public List<String> findNearbyUsers(String registrationId) {
		List<String> nearbyUserIds = new ArrayList<String>();
		try{
	    	  Statement s = conn.createStatement();
	    	  String sql = "select * from Help_user where reg_id = '" + registrationId + "'";
	    	  ResultSet res = s.executeQuery(sql);
	    	  if(res != null){
	    		  while(res.next()){
	    			  Location location = new Location();
	    			  location.setLatitude(res.getDouble("latitude"));
	    			  location.setLatitude(res.getDouble("longitude"));
	    			  String nearbySql = "select * from Help_user ";
	    		  }
	    	  }
	      }catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*public boolean registerUser(String imeiNo,String firstName,String lastName,int phoneNo,String token){*/
	public boolean registerUser(User user){
		
		try{
	    	  //Statement s = conn.createStatement();
	    	  String sql = "insert into help_user (imei_num,first_name,last_name, phone_num , user_token) values(? ,? , ? ,?, ?)";
	    	  PreparedStatement stmt = conn.prepareStatement(sql);
	    	  stmt.setString(1, user.getImeiNum());
	    	  stmt.setString(2, user.getFirstName());
	    	  stmt.setString(3, user.getLastName());
	    	  stmt.setString(4, user.getPhoneNumber());
	    	  stmt.setString(5, user.getUserToken());
	    	  int regId = stmt.executeUpdate(sql);
		}catch (SQLException e) {
			e.printStackTrace();}
		return true;
	}
}
