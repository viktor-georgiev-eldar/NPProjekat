/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Cartman
 */
public class DbConnectionFactory {
    private Connection connection;
    private static DbConnectionFactory instance;
    
    private DbConnectionFactory(){
        
    }
    
    public static DbConnectionFactory getInstance(){
        if(instance==null){
            instance=new DbConnectionFactory();
        }
        return instance;
    }
     
     
    public Connection getConnection() throws SQLException{
       
        if (connection == null || connection.isClosed()) {
            try {
                //Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/seminarski";
                String user = "root";
                String password = "admin";
                connection = DriverManager.getConnection(url, user, password);
                connection.setAutoCommit(false);
            } catch (SQLException ex) {
                System.out.println("Neuspesno uspostavljanje konekcije!\n" + ex.getMessage());
                throw ex;
            }
        }
        return connection;
    }
}
