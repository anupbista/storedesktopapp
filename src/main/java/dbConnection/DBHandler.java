package dbConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler extends Config{

    private Connection dbconnection;

    public Connection getConnection(){
        String connectionString = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname;
//        /***  Not needed on java 8 and above  **/
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            dbconnection = DriverManager.getConnection(connectionString,dbuser,dbpass);
//            System.out.println("Connected to Database");
        }
        catch (SQLException e){
            e.getStackTrace();
        }
        return dbconnection;
    }

}
