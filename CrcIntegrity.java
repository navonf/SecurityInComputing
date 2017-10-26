//------------------------------------------------------------------
// University of Central Florida
// CIS3360 - Fall 2017
// Program Author: Navon Francis & Jakob Sante
//------------------------------------------------------------------

import java.io.*;
import java.util.Scanner;
import java.lang.String;

public class CrcIntegrity {

  static final String poly = "1100110110101";

  public static void main(String[] args) {
    char mode = args[0].charAt(0);
    String input = args[1];

    System.out.println("--------------------------------------------------------------\n");
    System.out.println("CIS3360 Fall 2017 Integrity Checking Using CRC");
    System.out.println("Author: Navon Francis & Jakob Sante\n");
    System.out.print("The input string (hex): " + input);
    System.out.print("The input String (bin): ");
    spaceMeOutBro(hexToBin(input));
    System.out.print("The polynomial used (binary bit string): ");
    spaceMeOutBro(poly);
    System.out.println();

    if(mode == 'c') {
      System.out.println("Mode of operation: calculation");
    }
    else if(mode == 'v') {
      System.out.println("Mode of operation: verification");
    }
    else {
      System.out.println("Not a valid mode, goodbye.\n");
      System.exit(0);
    }
  }

  public static void spaceMeOutBro(String str) {
    int mod = 1;
    for(int i = 0; i < str.length(); i++) {
      System.out.print(str.charAt(i));
      if (mod % 4 == 0) {
        System.out.print(" ");
      }
      mod++;
    }
    System.out.println();
  }

  public static String hexToBin(String hex) {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < hex.length(); i++) {
      if(hex.charAt(i) == '0') {
        sb.append("0000");
      }
      else if(hex.charAt(i) == '1') {
        sb.append("0001");
      }
      else if(hex.charAt(i) == '2') {
        sb.append("0010");
      }
      else if(hex.charAt(i) == '3') {
        sb.append("0011");
      }
      else if(hex.charAt(i) == '4') {
        sb.append("0100");
      }
      else if(hex.charAt(i) == '5') {
        sb.append("0101");
      }
      else if(hex.charAt(i) == '6') {
        sb.append("0110");
      }
      else if(hex.charAt(i) == '7') {
        sb.append("0111");
      }
      else if(hex.charAt(i) == '8') {
        sb.append("1000");
      }
      else if(hex.charAt(i) == '9') {
        sb.append("1001");
      }
      else if(hex.charAt(i) == 'A') {
        sb.append("1010");
      }
      else if(hex.charAt(i) == 'B') {
        sb.append("1101");
      }
      else if(hex.charAt(i) == 'C') {
        sb.append("1100");
      }
      else if(hex.charAt(i) == 'D') {
        sb.append("1101");
      }
      else if(hex.charAt(i) == 'E') {
        sb.append("1110");
      }
      else if(hex.charAt(i) == 'F') {
        sb.append("1111");
      }
    }

    return sb.toString();
  }

}
