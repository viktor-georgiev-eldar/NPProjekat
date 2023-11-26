/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepository;

import java.util.List;

/**
 * Interfejs koji predstavlja repozitorijum za manipulaciju podacima.
 *
 * @param <T> Tip podataka sa kojima se radi
 * @param <K> Tip kljuca za pretragu podataka
 * 
 *  @author Viktor
 */
public interface Repository<T,K> {
    /**
     * Vrati sve podatke iz repozitorijuma.
     *
     * @return Lista podataka
     * @throws Exception U slucaju greske prilikom pristupa podacima
     */
    List<T> vratiSve() throws Exception;
    
    /**
     * Dodaj novi podatak u repozitorijum.
     *
     * @param t Podatak koji se dodaje
     * @return Broj koji oznacava uspeh dodavanja
     * @throws Exception U slucaju greske prilikom pristupa podacima
     */
    int dodaj(T t) throws Exception;
    
    /**
     * Vrsi izmenu postojeceg podatka u repozitorijumu.
     *
     * @param t Podatak koji se menja
     * @return Broj koji oznacava uspeh izmene
     * @throws Exception U slucaju greske prilikom pristupa podacima
     */
    int izmeni(T t) throws Exception;
    
    /**
     * Brisi podatak iz repozitorijuma.
     *
     * @param t Podatak koji se brise
     * @return Broj koji oznacava uspeh brisanja
     * @throws Exception U slucaju greske prilikom pristupa podacima
     */
    int izbrisi(T t) throws Exception;
    
    /**
     * Pronadji podatak u repozitorijumu na osnovu datog kljuca.
     *
     * @param k Kljuc za pretragu podataka
     * @return Pronadjeni podatak
     * @throws Exception U slucaju greske prilikom pristupa podacima
     */
    T nadji(K k) throws Exception;
}
