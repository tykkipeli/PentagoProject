/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import PentagoProject.Lauta;
import PentagoProject.Tekoaly;
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
public class TestTekoaly {
    
    public TestTekoaly() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void tekeeVoittavanSiirronJosSellainenLoytyy() {
        Lauta lauta = new Lauta(        "|O| |X|X|O| |\n" +
                                        "| | |O|X| | |\n" +
                                        "| |X|X|O| | |\n" +
                                        "|O|O|X|O|O|X|\n" +
                                        "|O|X|O|O| |X|\n" +
                                        "| |X|X|X| |X|");

        Tekoaly aly = new Tekoaly();
        aly.teeSiirto(lauta);
        assertTrue(lauta.loytyyViidenSuora(0));
    }
}
