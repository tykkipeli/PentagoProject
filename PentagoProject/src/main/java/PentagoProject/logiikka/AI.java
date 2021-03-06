package PentagoProject.logiikka;

// Tämä luokka on korvattu Tekoaly luokalla ja tätä luokkaa ei testata eikä käytetä missään!
// Käytän luokkaa vain erilaisten juttujen testailuun

import PentagoProject.pelikomponentit.Siirto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class AI {

    private Random rand = new Random();
    private Lauta lauta;
    private final int INF = 9999999;
    private final int MAXDEPTH = 20;
    private final int TIMELIMIT = 45;
    private long startingTIme;
    //private HashMap<Lauta, Integer> transtbl = new HashMap<>(10000000);
    private HashMap<Lauta,Integer>[] tbls = new HashMap[MAXDEPTH];
    private HashMap<Lauta,Integer> heuristicTbl = new HashMap<>(10000000);
    private Siirto killermove[] = new Siirto[100];
    private int laskuri = 0;
    private Lauta alkuLauta;
    private Lauta superLauta = null;

    public AI() {
        for (int i = 0; i < MAXDEPTH; i++) {
            tbls[i] = new HashMap<Lauta,Integer>();
            killermove[i] = null;
        }
    }

    public void setLauta(Lauta lauta) {
        this.lauta = lauta;
    }

    public void teeSiirto(Lauta lauta) {
        this.lauta = lauta;
        for (int i = 0; i < MAXDEPTH; i++) {
            tbls[i].clear();
        }
        this.alkuLauta = lauta.kopio();
        //randomSiirtelija(lauta);
        fiksuSiirtelija(lauta);
        //debugSiirtelija(lauta);
    }

    private void randomSiirtelija(Lauta lauta) {
        int x, y;
        x = 0;
        y = 0;
        while (x == 0 || !lauta.vapaa(x, y)) {
            x = rand.nextInt(6) + 1;
            y = rand.nextInt(6) + 1;
        }
        int kaanto = rand.nextInt(6) + 1;
        lauta.teeSiirto(0, x, y, kaanto);
    }
    
    private void debugSiirtelija(Lauta lauta) {
        this.laskuri = 0;
        this.lauta = lauta;
        int iterations = 3;
        this.startingTIme = System.nanoTime();
        for (int i = 1; i <= iterations; i++) {
            //if (laskuri > 1000000) break;
            negaMax(true, -INF+30, INF-30, 0, i);
        }
        /*
        while (laskuri < 100000) {
            System.out.println("here");
            iterations++;
            negaMax(true, -INF, INF, 0, iterations);
        }
        */
        ArrayList<Siirto> siirrot = generoiSiirrot(0,iterations, true);
        this.jarjestaSiirrot(siirrot);
        Collections.reverse(siirrot);   
        long pelaajanLauta = lauta.getTilanne(0);
        long botinLauta = lauta.getTilanne(1);
        Siirto parasSiirto = null;
        int parasValue = -INF-1;
        for (Siirto siirto: siirrot) {
            System.out.println(siirto.getValue());
            lauta.teeSiirto(0, siirto);
            lauta.tulostaLauta();
            int value = tbls[iterations].getOrDefault(lauta.kopio(), -INF);
            //System.out.println("ylläolevan kentän arvo: " + tbls[iterations].getOrDefault(lauta.kopio(), -INF));
            if (parasValue < value) {
                parasValue = value;
                parasSiirto = siirto;
            }
            peruSiirto(pelaajanLauta, botinLauta);
        }
        System.out.println("Alkuperäisen position arvo: " + tbls[iterations].getOrDefault(lauta.kopio(), -INF));
        lauta.teeSiirto(0, parasSiirto);
        System.out.println("uuden position arvo: " + tbls[iterations].getOrDefault(lauta.kopio(), -INF));
        System.out.println("Laskurin arvo: " + laskuri);
    }
    
    private void fiksuSiirtelija(Lauta lauta) {
        this.laskuri = 0;
        this.lauta = lauta;
        int iterations = MAXDEPTH-1;
        this.startingTIme = System.nanoTime();
        for (int i = 1; i <= MAXDEPTH-1; i++) {
            //if (laskuri > 1000000) break;
            System.out.println("Iteration: " + i);
            for (int j = 1; j <= i; j++) killermove[i] = null;
            negaMax(true, -INF+30, INF-30, 0, i);
            if (System.nanoTime()-this.startingTIme >= (long)1e9*this.TIMELIMIT) {
                iterations = i-1;
                break;
            }
        }
        System.out.println("Iterations: " + iterations);
        /*
        while (laskuri < 500000) {
            System.out.println("here");
            if (iterations >= MAXDEPTH-2) break;
            iterations++;
            negaMax(true, -INF+30, INF-30, 0, iterations);
        }
        */
        ArrayList<Siirto> siirrot = generoiSiirrot(0,iterations, true);
        this.jarjestaSiirrot(siirrot);
        Collections.reverse(siirrot);
        Siirto parasSiirto = siirrot.get(0);
        //System.out.println("Alkuperäisen position arvo: " + tbls[iterations].getOrDefault(lauta.kopio(), -INF));
        lauta.teeSiirto(0, parasSiirto);
        //System.out.println("uuden position arvo: " + tbls[iterations].getOrDefault(lauta.kopio(), -INF));
        System.out.println("Laskurin arvo: " + laskuri);
    }

    
    private int negaMax(boolean myTurn, int alpha, int beta, int depth, int goalDepth) {
        laskuri++;
        if (laskuri%1000000 == 0) System.out.println(laskuri);
        if (tbls[goalDepth].containsKey(lauta.kopio())) {
            return tbls[goalDepth].get(lauta.kopio());
        }
        int player = myTurn ? 0 : 1;
        if (lauta.loytyyViidenSuora(1) && lauta.loytyyViidenSuora(0)) {
            tbls[goalDepth].put(lauta.kopio(), 0);
            return 0;
        }
        if (lauta.loytyyViidenSuora() && lauta.getVoittajaInt() != player) {
            int value = (player == 1 ? INF - depth : -INF + depth);
            tbls[goalDepth].put(lauta.kopio(), value);
            return value;
        }
        if (lauta.onTaynna()) {
            tbls[goalDepth].put(lauta.kopio(), 0);
            return 0;
        }
        if (depth == goalDepth || System.nanoTime()-this.startingTIme >= (long)1e9*this.TIMELIMIT) {
        //if (depth == goalDepth) {
            //Heuristiikka?
            int value = heuristiikka();
            tbls[goalDepth].put(lauta.kopio(), value);
            return value;
        }
        int paras = (myTurn ? -INF : INF);
        ArrayList<Siirto> siirrot = generoiSiirrot(player, goalDepth-1, true);
        jarjestaSiirrot(siirrot);
        if (myTurn) Collections.reverse(siirrot);
        if (killermove[depth] != null && lauta.vapaa(killermove[depth].getX(), killermove[depth].getY())) siirrot.add(0,killermove[depth]);
        //järjestäSiirrot?
        
        long pelaajanLauta = lauta.getTilanne(0);
        long botinLauta = lauta.getTilanne(1);
        
        for (Siirto siirto : siirrot) {
            lauta.teeSiirto(player,siirto);
            if (myTurn) {
                int uusiarvo = negaMax(false, alpha, beta, depth+1, goalDepth);
                if (uusiarvo > paras) killermove[depth] = siirto;
                paras = Math.max(paras, uusiarvo);
                //paras = Math.max(paras, negaMax(false, alpha, beta, depth+1, goalDepth));
                alpha = Math.max(paras, alpha);
            } else {
                int uusiarvo = negaMax(true, alpha, beta, depth+1, goalDepth);
                if (uusiarvo < paras) killermove[depth] = siirto;
                paras = Math.min(paras, uusiarvo);
                beta = Math.min(beta, paras);
            }
            if (alpha >= beta) {
                peruSiirto(pelaajanLauta, botinLauta);
                //int value = (player == 0 ? INF/2 : -INF/2);
                //value = paras;
                //tbls[goalDepth].put(lauta.kopio(), value);
                //lauta.tulostaLauta();
                //System.out.println(transtbl.getOrDefault(lauta.kopio(), -INF));
                //extramaps[goalDepth-1].put(lauta.kopio(), paras);
                return paras;
            }
            peruSiirto(pelaajanLauta, botinLauta);
        }
        tbls[goalDepth].put(lauta.kopio(), paras);
        return paras;
    }    
    
    private void peruSiirto(long O, long X) {
        lauta.asetaBittiKentta(0, O);
        lauta.asetaBittiKentta(1, X);
    }

    private ArrayList<Siirto> generoiSiirrot(int player, int goalDepth, boolean withvalues) {
        long pelaajanLauta = lauta.getTilanne(0);
        long botinLauta = lauta.getTilanne(1);
        ArrayList<Siirto> tulos = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                if (lauta.vapaa(i, j)) {
                    for (int k = 1; k <= 8; k++) {
                        lauta.teeSiirto(player, i,j,k);
                        //tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), 0)));
                        
                        //if (withvalues) tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), extramaps[goalDepth].getOrDefault(lauta.kopio(), player == 0 ? -INF/2 : INF/2))));
                        
                        int value = -2*INF;
                        int kerroin = player == 1 ? 1 : -1;
                        for (int x = goalDepth; x >= 1; x--) {
                            if (tbls[x].containsKey(lauta.kopio())) {
                                value = tbls[x].get(lauta.kopio()) + 50000*kerroin*(goalDepth-x);
                                break;
                            }
                        }
                        if (value == -2*INF) value = halpaHeuristiikka();
                        if (withvalues) tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), value)));
                        
                        //if (withvalues) tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), halpaHeuristiikka())));
                        //int value = 0;
                        //if (withvalues) {
                        //    value = this.heuristicTbl.getOrDefault(lauta.kopio(), heuristiikka());
                        //    this.heuristicTbl.put(lauta.kopio(), value);
                        //}
                        //if (withvalues) tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), value)));
                        else tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), player == 0 ? -2*INF : 2*INF)));
                        //if (withvalues) tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), heuristiikka())));
                        //else tulos.add(new Siirto(i,j,k,tbls[goalDepth].getOrDefault(lauta.kopio(), 0)));
                        peruSiirto(pelaajanLauta, botinLauta);
                    }
                }
            }
        }
        return tulos;
    }

    private void jarjestaSiirrot(ArrayList<Siirto> siirrot) {
        Collections.sort(siirrot);
    }

    private int heuristiikka() {
        int oneMoveThreatsBot = 0;
        int oneMoveThreatsPlayer = 0;
        long pelaajanLauta = lauta.getTilanne(0);
        long botinLauta = lauta.getTilanne(1);
        ArrayList<Siirto> siirrot = generoiSiirrot(0, 0, false);
        for (Siirto siirto : siirrot) {
            lauta.teeSiirto(0, siirto);
            if (lauta.loytyyViidenSuora(0)) oneMoveThreatsBot++;
            this.peruSiirto(pelaajanLauta, botinLauta);
            lauta.teeSiirto(1, siirto);
            if (lauta.loytyyViidenSuora(1)) oneMoveThreatsPlayer++;
            this.peruSiirto(pelaajanLauta, botinLauta);
        }
        int res = 0;
        res = 1000*oneMoveThreatsBot - 1000*oneMoveThreatsPlayer;
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                if (lauta.symboliKohdassa(i, j) == 'O') {
                    res -= Math.abs(7-(1+2*(i-1)));
                    res -= Math.abs(7-(1+2*(j-1)));
                }
                if (lauta.symboliKohdassa(i, j) == 'X') {
                    res += Math.abs(7-(1+2*(i-1)));
                    res += Math.abs(7-(1+2*(j-1)));
                }
            }
        }
        return res;
    }
    
    private int halpaHeuristiikka() {
        int res = 0;
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                if (lauta.symboliKohdassa(i, j) == 'O') {
                    res -= Math.abs(7-(1+2*(i-1)));
                    res -= Math.abs(7-(1+2*(j-1)));
                }
                if (lauta.symboliKohdassa(i, j) == 'X') {
                    res += Math.abs(7-(1+2*(i-1)));
                    res += Math.abs(7-(1+2*(j-1)));
                }
            }
        }
        return res;
    }
}


