package rs.np.dbRepositoryImplementacija;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domenskiObjekti.Artikal;
import domenskiObjekti.Korisnik;
import domenskiObjekti.TipKorisnika;

class RepositoryKorisnikTest {

	private rs.np.dbRepository.DBRepository korisnikCrud;
	@BeforeEach
	void setUp() throws Exception {
		korisnikCrud = new RepositoryKorisnik();
	}

	@AfterEach
	void tearDown() throws Exception {
		korisnikCrud = null;
	}

	@Test
	void testUpisivanjeKorisnika() throws Exception {
		Korisnik user = new Korisnik(1, "Viktor", "Viktor123", "Viktor", "Georgiev", "0615648972", TipKorisnika.KORISNIK, false);
		Integer id = korisnikCrud.dodaj(user);
		
		Korisnik noviKorisnik = new Korisnik();
		noviKorisnik = (Korisnik) korisnikCrud.nadji(id);
		assertEquals(user.getIme(), noviKorisnik.getIme());
		assertEquals(user.getPrezime(), noviKorisnik.getPrezime());
		assertEquals(user.getUsername(), noviKorisnik.getUsername());
		assertEquals(user.getPassword(), noviKorisnik.getPassword());
		assertEquals(user.getTelefon(), noviKorisnik.getTelefon());
		assertEquals(user.isUlogovan(), noviKorisnik.isUlogovan());
		korisnikCrud.izbrisi(noviKorisnik);
	}
	
	@Test
	void testIzmenaKorisnika() throws Exception {
		Korisnik user = new Korisnik(1, "Viktor", "Viktor123", "Viktor", "Georgiev", "0615648972", TipKorisnika.KORISNIK, false);
		Integer id = korisnikCrud.dodaj(user);
		String noviBrojTelefona = "0618642571";
		
		Korisnik noviKorisnik = new Korisnik();
		noviKorisnik = (Korisnik) korisnikCrud.nadji(id);
		noviKorisnik.setTelefon(noviBrojTelefona);
		korisnikCrud.izmeni(noviKorisnik);
		noviKorisnik = (Korisnik) korisnikCrud.nadji(id);
		
		assertEquals(noviBrojTelefona, noviKorisnik.getTelefon());
		korisnikCrud.izbrisi(noviKorisnik);
	}
	
	@Test
	void testIzbrisiKorisnika() throws Exception {
		Korisnik user = new Korisnik(1, "Viktor", "Viktor123", "Viktor", "Georgiev", "0615648972", TipKorisnika.KORISNIK, false);
		Integer id = korisnikCrud.dodaj(user);
		
		Korisnik noviKorisnik = new Korisnik();
		noviKorisnik = (Korisnik) korisnikCrud.nadji(id);
		korisnikCrud.izbrisi(noviKorisnik);
		noviKorisnik = (Korisnik) korisnikCrud.nadji(id);
		assertEquals(0, noviKorisnik.getKorisnikId());
	}

	@Test
	void testDupliraniKorisnici() throws Exception {
		Korisnik user = new Korisnik(1, "Viktor", "Viktor123", "Viktor", "Georgiev", "0615648972", TipKorisnika.KORISNIK, false);
		Integer id = korisnikCrud.dodaj(user);
		assertThrows(java.lang.Exception.class, () -> korisnikCrud.dodaj(user));
		
		Korisnik noviKorisnik = new Korisnik();
		noviKorisnik = (Korisnik) korisnikCrud.nadji(id);
		korisnikCrud.izbrisi(noviKorisnik);
		noviKorisnik = (Korisnik) korisnikCrud.nadji(id);
		assertEquals(0, noviKorisnik.getKorisnikId());
	}
}
