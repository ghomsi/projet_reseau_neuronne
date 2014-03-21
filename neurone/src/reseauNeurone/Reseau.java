package reseauNeurone;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import lecture.CommandeException;
import lecture.Utile;

import analyseChiffres.CodeNumber;
import analyseChiffres.ConversionException;


/**
 * contient les poids, les resultat des application des fonctions d'activation, les delta de chaque couche
 * @author Souf
 *
 */
public class Reseau {
	float [] [] w1 ; // la matrice qui contiendra les poids de la premiere couche
	float [] [] w2 ; //la matrice qui contiendra les poids de la couche de sortie  
	float [] activation1 ;	//activation des neurones de la permiere couche 
	float [] activation2 = new float [10];  // activation des neurones de la couche de sortie
	float [] delta1; 	//le delta de la couche intermediaire (couche 1)
	float [] delta2 = new float[10];	//le delta de la couche de sortie  (couche 2)
	int [] stat = new int [2];          //statistique de reussite pour une liste donnée, case 0 pour les echec, case 1 pour reussite
	StringBuffer historique = new StringBuffer(); //sauvegarde l'historique des resultat des experiences tout au long d'une experience
	
	/**
	 * initialise les poids du reseau avec des valeur aleatoire ou des valeurs enregistré en fonction du parametre
	 * @param charger : vrai si on charge les valeurs des poids, sinon l'inverse
	 * @param taille : le nombre des neurones de la couches cachée 
	 * @throws ConversionException 
	 */
	public Reseau (boolean charger, int taille) throws ConversionException{
		w1 = new float [taille] [256];
		w2 = new float [10] [taille];
		activation1 = new float [taille];
		delta1 = new float [taille];
		if (!charger){
			Utile.init_aleatoire(w1);
			Utile.init_aleatoire(w2);
		}else
			upload_poid();
	}
	
	/**
	 * Constructeur qui prend en parametre la taille des neurones de la couche caché 
	 * @param taille
	 */
	public Reseau (int taille){
		w1 = new float [taille] [256];
		w2 = new float [10] [taille];
		activation1 = new float [taille];
		delta1 = new float [taille];
		Utile.init_aleatoire(w1);
		Utile.init_aleatoire(w2);
	}
	
	/**
	 * change la taille des neurones de couche couche et met met des poids aléatoirement
	 * @param taille : le nombre de neurone de la couche cache 
	 */
	public void upload_taille (int taille){
		w1 = new float [taille] [256];
		w2 = new float [10] [taille];
		activation1 = new float [taille];
		delta1 = new float [taille];
		Utile.init_aleatoire(w1);
		Utile.init_aleatoire(w2);
	}
	
	
	/**
	 * permet d'apprendre au reseau, donc de regler les poids et les enregistrer 
	 * @param liste : liste de nombre a apprendre
	 * @param iteration : le nombre d'iteration 
	 */
	public void apprentissage (ArrayList <CodeNumber> liste, int iteration){
		while(iteration != 0){
			Collections.shuffle(liste);
			Iterator <CodeNumber> iter = liste.iterator();
			while(iter.hasNext()){
				CodeNumber leNombre = iter.next();
			//----------------------------------------------------------------------------------------------
				//on calcule la fonction d'activation de la premiere couche 
				Traitement.calculer_activation(leNombre.getTableau(), w1, activation1);			
				//on calcule la fonction d'activation de la seconde couche 
				Traitement.calculer_activation(activation1,w2,activation2);
					
			//---------------Retropropagation---------------------------------------------------------------
				//on commence par calculer le delta de la couche de sortie
				Traitement.calculer_delta_sortie(delta2, activation2, leNombre.getNombre());
				//on calcule la somme des delta2 * poid correspondant 
				float[] delta2_w = Traitement.calcule_somme(w2, delta2);
				//calcule du delta de la couche intermediaire
				Traitement.calculer_delta_inter(delta1, activation1, delta2_w);
				//mise a jour des poids entre la couche intermediare et la couche de sortie
				Traitement.update_poid(w2, activation1, delta2);
				//mise a jour des poids entre la couche d'entre et la couche intermediaire
				Traitement.update_poid(w1, leNombre.getTableau(), delta1);	
			}
			iteration--;
		}
	}
	
	
	/**
	 * utilise le reseau pour reconnaitre un chiffre et renvoi un booléan  
	 * @param leNombre : le nombre représente par des pixels 
	 * @return False s'il ne reconnait pas le chiffre, sinon True
	 */
	protected boolean reconnaitre(CodeNumber leNombre){
		//on calcule la fonction d'activation de la premiere couche 
		Traitement.calculer_activation(leNombre.getTableau(), w1, activation1);			
		//on calcule la fonction d'activation de la seconde couche 
		Traitement.calculer_activation(activation1,w2,activation2);	
		//on verifie si on a activer le bon neurone
		boolean reconnue = Traitement.converge(activation2,leNombre.getNombre());	
		return reconnue;
	}
	
	
	/**
	 * fais une reconnaissance sur les pixels a partir d'un tableau d'entree 
	 * @param entree
	 * @return le resultat de la reconnaissance qui est le chiffre s'il est reconnue et -1 sinon
	 */
	public int reconnaitre(float[] entree){
		Traitement.calculer_activation(entree, w1, activation1);
		Traitement.calculer_activation(activation1,w2,activation2);
		return actif();
	}
	
