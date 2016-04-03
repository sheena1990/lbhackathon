package coreservice;

	import java.util.List;

	import javax.ws.rs.GET;
	import javax.ws.rs.Path;
	import javax.ws.rs.Produces;
	import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;


	
	@Path("/UserService")
	public class UserService {
		private static Logger log=Logger.getLogger(UserService.class.getName());
	   UserDao userDao = new UserDao();

	   @GET
	   @Path("/users")
	   @Produces(MediaType.APPLICATION_XML)
	   public List<User> getUsers(){
		  log.debug("request for users");
	      return userDao.getAllUsers();
	   }
	   
	   @GET
	   @Path("/sayhi")
	   
	   public String needHelp(){
	      return userDao.needHelp();
	   }
	   
	   
	}

