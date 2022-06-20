import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 
@WebServlet("/Access")
public class Update extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(Update.class);
	static {
	try {
	SimpleLayout layout = new SimpleLayout();
	ConsoleAppender appender =new ConsoleAppender(layout);
	logger.addAppender(appender);
	logger.setLevel(Level.DEBUG);
	logger.info("Updateclass::log4jsetup is ready");}
	
	 catch(Exception e) {			
		e.printStackTrace();
		logger.fatal("Updateclass::problem while setting up log4j");
	}}
	
	
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException {
		logger.debug("Updateclass::start of doPut method(-)");
		
		res.setContentType("application/json");
		String empl_ID= req.getParameter("empl_id");
	int empl_id=Integer.parseInt(empl_ID);
		
		String place = req.getParameter("place");
		
		PrintWriter out= res.getWriter();
		Connection c=null;
	    PreparedStatement ps=null;
	    ResultSet r=null;
		
		 try {
			 HttpSession s= req.getSession(false);
				if(s!=null)
				{
				Class.forName("com.mysql.jdbc.Driver");
				
				logger.debug("Updateclass::JDBC driver class is loaded");
			 c=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "Current-Root-Password");
			 logger.info("Updateclass::JDBC connection is established");
			 
		
 
				ps= c.prepareStatement("UPDATE employee.employee SET place=? WHERE empl_id =?");
				logger.debug("Updateclass::JDBC prepareStatement created");
				 ps.setString(1,place);
				 ps.setInt(2, empl_id);				
		         ps.executeUpdate();
		         
		         out.println("updated");
		         logger.debug("Updateclass::database updated");
		         ps= c.prepareStatement("SELECT * FROM employee.employee  where empl_id=?");   
		         System.out.println(empl_id);
	               ps.setInt(1,empl_id);
	               r = ps.executeQuery();
		         
			    while (r.next()) {
			    	JSONArray jsonr= new JSONArray();
			    	JSONObject jo= new JSONObject();
			     	jo.put("empl_id", r.getInt("empl_id"));
					jo.put("id", r.getInt("id"));
					jo.put("empl_name", r.getString("empl_name"));
					jo.put("phone_number", r.getString("phone_number"));
					jo.put("place", r.getString("place"));
					jo.put("role", r.getString("role"));
					
					jsonr.add(jo);
					out.println(jsonr);	
					logger.info("Updateclass::printed updated data");
		         
			    	}}}
		 catch(Exception e) {			
			e.printStackTrace();
			logger.fatal("Updateclass::unknown db problem"+e.getMessage());
		 	}}}
