/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.db;

import domenskiObjekti.Artikal;
import domenskiObjekti.Korisnik;
import domenskiObjekti.Racun;
import domenskiObjekti.StavkaRacuna;
import domenskiObjekti.TipKorisnika;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import rs.np.admin.forme.korisnik.PrikazKorisnikaForm;
/**
 *
 * @author Viktor
 */
public class DBBroker {

    Connection connection;

    public DBBroker() {
    }

    public void otvoriKonekciju() {
        String user = "root";
        String password = "root";
        String upit = "jdbc:mysql://localhost:3306/npprojekat";
        try {
            connection = DriverManager.getConnection(upit, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zatvoriKonekciju() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Korisnik> vratiListuKorisnika() throws SQLException {
        List<Korisnik> lista = new ArrayList<>();
        String upit = "SELECT * FROM korisnik k JOIN tip_korisnika tk ON tk.tipKorisnikaId=k.tipKorisnikaId";
        otvoriKonekciju();
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
        zatvoriKonekciju();
       
        return lista;
    }

    public List<Racun> vratiListuRacuna() throws SQLException{
        //U ovoj funkciji postaviti parametre kao sto su id, korisnik, datum
        //TODO napraviti funkciju koja ce citati stavke racuna za dati id
        List<Racun> lista=new ArrayList<>();
        String upit = "SELECT r.racunid,a.naziv, a.cena, sr.kolicina, k.ime, k.prezime,r.ukupno, r.datum FROM stavka_racuna sr\n" +
                      "JOIN racun r USING(racunid)\n" +
                      "JOIN artikal a USING(artikalid)\n" +
                      "JOIN korisnik k USING(korisnikid)\n" +
                      "GROUP BY r.racunid\n"+
                      "ORDER BY r.racunid";
        otvoriKonekciju();
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(upit);
        while(rs.next()){
            Racun r=new Racun();
            r.setRacunId(rs.getInt("r.racunid"));
            r.setDatum(rs.getTimestamp("r.datum").toLocalDateTime());
            r.setUkupno(rs.getDouble("r.ukupno"));
            Korisnik k=new Korisnik();
            k.setIme(rs.getString("k.ime"));
            k.setPrezime(rs.getString("k.prezime"));
            //TODO dodati ostale podatke za korisnika
            r.setKorisnik(k);
            List<StavkaRacuna> listaStavki=vratiListuStavkiZaRacun(r.getRacunId());
            r.setListaStavki(listaStavki);
            lista.add(r);
        }
        statement.close();
        zatvoriKonekciju();
        return lista;
    }
    
    private List<StavkaRacuna> vratiListuStavkiZaRacun(int racunId) throws SQLException {
        List<StavkaRacuna> lista=new ArrayList<>();
        String upit="SELECT * FROM stavka_racuna sr\n" +
                "JOIN artikal a USING(artikalid)\n" +
                "WHERE sr.racunid=?";
        otvoriKonekciju();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setInt(1, racunId);
        ResultSet rs=statement.executeQuery();

        while(rs.next()){
            StavkaRacuna sr=new StavkaRacuna();
            Artikal a=new Artikal();
            a.setArtikalId(rs.getInt("sr.artikalid"));
            a.setCena(rs.getDouble("a.cena"));
            a.setNaziv(rs.getString("a.naziv"));
            a.setOpis(rs.getString("a.opis"));
            sr.setArtikal(a);
            sr.setKolicina(rs.getInt("sr.kolicina"));
            lista.add(sr);
        }
        statement.close();
        zatvoriKonekciju();
        return lista;
    }
     
    public int dodajKorisnika(String ime, String prezime, String brojTeledona, String username, String password) throws SQLException {

        String upit = "INSERT INTO korisnik (ime,prezime,telefon,username,password,tipKorisnikaId,ulogovan) VALUES (?,?,?,?,?,?,?)";
        otvoriKonekciju();
        PreparedStatement ps = connection.prepareStatement(upit);
        ps.setString(1, ime);
        ps.setString(2, prezime);
        ps.setString(3, brojTeledona);
        ps.setString(4, username);
        ps.setString(5, password);
        ps.setString(6, String.valueOf(TipKorisnika.KORISNIK).toLowerCase());
        ps.setBoolean(7, false);
        int znak = ps.executeUpdate();
        ps.close();
        zatvoriKonekciju();
        return znak;
    }

    public boolean izmeniKorisnika(Korisnik k) throws SQLException {

        String upit = "UPDATE korisnik SET ime=?, prezime=?, username=?, password=?, telefon=? WHERE korisnikId=?";
        otvoriKonekciju();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setString(1, k.getIme());
        statement.setString(2, k.getPrezime());
        statement.setString(3, k.getUsername());
        statement.setString(4, k.getPassword());
        statement.setString(5, k.getTelefon());
        statement.setInt(6, k.getKorisnikId());
        int result = statement.executeUpdate();
        statement.close();
        zatvoriKonekciju();
        if (result == 1) {
            return true;
        }
        return false;
    }

    public int dodajArtikal(String naziv, String opis, double cena) throws SQLException{
        
        String upit = "INSERT INTO artikal (naziv,opis,cena) VALUES (?,?,?)";
        otvoriKonekciju();
        PreparedStatement ps = connection.prepareStatement(upit);
        ps.setString(1, naziv);
        ps.setString(2, opis);
        ps.setDouble(3, cena);
        int znak=ps.executeUpdate();
        ps.close();
        zatvoriKonekciju();
        return znak;
    }
    
    public boolean izmeniArtikal(Artikal a) throws SQLException{
        
        String upit = "UPDATE artikal SET naziv=?, opis=?, cena=? WHERE artikalId="+a.getArtikalId();
        otvoriKonekciju();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setString(1, a.getNaziv());
        statement.setString(2, a.getOpis());
        statement.setDouble(3, a.getCena());
        int result = statement.executeUpdate();
        statement.close();
        zatvoriKonekciju();
        if (result == 1) {
            return true;
        }
        return false;
    }
    
    public List<Artikal> vratiListuArtikala() throws SQLException{
        List<Artikal> lista = new ArrayList<>();
        String upit = "SELECT * FROM artikal";
        otvoriKonekciju();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(upit);
        while(rs.next()){
            Artikal a=new Artikal();
            a.setArtikalId(rs.getInt("artikalId"));
            a.setNaziv(rs.getString("naziv"));
            a.setOpis(rs.getString("opis"));
            a.setCena(rs.getDouble("cena"));
            lista.add(a);
        }
        rs.close();
        statement.close();
        zatvoriKonekciju();
        return lista;
    }
    
    public boolean izlogujKorisnika(String username) {
        String upit = "UPDATE korisnik SET ulogovan=0 WHERE username='" + username+"'";
        otvoriKonekciju();
        int rez = 0;
        try {
            Statement statement = connection.createStatement();
            rez = statement.executeUpdate(upit);
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        zatvoriKonekciju();
        if (rez == 1) {
            return true;
        }
        return false;
    }

    public void ulogujKorisnika(String username) {
        String upit = "UPDATE korisnik SET ulogovan=1 WHERE username='" + username+"'";
        otvoriKonekciju();
        
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(upit);
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        zatvoriKonekciju();
    }
    
    public void dodajRacun(Racun r) throws Exception{
        
        try{
            otvoriKonekciju();
            connection.setAutoCommit(false);
            String upit="INSERT INTO racun (datum,korisnikid,ukupno) VALUES (?,?,?)";
            
            PreparedStatement statement=connection.prepareStatement(upit,Statement.RETURN_GENERATED_KEYS);
            
            statement.setTimestamp(1, new  Timestamp(System.currentTimeMillis()));
            statement.setInt(2, r.getKorisnik().getKorisnikId());
            statement.setDouble(3, r.getUkupno());
            
            statement.executeUpdate();
            ResultSet rs=statement.getGeneratedKeys();
            
            if(rs.next()){
                int racunId=rs.getInt(1);
                r.setRacunId(racunId);
                
                upit="INSERT INTO stavka_racuna VALUES(?,?,?)";
                statement=connection.prepareStatement(upit);
                for(StavkaRacuna sr:r.getListaStavki()){
                    
                    statement.setInt(1, racunId);
                    statement.setInt(2, sr.getArtikal().getArtikalId());
                    statement.setInt(3, sr.getKolicina());
                    
                    statement.executeUpdate();
                }
                
                
            }else{
                throw new Exception("Nije generisan kljuc");
            }
            
            connection.commit();
        }catch(Exception e){
            connection.rollback();
            e.printStackTrace();
            throw new Exception("Neuspesna transakcija");
        }finally{
            connection.setAutoCommit(true);
            zatvoriKonekciju();
        }
        
    }

    public boolean izlogujKorisnike() {
        otvoriKonekciju();
        String upit="UPDATE korisnik SET ulogovan=0";
        
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(upit);
        } catch (SQLException ex) {
            return false;
        }
        finally {
        zatvoriKonekciju();
        }
        return true;
    }
    
    
    
    
}
