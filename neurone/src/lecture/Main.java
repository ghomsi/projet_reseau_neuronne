package lecture;

import java.io.FileNotFoundException;
import java.io.IOException;

import analyseChiffres.ConversionException;


public class Main {
	
		
	public static void main(String[] args) {
	
		try{
			if(args.length == 0)
				System.out.println("\nCommande introuvable. -h pour la liste des commandes\n");
			else{
				Commandes cmd = new Commandes(args);
				cmd.analyse();
			}
		}catch (CommandeException c){
			System.err.println("\n"+c.getMessage()+"\n");
		}catch (ConversionException p){
			System.err.println("\n"+p.getMessage()+"\n");
		} catch (FileNotFoundException e) {
			System.err.println("\n"+e.getMessage()+"\n");
		} catch (IOException e) {
			System.err.println("\n"+e.getMessage()+"\n");
		}
		
	}	

}