	/**
	 * traite une liste de tableau de pixel d'images, et effectue une reconnaissance, retourne un String qui contient l'ensemble des résultat en detaille
	 * @param lesEntrees : une liste de tableau de float representant les pixels d'une images
	 * @return le rapport du traitement de reconnaissance de la liste
	 * @throws CommandeException 
	 */
	public String reconnaitre (ArrayList <float []> lesEntrees) throws CommandeException{
		stat[0] = stat[1] = 0;
		String rapport = "Rapport de reconnaissance : \n";
		if (! lesEntrees.isEmpty()){
			Iterator <float []> iter = lesEntrees.iterator();
			while (iter.hasNext()){
				//on effectue une reconnaissance sur chaque image dans la liste
				int resultat = reconnaitre (iter.next());
				if(resultat != -1){
					rapport += resultat+"\n";
					stat[1]++;
				}
				else{
					rapport += "inconnue\n";
					stat[0]++;
				}				
			}
		}else
			throw new CommandeException("Le programme n'a pas traiter les image : liste vide d'image");
		return rapport;
	}

	
	/**
	 * fournit le nombre d'echec et de reussite, dans un tableau a deux cases en effectuant une reconnaissance
	 * @param listeNombre : la liste des nombres a traite 
	 * @return un tableau a deux cases, case 0 pour le nombre d'echec, case 1 pour le nombre de reussite
	 */
	public void stat_reconnaissance (ArrayList <CodeNumber> listeNombre){
		stat [1] = stat[0] = 0;
		Iterator <CodeNumber> iter = listeNombre.iterator();
		CodeNumber leNombre = null;
		while (iter.hasNext()){
			leNombre = iter.next();
			if(reconnaitre(leNombre))
				stat[1]++;
			else
				stat[0]++;
		}
		update_histo();
	}
	
	
	/**
	 * effectue l'apprentissage et la reconnaissance de deux liste differentes 
	 * @param apprent : le fichier d'apprentissage
	 * @param reco : le fichier de reconnaissance
	 * @throws ConversionException 
	 * @throws CommandeException 
	 * @throws FileNotFoundException 
	 */
	public void apprent_reco(String apprent, String reco) throws ConversionException, CommandeException, FileNotFoundException{
		if(! Utile.est_repertoire(apprent) && !Utile.est_repertoire(reco)){
			//traitemnt du fichier d'apprentissage 
			ArrayList <CodeNumber> lesNombres = Utile.LireFichier(apprent);
			apprentissage(lesNombres, 85); //on effectue l'apprentissage
			//on charge la liste pour la reconnaissance
			ArrayList <CodeNumber> lesNombres_reconnaissance = Utile.LireFichier(reco);
			stat_reconnaissance(lesNombres_reconnaissance); // reconnaissance
		}else{
			if(Utile.est_repertoire(apprent))
				throw new CommandeException(apprent+" n'est pas un fichier valide !");
			else
				throw new CommandeException(reco + " n'est pas un fichier valide !");
		}
	}
	
	
	/**
	 * recherche le neurone actif de la couche de sortie 
	 * @return un String comportant le neurone actif
	 */
	public int actif(){
		return Traitement.neurone_actif(activation2);
	}
	
	
	/**
	 * sauvegarde les poids du reseau dans un fichier 
	 */
	public boolean save_poid (String lien){
		if (! Utile.est_repertoire(lien)){
			FileWriter r = null;
			try{
				r = new FileWriter(lien,false); //ecraser le fichier
				r.write("couche1\n"+Utile.toStringTab(w1)+"\ncouche2\n"+Utile.toStringTab(w2));
				r.close();
			}catch (IOException e) {
				System.err.println("Impossible de sauvegarder les poids");
				return false;
			}
			return true;
		}else{
			System.out.println("\n"+lien+" n'est pas un fichier valide");
			return false;
		}
	}
	
	
	/**
	 * met a jour le champ historique en y ajoutant les statistiques des dernieres expérience 
	 * enregistre le nombre de neurone en couche cache, le teta, et les stats
	 */
	public void update_histo (){
		Date date = new Date(); //on recupere la date du jour
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM hh:mm:ss" ); // format d'impression
		historique.append("\n\n"+sdf.format(date) +
				"\nNombre de neurone couche intermediaire :" + w1.length +
				"\nteta : " + Traitement.teta +
				"\n" + afficher_stat());
	}
	
