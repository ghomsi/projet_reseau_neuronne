package reseauNeurone;


/**
 * contient des fonctions qui permette de faire les traitements au niveau du reseau de neurone
 * comme le calcul des produits de poid et leurs entree, ou encore le fonction d'activiation et la retropropagation
 * @author Souf
 *
 */
public class Traitement {
	
	/**
	 * la valeur de teta definit par defaut 
	 */
	public static double teta = 0.75;  
	static double seuil_activation = 0.9;
	static double seuil_desactivation = 0.1;
	
	
	/**
	 * calcule la fonction d'activation d'une couche de neurone
	 * @param poid : tableau des poids reliant la couche de depart a la couche de d'arrive
	 * @param resultat : tableau des resultats d'application de la fonction d'activation [ 1/(1*exp(-x)) ]
	 * @param entree : tableau des entrees
	 */
	public static void calculer_activation (float[] entree ,float [] [] poid , float [] resultat){
		//on calcule la somme des produit des entree et leurs poids
		float [] somme = calcule_somme(poid,entree);
		for (int i = 0 ; i < poid.length ; i++){  //pour chaque neurone i de la couche 
			resultat[i] = (float) (1/(1+Math.exp(-somme[i]))); // on applique la fonction segmoide a la somme obtenue 
			//System.out.println("---------------- \nActivation n¡"+i+" = "+resultat[i]);
		}
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * calcule la somme du produit de chaque entree et son arete de poid pour tous les neurones d'une couche 
	 * @param poid : tableau a 2 dimensions , 1ere dimension contient tous les neurones de la couche de sortie (ou intermediaire), 2nd dimension contient toutes les aretes connecte au neurone i
	 * @param entree : tableau a une dimension contenant toutes les valeur d'entree
	 * @return s : un tableau contenant le resultat pour chaque neurone 
	 */
	public static float[] calcule_somme (float [][] poid , float [] entree){
		//les neurones i de la couche de gauche
		//les neurones j de la couche de droite
		
		float [] s ; //tableau de somme de sortie
		
		/*on repere le fait qu'on est dans le sens normal si la taille des valeurs d'entree est egale a la taille 
		 * de la seconde dimension, car dans notre matrice de poid, chaque ligne i represente tout les poid j connectes a ce neurone
		 */
		//si on part dans le sens habituel
		if(entree.length == poid[0].length){
			s = new float [poid.length];
			for(int i = 0; i < entree.length ; i++) // neurone d'entre i
				for (int j = 0; j < s.length; j++)  //neurone de sortie j
					s[j] += poid[j][i]*entree[i];
		
		// si on part dans le sens inverse du sens habituel pour le retropropagation (de droite ˆ gauche)
		}else{
			s = new float [poid [0].length];
			for(int j = 0; j < entree.length ; j++) // neurone d'entre j
				for (int i = 0; i < s.length; i++)  //neurone de sortie i
					s[i] += poid[j][i]*entree[j];
		}
		return s;
	}
	
	/**
	 * calcule le delta pour la couche de sortie avec l'equation fournit en cour (retropropagation)
	 * @param delta : tableau 1 dimension, ou seront stocker les resultats
	 * @param activ_sortie : les resultat de l'application de fonction d'activation a la couche de sortie 
	 * @param leNombre : le nombre traite 
	 */
	public static void calculer_delta_sortie (float[] delta, float[] activ_sortie, int leNombre){
		//pour tout neurone k de sortie 
		for (int k = 0 ; k < delta.length ; k++){
			/*si on traite le neurone k correspondant au chiffre traite
			 * la valeur qu'on doit obtenir est 1 
			 */
			if(k == leNombre)
				delta[k] = activ_sortie[k]*(1-activ_sortie[k])*(1-activ_sortie[k]);
			//la valeur attendu c'est 0
			else
				delta[k] = activ_sortie[k]*(1-activ_sortie[k])*(0-activ_sortie[k]);
		}
	}
	
	/**
	 * calcule le delta de la couche intermediaire (retropropagation)
	 * @param delta : le tableau qui contiendra les resultats
	 * @param activ_inter : les resultats des application des fonctions d'activation dans la couche intermediaire (couche1)
	 * @param delta_w : le delta de tous les neurones de sortie 
	 */
	public static void calculer_delta_inter (float[] delta, float[] activ_inter, float[] delta_w){
		for (int j = 0; j < delta.length ; j++){
			delta[j] = activ_inter[j] * (1 - activ_inter[j]) * delta_w[j];
		}
	}
	
	
	/**
	 * met a jour les poids 
	 * @param poid : matrice de poid 
	 * @param ai : fonction d'activation du neurone i 
	 * @param delta : le taux d'erreur calcule au neurone j 
	 */
	public static void update_poid (float [][] poid, float [] ai, float [] delta){
		for (int i = 0 ; i < poid.length; i++){
			for (int j = 0; j < poid[i].length ; j++){
				poid[i][j] += (teta*delta[i]*ai[j]);
			}
		}
	}
	
	/**
	 * verifier la convergeance des neurones, donc verifier si seul le neurone en question est actif 
	 * @param activ_sortie : resultats des neurones de sortie 
	 * @param leNombre : le chiffre en question 
	 * @return boolean
	 */
	public static boolean converge (float[] activ_sortie, int leNombre){
		//si le neurone correspondant au nombre n'est pas actif
		if(activ_sortie[leNombre] < seuil_activation)
			return false; 
		else{
			//pour tous les autres neurones
			for (int i = 0 ; i < activ_sortie.length; i++){
				if(i != leNombre)
					//s'il y'en a un qui est actif 
					if(activ_sortie[i] > seuil_desactivation)
						return false;		
			}
		}
		//sinon donc seul le neurone en question est actif donc on retourne vrai 
		return true;
	}
	
	/**
	 * retourne le premier neurone actif, dont la valeur est superieur ou egale au seuil d'activation
	 * retourne -1 si aucun neurone n'est actif
	 * @param activ_sortie : resultat en sortie des neurones de la couche de sortie
	 * @return l'indice du neurone actif 
	 */
	public static int neurone_actif (float[] activ_sortie){
		for (int i = 0 ; i < activ_sortie.length; i++)
			if(activ_sortie[i] >= seuil_activation)
				return i;
		return -1;
	}
	
	/**
	 * modifie la valeur de teta qui est 0.75 par defaut 
	 * @param t
	 */
	public static void setTeta (double t){
		teta = t;
	}
	
}
