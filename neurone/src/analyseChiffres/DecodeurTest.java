package analyseChiffres;



import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import junit.framework.Assert;
import lecture.Utile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DecodeurTest {

	Decodeur crIm ;
	 static int Hauteur ;
	 static int Largeur ;
	 BufferedImage image;
	
	@Before
	public void setUp() throws Exception {
		crIm = new Decodeur();
		Hauteur = 16;
		Largeur = 16;
		
	}

	@After
	public void tearDown() throws Exception {
		image= null;	
	}

	
	@Test
	public void testCreerImage() {
		
		image = new BufferedImage(Hauteur,Largeur,BufferedImage.TYPE_INT_RGB);
		crIm.creerImage();
	    Assert.assertNotSame(image,crIm.getImage()); 	
	}
	
/**
 * test si l'image est bien sauvgarder dans le dossier 
 * @throws FileNotFoundException
 * @throws ConversionException
 */
	@Test
	public void testSauverImage() throws FileNotFoundException, ConversionException {
		
		ArrayList<CodeNumber> lesNombre;
		try {
			lesNombre = Utile.LireFichier("ressources/test1.txt");
			if(lesNombre.isEmpty())
				throw new ConversionException("Erreur : Aucun chiffres manuscrit charge.");
			Iterator <CodeNumber> iter = lesNombre.iterator();
			Decodeur.creerRepertoire(iter, "ressources/dossier");
			File image = new File("ressources/dossier/01.png");
	        assertTrue(image.isFile());
		} catch (FileNotFoundException e) {
			fail("chemin invalide");
			e.printStackTrace();
		}
	}
	
/**
 * test le dossier est bien créer 
 * @throws ConversionException
 */
	@Test
	public void testCreerRepertoire() throws ConversionException {
		ArrayList<CodeNumber> lesNombre;
		try {
			lesNombre = Utile.LireFichier("ressources/test1.txt");
			if(lesNombre.isEmpty())
				throw new ConversionException("Erreur : Aucun chiffres manuscrit charge.");
			Iterator <CodeNumber> iter = lesNombre.iterator();
			Decodeur.creerRepertoire(iter, "ressources/dossier");
			File dossier = new File("ressources/dossier");
	        assertTrue(dossier.isDirectory());
	        dossier.delete();
		} catch (FileNotFoundException e) {
			fail("chemin invalide");
			e.printStackTrace();
		}
		
	}

}
