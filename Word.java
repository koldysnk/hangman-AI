import java.util.*;

public class Word{

   private String[] word;
   private int size;

   //creates a word from string
   public Word(String s) {
      size = s.length();
      word = new String[size];
      for (int i = 0; i < size; i++ ) 
         word[i] = s.substring(i,i+1);
   }
   
   //creates a word that consists of blanks
   public Word(int len) {
      word = new String[len];
      size = len;
      for (int i = 0; i < size; i++ ) 
         word[i] = "_";
   }
   
   //changes the letter at index to str
   public boolean indexTo(int index, String str) {
      if (index > size || index < 0 || str == null || str.length() > 1) 
         return false;
      word[index] = str;
      return true;
   }
   
   //returns the value at index
   public String getIndex(int index) {
      return word[index];
   }
   
   //returns the length of the word
   public int getSize() {
      return size;
   }
   
   //checks if there are any blanks in the word
   public boolean complete() {
      for (int i = 0; i < size; i++) {
         if(word[i].equals("_"))
            return false;
      }
      return true;
   }
   
   //returns a list of the blank indexes
   public ArrayList<String> getValidIndex() {
      ArrayList<String> str = new ArrayList<String>();
      for (int i = 0; i < size; i++) {
         if(word[i].equals("_"))
            str.add(""+i);
      }
      return str;
      
   }
   
   //checks if Word w is a partial match of this
   public boolean partMatch(Word w) {
      if (size != w.getSize())
         return false;
      for (int i = 0; i < size; i++) {
         boolean b1 = !word[i].equals(w.getIndex(i));
         boolean b2 = !word[i].equals("_");
         boolean b3 = this.iterations(w.getIndex(i)) != w.iterations(w.getIndex(i));
         if (b1 && b2 && b3)
            return false;
      }
      return true;
   }
   
   //checcks if the words match exactly
   public boolean exactMatch(Word word) {
      return this.toString().equals(word.toString());
   }
   
   //checks to see if this word contains the string
   public boolean contains(String str) {
      for (int i = 0; i < size; i++) {
         if(word[i].equals(str))
            return true;
      }
      return false;
   }
   
   //converts the word to a string
   public String toString() {
      String str= "Word:  ";
      for (int i = 0; i < size; i++) {
         if (i>0 && (word[i].equals("_") || word[i-1].equals("_")))
            str += " ";
         str += word[i];
      }
      str += "\nIndex:";
      for (int i = 0; i < size; i++) {
         if (i>0 && word[i].equals("_") && word[i-1].equals("_"))
            str += "-";
         else
            str += " ";
         if(word[i].equals("_"))
            str += i;            
         else
            str += " ";
      }
      return str;
   }
   
   //checks to see how many times a letter occurs
   private int iterations(String let) {
      int count=0;
      for (int i = 0; i < size; i++) {
         if(word[i].equals(let))
            count++;
      }
      return count;
   }
}