package lecture;

/**
 * Exceptions des commandes 
 * @author Souf
 *
 */
public class CommandeException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public CommandeException (){
		super("\nCommande introuvable. -h pour la liste des commandes\n");
	}
	
	public CommandeException(String message){
		super(message);
	}
}
