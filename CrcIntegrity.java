//------------------------------------------------------------------
// University of Central Florida
// CIS3360 - Fall 2017
// Program Author: Navon Francis & Jakob Sante
//------------------------------------------------------------------

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.String;

public class CrcIntegrity {

  static final String poly = "1100110110101"; //1100110110101

  public static void main(String[] args) {
    char mode = args[0].charAt(0);
    String input = args[1];
    int inputLen = args[1].length();
    String hexInput = hexToBin(input);
    int whereIsCrc = args[1].length() - 3;

    System.out.println("--------------------------------------------------------------\n");
    System.out.println("CIS3360 Fall 2017 Integrity Checking Using CRC");
    System.out.println("Author: Navon Francis & Jakob Sante\n");

    System.out.println("The input string (hex): " + input);
    System.out.print("The input String (bin): ");
    spaceMeOutBro(hexInput);
    System.out.println("\n");

    System.out.print("The polynomial used (binary bit string): ");
    spaceMeOutBro(poly);
    System.out.println("\n");

    int cnt = 0;
    StringBuilder s = new StringBuilder();
    while(cnt < 3) {
      s.append(input.charAt( inputLen - cnt - 1 ));
      cnt++;
    }
    StringBuffer stringy = new StringBuffer(s.toString());
    String crc1 = stringy.reverse().toString();


    if(mode == 'c') {
      calculate(hexInput, poly, whereIsCrc);

    }
    else if(mode == 'v') {
      verification(hexInput, poly, whereIsCrc, crc1);
    }
    else {
      System.out.println("Not a valid mode, goodbye.\n");
      System.exit(0);
    }

  }

  public static void calculate(String hex, String poly, int whereIsCrc) {
    char[] hexArray = new char[hex.length() + 12];
    char[] polyArray = new char[hex.length() + 12];

    int firstOneIsAtThisLocation = 0;
    int flag = 0;

    System.out.println("Mode of operation: calculation\n");
    System.out.println("Number of zeroes that will be appended to the binary input: 12\n");
    System.out.println("The binary string difference after each XOR step of the CRC calculation:\n");

    // Build first hex array
    for(int i = 0; i < hexArray.length; i++) {
      if(i < hexArray.length - 12) {
        hexArray[i] = hex.charAt(i);
      }
      else {
        hexArray[i] = '0';
      }

      if(hexArray[i] == '1' && flag == 0) {
        firstOneIsAtThisLocation = i;
        flag = 1;
      }
    }

    // build poly
    for(int i = 0; i < polyArray.length; i++) {
      if(i < poly.length()) {
        polyArray[i] = poly.charAt(i);
      }
      else {
        polyArray[i] = '0';
      }
    }

    char[] holyHex = longDivision(hexArray, polyArray, whereIsCrc);
    System.out.println();

    String hi = "";

    if(whereIsCrc < 4) { // gets the last 3 hex digits
      StringBuilder sb = new StringBuilder();
      int count = 12;
      while(count < 24) {
        sb.append(holyHex[count]);
        count++;
      }
      hi = sb.toString();
    }
    else { // retrieves the hex when padding gets in the way
      int holyHexLen = holyHex.length - 13;
      StringBuilder sb = new StringBuilder();
      for(int i = holyHexLen; i > holyHexLen - 12; i--) {
        sb.append(holyHex[i]);
      }

      StringBuffer stringy = new StringBuffer(sb.toString());
      hi = stringy.reverse().toString();
    }


    System.out.print("The CRC computed from the input: ");
    spaceMeOutBro(hi);
    System.out.print("(bin) = ");
    System.out.println(binToHex(hi) + " (hex)\n");
  }

