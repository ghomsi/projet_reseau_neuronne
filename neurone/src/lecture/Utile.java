package lecture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import analyseChiffres.CodeNumber;
import analyseChiffres.ConversionException;

/**
 * une classe de fonction utile, tableau, une dimension, plusieurs dimensions
 * initialisation avec valeurs aleatoire dans un tablea etc..
 * @author Souf
 *
 */
public class Utile {
	
	
	/**
	 * lit un fichier de pixels de la forme x1;x2;...;x256;nb ou {x1,x2...,x256} sont des pixels et nb et le nombre represente par les pixels
	 * chaque ligne contient une representation de nombre
	 * Charge le contenu du fichier dŽcrit dans le chemin passŽ en param�tre dans une collection de type ArrayList 
	 * Attention => 
	 * @param chemin : reprŽsente le chemin du fichier qui doit �tre chargŽ
	 * @throws ConversionException 
	 * @throws FileNotFoundException 
	 */
	public static ArrayList <CodeNumber> LireFichier(String chemin) throws ConversionException, FileNotFoundException{
		//verification du chemin si c'est un fichier ou un repertoire ?
		if(!est_repertoire(chemin)){
			File fichier = new File(chemin);
			Scanner scanLigne = null;   //scanner de ligne 
			ArrayList <CodeNumber> lesImages = new ArrayList <CodeNumber>();
			scanLigne = new Scanner (fichier);
			String ligne =  scanLigne.nextLine();
			//on scanne la premiere ligne qui est la ligne d'exemple-de-format 
			Scanner scan = new Scanner (ligne).useDelimiter(",");
			int taille = 0;  //la taille de l'image, le nombre total de pixels
			//on compte le nombre de pixel, et donc la taille de tout les pixels 
			//pour s'adapter au nombres de pixels (le dernier elements et le nombre lui meme)
			while(scan.hasNext()){
				scan.next();
				taille++;
			}
			taille--;
			//on scan le code pixel de chaque nombre 
			while (scanLigne.hasNextLine()){
				ligne = scanLigne.nextLine();
				//si la ligne n'est pas vide
				if(!ligne.isEmpty()){
					//on remplace les delimiter par defaut "," par ";" car on utilisera la notation des 
					//float par "," car on va utiliser le nextFloat du scan par la suite 
					//nous avons remarquŽ que la nextFloat de la Class Scanner reconnais les float uniquement
					// par des "," et non pas avec des "."
					ligne = ligne.replace(",", ";");
					ligne = ligne.replace(".", ",");
					scan = new Scanner (ligne).useDelimiter(";");
					float [] tabPixel = new float[taille];   //le tableau de pixels du nombre
					//on va prendre tous les ŽlŽments sauf le dernier qui est bien sur le numero lui meme 
					for(int i = 0; i < taille  ; i++){
						if(scan.hasNextFloat())
							tabPixel[i] = scan.nextFloat();
					}
					int leNombre;
					if(scan.hasNextInt()){
						leNombre = scan.nextInt();
						CodeNumber leCode = new CodeNumber (taille); 
						leCode.copy(tabPixel); //on copie le tableau de pixel dans le tableau de la classe CodeC
						leCode.setNombre(leNombre);
						lesImages.add(leCode);
					}
				}
			}	
			return lesImages;
		}else
			throw new ConversionException (chemin + " n'est pas un fichier !");
	}
	
	
	/**
	 * initialise une matrice avec des valeur alŽatoire entre 1 et -1
	 * @param tab : tableau de "float" a 2 dimensions
	 */
	public static void init_aleatoire(float [] [] tab){
		for (int i = 0; i < tab.length; i++)
			for (int j = 0; j < tab[i].length;j++)
				tab[i][j] = (float) (Math.random()*2)-1;
	}
	
