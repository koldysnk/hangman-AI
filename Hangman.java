import java.util.*;
import java.io.*;

public class Hangman {

	//data
   public static ArrayList<Word> dictionary;
   public static final int NUMTESTS = 10000;
   public static ArrayList<String> losses;

   public static void main(String[] args) { 
      loadDictionary();   
      losses = new ArrayList<String>();
      startGame();
      /*************Testing**********************
      for (int i = 0; i < 100; i++) 
         testGame();
      testEach();
      *******************************************/
   }
	
   //The user thinks of a word and the computer tries to guess the word in less than 10 incorrect guesses.
   public static void startGame() {   
      Scanner s = new Scanner(System.in);
      boolean cont = true;
      do {
         //Game Intro
         System.out.println("Think of a word!");
         int length = 0;
         while (length < 1) {
            System.out.println("How many letters long is your word?");
            try{
               length = Integer.parseInt(s.nextLine());
               if (length < 1)
                  System.out.println("That number is too small.");
            }catch(Exception e) {
               System.out.println("You must type a number.");
            }
         }
         Word word = new Word(length);
         //Make Guesses
         int guesses = 10;
         boolean match = false;
         String used = "";
         displayStatus(guesses, word, match);
         ArrayList<Word> potentials = removeWrongLength(dictionary, length);
         while (guesses > 0 && !match) {
            String guess = guess(potentials,used);
            used += guess;
            //Check guess
            System.out.println("The computer's guess is: " + guess+"\nIs the guess correct? (yes/no)");
            String ans = s.nextLine().toLowerCase();
            while (!ans.equals("yes") && !ans.equals("no")){
               System.out.println("Please enter a valid response.\nThe computer's guess is: " + guess+"\nIs the guess correct? (yes/no)");      
               ans = s.nextLine().toLowerCase();
            }
            if(ans.equals("yes")){
               boolean place = true;
               while(place){
                  System.out.println("What index does the letter corespond to?");
                  ans = s.nextLine().toLowerCase();
                  while (!word.getValidIndex().contains(ans)){
                     System.out.println("Please enter a valid response.\nWhat index does the letter corespond to?");      
                     ans = s.nextLine().toLowerCase();
                  }
                  word.indexTo(Integer.parseInt(ans),guess);
                  System.out.println("Does this letter appear again? (yes/no)");
                  ans = s.nextLine().toLowerCase();
                  while (!ans.equals("yes") && !ans.equals("no")){
                     System.out.println("Please enter a valid response.\nDoes this letter appear again? (yes/no)");      
                     ans = s.nextLine().toLowerCase();
                  }
                  if(ans.equals("yes"))
                     place = true;
                  else
                     place = false;
               }
               potentials = keepLetter(potentials, guess, word);
            } else {
               guesses--;
               potentials = removeLetter(potentials, guess, word);               
            }
            
            if(word.complete())
               match = true;
            
            displayStatus(guesses, word, match);
         }
         
      	//Game Outro or Restart
         System.out.println("Would you like to play again? (yes/no)");
         cont = s.nextLine().toLowerCase().equals("yes");
      }while(cont);
      System.out.println("Thanks for Playing!");
   }
   
   //This method runs tests and provids win statistics on random words.
   public static void testGame() {
      int numWins = 0;
      int numGuesses = 0;
      for (int t = 0; t < NUMTESTS; t++) {
         //Choose word
         Word correctWord = dictionary.get((int)(Math.random()*dictionary.size()));
         int length = correctWord.getSize();
         Word word = new Word(length);
         //Make Guesses
         int guesses = 10;
         boolean match = false;
         String used = "";
         //displayStatus(guesses, word, match);
         ArrayList<Word> potentials = removeWrongLength(dictionary, length);
         while (guesses > 0 && !match) {
            String guess = guess(potentials,used);
            used += guess;
            //Check guess
            if(correctWord.contains(guess)){
               for(int i = 0; i < length; i++){
                  if(correctWord.getIndex(i).equals(guess))
                     word.indexTo(i,guess);
               }
               potentials = keepLetter(potentials, guess, word);
            } else {
               guesses--;
               potentials = removeLetter(potentials, guess, word);               
            }
            if(word.complete())
               match = true;
         }
         if (match){
            numWins++;
            numGuesses+= 10-guesses;
         } else{
            //losses.add(correctWord);
         }
      }
      //Prints test results
      System.out.println("Total tests: "+NUMTESTS);
      System.out.println("Total wins: "+numWins);
      System.out.println("Total losses: "+(NUMTESTS-numWins));   
      System.out.println("Win percentage: "+(((double)numWins/(double)NUMTESTS)*100)+"%");
      System.out.println("Average number of guesses before win: "+(numGuesses/numWins)+"\n");
      
      
   }
      
