/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.admin.niti;

import komunikacija.*;
import rs.np.admin.forme.AdminForm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Viktor
 */
public class OsluskivanjeKlijenata extends Thread {

    private ServerSocket ss;
    private AdminForm af;
    private List<ObradaKlijenata> listaPovezanihKlijenata;

    public OsluskivanjeKlijenata(AdminForm af) {
        this.af = af;
        listaPovezanihKlijenata = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(9000);
           
            while (!ss.isClosed()) {

                af.serverPokrenut();
                System.out.println("Server je pokrenut");
                while (true) {
                    Socket s = ss.accept();
                    //obrada klijenta
                    ObradaKlijenata ok = new ObradaKlijenata(s, af);
                    listaPovezanihKlijenata.add(ok);
                    ok.start();
                    System.out.println("Klijent se povezao");
                }

            }
        } catch (IOException ex) {
            System.out.println("Server je zaustavljen");
            //Logger.getLogger(OsluskivanjeKlijenata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zaustaviServer() {
        try {
            for (ObradaKlijenata ok : listaPovezanihKlijenata) {
                ok.obavestiKlijentaOZatvaranjuServera();
                ok.zatvoriSoket();
            }
            ss.close();
            af.serverZaustavljen();
        } catch (IOException ex) {
            Logger.getLogger(OsluskivanjeKlijenata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
