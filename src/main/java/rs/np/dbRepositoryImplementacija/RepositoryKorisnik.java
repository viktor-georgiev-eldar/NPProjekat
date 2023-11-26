/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepositoryImplementacija;

import rs.np.db.DBBroker;
import rs.np.dbRepository.DbConnectionFactory;
import domenskiObjekti.Artikal;
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
 * Implementacija interfejsa DBRepository za manipulaciju podacima vezanim za Korisnika.
 * 
 * @author Viktor
 */
public class RepositoryKorisnik implements rs.np.dbRepository.DBRepository<Korisnik,Integer>{

    /**
     * Konekcija sa bazom podataka
     */
    private Connection connection;

    /**
     * Klasa koja se koristi za manipulaciju korisnika nad bazom
     */
    public RepositoryKorisnik() {
        
    }
    
    /**
     * Vraća sve korisnike iz baze podataka.
     * @return Lista korisnika
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
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
    
    /**
     * Dodaje novog korisnika u bazu podataka.
     * @param t Korisnik koji se dodaje
     * @return ID dodatog korisnika
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public int dodaj(Korisnik t) throws Exception {
        String upit = "INSERT INTO korisnik (ime,prezime,telefon,username,password,tipKorisnikaId,ulogovan) VALUES (?,?,?,?,?,?,?)";
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(upit,Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, t.getIme());
        ps.setString(2, t.getPrezime());
        ps.setString(3, t.getTelefon());
        ps.setString(4, t.getUsername());
        ps.setString(5, t.getPassword());
        ps.setInt(6, 2);
        ps.setBoolean(7, false);
        ps.executeUpdate();
        ResultSet rs=ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
        	id = rs.getInt(1);
        }
        ps.close();
        connection.commit();
        return id;
    }

    /**
     * Vrši izmenu postojećeg korisnika u bazi podataka.
     * @param t Korisnik koji se menja
     * @return Broj izmenjenih redova u bazi
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
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
    
    /**
     * Briše korisnika iz baze podataka.
     * @param t Korisnik koji se briše
     * @return 1 ako je uspesno obrisan, inace 0
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public int izbrisi(Korisnik t) throws Exception {
    	String upit = "DELETE FROM korisnik WHERE korisnikId=?";
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setInt(1, t.getKorisnikId());
        int result = statement.executeUpdate();
        statement.close();
        connection.commit();
        if (result==1) {
        	return 1;
        }
        return 0;
    }

    /**
     * Pronalazi korisnika u bazi podataka na osnovu datog kljuca.
     * @param k Kljuc za pretragu korisnika
     * @return Pronađeni korisnik
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public Korisnik nadji(Integer k) throws Exception {
    	String upit = "SELECT * FROM korisnik WHERE korisnikId="+k;
        connection=DbConnectionFactory.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(upit);
        Korisnik a =new Korisnik();
        if (rs.next()) {
            a.setKorisnikId(rs.getInt("korisnikId"));
            a.setUsername(rs.getString("username"));
            a.setPassword(rs.getString("password"));
            a.setIme(rs.getString("ime"));
            a.setPrezime(rs.getString("prezime"));
            a.setTelefon(rs.getString("telefon"));
            a.setUlogovan(rs.getBoolean("ulogovan"));
        }
        return a;
    }
 
    
    /**
     * Uloguje korisnika na osnovu korisnickog imena.
     *
     * @param username Korisnicko ime koje se uloguje
     * @return Rezultat ulogovanja (true/false)
     */
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
    
    /**
     * Izloguje korisnika na osnovu korisnickog imena.
     *
     * @param username Korisnicko ime koje se izloguje
     * @return Rezultat izlogovanja (true/false)
     */
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
    
    /**
     * Izloguje sve korisnike.
     *
     * @return Rezultat izlogovanja svih korisnika (true/false)
     */
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
