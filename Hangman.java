import java.util.*;
import java.io.*;

public class Hangman {

	//data
	public int length = 0;
	public Scanner s;

	public static void Hangman(String[] args) [
		loadDictionary();
		s = new Scanner(System.in
		startGame();
	}
	
	public void startGame() {
		boolean cont = true;
		do {
			Stystem.out.println("Think of a word!");
			retrieveLength();
			
			System.out.println("Would you like to play again? (yes/no)");
			cont = s.nextLine().toLowerCase().equals("yes");
		}while(cont);
	}
	
	public retrieveLength() {
		while (length < 1) {
			System.out.println("\nHow many letters long is your word");
			length = Integer.parseInt(s.nextLine());
			if (length < 1)
				System.out.println("That number is too small");
		}
}
