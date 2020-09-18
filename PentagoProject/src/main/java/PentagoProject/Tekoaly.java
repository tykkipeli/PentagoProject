/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PentagoProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author matilaol
 */
public class Tekoaly {
    
    private Lauta lauta;
    private final int INF = 9999999;
    private final int MAXDEPTH = 20;
    private final int TIMELIMIT = 15;
    private boolean ajastus = true;
    private long startingTIme;
    private int laskuri = 0;
    private HashMap<Lauta,Integer>[] tbls = new HashMap[MAXDEPTH];
    private Siirto killermove[] = new Siirto[100];
    
    public Tekoaly() {
        for (int i = 0; i < MAXDEPTH; i++) {
            tbls[i] = new HashMap<Lauta,Integer>();
            killermove[i] = null;
        }
    }
    
    public void teeSiirto(Lauta lauta) {
        this.lauta = lauta;
        for (int i = 0; i < MAXDEPTH; i++) {
            tbls[i].clear();
            killermove[i] = null;
        }
        this.startingTIme = System.nanoTime();
        int iterations = MAXDEPTH-1;
        for (int i = 1; i <= iterations; i++) {
            System.out.println("iteration: " + i);
            //for (int j = 1; j <= i; j++) killermove[i] = null;
            negaMax(true, -INF+30, INF-30, 0, i);
            if (ajastus && System.nanoTime()-this.startingTIme >= (long)1e9*this.TIMELIMIT) {
                iterations = i-1;
                break;
            }
        }
        int best = -2*INF;
        Siirto parassiirto = null;
        for (Siirto siirto : generoiSiirrot(0, iterations)) {
            if (getArvo(siirto, iterations) > best) {
                best = getArvo(siirto, iterations);
                parassiirto = siirto;
            }
        }
        lauta.teeSiirto(0, parassiirto);
        System.out.println("Laskurin arvo: " + laskuri);
    }

    private int negaMax(boolean maximize, int alpha, int beta, int depth, int goalDepth) {
        laskuri++;
        if (tbls[goalDepth].containsKey(lauta.kopio())) {
            return tbls[goalDepth].get(lauta.kopio());
        }
        if (lauta.loytyyViidenSuora(1) && lauta.loytyyViidenSuora(0)) {
            tbls[goalDepth].put(lauta.kopio(), 0);
            return 0;
        }
        int player = maximize ? 0 : 1;
        if (lauta.loytyyViidenSuora() && lauta.getVoittajaInt() != player) {
            //int value = (player == 1 ? INF - depth : -INF + depth);
            int value = (player == 1 ? INF : -INF);
            tbls[goalDepth].put(lauta.kopio(), value);
            return value;
        }
        if (lauta.onTaynna()) {
            tbls[goalDepth].put(lauta.kopio(), 0);
            return 0;
        }
        if (depth == goalDepth || (ajastus && System.nanoTime()-this.startingTIme >= (long)1e9*this.TIMELIMIT)) {
            int value = heuristiikka();
            //value = 0;
            tbls[goalDepth].put(lauta.kopio(), value);
            return value;
        }
        int paras = (maximize ? -INF : INF);
        ArrayList<Siirto> siirrot = generoiSiirrot(player, goalDepth-1);
        Collections.sort(siirrot);
        if (maximize) Collections.reverse(siirrot);
        //if (killermove[depth] != null && lauta.vapaa(killermove[depth].getX(), killermove[depth].getY())) siirrot.add(0,killermove[depth]);
        
        long pelaajanLauta = lauta.getTilanne(0);
        long botinLauta = lauta.getTilanne(1);
        int counter = 0;
        for (Siirto siirto : siirrot) {
            lauta.teeSiirto(player,siirto);
            if (maximize) {
                int uusiarvo = negaMax(false, alpha, beta, depth+1, goalDepth);
                if (uusiarvo > paras) killermove[depth] = siirto;
                paras = Math.max(paras, uusiarvo);
                alpha = Math.max(paras, alpha);
            } else {
                int uusiarvo = negaMax(true, alpha, beta, depth+1, goalDepth);
                if (uusiarvo < paras) killermove[depth] = siirto;
                paras = Math.min(paras, uusiarvo);
                beta = Math.min(beta, paras);
            }
            if (alpha >= beta) {
                peruSiirto(pelaajanLauta, botinLauta);
                return paras;
            }
            peruSiirto(pelaajanLauta, botinLauta);
            counter++;
            //if (counter > 70) break;
        }
        tbls[goalDepth].put(lauta.kopio(), paras);
        return paras;
    }

    private int heuristiikka() {
        if (lauta.tasapeli()) return 0;
        if (lauta.loytyyViidenSuora(0)) return INF;
        if (lauta.loytyyViidenSuora(1)) return -INF;
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

    private ArrayList<Siirto> generoiSiirrot(int player, int depth) {
        long pelaajanLauta = lauta.getTilanne(0);
        long botinLauta = lauta.getTilanne(1);
        ArrayList<Siirto> tulos = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                if (lauta.vapaa(i, j)) {
                    for (int k = 1; k <= 8; k++) {
                        lauta.teeSiirto(player, i,j,k);
                        // Jos aiaksemmalla syvyydellä position arvo on jo saatu ratkaistua annetaan se siirron arvoksi
                        if (tbls[depth].containsKey(lauta.kopio())) tulos.add(new Siirto(i,j,k,tbls[depth].get(lauta.kopio())));
                        else tulos.add(new Siirto(i,j,k,heuristiikka()));
                        peruSiirto(pelaajanLauta, botinLauta);
                    }
                }
            }
        }
        return tulos;
    }

    private void peruSiirto(long pelaajanLauta, long Botinlauta) {
        lauta.asetaBittiKentta(0, pelaajanLauta);
        lauta.asetaBittiKentta(1, Botinlauta);
    }

    private int getArvo(Siirto siirto, int tblInd) {
        long pelaajanLauta = lauta.getTilanne(0);
        long botinLauta = lauta.getTilanne(1);
        lauta.teeSiirto(0, siirto);
        int arvo = tbls[tblInd].getOrDefault(lauta.kopio(), -INF);
        peruSiirto(pelaajanLauta, botinLauta);
        return arvo;
    }
    
}
