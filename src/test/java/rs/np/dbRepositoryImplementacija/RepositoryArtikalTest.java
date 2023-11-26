package rs.np.dbRepositoryImplementacija;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domenskiObjekti.Artikal;

class RepositoryArtikalTest {

	private rs.np.dbRepository.DBRepository artikalCrud;
	@BeforeEach
	void setUp() throws Exception {
		artikalCrud = new RepositoryArtikal();
	}

	@AfterEach
	void tearDown() throws Exception {
		artikalCrud = null;
	}

	@Test
	void testUpisivanjeArtikla() throws Exception {
		Artikal sok = new Artikal(255, "Vocni sok", "100% jagoda", 259);
		Integer id = artikalCrud.dodaj(sok);
		
		Artikal noviArtikal = new Artikal();
		noviArtikal = (Artikal) artikalCrud.nadji(id);
		assertEquals(sok.getNaziv(), noviArtikal.getNaziv());
		assertEquals(sok.getOpis(), noviArtikal.getOpis());
		assertEquals(sok.getCena(), noviArtikal.getCena());
		artikalCrud.izbrisi(noviArtikal);
	}
	
	@Test
	void testIzmenaArtikla() throws Exception {
		Artikal sok = new Artikal(255, "Vocni sok", "100% jagoda", 259);
		Integer id = artikalCrud.dodaj(sok);
		
		Artikal noviArtikal = new Artikal();
		noviArtikal = (Artikal) artikalCrud.nadji(id);
		noviArtikal.setCena(300);
		artikalCrud.izmeni(noviArtikal);
		noviArtikal = (Artikal) artikalCrud.nadji(id);
		
		assertEquals(sok.getNaziv(), noviArtikal.getNaziv());
		assertEquals(sok.getOpis(), noviArtikal.getOpis());
		assertEquals(300, noviArtikal.getCena());
		artikalCrud.izbrisi(noviArtikal);
	}
	
	@Test
	void testIzbrisiArtikla() throws Exception {
		Artikal sok = new Artikal(255, "Vocni sok", "100% jagoda", 259);
		Integer id = artikalCrud.dodaj(sok);
		
		Artikal noviArtikal = new Artikal();
		noviArtikal = (Artikal) artikalCrud.nadji(id);
		artikalCrud.izbrisi(noviArtikal);
		noviArtikal = (Artikal) artikalCrud.nadji(id);
		assertEquals(0, noviArtikal.getArtikalId());
	}

}
