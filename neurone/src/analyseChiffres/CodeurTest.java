package analyseChiffres;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodeurTest {

	Codeur cod ;
	CodeNumber codN;
	
	
	@Before
	public void setUp() throws Exception {
		cod = new Codeur();
		codN = new CodeNumber(256);
	}

	@After
	public void tearDown() throws Exception {
	cod = null;	
	}
	
	@Test
	public void testGetGris() {
		assertEquals(Codeur.getGris(6666666),158);
	}

	@Test
	public void testGetBlue() {
		assertEquals(Codeur.getBlue(6666666),170);
	}

	@Test
	public void testGetGreen() {
		assertEquals(Codeur.getGreen(6666666),185);
	}

	@Test
	public void testGetRed() {
		assertEquals(Codeur.getRed(6666666),101);
	}

/**
 * test que l'image du chemin a bien etait transformer en un tableau de pixels	
 */
	@Test
	public void testChiffrer_image() {
		
		float[] pixel = new float[256];
		float[] pixell = new float[256];
		try {
			pixel = Codeur.chiffrer_image("ressources/Imagestest/00.jpg");
		} catch (ConversionException e) {
			fail("chemin Invalide");
			e.printStackTrace();
		}
		Assert.assertArrayEquals(pixel,pixell,256);
	}

	/**
	 * test que capture_pixel a bien stocker les pixel de la primiere image dans la premiere  case
	 * de la liste de codeNumber.
	 */
	@Test
	public void testCapture_pixel() {
		
		ArrayList <CodeNumber> codepixel = new ArrayList<CodeNumber>();
		
		try {
			float [] pixel = new float[256];
			pixel[0]= 0;
			pixel = Codeur.chiffrer_image("Imagetest/00.jpg");
//			//codN.getTableau();
			Codeur.capture_pixel("Imagestest","00.jpg",codepixel);
			Assert.assertTrue(codepixel.get(0).getPixel(0)==pixel[0]);
		} catch (ConversionException e) {
			fail("chemin Invalide");
			e.printStackTrace();
		}
	}
	
/**
 * test que les nom des Images sont bien enregistrer dans le tableau 
 */
	@Test
	public void testAjout_nom_image() {
		ArrayList  <String> nom = new ArrayList<String>();
		nom.add("00.jpg");
		nom.add("10.jpg");
		try {
			assertEquals(nom.get(0), Codeur.ajout_nom_image("ressources/Imagestest").get(0));
		} catch (ConversionException e) {
			fail("chemin invalide");
			e.printStackTrace();
		}
	}

	@Test
	public void testConvertisseur() {
		float var = (float) Codeur.convertisseur((float)127.5);
		float var1 = (float) Codeur.convertisseur((float)255);
		assertTrue(var==0);
		assertTrue(var1==-1);
	}
	
	/**
	 * test si le fichier donné est bien créer avec une exception et l'assertion.
	 */
	@Test
	public void testChiffrer() {
		try {
			Codeur.chiffrer("ressources/Imagestest", "ressources/test1.txt");
			File fichiertest1 = new File("ressources/test1.txt");
			assertTrue(fichiertest1.isFile());
		} catch (ConversionException e) {
			fail("-------> ecriture dans fichier impossible");
		}
	
	}

}
