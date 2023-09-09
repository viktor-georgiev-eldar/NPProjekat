/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.tableModel;

import domenskiObjekti.Korisnik;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author agro
 */
public class TableModelKorisnici extends AbstractTableModel{

    List<Korisnik> lista;
    String[] kolone={"ime","prezime","broj telefona","username"};
    
    
    public TableModelKorisnici() {
        lista=rs.np.controller.ControllerAdministrator.getInstance().vratiKorisnike();
    }
    
    
    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Korisnik k=lista.get(rowIndex);
        switch(columnIndex){
            case 0:return k.getIme();
            case 1:return k.getPrezime();
            case 2:return k.getTelefon();
            case 3:return k.getUsername();
            default: return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }
    
    public Korisnik vratiKorisnikaPrekoImena(String ime){
        for(Korisnik k:lista){
            if(k.getUsername().equals(ime))
                return k;
        }
        return null;
    }
    
    
    
    
    
}