	/**
	 * retourne un string representant le contenue du tableau en parametre 
	 * @param tab : tableau de float a une dimension 
	 * @return String
	 */
	public static String toStringTab (float [] tab){
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < tab.length; i++)
			str.append("case "+i+" => "+tab[i]+"\n");
		return new String(str);
	}
	
	/**
	 * retourne un String representant le contenue du tableau en parametre 
	 * @param tab : tableau de float a 2 dimension
	 * @return String
	 */
	public static String toStringTab(float[][] tab){
		StringBuffer str = new StringBuffer();
		for(int i = 0; i < tab.length; i++){
			if(i != 0)
				str.append("\n");
			for (int j = 0; j < tab[i].length; j++){
				if(j != tab[i].length-1)
					str.append(tab[i][j]+";");
				else
					str.append(tab[i][j]);
			}	
		}
		return new String(str);
	}
	
	/**
	 * calcule le pourcentage de reussite a partir d'un tableau a deux cases
	 * @param resultat : tableau a 2 cases, la case 0 contient le nombre d'echecs, la case 1 contient le nombre de reussites
	 * @return float : pourcentage de reussite
	 */
	public static double stat_reussite (int [] resultat){
		int echec = resultat[0];
		int reussite = resultat[1];
		return (reussite*100)/(echec+reussite);
	}
	

	public static boolean verif_args(String[]args){
			if( (args[0].charAt(0) != '-'))
				return false;
		return true;
	}
	
	/**
	 * affiche une matrice
	 * @param tab : matrice 
	 */
	public static void afficher (float [] [] tab){
		for (int i = 0 ; i< tab.length; i++){
			for(int j = 0 ; j < tab[i].length ; j++){
				if(tab[i][j] == 0.0){
					System.out.println(i);
					System.out.print(tab[i][j]+";");
				}
			}
		}
	}
	
	/**
	 * affiche un tableau 
	 * @param tab
	 */
	public static void afficher (float [] tab){
		for (int i = 0 ; i < tab.length; i++)
			System.out.println("\ncase "+i+" : "+ tab[i]);
	}
	
	/**
	 * verifie si le lien correspond a un repertoir ou non 
	 * @param repertoir : le chemin du repertoir 
	 * @return vrai si c'est un repertoir ou l'inverse 
	 */
	public static boolean est_repertoire(String repertoire){
		File directory = new File(repertoire);
         if(!directory.isDirectory()){	 
			return false;
			
		}else{
			
          return true ;
		}
	}
	
	/**
	 * verifie si le carcatere en position p est un chiffre ou non 
	 * @return
	 */
	public static boolean verif_string_chiffre (String s, int position){
		if (position > s.length())
			return false;
		else{
			switch (s.charAt(position)){
			case '0' : return true;
			case '1' : return true;
			case '2' : return true;
			case '3' : return true; 
			case '4' : return true; 
			case '5' : return true; 
			case '6' : return true; 
			case '7' : return true;
			case '8' : return true;
			case '9' : return true;
			default : return false;
			}
		}
	}
	
	/**
	 * divise le string param en deux partie avec un delimiter '-'
	 * la premiere partie est le nombre de neurone en couche intermediaire
	 * la deuxieme partie est le teta qu'on peut modifier
	 * @param param : le String a diviser
	 * @return tableau de String qui est la division du String s passŽ en parametre, on retourn null si ce n'est pas des chiffres 
	 */
	public static String[] extraire (String param){
		String [] tab = new String [2];
		Scanner scan = new Scanner (param).useDelimiter("-");
		tab[0] = scan.next();
		tab[1] = scan.next();
		for(int i = 0; i < tab[0].length(); i++){
			if(! verif_string_chiffre(tab[0], i))
				return null;
		}
		//on virifie si la deuxieme partie est bien un reel entre 0 et 1 et 
		if(tab[1].length()>4 || tab[1].length() < 3){
			return null;
		}
		if(tab[1].charAt(1) != '.')
			return null;
		if(tab[1].charAt(0) == '0' && verif_string_chiffre(tab[1], 2)){
			if(tab[1].length() == 4){
				if(verif_string_chiffre(tab[1], 3))
					return tab;
				else
					return null;
			}else
				return tab;
		}
		return null;
	}
	
	/**
	 * copie le fichier source dans le fichier destination
	 * @param source : fichier source
	 * @param dest : fichier destination
	 * @return vrai si cela rŽussit
	 */
	public static boolean copyFile(File source, File dest){
		try{
			// Declaration et ouverture des flux
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(source);
			
			try{
				java.io.FileOutputStream destinationFile = null;
				
				try{
					destinationFile = new FileOutputStream(dest);
					
					// Lecture par segment de 0.5Mo 
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;
					
					while ((nbLecture = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, nbLecture);
					}
				} finally {
					destinationFile.close();
				}
			} finally {
				sourceFile.close();
			}
		} catch (IOException e){
			e.printStackTrace();
			return false; // Erreur
		}
		
		return true; // RŽsultat OK  
	}
	
	/**
	 * Convertie un InputStream ˆ un File. Donc recopie tout le contenu dans un fichier destination
	 * @param inputStream : le contenue ˆ copier
	 * @param dest : le fichier de destination 
	 * @return vrai si la copie rŽussit
	 */
	public static boolean inputStreamToFile (InputStream inputStream, File dest){
		try {
			OutputStream out = new FileOutputStream(dest);
			int read = 0;
			byte[] bytes = new byte[1024];
		 
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		 
			inputStream.close();
			out.flush();
			out.close();
			
		}catch (IOException e) {
			 return false;
		}
		return true;
	}
	
	/**
	 * verifie si le chemin correspond a un fichier image de type jpeg ou png
	 * @param chemin : chemin de l'image
	 * @return vrai si le chemin correpond a une image, l'inverse sinon
	 */
	public static boolean est_image (String chemin){
		if (! est_repertoire (chemin))
			return (chemin.toLowerCase().endsWith(".jpg") || chemin.toLowerCase().endsWith(".jpeg") || chemin.toLowerCase().endsWith(".png"));
		else 
			return false;
	}
}
