package lecture;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analyseChiffres.ConversionException;

public class CommandesTest {
	  /**
     * déclaration
     */
	Commandes com ;
	String [] args ;
	
	/**
	 * Initialisation
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		args = new String[3];
		args[0]="-name";
		//args[0]="-b";
		//args[1]="digits_train.txt";
		//args[2]="Images";
		com = new Commandes(args);
	
	}

	@After
	
	public void tearDown() throws Exception {
		com = null;
	}
	
    /**
     * test que la commande est la bonne si c'est pas le cas on léve une exception
     * @throws CommandeException
     * @throws IOException 
     */
	public void testAnalyse() throws CommandeException, IOException{
		
				try {
					com.analyse();
				} catch (FileNotFoundException e) {
					fail("fichier non trouver");
					e.printStackTrace();
				} catch (CommandeException e) {
					fail("\n---------------> Commande introuvable.");
				} catch (ConversionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
    /**
     * si c'est pas la commande -name et -h qui sont passé somme argument on léve une excepetion 
     * @throws CommandeException
     */
	@Test
	public void testCommand_1() throws CommandeException{
		
		try{
		com.commande_1();
		}catch(CommandeException e){
			fail("\n---------------> Commande introuvable.");
		}
		
	}
	
	/**
     * si c'est pas la commande -b et -i qui sont passé somme argument on léve une excepetion 
     * @throws CommandeException
     */
    public void testCommand_3() throws CommandeException, FileNotFoundException, ConversionException{
    	
    	try{
    		com.commande_3();
    		}catch(CommandeException e){
    			fail("\n---------------> Commande introuvable.");
    		}catch(ConversionException e){
    			fail(" \n Nom repertoir invalide");
    		}
		
	}
		
	/**
     * si c'est pas la commande -r -ri  et -w qui sont passé somme argument on léve une excepetion 
     * @throws CommandeException
	 * @throws IOException 
     */
	

    public void testCommand_4() throws  CommandeException,ConversionException, IOException{
	
    	try{
    		com.commande_4();
    		}catch(CommandeException e){
    			fail(" \n Commande introuvable");
    		}catch(ConversionException e){
    			fail(" \n Nom repertoir invalide");
    		}
    }
    
    

    /**
     * test que identification nous renvois bien les noms des personnes du groupe
     */
	@Test
	public void testIdentification() {		
	String str = "Groupe :\n-BOULEALF SOUFIANE\n-BELMEKNASSI Abdelhakim\n-CHAKIR Hatim\n-HUBERT Simon";
	assertEquals(com.identification(),str);
	
	}

	/**
     * test que Liste_option nous renvois bien la liste des options
     */
	@Test
	public void testListe_option() {
		
		String str ="\n----------------\n" +
				"REGLES ET FORMAT\n" +
				"----------------\n" +
				"Les codes des chiffres manuscrit enregistrŽs dans le fichier \"base.txt\" est de la forme => x1,x2,...,x256,leNombre." +
				"Le 1er caractere du nom d image doit contenir le nombre represente par l image.\n" +
				"Les caract�ristiques \"param\" Žcrit de la forme suivante => \"nombre_de_neurone_couche_cache\"-\"teta\" \n" +
				"teta : est le pas d'erreur compris entre 0 et 1 nŽcessaire pour le calcule et l'apprentissage. \n" +
				"\n" +
				"\n--------------------\n" +
				"LISTE DES COMMANDES\n" +
				"--------------------\n" +
				"\n-name : pour afficher les noms des membres du groupe.\n" +
				"\n-h : pour afficher la liste des options.\n" +
				"\n-b base.txt image : lit une base de chiffres manuscrits et crŽe un dossier \"image\" contenant les images " +
				"des chiffres de la \"base\". le numŽro que caractŽrise l'image sera inscrit sur le premier caract�re du nom de l image.\n" +
				"\n-i image base.txt : construit une base de chiffres manuscrits ˆ partir des fichiers contenus dans le dossier \"image\".\n" +
				"\n-r param base.txt donnees.txt : renvoie le taux de rŽussite des chiffres reconnus contenu dans \"donnees.txt\" par le" +
				"rŽseaux de neurones ayant comme caract�ristiques \"param\" et pour base d'apprentissage \"base.txt\".\n" +
				"\n-ri param base image : renvoi les valeurs des chiffres contenu dans les images du fichier \"image\" en effectuant une reconnaissance" +
				"sur l'ensemble des sous images reprŽsentant des chiffres, aprŽs avoir fais un apprentissage avec les images du dossier \"base\". \n" +
				"\n-w base.txt donnees.txt resultat.html : construit une page html contenant le taux de rŽussite des chiffres reconnus contenu" +
				"dans \"donnees.txt\" par le rŽseaux de neurones ayant comme base d apprentissage \"base.txt\" en faisant varier les" +
				"caracteristiques du reseaux.\n";
		assertEquals(com.liste_option(),str);
		
	}
}
