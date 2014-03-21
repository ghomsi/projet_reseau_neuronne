package analyseChiffres;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;

import lecture.Utile;

public class Codeur {
	
	static final int BLANC = -1;
	static final int LARGEUR = 20;
	static final int HAUTEUR = 15;

	
	/**
	 * convertie les images des nombres stocker dans le repertoire en pixels et
	 * les enregistre dans le fichier cible
	 * 
	 * @param repertoire
	 *            : d'images sources
	 * @param cible
	 * @throws ConversionException
	 */
	public  static void chiffrer(String repertoire, String cible) throws ConversionException {
		ArrayList <CodeNumber> codepixel = new ArrayList <CodeNumber> ();
		if (Utile.est_repertoire(repertoire)) {
			// on convertie les images en code de pixels et on les stock
			// dans la liste codepixel
			 codepixel = liste_pixels(repertoire);
			// si ce n'est pas un repertoire
			if (!Utile.est_repertoire(cible))
				// on enregistre tout le contenue de code pixel dans le
				// fichier cible
				ecriture(cible, codepixel);
			else
				throw new ConversionException(cible + " n'est pas un fichier, veuillez saisir un chemin de fichier valide");
			// si le nom du repertoire n'est pas valide
		} else
			throw new ConversionException(
					repertoire
							+ " n'est pas un repertoire, veuillez saisir un chemin de repertoire valide");
	}

	
	/**
	 * pour recuperer les niveau de gris de chaque image
	 * 
	 * @param RGB
	 * @return
	 */
	protected static int getGris(int RGB) {
		// TODO Auto-generated method stub
		return (30 * getRed(RGB) + 59 * getGreen(RGB) + 11 * getBlue(RGB)) / 100;
	}

	/**
	 * recuperer le tot de blue dans un pixel
	 * 
	 * @param RGB
	 * @return
	 */
	protected static int getBlue(int RGB) {
		// TODO Auto-generated method stub
		return RGB & 0xFF;
	}

	/**
	 * recuperer le tot de vert dans un pixel
	 * 
	 * @param RGB
	 * @return
	 */
	protected static int getGreen(int RGB) {
		// TODO Auto-generated method stub
		return (RGB >> 8) & 0xFF;
	}

	/**
	 * recuperer le tot de rouge dans un pixel
	 * 
	 * @param RGB
	 * @return
	 */
	protected static int getRed(int RGB) {
		// TODO Auto-generated method stub
		return (RGB >> 16) & 0xFF;

	}

	/**
	 * transforme l'image du chemin en un tableau de pixels (image jpeg ou png seulement supporte)
	 * 
	 * @param chemin : le chemin de l'image
	 * @return tableau de pixel de l'image
	 * @throws ConversionException
	 */
	public static float[] chiffrer_image(String chemin) throws ConversionException {
		float[] pixel = new float[256];
		int cont = 0;
		if (Utile.est_image(chemin)) {
			File file = new File(chemin);
			try {
				BufferedImage image = ImageIO.read(file);
				// parcourir les pixels de l'image
				for (int y = 0; y < image.getHeight(); y++) {	
				     for (int x = 0; x < image.getWidth(); x++) {     
						int RGB = image.getRGB(x,y);
						// recuperer le RBG de chaque pixel
						// stocke dans le tableau de pixel , le pixel en
						// question sous niveau de gris et simplifier
						pixel[cont] = (float) convertisseur(getGris(RGB));
						cont++;
					}
				}
			} catch (IOException e) {
				e.getMessage();
			}
		} else {
			throw new ConversionException(chemin
					+ " : n'est pas un chemin valide de fichier image");
		}
		return pixel;
	}
	

	/**
	 * parcourt le dossier passe en parametre et le nom de l'image de la on
	 * capture chaque pixel puis on le met en niveau de gris puis on le
	 * simplifie de facon qu'il sois entre (-1 et 1) puis en stock chaque pixel
	 * d'une image dans le tableau de pixel apres avoir parcouru touts les
	 * pixels d'une image on stock se tableau dans la arrayliste
	 * 
	 * @param repertoir : le repertoir de l'image
	 * @param nom : le nom du fichier image
	 * @throws ConversionException 
	 */
	protected static void capture_pixel(String repertoir, String nom, ArrayList <CodeNumber> codepixel) throws ConversionException {
		float [] pixel;
		pixel = chiffrer_image(repertoir + "/" + nom);
		CodeNumber leCode = new CodeNumber(256);
		try {
			leCode.setNombre(Integer.parseInt(nom.charAt(0) + ""));
		} catch (NumberFormatException n) {
			System.err
					.println("Avertissement : le nom du fichier " + repertoir + "/"
							+ nom
							+ " ne respecte pas la forme requise\n"
							+ "le premire caractere du nom doit contenir le chiffre que contient le fichier image\n");
		}
		leCode.copy(pixel); // on copie le tableau de pixel dans le tableau
		codepixel.add(leCode);
	}

