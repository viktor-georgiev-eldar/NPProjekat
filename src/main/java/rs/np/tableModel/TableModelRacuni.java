/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.tableModel;

import domenskiObjekti.Racun;
import domenskiObjekti.StavkaRacuna;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Viktor
 */
public class TableModelRacuni extends AbstractTableModel {

    private List<Racun> listaRacuna;
    private String[] naziviKolona={"racunId", "ime(konobar)", "prezime(konobar)", "iznos racuna", "datum i vreme"};


    public TableModelRacuni() {
        this.listaRacuna=rs.np.controller.ControllerAdministrator.getInstance().vratiListuRacuna();
        /*for (Racun racun : listaRacuna) {
           System.out.print(racun.getRacunId()+" ");
           System.out.print(racun.getUkupno());
           System.out.println();
           for(StavkaRacuna sr:racun.getListaStavki()){
               System.out.println("   "+sr.getArtikal()+" "+sr.getKolicina());
           }
           //System.out.println();
           System.out.println("------------------");
        }
        System.out.println("KRAJ");*/
        fireTableDataChanged();
    }
      
    @Override
    public int getRowCount() {
        return listaRacuna.size();
    }

    @Override
    public int getColumnCount() {
        return naziviKolona.length;
    }

    @Override
    public String getColumnName(int column) {
        return naziviKolona[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Racun r=listaRacuna.get(rowIndex);
        switch (columnIndex){
            case 0: return r.getRacunId();
            case 1: return r.getKorisnik().getIme();
            case 2: return r.getKorisnik().getPrezime();
            case 3: return r.getUkupno();
            case 4: return r.getDatum();
        }
        return null;
    }
    
    public String vratiStavke(int brojRacuna,int kolona){
        if (kolona!=3){
            return "";
        }
        Racun r=listaRacuna.get(brojRacuna);
        String rezultat="<html>NAZIV CENA KOLICINA<br>";
        for (StavkaRacuna sr: r.getListaStavki()){
            rezultat+=sr.getArtikal().getNaziv()+"   "+sr.getArtikal().getCena()+"   "+sr.getKolicina()+"<br>";
        }
        return rezultat+"</html>";
    }
    
}
