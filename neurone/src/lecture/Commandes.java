package lecture;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import reseauNeurone.Reseau;
import reseauNeurone.Traitement;

import analyseChiffres.CodeNumber;
import analyseChiffres.Codeur;
import analyseChiffres.ConversionException;
import analyseChiffres.Decodeur;

public class Commandes {
	
	private String [] args ;
	
	/**
	 * Constructeur 
	 * @param args : les arguments passe au programme 
	 */
	
	public Commandes(String [] args){
		this.args = args;
	}
	
	/**
	 * Analyse les arguments du programme pour executer les commandes
	 * @throws CommandeException
	 * @throws ConversionException
	 * @throws IOException 
	 */
	public void analyse () throws CommandeException, ConversionException, IOException{
		if(Utile.verif_args(args)){
			switch (args.length){
			case 1 : 
				commande_1(); break;
			case 3 : 
				commande_3(); break;
			case 4 :
				commande_4(); break;
			default : 
				throw new CommandeException();
			}		
		}else{
			throw new CommandeException ();
		}
		
	}
	
	/**
	 * traite les commande suivante : -name et -h
	 * @throws CommandeException
	 */
	protected void commande_1() throws CommandeException{
		if(args[0].equals("-name")){
			System.out.println(identification());
		}else{
			if(args[0].equals("-h"))
				System.out.println(liste_option());
			else
				throw new CommandeException();
		}
				
	}
	
