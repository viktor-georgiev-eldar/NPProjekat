/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.admin.niti;

import domenskiObjekti.Korisnik;
import rs.np.admin.forme.AdminForm;
import com.mysql.cj.x.protobuf.MysqlxNotice;
import rs.np.controller.ControllerAdministrator;
import domenskiObjekti.Artikal;
import domenskiObjekti.Racun;
import domenskiObjekti.TipKorisnika;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import komunikacija.KlijentskiZahtev;
import komunikacija.Operacije;
import komunikacija.ServerskiOdgovor;

/**
 *
 * @author agro
 */
public class ObradaKlijenata extends Thread {

    private Socket socket;
    private AdminForm af;
    private Korisnik korisnik;

    public ObradaKlijenata(Socket socket, AdminForm af) {
        this.socket = socket;
        this.af = af;
    }

    @Override
    public void run() {
        KlijentskiZahtev kz;
        while (!socket.isClosed()) {
            try {
                kz = primiZahtev();
                switch (kz.getOperacija()) {
                    case Operacije.ZATVARANJE_FORME:
                        boolean uspesno = ControllerAdministrator.getInstance().izlogujKorisnika(kz.getPosiljalac().getUsername());
                        if (uspesno) {
                            posaljiOdgovor(new ServerskiOdgovor(Operacije.ZATVARANJE_FORME, null, uspesno));
                        } else {
                            posaljiOdgovor(new ServerskiOdgovor(Operacije.ZATVARANJE_FORME, null, false));
                        }
                        break;
                        
                    case Operacije.EVIDENTIRANJE://korisnik se povezao na server i server ga evidentira u bazu podataka
                        ControllerAdministrator.getInstance().ulogujKorisnika(kz.getPosiljalac().getUsername());
                        break;

                    case Operacije.VRATI_LISTU_ARTIKALA:
                    	String lista=ControllerAdministrator.getInstance().vratiArtikle();
                        posaljiOdgovor(new ServerskiOdgovor(Operacije.VRATI_LISTU_ARTIKALA, lista, true));
                        break;
                        
                    case Operacije.EVIDENTIRAJ_RACUN:
                        Racun racun=(Racun) kz.getArgument();
                        boolean znak=ControllerAdministrator.getInstance().dodajRacun(racun);
                        posaljiOdgovor(new ServerskiOdgovor(Operacije.EVIDENTIRAJ_RACUN, null, znak));
                        break;
                    case Operacije.LOGOVANJE:
                        Korisnik korisnik=kz.getPosiljalac();
                        Korisnik proveren=ControllerAdministrator.getInstance().vratiKorisnika(korisnik.getUsername(), korisnik.getPassword());
                        if(proveren==null || proveren.getTipKorisnika().equals(TipKorisnika.ADMINISTRATOR)){                            
                                posaljiOdgovor(new ServerskiOdgovor(Operacije.LOGOVANJE, null, false));
                        }else{
                           if(ControllerAdministrator.getInstance().proveriKorisnika(korisnik.getUsername(), korisnik.getPassword())=="korisnik"){
                               posaljiOdgovor(new ServerskiOdgovor(Operacije.LOGOVANJE, proveren, true));
                           }else{
                               posaljiOdgovor(new ServerskiOdgovor(Operacije.LOGOVANJE, null, false));
                           }
                        }
                        break;
                    default:
                        System.out.println("Nepoznati zahtev");
                        break;
                }
            } catch (Exception ex) {
                try {
                    socket.close();
                } catch (IOException ex1) {
                }
            }
        }

    }

    public void zatvoriSoket() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ObradaKlijenata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private KlijentskiZahtev primiZahtev() {
        KlijentskiZahtev kz = null;
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            kz = (KlijentskiZahtev) in.readObject();
        } catch (IOException ex) {
            //Logger.getLogger(ObradaKlijenata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ObradaKlijenata.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kz;
    }

    private void posaljiOdgovor(ServerskiOdgovor so) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(so);
            out.flush();
        } catch (IOException ex) {
           // Logger.getLogger(ObradaKlijenata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obavestiKlijentaOZatvaranjuServera() {
        ServerskiOdgovor so = new ServerskiOdgovor(Operacije.GASENJE_SERVERA, null, true);
        posaljiOdgovor(so);
    }
}
