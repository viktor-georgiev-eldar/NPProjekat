/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepositoryImplementacija;

import rs.np.dbRepository.DbConnectionFactory;
import domenskiObjekti.Korisnik;
import domenskiObjekti.Racun;
import domenskiObjekti.StavkaRacuna;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Viktor
 */
public class RepositoryRacun implements rs.np.dbRepository.DBRepository<Racun,Integer>{

    private Connection connection;

    public RepositoryRacun() {
        
    }
    
    @Override
    public List<Racun> vratiSve() throws Exception {
        //U ovoj funkciji postaviti parametre kao sto su id, korisnik, datum
        //TODO napraviti funkciju koja ce citati stavke racuna za dati id
    	// max() u selectu je dodat zbog razlicite postavke sql flaga 'only_full_group_by'. info: https://stackoverflow.com/questions/41887460/select-list-is-not-in-group-by-clause-and-contains-nonaggregated-column-inc
        List<Racun> lista=new ArrayList<>();
        String upit = "SELECT r.racunid,max(a.naziv), max(a.cena), max(sr.kolicina), max(k.ime), max(k.prezime), max(r.ukupno), max(r.datum) FROM stavka_racuna sr\n" +
                      "JOIN racun r USING(racunid)\n" +
                      "JOIN artikal a USING(artikalid)\n" +
                      "JOIN korisnik k USING(korisnikid)\n" +
                      "GROUP BY r.racunid\n"+
                      "ORDER BY r.racunid";
        connection=DbConnectionFactory.getInstance().getConnection();
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(upit);
        while(rs.next()){
            Racun r=new Racun();
            r.setRacunId(rs.getInt("r.racunid"));
            r.setDatum(rs.getTimestamp("max(r.datum)").toLocalDateTime());
            r.setUkupno(rs.getDouble("max(r.ukupno)"));
            Korisnik k=new Korisnik();
            k.setIme(rs.getString("max(k.ime)"));
            k.setPrezime(rs.getString("max(k.prezime)"));
            //TODO dodati ostale podatke za korisnika
            r.setKorisnik(k);
            List<StavkaRacuna> listaStavki=RepositoryStavkaRacuna.getInstance().nadjiZaId(r.getRacunId());
            r.setListaStavki(listaStavki);
            lista.add(r);
        }
        statement.close();
        return lista;
    }

    @Override
    public int dodaj(Racun t) throws Exception {
        try{
            optimizujRacun(t);
            connection=DbConnectionFactory.getInstance().getConnection();
            String upit="INSERT INTO racun (datum,korisnikid,ukupno) VALUES (?,?,?)";
            
            PreparedStatement statement=connection.prepareStatement(upit,Statement.RETURN_GENERATED_KEYS);
            
            statement.setTimestamp(1, new  Timestamp(System.currentTimeMillis()));
            statement.setInt(2, t.getKorisnik().getKorisnikId());
            statement.setDouble(3, t.getUkupno());
            
            statement.executeUpdate();
            ResultSet rs=statement.getGeneratedKeys();
            
            if(rs.next()){
                int racunId=rs.getInt(1);
                t.setRacunId(racunId);
                
                upit="INSERT INTO stavka_racuna VALUES(?,?,?)";
                statement=connection.prepareStatement(upit);
                for(StavkaRacuna sr:t.getListaStavki()){
                    
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
        }
        return 1;
    }

    @Override
    public int izmeni(Racun t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int izbrisi(Racun t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Racun nadji(Integer k) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void optimizujRacun(Racun r) {
        ArrayList<StavkaRacuna> pomocnaLista=new ArrayList<>();
        for(StavkaRacuna sr:r.getListaStavki()){
            dodajStavkuUListu(pomocnaLista, sr);
        }
        r.setListaStavki(pomocnaLista);
        izracunajStavkuUkupno(r);
    }
    
    private void dodajStavkuUListu(ArrayList<StavkaRacuna> lista,StavkaRacuna stavka){
        
        for(StavkaRacuna sr:lista){
            if(sr.getArtikal().getArtikalId()==stavka.getArtikal().getArtikalId()){
                sr.setKolicina(sr.getKolicina()+stavka.getKolicina());
                return;
            }
        }
        lista.add(stavka);
    }

    private void izracunajStavkuUkupno(Racun racun) {
        double suma=0;
        for(StavkaRacuna sr:racun.getListaStavki()){
            suma+=sr.getArtikal().getCena()*sr.getKolicina();
        }
        racun.setUkupno(suma);
    }
}
