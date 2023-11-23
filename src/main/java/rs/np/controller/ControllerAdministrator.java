/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.controller;

import rs.np.db.DBBroker;
import rs.np.dbRepositoryImplementacija.RepositoryArtikal;
import rs.np.dbRepositoryImplementacija.RepositoryKorisnik;
import rs.np.dbRepositoryImplementacija.RepositoryRacun;
import domenskiObjekti.Artikal;
import domenskiObjekti.Korisnik;
import domenskiObjekti.Racun;
import domenskiObjekti.StavkaRacuna;
import domenskiObjekti.TipKorisnika;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author agro
 */
public class ControllerAdministrator {
    
    private static ControllerAdministrator instance;
    private final rs.np.dbRepository.DBRepository korisnikCrud;
    private final rs.np.dbRepository.DBRepository artikalCrud;
    private final rs.np.dbRepository.DBRepository racunCrud;

    private ControllerAdministrator() {
        korisnikCrud=new RepositoryKorisnik();
        artikalCrud=new RepositoryArtikal();
        racunCrud=new RepositoryRacun();
    }
    
    public static ControllerAdministrator getInstance(){
        if(instance==null)
            instance=new ControllerAdministrator();
        return instance;
    }
    
    public String proveriKorisnika(String username,String password){
        List<Korisnik> lista=null;
        try {
            lista=korisnikCrud.vratiSve();
        } catch (Exception ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print("Username: ");
        System.out.println(username);
        System.out.print("Password: ");
        System.out.println(password);
        System.out.println("Korisnici: ");
        System.out.println(lista);
        for(Korisnik k:lista){
            if(k.getUsername().equals(username)){
                if(k.getPassword().equals(password)){
                    if(k.isUlogovan()==true) return "ulogovan";
                    else if(k.getTipKorisnika()==TipKorisnika.ADMINISTRATOR) return "administrator";
                    else if(k.getTipKorisnika()==TipKorisnika.KORISNIK) return "korisnik";
                    else return "greska";
                }
                return "Neispravna sifra";
            }
        }
        return "korisnik ne postoji";
    }
    
    /*public Korisnik proveriKorisnika(String username,String password){
        List<Korisnik> lista=null;
        try {
            lista=db.vratiListuKorisnika();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        Korisnik korisnik=null;
        for(Korisnik k:lista){
            if(k.getUsername().equals(username)){
                if(k.getPassword().equals(password)){
                    korisnik=k;
                    break;
                }
            }
        }
        return korisnik;
    }*/
    
    
    public int dodajKorisnika(String ime,String prezime,String brojTeledona,String username,String password) throws SQLException{
        Korisnik k=new Korisnik(0, username, password, ime, prezime, brojTeledona, TipKorisnika.KORISNIK, false);
        int znak=0;
                korisnikCrud.connect();
        try{            
                znak=korisnikCrud.dodaj(k);
                korisnikCrud.commit();            
        }catch(Exception e){
            korisnikCrud.rollback();
        }finally{
            korisnikCrud.disconnect();
        }
        return znak;
    }
    
    public List<Korisnik> vratiKorisnike(){
        List<Korisnik> lista=null;
        
        try {
            lista=korisnikCrud.vratiSve();
            //lista.stream().filter(k->k.getTipKorisnika()==TipKorisnika.KORISNIK).forEach(System.out::println);
            lista=lista.stream().filter(k->k.getTipKorisnika()==TipKorisnika.KORISNIK).collect(Collectors.toList());//ostavljamo samo korisnike u listi
            
        } catch (SQLException ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  lista;
    }
    
    public int izmeniKorisnika(Korisnik k) throws Exception{
        int result=0;
        try {
            result=korisnikCrud.izmeni(k);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return result;
    }

    public Korisnik vratiKorisnika(String username, String password) {
        Korisnik korisnik=null;
        List<Korisnik> lista=null;
        try {
            lista=korisnikCrud.vratiSve();
        } catch (Exception ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Korisnik k:lista){
            if(k.getUsername().equals(username) && k.getPassword().equals(password)){
                    korisnik=k;
            }
        }
        return korisnik;
    }
    
    public int dodajArtikal(String naziv,String opis, double cena) throws Exception{
        int znak=0;
        Artikal a=new Artikal(0, naziv, opis, cena);
        try {
            znak=artikalCrud.dodaj(a);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return znak;
    }
    
     public int izmeniArtikal(Artikal a) throws Exception{
         int result=0;
        try {
            result=artikalCrud.izmeni(a);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
     }

    
    public List<Artikal> vratiArtikle() throws Exception{
        
        List<Artikal> lista=null;
        try {
            lista=artikalCrud.vratiSve();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public List<Racun> vratiListuRacuna(){
        
        List<Racun> lista=null;
        try {
            lista=racunCrud.vratiSve();
        } catch (Exception ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public void ulogujKorisnika(String username){
        ((RepositoryKorisnik)korisnikCrud).uloguj(username);
    }
    public boolean izlogujKorisnika(String username){
        return ((RepositoryKorisnik)korisnikCrud).izloguj(username);
    }
    
    public boolean dodajRacun(Racun r){
        int znak=0;
        try {
            znak=racunCrud.dodaj(r);
        } catch (Exception ex) {
            Logger.getLogger(ControllerAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (znak>=1) return true;
        return false;       
    }

    public boolean izlogujKorisnike() {
        return ((RepositoryKorisnik)korisnikCrud).izlogujSve();
    }
    
    
}
