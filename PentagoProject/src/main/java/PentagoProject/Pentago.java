
package PentagoProject;

import java.util.Scanner;

public class Pentago {

    public static void main(String[] args) {
         Scanner lukija = new Scanner(System.in);
         Kayttoliittyma kayttis = new Kayttoliittyma(lukija);
         kayttis.run();
         //kayttis.testaile();
    }
    
}