   //This method runs tests and provids win statistics on entire dictionary.
   public static void testEach() {
      int numWins = 0;
      int numGuesses = 0;
      for (int t = 0; t < dictionary.size(); t++) {
         //Choose word
         Word correctWord = dictionary.get(t);
         int length = correctWord.getSize();
         Word word = new Word(length);
         //Make Guesses
         int guesses = 10;
         boolean match = false;
         String used = "";
         //displayStatus(guesses, word, match);
         ArrayList<Word> potentials = removeWrongLength(dictionary, length);
         while (guesses > 0 && !match) {
            String guess = guess(potentials,used);
            used += guess;
            //Check guess
            if(correctWord.contains(guess)){
               for(int i = 0; i < length; i++){
                  if(correctWord.getIndex(i).equals(guess))
                     word.indexTo(i,guess);
               }
               potentials = keepLetter(potentials, guess, word);
            } else {
               guesses--;
               potentials = removeLetter(potentials, guess, word);               
            }
            if(word.complete())
               match = true;
         }
         if (match){
            numWins++;
            numGuesses+= 10-guesses;
         } else{
            String lostWord = "";
            for (int i = 0; i<length; i++)
               lostWord += correctWord.getIndex(i);
            losses.add(lostWord);
         }
      }
      //Prints test results
      System.out.println("Total tests: "+dictionary.size());
      System.out.println("Total wins: "+numWins);
      System.out.println("Total losses: "+(dictionary.size()-numWins));   
      System.out.println("Win percentage: "+(((double)numWins/(double)dictionary.size())*100)+"%");
      System.out.println("Average number of guesses before win: "+(numGuesses/numWins));
      System.out.println("Words that are not guessed: "+Arrays.toString(losses.toArray())+"\n");
      
   }

   
   //This method reads from the dictionary and populates the list of potential words. 
   public static void loadDictionary() {
      dictionary = new ArrayList<Word>();
      try {
         File file = new File("dictionary.txt"); 
         BufferedReader br = new BufferedReader(new FileReader(file)); 
         String str;
         while ((str = br.readLine()) != null)
            dictionary.add(new Word(str.toLowerCase()));
      } catch(Exception e) {
         System.out.println("ERROR: NO DICTIONARY WAS READ");
      }
   }
    
   //This method removes all of the words that do not match the desired length.
   public static ArrayList<Word> removeWrongLength(ArrayList<Word> list,int len) {
      ArrayList<Word> temp = list;
      for (int i = temp.size()-1; i >= 0; i--) {
         if (temp.get(i).getSize() != len){
            temp.remove(i);
         }
      }
      return temp;
   }
   
   //This method removes the words containing the provided incorecctly guessed letter.
   public static ArrayList<Word> removeLetter(ArrayList<Word> list,String let, Word word) {
      ArrayList<Word> temp = new ArrayList<Word>();
      for (int i = list.size()-1; i >= 0; i--) {
         if (!list.get(i).contains(let)){
            temp.add(0,list.get(i));
         }
      }
      return temp;
   }
   
   //This method removes all words that do not math the word updated with the correctly guessed letter.
   public static ArrayList<Word> keepLetter(ArrayList<Word> list,String let, Word word) {
      ArrayList<Word> temp = new ArrayList<Word>();
      for (int i = list.size()-1; i >= 0; i--) {
         if (word.partMatch(list.get(i))){
            temp.add(0,list.get(i));
         }
      }
      return temp;
   }
   
   //This method prints the status of the game after the computer's last guess.
   public static void displayStatus(int g, Word w, boolean win) {
      //Add function for displaying hangman
      System.out.println(w);
      System.out.println("Number of guesses left: "+g);
      if (g == 0)
         System.out.println("The Computer Loses!");
      else if (win)
         System.out.println("The Computer Wins!");
      else
         System.out.println("The computer will make a guess.");
   }
   
   //This method decides which letter the computer should guess based on the previous guesses.
   public static String guess(ArrayList<Word> list, String used) {
      String guess = "";
      PairList pList = new PairList();
      for (int i = 0; i < list.size(); i++) {
         Word temp = list.get(i);
         String localUsed = used;
         for (int j = 0; j < temp.getSize();j++) {
            if (!localUsed.contains(temp.getIndex(j))){
               pList.addLetter(temp.getIndex(j));
               localUsed += temp.getIndex(j);
            }
         }
      }
      if (pList.size() == 0)
         return Character.toString((char) ((Math.random() * 26) + 97));
      else
         guess = pList.get(0);
      return guess;
   }
}
