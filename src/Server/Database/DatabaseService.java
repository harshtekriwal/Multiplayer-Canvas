package Server.Database;

import java.sql.*;

public class DatabaseService {
    private Connection connection;
    public DatabaseService(){
        try{
            this.connection=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/multiusercanvas", "root", "rambo");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void registerUser(String username,String password){
        try{
            Statement statement = connection.createStatement();
            String query="insert into userDetails values(" + "'" +username + "'" + "," +  "'" + password + "'"+")";
            int result= statement.executeUpdate(query);
        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
    }
    public boolean isValidUser(String username, String password) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet2 = statement.executeQuery("select count(*) from userDetails where username =" + "'" + username + "'" + " AND password = " +  "'" + password + "'" );
            resultSet2.next();
            int count = resultSet2.getInt(1);

            if(count == 1) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean doesUserExists(String username) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet2 = statement.executeQuery("select count(*) from userDetails where username =" + "'" + username + "'" );
            resultSet2.next();
            int count = resultSet2.getInt(1);
            if(count == 1) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