  public static void verification(String hex, String poly, int whereIsCrc, String crc1) {
    char[] hexArray = new char[hex.length() + 12];
    char[] polyArray = new char[hex.length() + 12];
    int firstOneIsAtThisLocation = 0;
    int flag = 0;

    System.out.println("Mode of operation: verification\n");
    System.out.print("The CRC observed at the end of the input: ");
    spaceMeOutBro(hexToBin(crc1));
    System.out.println("= " + crc1 + " (hex) \n");
    System.out.println("The binary string difference after each XOR step of the CRC calculation:\n");

    // Build first hex array
    for(int i = 0; i < hexArray.length; i++) {
      if(i < hexArray.length - 12) {
        hexArray[i] = hex.charAt(i);
      }
      else {
        hexArray[i] = '0';
      }

      if(hexArray[i] == '1' && flag == 0) {
        firstOneIsAtThisLocation = i;
        flag = 1;
      }
    }

    // build poly
    for(int i = 0; i < polyArray.length; i++) {
      if(i < poly.length()) {
        polyArray[i] = poly.charAt(i);
      }
      else {
        polyArray[i] = '0';
      }
    }

    char[] holyHex = longDivision(hexArray, polyArray, whereIsCrc);
    System.out.println();

    String hi = "";

    if(whereIsCrc < 4) { // gets the last 3 hex digits
      StringBuilder sb = new StringBuilder();
      int count = 12;
      while(count < 24) {
        sb.append(holyHex[count]);
        count++;
      }
      hi = sb.toString();
    }
    else { // retrieves the hex when padding gets in the way
      int holyHexLen = holyHex.length - 13;
      StringBuilder sb = new StringBuilder();
      for(int i = holyHexLen; i > holyHexLen - 12; i--) {
        sb.append(holyHex[i]);
      }

      StringBuffer stringy = new StringBuffer(sb.toString());
      hi = stringy.reverse().toString();
    }

    System.out.print("The CRC computed from the input: ");
    spaceMeOutBro(hi);
    System.out.print("(bin) = ");

    if(binToHex(hi).equals("000")) {
      System.out.println(crc1 + " (hex)\n");
      System.out.println("Did the CRC check pass: (Yes or No): Yes\n");
    }
    else {
      System.out.println(binToHex(hi) + " (hex)\n");
      System.out.println("Did the CRC check pass: (Yes or No): No\n");
    }
  }

  public static char[] longDivision(char[] hex, char[] polyO, int extraLen) {
    int flag = 1;
    int zeros = 0;
    int zerosInFront = 0;

    // first print
    spaceMeOutArray(hex);

    // loop for each time we perform division
    while((zeros + 13) < hex.length - (extraLen * 4)) {
      zeros = 0;

      // now, line up the poly array. first count how many zeros are in front
      for(int i = 0; i < hex.length; i++) {
        if(hex[i] == '0') {
          zeros++;
        }
        else {
          break;
        }
      }

      char[] poly = new char[polyO.length + 12];

      for(int i = 0; i < polyO.length; i++) {
        if(i < zeros) {
          poly[i] = '0';
        }
        else {
          poly[i] = polyO[i-zeros];
        }
      }

      // loops through our array indecies
      for(int i = 0; i < hex.length; i++) {
        // carry out XOR operations on each bit in our hex
        hex[i] = xor(hex[i], poly[i]);
      }

      spaceMeOutArray(hex);
    }
    return hex;
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
  }

  public static void spaceMeOutArray(char[] str) {
    int mod = 1;
    for(int i = 0; i < str.length; i++) {
      System.out.print(str[i]);
      if (mod % 4 == 0) {
        System.out.print(" ");
      }
      mod++;
    }
    System.out.println();
  }

  public static String binToHex(String bin) {
    StringBuilder big = new StringBuilder();
    StringBuilder sb = new StringBuilder();

    for(int i = 1; i < bin.length() + 1; i++) {
      sb.append(bin.charAt(i-1));

      if(i % 4 == 0)
      {
        if(sb.toString().equals("0000")) {
          big.append("0");
        }
        else if(sb.toString().equals("0001")) {
          big.append("1");
        }
        else if(sb.toString().equals("0010")) {
          big.append("2");
        }
        else if(sb.toString().equals("0011")) {
          big.append("3");
        }
        else if(sb.toString().equals("0100")) {
          big.append("4");
        }
        else if(sb.toString().equals("0101")) {
          big.append("5");
        }
        else if(sb.toString().equals("0110")) {
          big.append("6");
        }
        else if(sb.toString().equals("0111")) {
          big.append("7");
        }
        else if(sb.toString().equals("1000")) {
          big.append("8");
        }
        else if(sb.toString().equals("1001")) {
          big.append("9");
        }
        else if(sb.toString().equals("1010")) {
          big.append("A");
        }
        else if(sb.toString().equals("1011")) {
          big.append("B");
        }
        else if(sb.toString().equals("1100")) {
          big.append("C");
        }
        else if(sb.toString().equals("1101")) {
          big.append("D");
        }
        else if(sb.toString().equals("1110")) {
          big.append("E");
        }
        else if(sb.toString().equals("1111")) {
          big.append("F");
        }
        sb.setLength(0);
      }
    }

    return big.toString();
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

  public static char xor(char foo, char bar) {

    if(foo == '1' && bar == '1') {
      return '0';
    }
    else if(foo == '0' && bar == '1') {
      return '1';
    }
    else if(foo == '1' && bar == '0') {
      return '1';
    }
    else if(foo == '0' && bar == '0') {
      return '0';
    }

    return '?';
  }

}
