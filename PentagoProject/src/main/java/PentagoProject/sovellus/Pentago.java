
package PentagoProject.sovellus;

import PentagoProject.kayttoliittyma.GUI;
import java.awt.EventQueue;
import java.util.Scanner;

public class Pentago {

    public static void main(String[] args) {
        /*
         Scanner lukija = new Scanner(System.in);
         Kayttoliittyma kayttis = new Kayttoliittyma(lukija);
         kayttis.run();
         //kayttis.testaile();
         */

         EventQueue.invokeLater(() -> {
            var ex = new GUI();
            ex.setVisible(true);
        });
    }
    
}
