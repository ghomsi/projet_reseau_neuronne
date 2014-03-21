package reseauNeurone;

import static org.junit.Assert.*;

import java.io.File;

import lecture.Utile;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import analyseChiffres.CodeNumber;
import analyseChiffres.ConversionException;

public class ReseauTest {

	/**
	 * Declaration
	 */
	Reseau res; 
	float [] [] w1 ; // la matrice qui contiendra les poids de la premiere couche
	float [] [] w2 ; //la matrice qui contiendra les poids de la couche de sortie  
	float [] activation1 ;	//activation des neurones de la permiere couche 
	float [] activation2 = new float [10];  // activation des neurones de la couche de sortie
	float [] delta1; 	//le delta de la couche intermediaire (couche 1)
	float [] delta2 = new float[10];	//le delta de la couche de sortie  (couche 2)
	int [] stat = new int [2];          //statistique de reussite pour une liste donnée, case 0 pour les echec, case 1 pour reussite
	StringBuffer historique = new StringBuffer();
	
	/**
	 * initialisation
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		res = new Reseau(false,10);
	}

	@After
	public void tearDown() throws Exception {
		res = null;
	}

	/**
	 * test si l'objet reseau n'est pas null est qu'il a bien instancier w1 avec des aleur aleatoir 
	 * @throws ConversionException 
	 */
	@Test
	public void testReseau() throws ConversionException {	
		res = new Reseau(false,10);
		float [] [] matrice1 = new float[10][256];
		Utile.init_aleatoire(matrice1);
		assertNotNull(res);
		Assert.assertSame(w1, activation1);
	}
	
    /**
    * test s'il retourne bien a false si le reseau ne reconais pas le nombre 
    */
	@Test
	public void testReconnaitre() {
		CodeNumber lenombre = new CodeNumber(2);
		boolean a = res.reconnaitre(lenombre);
		assertFalse(a);	
	}
    
	/**
	 * test bien si le neurone n'est pas actif on retourne -1
	 */
	@Test
	public void testActif() {
		assertEquals(res.actif(),-1);
	}
   
	@Test
	public void testSave_historique() {
	 res.save_historique("ressources/historique_stat.txt");
	 File histo = new File("ressources/historique_stat.txt");
	 assertTrue(histo.isFile());
	}

}
