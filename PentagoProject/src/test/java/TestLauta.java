/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import PentagoProject.logiikka.Lauta;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author matilaol
 */
public class TestLauta {
    
    public TestLauta() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void laudanKaantoMyotapaivaanToimii() {
        Lauta lautaAlussa = new Lauta(  "| | |X|X| | |\n" +
                                        "| | |O|O| | |\n" +
                                        "| |X|O|O| | |\n" +
                                        "|O|O|X|O|O|X|\n" +
                                        "|X| |O|O| |X|\n" +
                                        "| | | |X|X|X|");
        lautaAlussa.teeSiirto(0,1,1,8);
        
        Lauta lautaLopussa = new Lauta( "|O| |X|X| | |\n" +
                                        "| | |O|O| | |\n" +
                                        "| |X|O|O| | |\n" +
                                        "|O|O|X|X|O|O|\n" +
                                        "|X| |O|X| |O|\n" +
                                        "| | | |X|X|X|");
        assertTrue(lautaAlussa.equals(lautaLopussa));
    }
    
    @Test
    public void laudanKaantoVastapaivaanToimii() {
        Lauta lautaAlussa = new Lauta(  "| | |X|X| | |\n" +
                                        "| | |O|O| | |\n" +
                                        "| |X|O|O| | |\n" +
                                        "|O|O|X|O|O|X|\n" +
                                        "|X| |O|O| |X|\n" +
                                        "| | | |X|X|X|");
        lautaAlussa.teeSiirto(0,1,1,5);
        
        Lauta lautaLopussa = new Lauta( "|O| |X|X| | |\n" +
                                        "| | |O|O| | |\n" +
                                        "| |X|O|O| | |\n" +
                                        "|X|O| |O|O|X|\n" +
                                        "|O| | |O| |X|\n" +
                                        "|O|X| |X|X|X|");
        assertTrue(lautaAlussa.equals(lautaLopussa));
    }
    
    @Test
    public void tunnistaViidensuoraToimiiVaaka() {
        Lauta lauta = new Lauta(        "|O| |X|X| | |\n" +
                                        "| | |O|O| | |\n" +
                                        "| |X|O|O| | |\n" +
                                        "|O|O|X|O|O|X|\n" +
                                        "|X| |O|O| |X|\n" +
                                        "| |X|X|X|X|X|");

        assertTrue(lauta.loytyyViidenSuora());
    }
    
    @Test
    public void tunnistaViidensuoraToimiiPysty() {
        Lauta lauta = new Lauta(        "|O| |X|O| | |\n" +
                                        "| | |O|O| | |\n" +
                                        "| |X|O|O| | |\n" +
                                        "|O|O|X|O|O|X|\n" +
                                        "|X| |O|O| |X|\n" +
                                        "| |X|X|X| |X|");

        assertTrue(lauta.loytyyViidenSuora());
    }
    
    @Test
    public void tunnistaViidensuoraToimiiViisto() {
        Lauta lauta = new Lauta(        "|O| |X|X|O| |\n" +
                                        "| | |O|O| | |\n" +
                                        "| |X|O|O| | |\n" +
                                        "|O|O|X|O|O|X|\n" +
                                        "|O|X|O|O| |X|\n" +
                                        "| |X|X|X| |X|");

        assertTrue(lauta.loytyyViidenSuora());
    }
    
    @Test
    public void eiLoydaViidensuoraaKunSellaistaEiOle() {
        Lauta lauta = new Lauta(        "|O|X|X|X|O| |\n" +
                                        "|O| |O|O|X|X|\n" +
                                        "| |X|O|O|O|O|\n" +
                                        "|O|X|X|O|O|X|\n" +
                                        "|O|X|O|O|O|X|\n" +
                                        "|X|X|X|X|O|X|");

        assertFalse(lauta.loytyyViidenSuora());
    }
    
    @Test
    public void tunnistaaTasapelin() {
        Lauta lauta = new Lauta(        "|O|X|X|X|O| |\n" +
                                        "|O| |O|O|X|O|\n" +
                                        "| |X|O|X|O|O|\n" +
                                        "|O|X|X|O|O|X|\n" +
                                        "|O|X|O|O|O|X|\n" +
                                        "|X|O|X|X|O|X|");

        assertTrue(lauta.tasapeli());
    }

}
