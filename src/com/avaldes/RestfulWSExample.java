package com.avaldes;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.avaldes.model.Actor;
import com.avaldes.model.Admin;
import com.avaldes.model.ExcelOutputService;
import com.avaldes.model.Login;
import com.avaldes.model.MailCredentials;
import com.avaldes.model.Mailer;

@Path("/actors")
public class RestfulWSExample {
	static final String api_version = "1.01A rev.18729";
	static Logger logger = Logger.getLogger(RestfulWSExample.class);
	static String xmlString = null;
	static Map<String, Actor> actors = new HashMap<String, Actor>();
	static Map<String, Admin> admins = new HashMap<String, Admin>();
	//static int secureToken = 0;
	/*static { 
		System.out.println("Initializing Internal DataStore...");
		actors.put("123", new Actor(123, "Hugh Jackson", "Hugh Michael Jackman", "October 12, 1968", "hughjackman@mail.com", true));
		actors.put("124", new Actor(124, "Jennifer Lawrence", "Jennifer Shrader Lawrence", "August 15, 1990", "jennifer@mail.com", true));
		actors.put("345", new Actor(345, "Jennifer Lopez", "Jennifer Lynn Lopez", "July 24, 1969", "jlo@verizon.com", true));
		actors.put("333", new Actor(333, "Jennifer Anniston", "Jennifer Joanna Aniston", "February 11, 1969", "jennifer.anniston@eonline.com", true));
		actors.put("444", new Actor(444, "Julia Roberts", "Julia Fiona Roberts ", "October 28, 1967", "julia.roberts@att.com", true));
		actors.put("777", new Actor(777, "Chris Evans", "Christopher Robert Evans", "June 13, 1981", "chris.evans@comcast.com", true));
		actors.put("654", new Actor(654, "Robert Downey Jr.", "Robert John Downey Jr", "April 4, 1965", "robertdowney@verizon.com", true));
		actors.put("255", new Actor(255, "Johnny Depp", "John Christopher Depp II", "June 9, 1963", "johndepp@hollywood.com", true));		
		actors.put("989", new Actor(989, "Scarlet Johansson", "Scarlett Ingrid Johansson", "November 22, 1984", "scarjo@mail.com", true));
	}*/

	@Path("/version") 
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion() {
		return "<p>Version: " + api_version + "</p>";
	}

