package lecture;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import analyseChiffres.CodeNumber;
import analyseChiffres.ConversionException;

public class UtileTest {

	/**
	 * Déclaration
	 */
  Utile ut;
	
	@Before
	public void setUp() throws Exception {
		ut = new Utile();
	}

	@After
	public void tearDown() throws Exception {
		ut = null;
	}

	@Test
	public void testLireFichier() throws FileNotFoundException, ConversionException {
		
		ArrayList <CodeNumber> lesimage = Utile.LireFichier("ressources/test_lirefichier.txt");
		ArrayList <CodeNumber> im = new ArrayList<CodeNumber>(); ;	
		
		        CodeNumber c = new CodeNumber(1);
				im.add(c);	
				Assert.assertNotSame(lesimage.get(0),im.get(0));
	}

	@Test
	public void testInit_aleatoire() {
		float[][] tab = new float[10][10] ;
		 float[][] tab2  = new float[10][10]; 
		 Utile.init_aleatoire(tab);
		 Utile.init_aleatoire(tab2);
	    Assert.assertNotSame(tab , tab2);
	}

	@Test
	public void testToStringTabFloatArray() {
		float[] tab = new float[1];
		tab[0]= (float) 0.1;
		assertEquals(Utile.toStringTab(tab),"case 0 => 0.1\n");
	}

	@Test
	public void testToStringTabFloatArrayArray() {
		 float[][] tab = new float[1][1];	
			tab[0][0]= (float) 0.1;

			assertEquals(Utile.toStringTab(tab),"0.1");
	}

	@Test
	public void testStat_reussite() {
		int[] tab = new int [2] ;
		tab[0] = 10;
		tab[1] = 90;
	    double res = Utile.stat_reussite(tab);
	    assertTrue(res==90.0);
       
	}

	@Test
	public void testVerif_args() {

		String[] args = new String[1] ;
	    args[0] = "/";	
	    assertFalse(Utile.verif_args(args));
	}


	@Test
	public void testEst_repertoire() {
		File rid = new File ("ressources/Imagestest");	
		assertTrue(rid.isDirectory());
	}


	@Test
	public void testVerif_string_chiffre() {
		boolean ver = Utile.verif_string_chiffre("test",9);
		boolean ver2 = Utile.verif_string_chiffre("test",0);
		assertFalse(ver);
		assertEquals(ver2,false);
	}

	@Test
	public void testExtraire() {
		String [] tab = new String [2];
		tab[0] = "0,0000";
		tab[1] = "1,0000";	
		Assert.assertArrayEquals(Utile.extraire("-0.0000-1.0000-"),null);
	}

	@Test
	public void testEst_image() {
		assertTrue(Utile.est_image("Imagestest/00.jpg"));
	}

}
