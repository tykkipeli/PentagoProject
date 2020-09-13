package PentagoProject;

import java.util.Scanner;

public class Kayttoliittyma {

    private Scanner lukija;
    private Lauta lauta;
    private boolean paattynyt = false;
    private boolean pelaajanVuoro = false;
    private AI tekoaly = new AI();

    public Kayttoliittyma(Scanner lukija) {
        this.lukija = lukija;
        lauta = new Lauta();
    }
    
    public void testaile() {
        String kentta = "|O| | | | | |\n" +
                        "|X|X|X| |X| |\n" +
                        "| | |O| | | |\n" +
                        "|O|O|X| |O|O|\n" +
                        "|X|O|X| |O|X|\n" +
                        "| | |X| | |O|";

        /*
        kentta =        "| | | | | | |\n" +
                        "| | | | | | |\n" +
                        "| | | | | | |\n" +
                        "| | | | | | |\n" +
                        "| | | |X| | |\n" +
                        "| | | |O| | |";
        */
       
        lauta = new Lauta(kentta);
        //System.out.println(lauta.bitboard[0]);
        //System.out.println(lauta.bitboard[1]);
        lauta.tulostaLauta();
        tekoaly.teeSiirto(lauta);
        lauta.tulostaLauta();
    }

    public void run() {
        System.out.println("Kumpi aloittaa? pelaaja/botti");
        String input = lukija.nextLine();
        if (input.equals("pelaaja")) {
            this.pelaajanVuoro = true;
        } else {
            this.pelaajanVuoro = false;
        }
        gameLoop();
    }

    private void gameLoop() {
        while (!paattynyt) {
            lauta.tulostaLauta();
            System.out.println("Täyttöaste: " + lauta.getTayttoaste());
            if (this.pelaajanVuoro) {
                pelaajanSiirto();
            } else {
                tekoaly.teeSiirto(lauta);
            }
            if (lauta.loytyyViidenSuora() || lauta.tasapeli()) {
                paattynyt = true;
            }
            this.pelaajanVuoro = !this.pelaajanVuoro;
        }
        lauta.tulostaLauta();
        if (lauta.tasapeli()) System.out.println("Tasapeli!");
        else System.out.println(lauta.getVoittaja() + " voitti!");
    }

    private void pelaajanSiirto() {
        System.out.println("Syotä siirron koordinaatit ja kaanto muodossa vaakarivi pystyryvi kaanto");
        String siirto = lukija.nextLine();
        while (!kelvollinen(siirto)) {
            System.out.println("Virheellinen siirto");
            System.out.println("Syotä siirron koordinaatit ja kaanto muodossa vaakarivi pystyrivi kaanto");
            siirto = lukija.nextLine();
        }
        teeSiirto(siirto);
    }

    private boolean kelvollinen(String siirto) {
        String[] data = siirto.split(" ");
        if (data.length != 3) {
            return false;
        }
        int x, y, kaanto;
        try {
            x = Integer.parseInt(data[0]);
            y = Integer.parseInt(data[1]);
            kaanto = Integer.parseInt(data[2]);

        } catch (NumberFormatException e) {
            return false;
        }
        if (kaanto < 1 || kaanto > 8) return false;
        if (x < 1 || y < 1 || x > 6 || y > 6) return false;
        if (!lauta.vapaa(x,y)) return false;
        return true;
    }

    private void teeSiirto(String siirto) {
        String[] data = siirto.split(" ");
        int x, y, kaanto;
        x = Integer.parseInt(data[0]);
        y = Integer.parseInt(data[1]);
        kaanto = Integer.parseInt(data[2]);
        lauta.teeSiirto(1,x,y,kaanto);
    }

}
