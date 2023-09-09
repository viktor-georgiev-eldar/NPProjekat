/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.np.dbRepository;

import java.util.List;

/**
 *
 * @author Viktor
 */
public interface Repository<T,K> {
    List<T> vratiSve() throws Exception;
    int dodaj(T t) throws Exception;
    int izmeni(T t) throws Exception;
    int izbrisi(T t) throws Exception;
    T nadji(K k) throws Exception;
}
