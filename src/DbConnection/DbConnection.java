package DbConnection;

import java.sql.*;
import java.util.Properties;

public class DbConnection {
    Connection connection = null;
    public Connection getConnection() {

        try {
            // Add the code on the env file
        }catch (SQLException e) {
            System.out.println("Unable to connect to DB");
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    //* Crear conexion statement y query
    public ResultSet readFromDb(String query) throws SQLException {
        ResultSet data = null;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            data = statement.executeQuery(query);
//            con.close();
        }catch (SQLException e) {
            System.out.println("Unable to get the Departaments Data");
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error while closing connection");
            e.printStackTrace();
        }
    }
}
