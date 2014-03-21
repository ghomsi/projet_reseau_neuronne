package analyseChiffres;

import java.text.DecimalFormat;

/**
 * Contient un attribut de type tableau de float reprŽsentant les 256 code de pixels de l'image d'un nombre 
 * et un attribut de type entier qui reprŽsente le nombre lui m�me 
 * @author Souf
 *
 */
public class CodeNumber {
	private float [] tableau; //le code de pixel du nombre en question
	private int nombre; // le nombre 
	
	/**
	 * definie la taille du tableau de couleur 
	 * @param a
	 */
	public CodeNumber(int taille){
		tableau = new float[taille];
	}
	
	/**
	 * ajouter un code de pixel au tableau de code
	 * @param indice
	 * @param codePixel
	 */
	public void ajouterPixel (int indice, float codePixel ){
		tableau [indice] = codePixel;
	}
	
	/**
	 * Copier un tableau dans le tableau d'instance CodeC
	 * @param tab2
	 * @return boolean qui confirme le succŽe de la copie
	 */
	public boolean copy(float [] tab2){
		if(tab2.length == this.tableau.length){
			int taille = tab2.length;
			for (int i = 0 ; i < taille ; i++){
					this.tableau[i] = tab2[i];
			}
			return true;
		}else
			return false;
	}
	
	/**
	 * ecrire dans un fichier les code des pixel recuperer des images données 	(non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	public String toString(){	
		DecimalFormat df = new DecimalFormat ( ) ; // utiliser DecimalFormat pour pouvoir arrandir les nombre 
		df.setMaximumFractionDigits ( 4 ) ; //arrondi le code des pixels à 4 chiffres apres la virgules 
		df.setMinimumFractionDigits ( 4 ) ; 
		df.setDecimalSeparatorAlwaysShown (false) ;
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < this.tableau.length ; i++){
			String stri = df.format(tableau[i]);
			stri = stri.replace(',', '.');
			str.append(""+stri+";");
		}
		str.append(nombre+"\n");
		return new String (str);
	}
	
	
	/**
	 * modifier le chiffre represente par les pixels 
	 * @param nombre : entier 
	 */
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return la valeur du nombre de pixels 
	 */
	public int getNombre() {
		return nombre;
	}
	/**
	 * revoi le pixel stocker ˆ l'indice passŽ en param�tre
	 * @param indice l'indice de la case dans la quelle est stockŽ le code du pixel 
	 * @return
	 */
	public float getPixel(int indice){
		return this.tableau[indice];
	}
	
	/**
	 * @return le tableau de pixels 
	 */
	public float[] getTableau (){
		return this.tableau;
	}
}
