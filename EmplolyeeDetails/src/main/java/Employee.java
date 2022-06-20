import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Employee {
		
	public  boolean validate(String empl_name,String role,String phone_number,String place) 
	{
		boolean status = false;  
	
		try{  
	Class.forName("com.mysql.cj.jdbc.Driver");
	
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "Current-Root-Password");
	
		PreparedStatement ps= con.prepareStatement("SELECT * FROM employee.employee where empl_name=? and role=? ");  
			ps.setString(1,empl_name); 
			ps.setString(2,role); 

			ResultSet rs=ps.executeQuery();  

if(rs.next()){
	status= true;
}
	   ps.close();
	   con.close();
	   }catch(Exception e)
		{e.printStackTrace();}
		return status;
}
}