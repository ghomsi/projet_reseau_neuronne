package analyseChiffres;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodeNumberTest {

	/**
	 * Déclaration
	 */
	CodeNumber codN;
	float[] tableau;
	int nombre ;
		
	/**
	 * Initialisation
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		 codN = new CodeNumber(256);
		 tableau = new float[256];
		 tableau[0] = 0;
		 nombre = 0 ;
	}

	@After
	public void tearDown() throws Exception {
		codN= null;
	}

	/**
	 * test si le tableau est créer avec la taille prédéfini 
	 */
	@Test
	public void testCodeNumber() {
	 Assert.assertArrayEquals(tableau,codN.getTableau(),256);
	}

	/**
	 * test l'ajout d'un pixel au tableau 
	 */
	@Test
	public void testAjouterPixel() {
          codN.ajouterPixel(0,(float) 1.0000);      
          assertEquals(codN.getPixel(0),tableau[0],1.0);
          
		
	}
	
	/**
	 * test que les  deux tableau sont les meme 
	 */
	@Test
	public void testCopy() {
		float[] tab2 = new float[256];
		int taille = tab2.length;
		for (int i = 0 ; i < taille ; i++)
				tab2[i] = this.tableau[i];
		
		Assert.assertArrayEquals(tab2,tableau, taille);
	}
	
   /**
    *test la methode to string 
    */
	@Test
	public void testToString() {
		
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < 256 ; i++){
				str = str.append("0.0000;");
		}
		str.append(nombre+"\n");
		String str1 = new String (str);
		
		assertEquals(str1,codN.toString());
	}

	@Test
	public void testSetNombre() {
		codN.setNombre(5);
		assertEquals(codN.getNombre(),5);
	}

	@Test
	public void testGetPixel() {
		Assert.assertEquals(tableau[0],codN.getPixel(0),0);
	}

	@Test
	public void testGetTableau() {
		Assert.assertArrayEquals(tableau,codN.getTableau(),256);
	}

}
