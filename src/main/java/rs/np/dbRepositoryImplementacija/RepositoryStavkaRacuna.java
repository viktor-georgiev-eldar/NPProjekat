/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepositoryImplementacija;

import rs.np.dbRepository.DbConnectionFactory;
import domenskiObjekti.Artikal;
import domenskiObjekti.StavkaRacuna;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Viktor
 */
public class RepositoryStavkaRacuna implements rs.np.dbRepository.DBRepository<StavkaRacuna,Integer>{

    private Connection connection;
    private static RepositoryStavkaRacuna instance; 

    private RepositoryStavkaRacuna() {
        
    }
    public static RepositoryStavkaRacuna getInstance(){
        if(instance==null)
            instance=new RepositoryStavkaRacuna();
        return instance;
    }
    
    @Override
    public List<StavkaRacuna> vratiSve() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int dodaj(StavkaRacuna t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int izmeni(StavkaRacuna t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int izbrisi(StavkaRacuna t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StavkaRacuna nadji(Integer k) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<StavkaRacuna> nadjiZaId(Integer k) throws Exception {
        List<StavkaRacuna> lista=new ArrayList<>();
        String upit="SELECT * FROM stavka_racuna sr\n" +
                "JOIN artikal a USING(artikalid)\n" +
                "WHERE sr.racunid=?";
        connection=DbConnectionFactory.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setInt(1, k);
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
        return lista;
    }
    
}
