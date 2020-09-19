package PentagoProject.logiikka;

import PentagoProject.pelikomponentit.Siirto;
import java.util.Arrays;

public class Lauta {

    private final int SIZE = 6;

    public long bitboard[] = new long[2];
    private int voittaja = 1;
    private int tayttoAste = 0;

    public Lauta() {
    }

    // debuggailua varten konstruktori, jolle voi antaa tilanteen merkkijonona
    public Lauta(String kentta) {
        String rivit[] = kentta.split("\n");
        for (int i = 0; i < 6; i++) {
            for (int j = 1; j <= 11; j+=2) {
                if (rivit[i].charAt(j) == 'X') {
                    teeSiirto(1,i+1,j/2+1);
                }
                if (rivit[i].charAt(j) == 'O') {
                    teeSiirto(0,i+1,j/2+1);
                }
            }
            System.out.println("");
        }
    }

    public String getVoittaja() {
        if (voittaja == 1) {
            return "Pelaaja";
        }
        return "Botti";
    }
    
    public Lauta kopio() {
        Lauta uusi = new Lauta();
        uusi.bitboard[0] = this.bitboard[0];
        uusi.bitboard[1] = this.bitboard[1];
        return uusi;
    }
    
    public long getTilanne(int p) {
        return bitboard[p];
    }

    public int getVoittajaInt() {
        return voittaja;
    }
    
    public int getTayttoaste() {
        int ans = 0;
        for (long i = 0; i < 64; i++) {
            if (((1L<<i)&bitboard[0]) != 0) ans++;
            if (((1L<<i)&bitboard[1]) != 0) ans++;
        }
        return ans;
    }

    public void tulostaLauta() {
        System.out.print(" ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(" " + i);
        }
        System.out.println("");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + "|");
            for (int j = 1; j <= SIZE; j++) {
                System.out.print(symboliKohdassa(i, j) + "|");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public char symboliKohdassa(int x, int y) {
        long mask = laskeMask(x, y);
        if ((mask & bitboard[0]) != 0) {
            return 'O';
        }
        if ((mask & bitboard[1]) != 0) {
            return 'X';
        }
        return ' ';
    }

    // pelaaja p tekee siirron riville x sarakkeeseen y ja suorittaa käännön kaanto
    public void teeSiirto(int p, int x, int y, int kaanto) {
        long mask = laskeMask(x, y);
        bitboard[p] = mask | bitboard[p];
        teeKaanto(kaanto);
        tayttoAste++;
    }
    
    public void teeSiirto(int p, int x, int y) {
        long mask = laskeMask(x, y);
        bitboard[p] = mask | bitboard[p];
        tayttoAste++;
    }

    public void asetaBittiKentta(int p, long q) {
        tayttoAste--;
        bitboard[p] = q;
    }

    private long laskeMaskShift(int x, int y) {
        return ((x - 1) * 7 + y - 1);
    }

    private long laskeMask(int x, int y) {
        return 1L << laskeMaskShift(x, y);
    }
    
    public boolean loytyyViidenSuora() {
        if (loytyyViidenSuora(0)) {
            voittaja = 0;
            return true;
        }
        if (loytyyViidenSuora(1)) {
            voittaja = 1;
            return true;
        }
        return false;
    }

    public boolean loytyyViidenSuora(int p) {
        //vaaka
        long x = bitboard[p];
        if (((x << 1L) & (x >> 1L) & (x << 2L) & (x >> 2L) & x) != 0) return true;
        //pysty
        if (((x << 7L) & (x >> 7L) & (x << 14L) & (x >> 14L) & x) != 0) return true;
        //vino
        if (((x << 6L) & (x >> 6L) & (x << 12L) & (x >> 12L) & x) != 0) return true;
        if (((x << 8L) & (x >> 8L) & (x << 16L) & (x >> 16L) & x) != 0) return true;
        return false;
    }

    public boolean vapaa(int x, int y) {
        long mask = laskeMask(x,y);
        if (((mask & bitboard[0]) == 0L) && ((mask & bitboard[1]) == 0L)) return true;
        return false;
    }

    private void teeKaanto(int kaanto) {
        if (kaanto == 1) {
            kaannaVasemmalle(2, 2);
        } else if (kaanto == 2) {
            kaannaOikealle(2, 2);
        } else if (kaanto == 3) {
            kaannaVasemmalle(2, 5);
        } else if (kaanto == 4) {
            kaannaOikealle(2, 5);
        } else if (kaanto == 5) {
            kaannaVasemmalle(5, 2);
        } else if (kaanto == 6) {
            kaannaOikealle(5, 2);
        } else if (kaanto == 7) {
            kaannaVasemmalle(5, 5);
        } else if (kaanto == 8) {
            kaannaOikealle(5, 5);
        }
    }

    private void kaannaVasemmalle(int x, int y) {
        for (int p = 0; p <= 1; p++) {
            long kopio = bitboard[p];
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    long bitti = laskeMask(i + x, j + y) & bitboard[p];
                    if (bitti != 0) {
                        kopio |= laskeMask(-j+x, i+y);
                    } else {
                        kopio &= (~laskeMask(-j+x, i+y));
                    }
                }
            }
            bitboard[p] = kopio;
        }
    }
    
    private void kaannaOikealle(int x, int y) {
        for (int p = 0; p <= 1; p++) {
            long kopio = bitboard[p];
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    long bitti = laskeMask(i + x, j + y) & bitboard[p];
                    if (bitti != 0) {
                        kopio |= laskeMask(j+x, -i+y);
                    } else {
                        kopio &= (~laskeMask(j+x, -i+y));
                    }
                }
            }
            bitboard[p] = kopio;
        }
    }

    private boolean taysi() {
        return tayttoAste == SIZE * SIZE;
    }

    @Override
    public int hashCode() {
        long hash = bitboard[0]*23;
        hash += bitboard[1];
        return (int)hash;
    }

    @Override
    public boolean equals(Object obj) {
        final Lauta other = (Lauta) obj;
        if (this.bitboard[0] == other.bitboard[0] && this.bitboard[1] == other.bitboard[1]) return true;
        return false;
    }

    public void teeSiirto(int player, Siirto siirto) {
        this.teeSiirto(player, siirto.getX(), siirto.getY(), siirto.getKaanto());
    }
    
    public boolean onTaynna() {
        return this.getTayttoaste() == SIZE*SIZE;
    }

    public boolean tasapeli() {
        if (this.loytyyViidenSuora(0) && this.loytyyViidenSuora(1)) return true;
        if (!this.loytyyViidenSuora() && this.tayttoAste == SIZE*SIZE) return true;
        return false;
    }

}