	@Path("/login") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String authentication(Login login) {
		String canLogin = "Not Autorized";
		try 
		{
			Class.forName ("oracle.jdbc.OracleDriver");
			Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
			Statement stmt = c.createStatement();
			ResultSet res = stmt.executeQuery("select * from actor_users");
			while (res.next()) {
				String userId = res.getString("USERID");
				String password = res.getString("PASSWORD");
				if( userId.equals(login.getUserId()) && password.equals(login.getPassword())){
					canLogin = "Authorized";
					//Mailer.send(MailCredentials.GMAIL_ID,MailCredentials.GMAIL_PASSWORD,"chaudharyanish1990@gmail.com","You have Logged In!","How r u?");
					System.out.println("Authentication Successful......");
					break;
				}
			}
			res.close();
			stmt.close();
			c.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{

		}
		
		System.out.println("Authentication Done......");
		return canLogin;		
	}
	
	    @Path("/fetchAdmins")
	    @GET
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public ArrayList<Admin> getAllAdmins() {
			try 
			{
				Class.forName ("oracle.jdbc.OracleDriver");
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
				Statement stmt = c.createStatement();
				ResultSet res = stmt.executeQuery("select * from admin_users");
				while (res.next()) {
					Admin admin = new Admin();
					admin.setName(res.getString("NAME"));
					admin.setUserId(res.getString("USERID"));
					admin.setEmail(res.getString("EMAIL"));
					admin.setPhoneNo(res.getLong("PHONENO"));
					admin.setPassword(res.getString("PASSWORD"));
					admin.setCity(res.getString("CITY"));	
					admin.setCountry(res.getString("COUNTRY"));
					admin.setElligiblity(res.getInt("ACTIVE"));
					admins.put(""+admin.getUserId(), admin);
				}
				res.close();
				stmt.close();
				c.close();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally{

			}
			System.out.println("Getting all admins...");

			ArrayList<Admin> adminList = new ArrayList<Admin>(admins.values());
//			ExcelOutputService e = new ExcelOutputService();
//			XSSFWorkbook workbook = e.createExcelOutputFile();
//			String path = "F:\\Freelancing\\jars\\Excel_Output1.xlsx";
//			FileOutputStream fop;
//			try {
//				fop = new FileOutputStream(path);
//				workbook.write(fop);
//				fop.close();
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			return adminList;
		}

	    @Path("/access/{userId}")
		@GET
		@Produces({ MediaType.APPLICATION_JSON })
		public Admin accessManagement(@PathParam("userId") String userId) {
			System.out.println("Getting amin by userId: " + userId);
			Admin admin = new Admin();
			try 
			{
				Class.forName ("oracle.jdbc.OracleDriver");
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
				PreparedStatement stmt = c.prepareStatement("Update admin_users SET active = ? where userId = ?");
				stmt.setInt(1,1);
				stmt.setString(2,userId);
				int res=stmt.executeUpdate();
				
				stmt.close();
				//c.close();
				
				PreparedStatement pstmt = c.prepareStatement("Select * FROM admin_users where userId = ?");
				pstmt.setString(1,userId);
				ResultSet grst = pstmt.executeQuery();
				Admin user = new Admin();
				
				while (grst.next()) {
					user.setUserId(grst.getString("USERID"));
					user.setPassword(grst.getString("PASSWORD"));	
				}
				grst.close();
				pstmt.close();
				
				PreparedStatement pst = c.prepareStatement("INSERT INTO actor_users"
				+ "(USERID, PASSWORD) VALUES"
				+ "(?,?)");
				pst.setString(1,user.getUserId());
				pst.setString(2,user.getPassword());
				
				ResultSet rs=pst.executeQuery();
				//Mailer.send(MailCredentials.GMAIL_ID,MailCredentials.GMAIL_PASSWORD,"chaudharyanish1990@gmail.com","You have been given access on ACTOR MANAGEMENT INTERFACE!!!","How r u?");
				rs.close();
				pst.close();
				c.close();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally{

			}

			//Actor actor = actors.get(id);
			if (admin != null) {
				logger.info("Inside , returned: " + admin.toString());
			} else {
				logger.info("Inside , ID: " + userId + ", NOT FOUND!");
			}
			return admin;
		}
	
	    @Path("/revoke/{userId}")
		@GET
		@Produces({ MediaType.APPLICATION_JSON })
		public Admin revokeManagement(@PathParam("userId") String userId) {
			System.out.println("Getting amin by userId: " + userId);
			Admin admin = new Admin();
			try 
			{
				Class.forName ("oracle.jdbc.OracleDriver");
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
				PreparedStatement stmt = c.prepareStatement("Update admin_users SET active = ? where userId = ?");
				stmt.setInt(1,0);
				stmt.setString(2,userId);
				int res=stmt.executeUpdate();
				
				stmt.close();
				//c.close();
				
//				PreparedStatement pstmt = c.prepareStatement("Select * FROM admin_users where userId = ?");
//				pstmt.setString(1,userId);
//				ResultSet grst = pstmt.executeQuery();
//				Admin user = new Admin();
//				
//				while (grst.next()) {
//					user.setUserId(grst.getString("USERID"));
//					user.setPassword(grst.getString("PASSWORD"));	
//				}
//				grst.close();
//				pstmt.close();
				
				PreparedStatement pst = c.prepareStatement("DELETE actor_users where userId =?");
				pst.setString(1,userId);
				
				ResultSet rs=pst.executeQuery();
				//Mailer.send(MailCredentials.GMAIL_ID,MailCredentials.GMAIL_PASSWORD,"chaudharyanish1990@gmail.com","Your access has been revoked from ACTOR MANAGEMENT INTERFACE!!!","How r u?");
				rs.close();
				pst.close();
				c.close();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally{

			}

			//Actor actor = actors.get(id);
			if (admin != null) {
				logger.info("Inside , returned: " + admin.toString());
			} else {
				logger.info("Inside , ID: " + userId + ", NOT FOUND!");
			}
			return admin;
		}
	
	    @Path("/sendToken/{userId}")
		@GET
		@Produces({ MediaType.APPLICATION_JSON })
		public void sendToken(@PathParam("userId") String userId) {
			System.out.println("Getting userId: " + userId);
			
			try 
			{
				SecureRandom secureToken = new SecureRandom();
				  for (int i = 0; i < 20; i++) {
				    System.out.println(secureToken.nextInt(21));
				  }
				int number = new Integer(secureToken.nextInt());
				Class.forName ("oracle.jdbc.OracleDriver");
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
				PreparedStatement pst = c.prepareStatement("INSERT INTO Admin_Secure_Token"
						+ "(USERID, SECURE_TOKEN) VALUES"
						+ "(?,?)");
				pst.setString(1,userId);
				pst.setInt(2,number);
				
				ResultSet rs=pst.executeQuery();
				
				rs.close();
				pst.close();
				c.close();
				String tokenMsg = "Your Verification Code is: "+number;
				System.out.println(tokenMsg);
				Mailer.send(MailCredentials.GMAIL_ID,MailCredentials.GMAIL_PASSWORD,"chaudharyanish1990@gmail.com","Your One Time Password. DO NOT SHARE!!!",tokenMsg);
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally{

			}

			logger.info("DONE- ******SENT OTP TO USER FOR AUTHENTICATION*****");
		}
	
	    @Path("/verify/{userId}/{token}")
		@GET
		@Produces({ MediaType.APPLICATION_JSON })
		public boolean verifyEmailId(@PathParam("userId") String userId,@PathParam("token") long token) {
			System.out.println("Getting userId: " + userId);
			boolean verification = false;
			try 
			{
				Class.forName ("oracle.jdbc.OracleDriver");
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
				PreparedStatement pst = c.prepareStatement("Select * from Admin_Secure_Token where userId=?");
				pst.setString(1,userId);
				//pst.setLong(2,token);
				
				ResultSet rs=pst.executeQuery();
//				if (!rs.next()) {
//				  verification = true;
//				  logger.info("DONE- ******VERIFIED SUCCESSFULLY*****");
//				}
				while (rs.next()) {
					String id = rs.getString("USERID");
					long nmbr = rs.getLong("SECURE_TOKEN");
					if(id.equals(userId) && nmbr == token){
						verification = true;
						logger.info("DONE- ******VERIFIED SUCCESSFULLY*****");	
					}
					
				}
				rs.close();
				pst.close();
				c.close();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally{

			}

			logger.info("DONE- Returning from verfication--->"+verification);
			return verification;
		}
	
	    @Path("/reset/{userId}/{password}")
		@GET
		@Produces({ MediaType.APPLICATION_JSON })
		public boolean resetPassword(@PathParam("userId") String userId,@PathParam("password") String password) {
			System.out.println("Getting userId: " + userId);
			boolean reset = false;
			try 
			{
				Class.forName ("oracle.jdbc.OracleDriver");
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
				PreparedStatement pst = c.prepareStatement("UPDATE admin_users SET password = ? where userId=?");
				pst.setString(1,password);
				pst.setString(2,userId);
				
				int admin_rs = pst.executeUpdate();
				
				pst.close();
				PreparedStatement act = c.prepareStatement("UPDATE actor_users SET password = ? where userId=?");
				act.setString(1,password);
				act.setString(2,userId);
				int actor_rs = act.executeUpdate();
				
				System.out.println(admin_rs);
				System.out.println(actor_rs);
				if( admin_rs == 1 && actor_rs == 1){
					reset = true;
				}
				act.close();
				c.close();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally{

			}

			logger.info("DONE- Returning from reset--->"+reset);
			return reset;
		}
	
	    @Path("/deleteAdmin/{userId}")
		@DELETE
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public boolean deleteAdminById(@PathParam("userId") String userId) {
			System.out.println("Deleting admiin with ID: " + userId);
            boolean delete = false;
			try 
			{
				Class.forName ("oracle.jdbc.OracleDriver");
				Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
				PreparedStatement stmt = c.prepareStatement("DELETE ADMIN_USERS WHERE USERID = ?");
				stmt.setString(1,userId);
				int delete_admin=stmt.executeUpdate();
				stmt.close();
				
				PreparedStatement pstmt = c.prepareStatement("DELETE ACTOR_USERS WHERE USERID = ?");
				pstmt.setString(1,userId);
				int delete_actor=pstmt.executeUpdate();
				stmt.close();
				c.close();
				
				if( delete_actor == 1 && delete_admin == 1){
					delete = true;
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally{

			}
			logger.info("DONE- Returning from reset--->"+delete);
			return delete;
	   }

	@Path("/addAdmin") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Admin authentication(Admin user) {
		try 
		{
			Class.forName ("oracle.jdbc.OracleDriver");
			Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
			PreparedStatement stmt = c.prepareStatement("INSERT INTO admin_users"
					+ "(NAME, USERID, EMAIL, PHONENO, PASSWORD, CITY, COUNTRY, ACTIVE) VALUES"
					+ "(?,?,?,?,?,?,?,?)");
			stmt.setString(1,user.getName());
			stmt.setString(2,user.getUserId());
			stmt.setString(3,user.getEmail());
			stmt.setLong(4,user.getPhoneNo());
			stmt.setString(5,user.getPassword());
			stmt.setString(6,user.getCity());
			stmt.setString(7,user.getCountry());
			stmt.setInt(8, user.getElligiblity());
			ResultSet res=stmt.executeQuery();

			res.close();
			stmt.close();
			c.close();
//			PreparedStatement pst = c.prepareStatement("INSERT INTO actor_users"
//					+ "(USERID, PASSWORD) VALUES"
//					+ "(?,?)");
//			pst.setString(1,user.getUserId());
//			pst.setString(2,user.getPassword());
//			
//			ResultSet rs=pst.executeQuery();
//			
//			rs.close();
//			pst.close();
//			c.close();
			//Mailer.send(MailCredentials.GMAIL_ID,MailCredentials.GMAIL_PASSWORD,"chaudharyanish1990@gmail.com","You have been given access on ACTOR MANAGEMENT INTERFACE!!!","How r u?");
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{

		}
		
		System.out.println("Admin Added......");
		return user;		
	}

	
	// This is the default @PATH
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Actor> getAllActors() {
		try 
		{
			Class.forName ("oracle.jdbc.OracleDriver");
			Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
			Statement stmt = c.createStatement();
			ResultSet res = stmt.executeQuery("select * from actors");
			while (res.next()) {
				Actor actor = new Actor();
				actor.setId(res.getInt("ID"));
				actor.setName(res.getString("NAME"));
				actor.setBirthName(res.getString("BIRTHNAME"));
				actor.setBirthDate(res.getString("BIRTHDATE"));
				actor.setEmail(res.getString("EMAIL"));
				actor.setActive(res.getString("ACTIVE"));	
				actors.put(""+actor.getId(), actor);
			}
			res.close();
			stmt.close();
			c.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{

		}
		System.out.println("Getting all actors...");

		ArrayList<Actor> actorList = new ArrayList<Actor>(actors.values());
		return actorList;
	}

	@Path("/getActor/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Actor getActorById(@PathParam("id") String id) {
		System.out.println("Getting actor by ID: " + id);
		Actor actor = new Actor();
		try 
		{
			Class.forName ("oracle.jdbc.OracleDriver");
			Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
			PreparedStatement stmt = c.prepareStatement("select * from actors where id=?");
			stmt.setString(1,id);
			ResultSet res=stmt.executeQuery();

			while (res.next()) {
				actor.setId(res.getInt("ID"));
				actor.setName(res.getString("NAME"));
				actor.setBirthName(res.getString("BIRTHNAME"));
				actor.setBirthDate(res.getString("BIRTHDATE"));
				actor.setEmail(res.getString("EMAIL"));
				actor.setActive(res.getString("ACTIVE"));	
				//actors.put(""+actor.getId(), actor);
			}
			res.close();
			stmt.close();
			c.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{

		}

		//Actor actor = actors.get(id);
		if (actor != null) {
			logger.info("Inside getActorById, returned: " + actor.toString());
		} else {
			logger.info("Inside getActorById, ID: " + id + ", NOT FOUND!");
		}
		return actor;
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Actor updateActor(Actor actor) {
		//actors.put(""+actor.getId(), actor);

		try 
		{
			Class.forName ("oracle.jdbc.OracleDriver");
			Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
			PreparedStatement stmt = c.prepareStatement("update actors SET NAME= ? , BIRTHNAME = ? , BIRTHDATE = ? ,EMAIL= ?, " +
					"ACTIVE= ? where id=?");
			stmt.setString(1,actor.getName());
			stmt.setString(2,actor.getBirthName());
			stmt.setString(3,actor.getBirthDate());
			stmt.setString(4,actor.getEmail());
			stmt.setString(5,actor.getActive());
			stmt.setInt(6,actor.getId());
			int update=stmt.executeUpdate();
			System.out.println("Updated Succesfully " + update);
			//res.close();
			stmt.close();
			c.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{

		}

		System.out.println("updateActor with ID: " + actor.getId());
		if (actor != null) {
			logger.info("Inside updateActor, returned: " + actor.toString());
		} else {
			logger.info("Inside updateActor, ID: " + actor.getId() + ", NOT FOUND!");
		}
		return actor; 
	}

	@Path("/search/{query}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Actor> searchActorByName(@PathParam("query") String query) {
		System.out.println("Searching actor by Name: " + query);

		ArrayList<Actor> actorList = new ArrayList<Actor>();	  
		for (Actor c: actors.values()) {
			if (c.getName().toUpperCase().contains(query.toUpperCase()))
				actorList.add(c);
		}
		return actorList;
	}

	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Actor addActor(Actor actor) {
		System.out.println("Adding actor with ID: " + actor.getId());
		try 
		{
			Class.forName ("oracle.jdbc.OracleDriver");
			Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
			PreparedStatement stmt = c.prepareStatement("INSERT INTO actors"
					+ "(ID, NAME, BIRTHNAME, BIRTHDATE, EMAIL, ACTIVE) VALUES"
					+ "(?,?,?,?,?,?)");
			stmt.setInt(1,actor.getId());
			stmt.setString(2,actor.getName());
			stmt.setString(3,actor.getBirthName());
			stmt.setString(4,actor.getBirthDate());
			stmt.setString(5,actor.getEmail());
			stmt.setString(6,actor.getActive());		
			ResultSet res=stmt.executeQuery();

			res.close();
			stmt.close();
			c.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{

		}
		if (actor != null) {
			System.out.println("Inside addActor, returned: " + actor.toString());
			actors.put(""+actor.getId(), actor);
			System.out.println("# of actors: " + actors.size());
			System.out.println("Actors are now: " + actors);
		} else {
			System.out.println("Inside addActor, Unable to add actors...");
		} 
		return actor;
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Actor deleteActorById(@PathParam("id") String id) {
		System.out.println("Deleting actor with ID: " + id);

		try 
		{
			Class.forName ("oracle.jdbc.OracleDriver");
			Connection c = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","SYS as sysdba","system");
			PreparedStatement stmt = c.prepareStatement("DELETE ACTORS WHERE ID = ?");
			stmt.setString(1,id);
			int delete=stmt.executeUpdate();
			System.out.println("Deleted Succesfully " + delete);
			//res.close();
			stmt.close();
			c.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{

		}
		
		Actor actor = actors.remove(id);
		if (actor != null) {
			logger.info("Inside deleteActorById, returned: " + actor.toString());
		} else {
			logger.info("Inside deleteActorById, ID: " + id + ", NOT FOUND!");
		}
		return actor;
	}
}

//http://localhost:8081/RestfulWebServiceExample/index.html

//
//1	/rest/actors	   GET	Returns a list of all the actors available
//2	/rest/actors/{id}	GET	Returns actor based on the id
//3	/rest/actors/search/{query}	GET	Returns all of the actors matching the query anywhere in the name
//4	/rest/actors/{id}	DELETE	Delete the actor in the data store based on the id
//5	/rest/actors/{id}	PUT	Updates the actor in the data store based on the id
//6	/rest/actors/add	POST	Inserts the actor into the data store based on the contents of the form