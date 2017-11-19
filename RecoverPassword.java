/*
 * University of Central Florida
 * CIS 3360 - Fall 2017
 * Author: Navon Francis & Jakob Sante
 */

import java.io.*;
import java.util.*;
import java.lang.String;

public class RecoverPassword {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    File file = new File(args[0]);
    Scanner sc = new Scanner(file);
    int input = Integer.parseInt(args[1]);
    long hash = 0;
    int unsalted = 0;
    int isFound = 0;
    StringBuilder sb = new StringBuilder();
    String temp = "";
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> noSalt = new ArrayList<String>();

    // scan in passwords
    while(sc.hasNext()) {
        list.add(sc.next());
    }

    // password to unsalted
    for(int i = 0; i < list.size(); i++) {
      for(int j = 0; j < list.get(i).length(); j++) {
        // convert to ascii
        unsalted = (int) list.get(i).charAt(j);

        // append to our salted string builder
        sb.append(unsalted);
      }
      // make temp, set the string builder to
      // this String.
      temp = sb.toString();

      // now add the salted element.
      noSalt.add(i, temp);

      // clear our string builder for reuse.
      sb.setLength(0);
    }

    // comparing all combinations of the
    // salted password.
    int saltySpitoon = 0;    // combinations
    int howToughAreYa = 0;   // index of string password
    int howToughAmI = 0;     // salt value
    long bowlOfNails = 0;    // left
    long withoutAnyMilk = 0; // right

    for(int i = 0; i < noSalt.size(); i++) {
      for(int j = 0; j <= 999; j++) {
        saltySpitoon++;             // increase combinations
        sb.append(noSalt.get(i));   // create ascii num
        sb.insert(0, makeSalt(j));  // insert 0's if necessary
        bowlOfNails = Long.parseLong(sb.toString().substring(0, 7));
        withoutAnyMilk = Long.parseLong(sb.toString().substring(7, 15));
        sb.setLength(0);

        hash = ((243 * bowlOfNails/*left*/) + withoutAnyMilk/*right*/) % 85767489;

        if(hash == input) {
          howToughAreYa = i;
          howToughAmI = j;
          isFound = 1;
          break;
        }
      }
      if(isFound == 1)
        break;
    }


    // output
    System.out.println("\nCIS3360 Password Recovery by Navon Francis & Jakob Sante");
    System.out.println("\n\tDictionary file name\t   : " + file.getName());
    System.out.println("\tSalted password hash value : " + input);
    System.out.println("\n\tIndex  Word   Unsalted ASCII equivalent\n");
    for(int i = 1; i <= list.size(); i++) {
      if(i < 10)
        System.out.println("\t" + "  " + i + " :  " + list.get(i-1) + " " + noSalt.get(i-1));
      else if(i < 100)
        System.out.println("\t" + " " + i + " :  " + list.get(i-1) + " " + noSalt.get(i-1));
      else
        System.out.println("\t" + i + " :  " + list.get(i-1) + " " + noSalt.get(i-1));
    }

    if(isFound == 1) {
      System.out.println("\nPassword recovered:");
      System.out.println("\tPassword\t\t: " + list.get(howToughAreYa));
      System.out.println("\tASCII Value\t\t: " + noSalt.get(howToughAreYa));
      System.out.println("\tSalt Value\t\t: " + howToughAmI);
      System.out.println("\tCombinations tested\t: " + saltySpitoon + "\n");
    }
    else {
      System.out.println("\nPassword not found in dictionary");
      System.out.println("\nCombinations tested : " + saltySpitoon + "\n");
    }
  }

  public static String makeSalt(int i) {
    StringBuilder sb = new StringBuilder();
    String salty = "";

    for(int x = 0; x <= 999; x++) {
      if(x < 10) {
        sb.append(Integer.toString(x));
        sb.insert(0, "00");
      }
      else if(x < 100) {
        sb.append(Integer.toString(x));
        sb.insert(0, "0");
      }
      else
        sb.append(Integer.toString(x));

      salty = sb.toString();
      sb.setLength(0);

      if(Integer.valueOf(salty) == i)
        break;
    }

    return salty;
  }

}
