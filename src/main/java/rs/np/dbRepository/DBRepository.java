/*
 * Ovaj interfejs predstavlja proširenje Repository interfejsa za rad sa bazom podataka.
 */
package rs.np.dbRepository;

import java.sql.SQLException;

/**
 * Interfejs koji proširuje Repository interfejs i pruža dodatne funkcije za rad sa bazom podataka.
 *
 * @param <T> Tip podataka sa kojima se radi
 * @param <K> Tip kljuca za pretragu podataka
 * 
 *  @author Viktor
 */
public interface DBRepository<T,K> extends Repository<T,K> {
    /**
     * Uspostavlja konekciju sa bazom podataka.
     *
     * @throws SQLException U slucaju greske prilikom uspostavljanja konekcije
     */
    default public void connect() throws SQLException{
        DbConnectionFactory.getInstance().getConnection();
    }
    
    /**
     * Prekida konekciju sa bazom podataka.
     *
     * @throws SQLException U slucaju greske prilikom prekida konekcije
     */
    default public void disconnect() throws SQLException{
        DbConnectionFactory.getInstance().getConnection().close();
    }
    
    /**
     * Izvršava komitu nad bazom podataka.
     *
     * @throws SQLException U slucaju greske prilikom komita
     */
    default public void commit() throws SQLException{
        DbConnectionFactory.getInstance().getConnection().commit();
    }
    
    /**
     * Vraća bazu podataka u prethodno stanje iz prethodnog komita.
     *
     * @throws SQLException U slucaju greske prilikom rollback-a
     */
    default public void rollback() throws SQLException{
        DbConnectionFactory.getInstance().getConnection().rollback();
    }
}