	/**
	 * traite les commandes : -b et -i
	 * @throws CommandeException
	 * @throws ConversionException 
	 * @throws FileNotFoundException 
	 */
	protected void commande_3() throws CommandeException, ConversionException, FileNotFoundException{
		//si ce n'est pas la commande -b ou -i
		args[0] = args[0].replace(" ", "");
		if(args[0].length() > 2)
			throw new CommandeException();
		switch (args[0].charAt(1)){
		//commande -b
		case 'b' :
			System.out.println("\nLecture du fichier en cours...");
			ArrayList <CodeNumber> lesNombre = Utile.LireFichier(args[1]);
			if(lesNombre.isEmpty())
				throw new ConversionException("Erreur : Aucun chiffres manuscrit charge.");
			Iterator <CodeNumber> iter = lesNombre.iterator();
			System.out.println("\nCreation des images en cours..\n");
			Decodeur.creerRepertoire(iter, args[2]);
			System.out.println("\ndone.\n");
			break;
		//commande -i
		case 'i' : 
			System.out.println("\nChargement du contenue du dossier ... \n");
			Codeur.chiffrer(args[1], args[2]);
			System.out.println("\ndone.\n");
			break;
		//si ca ne correpond a aucune des deux commandes
		default : 
			throw new CommandeException();
		}
	}
	
	
	/**
	 * traite les commandes : -r , -ri et -w
	 * @throws CommandeException
	 * @throws ConversionException 
	 * @throws IOException 
	 */
	protected void commande_4() throws CommandeException, ConversionException, IOException{
		if(args[0].length() > 2){
			//pour la commande -ri 
			if(args[0].equals("-ri")){
				String [] param = Utile.extraire(args[1]);
				if(param == null)
					throw new CommandeException("erreur 1er parametre invalide veuillez respectez la forme \"nombre_de_neurone_couche_cache\"-\"" +
							"teta\"");
				System.out.println("\nParametrage du nombre des neurones de la couche cach�e...\n");
				Reseau leReseau = new Reseau (Integer.parseInt(param[0]));
				System.out.println("Parametrage du teta pour le calcule...\n");
				Traitement.setTeta(Double.parseDouble(param[1]));
				System.out.println("Chargement des images pour l'apprentissage ... \n");
				//extraction des images du repertoir passe en argument du programme
				ArrayList <CodeNumber> list_pixel = Codeur.liste_pixels(args[2]);
				System.out.println("Debut de l'apprentissage ... \n");
				//apprentissage avec la liste d'images charge
				leReseau.apprentissage(list_pixel, 85);
				
				System.out.println("Chargement des images de reconnaissance ...\n");
				//chargement de l'image comportant plusieurs images de chiffre
				ArrayList <float []> liste_tableau = Codeur.chiffrage(args[3]);
				System.out.println("Debut de la reconnaisance ... \n");
				//traitement de la liste des sous images
				System.out.println(leReseau.reconnaitre(liste_tableau)+
						"\n" + leReseau.afficher_stat() + "\n" +
						"\ndone. \n");
			}
			else
				throw new CommandeException();
		}else{
			switch (args[0].charAt(1)){
			//-----------------------------------------------------------
			case 'r' : 
				String [] param = Utile.extraire(args[1]);
				if(param == null)
					throw new CommandeException("erreur 1er parametre invalide veuillez respecter la forme \"nombre_de_neurone_couche_cache\"-\"" +
							"teta\"");
				System.out.println("\nParametrage du nombre des neurones de la couche cach�e...\n");
				Reseau leReseau = new Reseau (Integer.parseInt(param[0]));
				System.out.println("Parametrage du teta pour le calcule...\n");
				Traitement.setTeta(Double.parseDouble(param[1]));
				
				System.out.println("Debut de l'Apprentissage ... et preparation pour la reconnaissance ...\n" +
						"Veuillez patienter ... \n");
				//ici on va apprendre a partir de l'argument 2
				//et on va faire une reconnaissance avec l'argument 3
				leReseau.apprent_reco(args[2], args[3]);
				System.out.println(leReseau.afficher_stat()); 
				System.out.println("\ndone.\n");
				break;
			//----------------------------------------------------------------	
				//faire l'apprentissage avec le fichier d'argument 1 et la reconnaissance avec le fichier d'argument 2
				//en faisant varier le nombre de la couche cache et le teta (pas d'erreur)
				//on affiche les stats de l'experience sur une page web
			case 'w' :
				Reseau reseau_defaut = new Reseau(50);
				Traitement.setTeta(0.2);
				System.out.println("\nCette operation prendra quelques minutes. Veuillez patienter ... " +
						"\nTraitement en cours...\n");
				while(Traitement.teta < 0.95){
					int taille = 50; //taille des neurones de cache cache
					while (taille < 256){
						reseau_defaut.upload_taille(taille);
						reseau_defaut.apprent_reco(args[1], args[2]);
						taille *= 2; //on varie le nombre de neurone de la couche cache
					}
					Traitement.setTeta(Traitement.teta * 2); //on varie le teta
				}
				System.out.println("\nExecution de la page Html ...");
				reseau_defaut.fichierHtml(args[3]);
				break;
			default : throw new CommandeException();
			}
		}
	}
	
	
	/**
	 * retourne le string comportant le nom des membres du groupe
	 * @return les membres du groupe
	 */
	public String identification (){
		return ("Groupe :\n-BOULEALF SOUFIANE\n-BELMEKNASSI Abdelhakim\n-CHAKIR Hatim\n-HUBERT Simon");
	}
	
	
	/**
	 * Retourne un String de toutes les inforamtions sur les commandes 
	 * @return la liste des options du programme
	 */
	public String liste_option(){
		return ("\n----------------\n" +
				"REGLES ET FORMAT\n" +
				"----------------\n" +
				"Les codes des chiffres manuscrit enregistres dans le fichier \"base.txt\" est de la forme => x1,x2,...,x256,leNombre " +
				"Le 1er caractere du nom d image doit contenir le nombre represente par l image.\n" +
				"Les caract�ristiques \"param\" Žcrit de la forme suivante => \"nombre_de_neurone_couche_cache\"-\"teta\" \n" +
				"teta : est le pas d'erreur compris entre 0 et 1 necessaire pour le calcule et l'apprentissage. \n" +
				"\n" +
				"\n--------------------\n" +
				"LISTE DES COMMANDES\n" +
				"--------------------\n" +
				"\n-name : afficher les noms des membres du groupe.\n" +
				"\n-h : pour afficher la liste des options.\n" +
				"\n-b base.txt image : lit une base de chiffres manuscrits et crŽe un dossier \"image\" contenant les images " +
				"des chiffres de la \"base\". le numŽro que caractŽrise l'image sera inscrit sur le premier caract�re du nom de l image.\n" +
				"\n-i image base.txt : construit une base de chiffres manuscrits ˆ partir des fichiers contenus dans le dossier \"image\".\n" +
				"\n-r param base.txt donnees.txt : renvoie le taux de rŽussite des chiffres reconnus contenu dans \"donnees.txt\" par le " +
				"rŽseaux de neurones ayant comme caracteristiques \"param\" et pour base d'apprentissage \"base.txt\".\n" +
				"\n-ri param base image : renvoi les valeurs des chiffres contenu dans les images du fichier \"image\" en effectuant une reconnaissance " +
				"sur l'ensemble des sous images reprŽsentant des chiffres, aprŽs avoir fais un apprentissage avec les images du dossier \"base\". \n" +
				"\n-w base.txt donnees.txt resultat.html : construit une page html contenant le taux de rŽussite des chiffres reconnus contenu " +
				"dans \"donnees.txt\" par le reseaux de neurones ayant comme base d apprentissage \"base.txt\" en faisant varier les " +
				"caracteristiques du reseau.\n");
	}
	
	
	
}
