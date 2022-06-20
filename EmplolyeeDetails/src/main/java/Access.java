import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 
@WebServlet("/Access")
public class Access extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(Access.class);
	static {
	try {
	SimpleLayout layout = new SimpleLayout();
	ConsoleAppender appender =new ConsoleAppender(layout);
	logger.addAppender(appender);
	logger.setLevel(Level.DEBUG);
	logger.info("Acceptclass::log4jsetup is ready");}
	
	 catch(Exception e) {			
		e.printStackTrace();
		logger.fatal("Acceptclass::problem while setting up log4j");
	}}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException {
		
		logger.debug("Acceptclass::start of doGet method(-)");
		
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		String empl_name=req.getParameter("username");
		String role= req.getParameter("password");
		String empl_id= req.getParameter("empl_id");
		
		PrintWriter out= res.getWriter();
		Employee emp= new Employee();
		Connection c=null;
	    PreparedStatement ps=null;
	    Statement st=null;
	    ResultSet r=null;
		
		 try {
			 if(emp.validate(empl_name, role)) {
				Class.forName("com.mysql.jdbc.Driver");
				
				logger.debug("Acceptclass::JDBC driver class is loaded");
				
			 c=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "Current-Root-Password");
			 
			 logger.info("Acceptclass::JDBC connection is established");
			  HttpSession s=req.getSession();  
		        s.setAttribute("empl_id",empl_id);
		        
		        logger.debug("Acceptclass::session created");
		       
			if(role.equals("admin")) {
				System.out.println("CONNECTION SUCCESSFUL");
				st= c.createStatement();
				
				logger.debug("Acceptclass::JDBC statement created");
				
     			r = st.executeQuery("SELECT * FROM employee.employee");
			 while (r.next()) {
		            JSONArray jsonr= new JSONArray();
			        JSONObject jo= new JSONObject();
			        jo.put("empl_id", r.getLong("empl_id"));
					jo.put("id", r.getLong("id"));
					jo.put("empl_name", r.getString("empl_name"));
					jo.put("phone_number", r.getLong("phone_number"));
					jo.put("place", r.getString("place"));
					jo.put("role", r.getString("role"));
					
			        jsonr.add(jo);
			        out.println(jsonr);
			}
			 logger.debug("resultset object is processed");
			 RequestDispatcher rd = req.getRequestDispatcher("Update");
             rd.include(req, res);
				}
			else {
				 ps= c.prepareStatement("SELECT * FROM employee.employee  where empl_name=?");   
		         ps.setString(1,empl_name);
		         ResultSet rs = ps.executeQuery();
		         while (rs.next()) {
		            JSONArray jsonr= new JSONArray();
			        JSONObject jo= new JSONObject();
			        jo.put("empl_id", rs.getLong("empl_id"));
				    jo.put("id", rs.getLong("id"));
					jo.put("empl_name", rs.getString("empl_name"));
					jo.put("phone_number", rs.getLong("phone_number"));
					jo.put("place", rs.getString("place"));
					jo.put("role", rs.getString("role"));
					
			        jsonr.add(jo);
			        out.println(jsonr);
			        logger.warn("records of resultset is retrieved by using  getString method for all columns.change them accordingly.");
		         }}}
			 RequestDispatcher rd = req.getRequestDispatcher("Update");
             rd.include(req, res);
				
			 }
		 catch(Exception e) {			
				e.printStackTrace();
				logger.fatal("Acceptclass::unknown db problem"+e.getMessage());
			}
			 finally{
				 logger.debug("Acceptclass::clossing JDBC objects");
				 try {
					 if( r!= null)
					 r.close();
					 logger.debug("Acceptclass::resultset object is closed");
				 }
				 catch(SQLException se) {
					 se.printStackTrace();
					 logger.error("Acceptclass:: failed to close resultset object"+se.getMessage());
					 
				 }
				 try {
					 if( st!= null)
					 st.close();
					 logger.debug("Acceptclass::statement object is closed");
				 }
				 catch(SQLException se) {
					 se.printStackTrace();
					 logger.error("Acceptclass:: failed to close statement object"+se.getMessage());
					 
				 }
				 try {
					 if( c!= null)
					 c.close();
					 logger.debug("Acceptclass::connection object is closed");
				 }
				 catch(SQLException se) {
					 se.printStackTrace();
					 logger.error("Acceptclass:: failed to close connection object"+se.getMessage());
					 
				 }
				 }
			 try {
				 if( r!= null)
				 r.close();
				 logger.debug("Acceptclass::resultset object is closed");
			 }
			 catch(SQLException se) {
				 se.printStackTrace();
				 logger.debug("Acceptclass::failed to close resultset object");
				 
			 		}
			 logger.debug("Acceptclass::end of the main method");
			 	}
			 	}