	/**
	 * Remplir le tableau de pixel avec le nom de l'image et le code des pixels
	 * en utilisant capture_pixel
	 * 
	 * @param repertoir
	 * @param tableau
	 * @throws ConversionException 
	 */
	public static ArrayList <CodeNumber> liste_pixels(String repertoir) throws ConversionException {
		ArrayList <CodeNumber> codepixel = new ArrayList <CodeNumber> ();
		if (Utile.est_repertoire(repertoir)) {
			ArrayList <String> nom_images = ajout_nom_image(repertoir);
			if(nom_images.isEmpty())
				throw new ConversionException("Aucune image trouve dans le repertoire \n" +
						"Veuillez respecter la forme des noms d'images, le premier caract�re du nom de l'image doit etre le chiffre reprŽsentŽ par l'image.\n" +
						"Seul les images jpeg et png sont pris en charge");
			Iterator<String> itere = nom_images.iterator();
			while (itere.hasNext()) {
				String image = itere.next();
				capture_pixel(repertoir, image, codepixel);
			}
			return codepixel;
		}else
			throw new ConversionException (repertoir+" n'est pas un repertoire");
	}

	/**
	 * stocker les noms des images dans une ArrayList
	 * @param repertoir
	 * @return liste de nom de fichiers
	 * @throws ConversionException 
	 */
	protected static ArrayList <String> ajout_nom_image(String repertoir) throws ConversionException {
		ArrayList <String> nom_images = new ArrayList <String> ();
		if(Utile.est_repertoire(repertoir)){
			String nom = null;
			File dossier = new File(repertoir);
			File subfiles[] = dossier.listFiles();
			for (int i = 0; i < subfiles.length; i++) {
				nom = subfiles[i].getName();
				// on ajoute que les fichiers images
				if (nom.toLowerCase().endsWith(".jpg")
						|| nom.toLowerCase().endsWith(".jpeg")
						|| nom.toLowerCase().endsWith(".png"))
					nom_images.add(nom);
			}
			return nom_images;
		}else 
			throw new ConversionException (repertoir+" n'est pas un repertoire valide");
	}
	/**
	 * parcourir la arrayliste qui contient touts les tableau de pixels des
	 * images du dossier et ecrire dans un fichier tout les codes des pixels
	 */
	protected static void ecriture(String nom, ArrayList<CodeNumber> liste) {
		
		File MyFile = new File(nom);
		MyFile.delete();
		Iterator<CodeNumber> iter = liste.iterator();// un iterator pour
														// parcourir la liste
		try {
			FileWriter fichier = new FileWriter(new File(nom), true);// ecrire dans le
															// fichier qui a le
															// nom donné en
															// parametre
			for (int i = 0; i < 256; i++)
				fichier.write("X" + i + "   ,");
			fichier.write("classe \n");
			fichier.write("\n");
			fichier.close();
			while (iter.hasNext()) { // tant qu'il y a des elements dans la
										// liste on la parcour
				fichier = new FileWriter(nom, true);// ecrire dans le fichier
													// qui a le nom donné en
													// parametre
				CodeNumber c = iter.next();
				fichier.write(c.toString());
				fichier.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Ecriture dans le fichier termine.");
	}

	/**
	 * converti le code couleur du pixel qui est entre 0 et 255 , a nombre qui
	 * est entre -1 et 1
	 * @param nombre
	 * @return
	 */
	protected static double convertisseur(float nombre) {
		return ((double) nombre - 127.5) / -127.5;
	}
	
	/**
	 * retourne le cadre de l'image
	 * @param image
	 * @return
	 */
	static int[] cadreImage(BufferedImage image) {

		int largeur = image.getWidth();// la largeur de l'image
		int hauteur = image.getHeight();// la hauteur de l'image
		int[] res = new int[4];
		
		for (res[0] = 0; image.getRGB(res[0], hauteur / 2) == BLANC
				&& res[0] < largeur; res[0]++) {
		}
		for (res[1] = largeur - 1; image.getRGB(res[1], hauteur / 2) == BLANC
				&& res[1] >= 0; res[1]--) {
		}
		for (res[2] = 0; image.getRGB(largeur / 2, res[2]) == BLANC
				&& res[2] < largeur; res[2]++) {
		}
		for (res[3] = hauteur - 1; image.getRGB(largeur / 2, res[3]) == BLANC
				&& res[3] >= 0; res[3]--) {
		}
		return res;
	}
	
	/**
	 * decoupe l'image au coordonnŽes passŽ en parametre
	 * @param image
	 * @param x
	 * @param y
	 * @return BufferedImage de la nouvelle image decoupŽ
	 */
	static BufferedImage imageChiffres(BufferedImage image, int x, int y) {
		int[] cadre = cadreImage(image);
		int taille = (cadre[1] - cadre[0]) / LARGEUR;

		return image.getSubimage(cadre[0] + 3 + (x * (cadre[1] - cadre[0]))
				/ LARGEUR,
				cadre[2] + 3 + (y * (cadre[3] - cadre[2])) / HAUTEUR,
				taille - 3, taille - 3);
	}

	/**
	 * redimensionne l'image avec le facteur passŽ en parametre
	 * @param bImage : image a redimensionne
	 * @param factor : le facteur de redimension
	 * @return
	 */
	protected static BufferedImage redimensionner(BufferedImage bImage,double factor) {
		int destWidth = (int) (bImage.getWidth() * factor);
		int destHeight = (int) (bImage.getHeight() * factor);
		// créer l'image de destination
		GraphicsConfiguration configuration = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		BufferedImage bImageNew = configuration.createCompatibleImage(
				destWidth, destHeight);
		Graphics2D graphics = bImageNew.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		// dessiner l'image de destination
		graphics.drawImage(bImage, 0, 0, destWidth, destHeight, 0, 0,
				bImage.getWidth(), bImage.getHeight(), null);
		graphics.dispose();
		return bImageNew;
	}

	/**
	 * prend l'image en parametre et la convertie en pixels ˆ l'aide de la
	 * fonction chiffrer_image, avant de l'ajouter a la liste passŽ en param�tres
	 * @param repertoir : le repertoir de travaille
	 * @param nom : le nom du fichier a convertir
	 * @param tableaupixel : le tableau ou il faut ajouter le resultat de la conversion de l'image
	 * @throws ConversionException
	 */
	protected static void capture_pixel_simple(String repertoir, String nom, ArrayList<float[]> tableaupixel) throws ConversionException {
		float[] pixel = chiffrer_image(repertoir + "/" + nom);
		tableaupixel.add(pixel);
	}

	/**
	 * remplit la liste de tableau de pixels avec les images du repertoire passŽ
	 * en parametre
	 * @param repertoir : le repertoire traitŽ
	 * @param tableau : tableau contenant les nom des fichiers du repertoire
	 * @return une liste de tableau de pixels
	 * @throws ConversionException
	 */
	protected static ArrayList<float[]> remplir_tableau_simple(String repertoir,ArrayList<String> tableau) throws ConversionException {
		ArrayList<float[]> tableaupixel = new ArrayList<float[]>();
		if (Utile.est_repertoire(repertoir)) {
			Iterator<String> itere = tableau.iterator();
			while (itere.hasNext()) {
				String image = itere.next();
				capture_pixel_simple(repertoir, image, tableaupixel);
			}
			return tableaupixel;
		} else
			throw new ConversionException("Impossible de charger les images "
					+ repertoir + " n'est un repertoire");
	}

	/**
	 * traite une image contenant plusieur chiffre, pour retourner une liste de
	 * tableau representant les pixels de chaque chiffre sur 256 pixels
	 * 
	 * @param image
	 *            : l'image contenant plusieur sous images de chiffre
	 * @return liste de tableau de pixels
	 * @throws IOException
	 * @throws ConversionException
	 */
	public static ArrayList<float []> chiffrage(String image) throws IOException, ConversionException {
		BufferedImage images = null;
		try {
			// lecture depuis le fichier
			File file = new File(image);
			images = ImageIO.read(file);
		} catch (IOException e) {
			throw new ConversionException(
					"Image invalide ...\nFermeture du programme...");
		}
		BufferedImage[][] imageChiffres = new BufferedImage[LARGEUR][HAUTEUR];
		File fichier = new File("Images");
		fichier.mkdir();
		for (int i = 0; i < LARGEUR; i++)
			for (int j = 0; j < HAUTEUR; j++) {
				imageChiffres[i][j] = imageChiffres(images, i, j);
				try {
					// Sauvegarde en JPEG
					File file = new File("Images/"+ i + "" + j + ".png");
					ImageIO.write(imageChiffres[i][j], "png", file);
				} catch (IOException e) {
					throw new ConversionException(
							"Impossible de sauvegarder le repertoire de travaille...\nOperation annule.");
				}
			}
		ArrayList <String> nom_images = ajout_nom_image("Images");
		Iterator<String> itere = nom_images.iterator();
		while (itere.hasNext()) {
			String nom = itere.next();
			BufferedImage imag = ImageIO.read(new File("Images/" + nom));
			BufferedImage imagnew = redimensionner(imag, 0.52);
			ImageIO.write(imagnew, "png", new File("Images/" + nom));
		}
		return remplir_tableau_simple("Images",nom_images);
	}
}