	/**
	 * sauvegarde l'historique des statistique enregistré pendant une experience
	 */
	public boolean save_historique(String lien){
		if (! Utile.est_repertoire(lien)){
			String str = new String (historique);
			FileWriter r = null;
			try{
				r = new FileWriter("ressources/historique_stat.txt", true); //ecrire sur le fichier sans supprimer le contenue existant 
				r.write(str);
				r.close();
			}catch(IOException e){
				System.err.println("\nLe programme n'a pas pu sauvegarder l'historique de statistique \n");
				return false;
			}
			return true;
		}else{
			System.err.println("\n"+lien+" n'est pas un fichier valide");
			return false;
		}
	}
	
	/**
	 * telecharger les valeurs des poids enregistre lors de l'apprentissage des perceptron 
	 * @throws ConversionException 
	 */
	public void upload_poid () throws ConversionException{
		Scanner scan = null;
		File fichier = null;
		String ligne = new String();
		try{
			InputStream source = ClassLoader.getSystemClassLoader().getResourceAsStream("ressources/lesPoids.txt");
			if ( ! Utile.inputStreamToFile (source,new File ("./lesPoids.txt")))
				throw new ConversionException("\nLa copie du fichier lesPoids.txt a échoué, Impossible de traiter l'image");
			fichier = new File("./lesPoids.txt");
			scan = new Scanner(fichier);
			while(scan.hasNextLine()){
				ligne = scan.nextLine();
				if(ligne.startsWith("couch1"))
					for(int i = 0; i < w1.length; i++){
						ligne = scan.nextLine();
						ligne = ligne.replace('.', ',');
						Scanner scanligne = new Scanner(ligne).useDelimiter(";");
						for(int j = 0 ; j < w1[i].length; j++){
							w1[i][j] = scanligne.nextFloat();
						}
					}
				
				if(ligne.startsWith("couche2"))
					for(int i = 0; i < w2.length ; i++){
						ligne = scan.nextLine();
						ligne = ligne.replace('.', ',');
						Scanner scanligne = new Scanner(ligne).useDelimiter(";");
						for (int j = 0 ; j < w2[i].length; j++){
							if(scanligne.hasNextFloat())
								w2[i][j] = scanligne.nextFloat();
							else
								System.err.println(scanligne);
						}		
					}
			}
		}catch(FileNotFoundException e2){
			System.err.println("Impossible de charger les poids");
		}
	}
	
	/**
	 * enregistre les details des resultat de sortie des neurones de sortie d'une experience dans un fichier 
	 * @param leNombre
	 * @param reconnue
	 */
	public void detailler (int leNombre, boolean reconnue){
		String s = "chiffre = "+leNombre+"\n"+Utile.toStringTab(activation2)+"\nReconnu => "+reconnue+"\n";
		FileWriter r = null;
		try{
			r = new FileWriter ("ressources/detaille_sortie.txt",true);
			r.write(s);
			r.close();
		}catch(IOException e){
			e.printStackTrace();
			System.err.println("Erreur : ecriture => detailles resultat neurones sortie !!");
		}
	}
	
	/**
	 * affiche les statistiques d'une experience de reconnaissance
	 */
	public String afficher_stat (){
		String s = new String();
		s += ("echecs  => "+ stat[0]+"\n");
		s += ("Reussite => "+ stat[1]+"\n");
		s += ("statistique de reussite  => "+ Utile.stat_reussite(stat)+"%");
		return s;
	}
	
	
	/**
	 * genere un fichier html avec les statistiques, et l'execute automatiquement
	 * @param html : le nom du fichier html 
	 * @throws CommandeException 
	 */
	public void fichierHtml (String html) throws CommandeException {
		if(Utile.est_repertoire(html))
			throw new CommandeException(html+" n'est pas un fichier valide !");
		else{
			FileWriter r = null ;
			try {
				File f = new File (html);
				//Ecriture dans le fichier html
				r = new FileWriter (html,false);
				r.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> \n");
				r.write("<html lang=\"fr\" xml:lang=\"fr\" xmlns=\"http://www.w3.org/1999/xhtml\"> \n");
				r.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> \n");
				//on fournit le lien du fichier css qui sera recopier dans le meme dossier contenant le fichier html
				r.write("<link rel=\"stylesheet\" type=\"text/css\" href=\""+f.getAbsolutePath()+".css"+"\" /> \n");
				r.write("<head> <title>Taux de Reussite</title> \n");
				r.write("</head> \n <body> <h1> Reseau  de Neurones <br/> </h1> \n"); 
				r.write("<h3> Voici le taux de reussite des chiffres reconnus contenu dans donnees.txt : </h3> \n"); 
				r.write("<pre>" + historique + " </pre> \n </body> \n </html>");
				r.close();
				//ici on copie le fichier css enregistré dans le dossier ressource, dans l'emplacement du fichier html
				InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("neurone.css");
				if ( ! Utile.inputStreamToFile (is,new File (f.getAbsolutePath()+".css")))
					System.out.println("\nLa copie du fichier neurone.css a échoué, affichage de page html basique activé. ");
				//on execute la page html
				Desktop desktop = Desktop.getDesktop();
				desktop.open(new File(html));
			}catch(IOException e){
				System.err.println("Erreur : Fichier introuvable");
			}
		}
	}
	
}
