package rs.np.dbRepositoryImplementacija;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domenskiObjekti.Artikal;
import domenskiObjekti.Korisnik;
import domenskiObjekti.Racun;
import domenskiObjekti.StavkaRacuna;
import domenskiObjekti.TipKorisnika;

class RepositoryRacunTest {

	private rs.np.dbRepository.DBRepository racunCrud;
	@BeforeEach
	void setUp() throws Exception {
		racunCrud = new RepositoryRacun();
	}

	@AfterEach
	void tearDown() throws Exception {
		racunCrud = null;
	}

	@Test
	void testUpisivanjeRacuna() throws Exception {
		Korisnik user = new Korisnik(1, "Viktor", "Viktor123", "Viktor", "Georgiev", "0615648972", TipKorisnika.KORISNIK, false);
		rs.np.dbRepository.DBRepository korisnikCrud = new RepositoryKorisnik();
		int korisnikId =korisnikCrud.dodaj(user);
		user.setKorisnikId(korisnikId);
		Artikal sok = new Artikal(255, "Vocni sok", "100% jagoda", 259);
		rs.np.dbRepository.DBRepository artikalCrud = new RepositoryArtikal();
		int artikalId = artikalCrud.dodaj(sok);
		sok.setArtikalId(artikalId);
		List<StavkaRacuna> stavkeRacuna= new ArrayList<StavkaRacuna>();
		StavkaRacuna sr =new StavkaRacuna(sok, 2);
		stavkeRacuna.add(sr);
		Racun racun = new Racun(1, user, stavkeRacuna);
		Integer id = racunCrud.dodaj(racun);
		
		Racun noviRacun= new Racun();
		noviRacun = (Racun) racunCrud.nadji(id);
		assertEquals(racun.getKorisnik().getIme(), noviRacun.getKorisnik().getIme());
		assertEquals(racun.getKorisnik().getPrezime(), noviRacun.getKorisnik().getPrezime());
//		assertEquals(user.getPrezime(), noviRacun.getPrezime());
//		assertEquals(user.getUsername(), noviRacun.getUsername());
//		assertEquals(user.getPassword(), noviRacun.getPassword());
//		assertEquals(user.getTelefon(), noviRacun.getTelefon());
//		assertEquals(user.isUlogovan(), noviRacun.isUlogovan());
		racunCrud.izbrisi(noviRacun);
		korisnikCrud.izbrisi(user);
		artikalCrud.izbrisi(sok);
	}
	
//	@Test
//	void testIzmenaRacuna() throws Exception {
//		Korisnik user = new Korisnik(1, "Viktor", "Viktor123", "Viktor", "Georgiev", "0615648972", TipKorisnika.KORISNIK, false);
//		Integer id = racunCrud.dodaj(user);
//		String noviBrojTelefona = "0618642571";
//		
//		Korisnik noviKorisnik = new Korisnik();
//		noviKorisnik = (Korisnik) racunCrud.nadji(id);
//		noviKorisnik.setTelefon(noviBrojTelefona);
//		racunCrud.izmeni(noviKorisnik);
//		noviKorisnik = (Korisnik) racunCrud.nadji(id);
//		
//		assertEquals(noviBrojTelefona, noviKorisnik.getTelefon());
//		racunCrud.izbrisi(noviKorisnik);
//	}
//	
//	@Test
//	void testIzbrisiRacun() throws Exception {
//		Korisnik user = new Korisnik(1, "Viktor", "Viktor123", "Viktor", "Georgiev", "0615648972", TipKorisnika.KORISNIK, false);
//		Integer id = racunCrud.dodaj(user);
//		
//		Korisnik noviKorisnik = new Korisnik();
//		noviKorisnik = (Korisnik) racunCrud.nadji(id);
//		racunCrud.izbrisi(noviKorisnik);
//		noviKorisnik = (Korisnik) racunCrud.nadji(id);
//		assertEquals(0, noviKorisnik.getKorisnikId());
//	}

}
