import java.util.*;

public class PairList
{
   private ArrayList<Pair> pairs;
   private int size;

   public PairList()
   {
      pairs = new ArrayList<Pair>();
      size = 0;
   }

   public void addLetter(String l) {
      int i = 0;
      while(i < size && !pairs.get(i).key().equals(l))
         i++;
      if(i == size){
         pairs.add(new Pair(l,1));
         size++;
      } else {
         pairs.get(i).incValue();
         while(i > 0 && pairs.get(i).value()>pairs.get(i-1).value()){
            Pair temp = pairs.get(i-1);
            pairs.set(i-1,pairs.get(i));
            pairs.set(i,temp);
         }
      }
   }
   
   public int size(){
      return size;
   }
   
   public String get(int index) {
      return pairs.get(index).key();
   }
}