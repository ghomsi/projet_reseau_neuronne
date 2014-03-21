package analyseChiffres;

/**
 * 
 * permet de crŽer un rŽpertoire d'image ˆ l'aide d'une base de chiffres 
 * Les param�tres sont la hauteur et la largeur de l'image en pixels et l'image elle m�me
 * 
 */
	
import java.awt.Color;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class Decodeur {

	private static int Hauteur = 16 ;
	private static int Largeur = 16 ;
	private BufferedImage image;
	  
	
	/**
	 * @return l'image qui a ŽtŽ crŽer
	 */
	public BufferedImage getImage() {
		return image;
	}
	  
	/**
	 * Permet de creer une image de type RGB
	 */
	protected void creerImage() {
		image = new BufferedImage(Hauteur,Largeur,BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	* Permet de dessiner l'image pixel par pixel
	* @param Pixeles : tableau de float reprŽsentant tout les niveaux de gris des pixels de l'image
	*/
	protected void dessinerImage( float [] Pixeles ) {
		int somme = 0 ;
		for (int x = 0; x < 16; x++) {
			for(int y = 0 ; y < 16 ; y++){	
				int couleur = (int) (255-(Pixeles[somme]+1.00)*127.5);
				Color c = new Color (couleur,couleur,couleur);
				image.setRGB(y, x,c.getRGB());
				somme++ ;
			}
		}
	}
	
	/**
	 * Permet de sauvegarder l'image cree
	 * @param chemin : chemin de sauvegarde de l'image
	 * @param nom : nom de l'image
	 * @param format : format de l'image
	 */
	protected void sauverImage(String chemin, String nom, String format) {
		try {
		    File image = new File(chemin+nom+"."+format);
		    ImageIO.write(getImage(),format,image);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Permet de creer le repertoire d'images
	 * @param iter : liste des differents CodeNumber 
	 * @param nomRep : nom du RŽpertoire d'image
	 * @param nomImg : nom des images ( elles seront numerotees par le ssp )
	 * @throws ConversionException 
	 */
	public static void creerRepertoire(Iterator <CodeNumber> iter, String nomRep) throws ConversionException {
		new File(nomRep).mkdir(); 
		int i = 1;
		while(iter.hasNext()) {
			CodeNumber leNombre = iter.next();
			String nom = "" + leNombre.getNombre() + i ;
			Decodeur t = new Decodeur();
			t.creerImage();
			t.dessinerImage(leNombre.getTableau());
			t.sauverImage(nomRep+"/",nom,"png");
			i++;
		}
	}
}
	
