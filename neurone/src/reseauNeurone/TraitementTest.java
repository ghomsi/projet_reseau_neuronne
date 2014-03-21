package reseauNeurone;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TraitementTest {

	/**
	 * Déclaration
	 */
	Traitement tr ;
	 int nombre ;
	static double teta ;  
	static double seuil_activation;
	static double seuil_desactivation ;
	float [][] poid ;
	float [] entree;
	float [] sortie;
	
	/**
	 * Initialisation
	 */
	@Before
	public void setUp() throws Exception {
	
		tr = new Traitement();
		poid =  new float[1][1] ;
		entree =  new float [1];
		sortie =  new float [1];
		
		teta = 0.5;
		seuil_activation = 0.9;
		seuil_desactivation = 0.1;
	
		 poid[0][0]=(float) 0.25;
		 entree[0] = (float) 0.5;
		 sortie[0] = (float) 0.2;
		 nombre = 0;
		
	}

	@After
	public void tearDown() throws Exception {
		tr = null;
	}

	/**
	 * test que le calcule de l'activation ce fait de la bonne facon 
	 */
	@Test
	public void testCalculer_activation() {
		
		 float[] resultat = new float[1];	 
		 Traitement.calculer_activation(entree, poid, resultat);	 
		 assertTrue(resultat[0]==(float) 0.53120935);
		 
	}

	/**
	 * test que calcule somme calcule bien la somme des poid de la couche intermediaire donées et les poid d'entrer
	 */
	@Test
	public void testCalcule_somme() {
		
		poid[0][0]=(float) 0.25;
	    entree[0] = (float) 0.5;
	    float[] r = Traitement.calcule_somme(poid, entree);
	    assertTrue(r[0]==(float) 0.125);
	    
	    
	}
    /**
     * test le calcule du delta de la couche de sortie 
     */
	@Test
	public void testCalculer_delta_sortie() {
		
		float[] delta = new float[1];
		Traitement.calculer_delta_sortie(delta, sortie, nombre);
		assertTrue(delta[0]==(float)0.128);
		
	}

	/**
	 * test le calcule du delta de la couche intermediaire
	 */
	@Test
	public void testCalculer_delta_inter() {
		
		float[] delta = new float[1];
		float[] inter = new float[1];
		float[] w = new float[1];
		inter[0] = (float)0.5;
		w[0] =(float)0.2;
		Traitement.calculer_delta_inter(delta, inter, w);
		assertTrue(delta[0]==(float)0.05);
	}

	
	@Test
	public void testUpdate_poid() {
		
		float[] ai = new float[1];
		float[] delta = new float[1];
		ai[0] = (float)0.5;
		delta[0] =(float)0.2;
		Traitement.update_poid(poid, ai, delta);
		assertTrue(poid[0][0]==(float)0.325);
	}

	/**
	 * test que la mise a jour des poid ce fait avec succes 
	 */
	@Test
	public void testConverge() {
		boolean res = Traitement.converge(sortie, nombre);	
		assertFalse(res);
	}

	/**
	 * test qu'il retourne bien -1 si aucun des nerone n'est actif 
	 */
	@Test
	public void testNeurone_actif() {
		int r = Traitement.neurone_actif(sortie);
		assertEquals(r,-1);
		
	}

}
