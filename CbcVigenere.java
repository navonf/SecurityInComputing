//------------------------------------------------------------------
// University of Central Florida
// CIS3360 - Fall 2017
// Program Author: Jakob Sante & Navon Francis
//------------------------------------------------------------------

import java.io.*;
import java.util.Scanner;
import java.lang.String;

public class CbcVigenere {

  public static void main(String[] args) throws FileNotFoundException {
    Scanner sc = new Scanner(new File(args[0]));
    String key = args[1];
    String init = args[2];
    StringBuilder sb = new StringBuilder();

    // Build plaintext string
    while(sc.hasNext()) {
      sb.append(sc.next());
    }

    String str = sb.toString();
    StringBuilder plaintext = new StringBuilder();

    // Build alphabetic plaintext string
    for(char c : str.toCharArray()) {
      if (Character.isAlphabetic(c)) {
        plaintext.append(c);
      }
    }

    // Convert to an all lowercase string
    String pt = plaintext.toString().toLowerCase();

    System.out.println("CBC Vignere by Jakob Sante & Navon Francis");
    System.out.println("Plaintext input: " + args[0]);
    System.out.println("Vignere keyword: " + args[1]);
    System.out.println("Initialization vector: " + args[2] + "\n");

    encryptMe(pt, key, init);
  }

  public static void encryptMe(String plaintext, String key, String init) {
    // Plaintext before encryption
    System.out.println("Clean plaintext: \n\n" + plaintext);

    // Structures used for encryption
    char c[] = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    StringBuilder ciphertext = new StringBuilder();
    int blockLen = key.length();
    int val = 0;
    int block = 0;
    int pad = 0;

    System.out.println();

    for(int i=0; i<plaintext.length(); i++) {
      // Calculate value by adding plaintext, key, value.
      // Do this for the first block
      if(i < blockLen) {
        val = (plaintext.charAt(i) - 'a') +
              (init.charAt(block) - 'a')  +
              (key.charAt(block) - 'a')   ;
      }
      else {  // Continue to use previous block
        val = (plaintext.charAt(i) - 'a')    +
              (ciphertext.charAt(i-blockLen) - 'a') +
              (key.charAt(block) - 'a')      ;
      }

      // No mod case
      if(val < 26) {
        ciphertext.append(c[val]);
      }
      else { // mod, over 25
        ciphertext.append(c[(val%26)]);
      }

      block++;

      // Reset block if it's 6
      if(block == blockLen) {
        block = 0;
      }
    }

    // Padding algorithm, if needed
    for(int i=block; i<blockLen; i++) {
      val = (23) + (key.charAt(i) - 'a') +
            (ciphertext.charAt(plaintext.length() - blockLen) - 'a');

      if(val < 26) {
        ciphertext.append(c[val]);
      }
      else { // mod, over 25
        ciphertext.append(c[(val%26)]);
      }

      pad++;
    }

    System.out.println("Ciphertext: \n\n" + ciphertext + "\n\n");
    System.out.println("Number of characters in plaintext file: " + plaintext.length());
    System.out.println("Block size = " + blockLen);
    System.out.println("Number of pad characters added: " + pad);
  }

}
