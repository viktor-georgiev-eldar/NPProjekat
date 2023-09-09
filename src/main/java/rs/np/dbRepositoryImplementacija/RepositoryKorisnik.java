/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepositoryImplementacija;

import rs.np.db.DBBroker;
import rs.np.dbRepository.DbConnectionFactory;
import domenskiObjekti.Korisnik;
import domenskiObjekti.TipKorisnika;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Viktor
 */
public class RepositoryKorisnik implements rs.np.dbRepository.DBRepository<Korisnik,Long>{

    private Connection connection;

    public RepositoryKorisnik() {
        
    }
    
    @Override
    public List<Korisnik> vratiSve() throws Exception {
        connection=DbConnectionFactory.getInstance().getConnection();
        List<Korisnik> lista = new ArrayList<>();
        String upit = "SELECT * FROM korisnik k JOIN tip_korisnika tk ON tk.tipKorisnikaId=k.tipKorisnikaId";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(upit);
        while (rs.next()) {
            Korisnik k = new Korisnik();
            k.setIme(rs.getString("ime"));
            k.setPrezime(rs.getString("prezime"));
            k.setUsername(rs.getString("username"));
            k.setPassword(rs.getString("password"));
            k.setTelefon(rs.getString("telefon"));
            k.setKorisnikId(rs.getInt("korisnikId"));
            k.setTipKorisnika(TipKorisnika.valueOf(rs.getString("nazivTipaKorisnika").toUpperCase()));
            k.setUlogovan(rs.getBoolean("ulogovan"));
            lista.add(k);
        }
        rs.close();
        statement.close();
        return lista;
    }

    @Override
    public int dodaj(Korisnik t) throws Exception {
        String upit = "INSERT INTO korisnik (ime,prezime,telefon,username,password,tipKorisnikaId,ulogovan) VALUES (?,?,?,?,?,?,?)";
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(upit);
        ps.setString(1, t.getIme());
        ps.setString(2, t.getPrezime());
        ps.setString(3, t.getTelefon());
        ps.setString(4, t.getUsername());
        ps.setString(5, t.getPassword());
        ps.setInt(6, 2);
        ps.setBoolean(7, false);
        int znak = ps.executeUpdate();
        ps.close();
        connection.commit();
        return znak;
    }

    @Override
    public int izmeni(Korisnik t) throws Exception {
        String upit = "UPDATE korisnik SET ime=?, prezime=?, username=?, password=?, telefon=? WHERE korisnikId=?";
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setString(1, t.getIme());
        statement.setString(2, t.getPrezime());
        statement.setString(3, t.getUsername());
        statement.setString(4, t.getPassword());
        statement.setString(5, t.getTelefon());
        statement.setInt(6, t.getKorisnikId());
        int result = statement.executeUpdate();
        statement.close();
        connection.commit();
        return result;
    }
    
    @Override
    public int izbrisi(Korisnik t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Korisnik nadji(Long k) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    public boolean uloguj(String username){
        String upit = "UPDATE korisnik SET ulogovan=1 WHERE username='" + username+"'"; 
        int rez=0;
        try {
            connection=DbConnectionFactory.getInstance().getConnection();
            Statement statement = connection.createStatement();
            rez=statement.executeUpdate(upit);
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);            
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RepositoryKorisnik.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
        if (rez == 1) {
            return true;
        }
        return false;
    }
    
    public boolean izloguj(String username){
        String upit = "UPDATE korisnik SET ulogovan=0 WHERE username='" + username+"'";        
        int rez = 0;
        try {
            connection=DbConnectionFactory.getInstance().getConnection();
            Statement statement = connection.createStatement();
            rez = statement.executeUpdate(upit);
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RepositoryKorisnik.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if (rez == 1) {
            return true;
        }
        return false;
    }
    
    public boolean izlogujSve(){
        String upit="UPDATE korisnik SET ulogovan=0";
        try {
            connection=DbConnectionFactory.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(upit);
            connection.commit();
        } catch (SQLException ex) {           
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RepositoryKorisnik.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }
        return true;
    }    
    
}
