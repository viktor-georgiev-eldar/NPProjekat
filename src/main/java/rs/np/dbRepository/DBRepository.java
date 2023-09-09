/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepository;

import java.sql.SQLException;

/**
 *
 * @author Viktor
 * @param <T>
 */
public interface DBRepository<T,K> extends Repository<T,K> {
    default public void connect() throws SQLException{
        DbConnectionFactory.getInstance().getConnection();
    }
    
    default public void disconnect() throws SQLException{
        DbConnectionFactory.getInstance().getConnection().close();
    }
    
    default public void commit() throws SQLException{
        DbConnectionFactory.getInstance().getConnection().commit();
    }
    
    default public void rollback() throws SQLException{
        DbConnectionFactory.getInstance().getConnection().rollback();
    }
}
