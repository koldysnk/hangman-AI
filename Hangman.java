import java.util.*;
import java.io.*;

public class Hangman {

	//data
   public static ArrayList<Word> dictionary;

   public static void main(String[] args) {
   /***********Word tests*************
      Word word = new Word(4);
      System.out.println(word.toString());
      word.indexTo(3,"c");
      System.out.println(word.toString());
      word.indexTo(2,"b");
      System.out.println(word.toString());
      word.indexTo(0,"a");
      System.out.println(word.toString());
   ***********************************/      
      startGame();
   }
	
   public static void startGame() {
      loadDictionary();   
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
   
   public static ArrayList<Word> removeWrongLength(ArrayList<Word> list,int len) {
      ArrayList<Word> temp = list;
      for (int i = temp.size()-1; i >= 0; i--) {
         if (temp.get(i).getSize() != len){
            temp.remove(i);
         }
      }
      return temp;
   }
   
   public static ArrayList<Word> removeLetter(ArrayList<Word> list,String let, Word word) {
      ArrayList<Word> temp = new ArrayList<Word>();
      for (int i = list.size()-1; i >= 0; i--) {
         if (!list.get(i).contains(let)){
            temp.add(0,list.get(i));
         }
      }
      return temp;
   }
   
   public static ArrayList<Word> keepLetter(ArrayList<Word> list,String let, Word word) {
      ArrayList<Word> temp = new ArrayList<Word>();
      for (int i = list.size()-1; i >= 0; i--) {
         if (word.partMatch(list.get(i))){
            temp.add(0,list.get(i));
         }
      }
      return temp;
   }
   
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
