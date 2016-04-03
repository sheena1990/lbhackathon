package com.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.JsonObject;
import com.model.ProcessInformation;
import com.model.beans.Location;
import com.model.beans.User;
import com.model.dao.GeneralDao;
import com.model.dao.UpStreamNew;
import com.sun.istack.internal.logging.Logger;
import com.util.Constants;

/**
 * Servlet implementation class ControlServlet
 */
@WebServlet(description = "Control servlet", urlPatterns = { "/ControlServlet" })
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ProcessInformation processInformation = new ProcessInformation();
    //private Logger log = Logger.getLogger(ControlServlet.class);   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControlServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//log.info("inside doGet");
		System.out.println("Inside do get");
		//GeneralDao generalDao = new GeneralDao();
		//generalDao.test();
		//Thread t = new Thread();
		//t.start();
		String apiKey = "AIzaSyAFP_1zVBUoBiUVTDJQLNerheX3VjBIkOw";
		String senderKey = "1034234491032";
		String serviceName = "HelpMeServer";
		

		UpStreamNew upst = new UpStreamNew(apiKey,senderKey,serviceName) {
			
			@Override
			public void onMessage(String from, JsonObject jData) {
				// TODO Auto-generated method stub
				
				System.out.println("From inp :: "+from);
				System.out.println("Json data inp ::"+jData);
				/*String action = jData.get("action").getAsString();
				System.out.println(action);
				 String toToken = jData.get("to").getAsString();
				 System.out.println(toToken);
		        */  
				ProcessInformation p =new ProcessInformation();
				String action = jData.get("action").getAsString();
				if(action.equalsIgnoreCase(Constants.REGISTER)){
					User user = p.createUserFromInput(jData);
					processInformation.registerUser(user);
				}else if(action.equalsIgnoreCase(Constants.COORDINATES)){
					
				}else if(action.equalsIgnoreCase(Constants.HELP)){
					System.out.println("Asking for help");
					p.sendHelp(jData);
				}
				 
				
			}


		};

	System.out.println("Ended");

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		//log.info("inside doPost");
		System.out.println("Inside do post");
		
	}

}
	

