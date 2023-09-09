/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.tableModel;

import domenskiObjekti.Artikal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author agro
 */
public class TableModelArtikli extends AbstractTableModel{
    
    private List<Artikal> listaArtikala;
    private String[] naziviKolona={"naziv","opis","cena"};

    public TableModelArtikli() {
        try {
            listaArtikala=rs.np.controller.ControllerAdministrator.getInstance().vratiArtikle();
        } catch (Exception ex) {
            Logger.getLogger(TableModelArtikli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    @Override
    public int getRowCount() {
        return listaArtikala.size();
    }

    @Override
    public int getColumnCount() {
        return naziviKolona.length;
    }

    @Override
    public String getColumnName(int column) {
        if(column<naziviKolona.length)
            return naziviKolona[column];
        return "greska";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Artikal a=listaArtikala.get(rowIndex);
        switch(columnIndex){
            case 0:return a.getNaziv();
            case 1:return a.getOpis();
            case 2:return a.getCena();
            default: return "greska";
        }
    }

    public Artikal vratiArtikalPrekoImena(String ime){
        for(Artikal a:listaArtikala){
            if(a.getNaziv().equals(ime))
                return a;
        }
        return null;
    }
    
}
