/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepositoryImplementacija;

import rs.np.dbRepository.DbConnectionFactory;
import domenskiObjekti.Artikal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacija interfejsa DBRepository za manipulaciju podacima vezanim za Artikal.
 * 
 * @author Viktor
 */
public class RepositoryArtikal implements rs.np.dbRepository.DBRepository<Artikal,Integer>{

	
    /**
     * Konekcija sa bazom podataka
     */
    private Connection connection;

    
    /**
     * Klasa koja se koristi za manipulaciju artikala nad bazom
     */
    public RepositoryArtikal() {
        
    }
    
    /**
     * Vraća sve artikle iz baze podataka.
     * @return Lista artikala
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public List<Artikal> vratiSve() throws Exception {
        List<Artikal> lista = new ArrayList<>();
        String upit = "SELECT * FROM artikal";
        connection=DbConnectionFactory.getInstance().getConnection();
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
        return lista;
    }

    /**
     * Dodaje novi artikal u bazu podataka.
     * @param t Artikal koji se dodaje
     * @return ID dodatog artikla
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public int dodaj(Artikal t) throws Exception {
        String upit = "INSERT INTO artikal (naziv,opis,cena) VALUES (?,?,?)";
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement(upit,Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, t.getNaziv());
        ps.setString(2, t.getOpis());
        ps.setDouble(3, t.getCena());
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
     * Vrši izmenu postojećeg artikla u bazi podataka.
     * @param t Artikal koji se menja
     * @return Broj izmenjenih redova u bazi
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public int izmeni(Artikal t) throws Exception {
        String upit = "UPDATE artikal SET naziv=?, opis=?, cena=? WHERE artikalId="+t.getArtikalId();
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setString(1, t.getNaziv());
        statement.setString(2, t.getOpis());
        statement.setDouble(3, t.getCena());
        int result = statement.executeUpdate();
        statement.close();
        connection.commit();
        return result;
    }

    /**
     * Briše artikal iz baze podataka.
     * @param t Artikal koji se briše
     * @return 1 ako je uspesno obrisan, inace 0
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public int izbrisi(Artikal t) throws Exception {
    	String upit = "DELETE FROM artikal WHERE artikalId=?";
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setInt(1, t.getArtikalId());
        int result = statement.executeUpdate();
        statement.close();
        connection.commit();
        if (result==1) {
        	return 1;
        }
        return 0;
    }

    /**
     * Pronalazi artikal u bazi podataka na osnovu datog kljuca.
     * @param k Kljuc za pretragu artikla
     * @return Pronađeni artikal
     * @throws Exception U slucaju greške prilikom pristupa podacima
     */
    @Override
    public Artikal nadji(Integer k) throws Exception {
    	String upit = "SELECT * FROM artikal WHERE artikalId="+k;
        connection=DbConnectionFactory.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(upit);
        Artikal a =new Artikal();
        if (rs.next()) {
            a.setArtikalId(rs.getInt("artikalId"));
            a.setNaziv(rs.getString("naziv"));
            a.setOpis(rs.getString("opis"));
            a.setCena(rs.getDouble("cena"));
        }
        return a;
    }
    
}
