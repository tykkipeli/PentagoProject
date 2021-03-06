/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PentagoProject.kayttoliittyma;

import PentagoProject.logiikka.Lauta;
import PentagoProject.logiikka.Tekoaly;
import PentagoProject.pelikomponentit.Siirto;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame{
    
    private Lauta lauta = new Lauta();
    public static boolean paattynyt = false;
    public static boolean kaynnissa = true;
    public static boolean pelaajanVuoro = true;
    public static boolean kaantovaihe = false;
    private MyPanel kentta;
    //private AI tekoaly = new AI();
    private Tekoaly tekoaly = new Tekoaly();
    
    
    public GUI() {
        initUI();
    }

    private void initUI() {

       this.setResizable(false);
       
       JPanel ulkopaneeli = new JPanel();
       ulkopaneeli.setLayout(new BorderLayout());
       this.kentta = new MyPanel(lauta, tekoaly);
       kentta.setPreferredSize(new Dimension(800,800));
       
       //kentta.setBackground(Color.green);
       
       
       add(ulkopaneeli);
       
       JButton buttonPelaajaAloittaa = new JButton("Restart: Pelaaja aloittaa");
       JButton buttonBottiAloittaa = new JButton("Restart: Botti aloittaa     ");
       
       buttonPelaajaAloittaa.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e){  
            if (!GUI.kaynnissa || GUI.pelaajanVuoro) {
                GUI.kaantovaihe = false;
                GUI.kaynnissa = true;
                lauta = new Lauta();
                kentta.alusta();
                kentta.setLauta(lauta);
                kentta.setTeksti("Sinun vuoro");
                GUI.pelaajanVuoro = true;
                kentta.repaint();
            }
        }  
        });  
       
       buttonBottiAloittaa.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e) {
            if (!GUI.kaynnissa || GUI.pelaajanVuoro) {
                GUI.kaynnissa = true;
                lauta = new Lauta();
                kentta.alusta();
                kentta.setLauta(lauta);
                kentta.setTeksti("Odota hetki, botti miettii");
                kentta.repaint();
                GUI.pelaajanVuoro = false;
                GUI.kaantovaihe = false;
                Thread thread = new Thread(() -> {
                        Lauta uusilauta = lauta.kopio();
                        tekoaly.teeSiirto(uusilauta);
                        GUI.pelaajanVuoro = true;
                        kentta.setLauta(uusilauta);
                        kentta.setTeksti("Sinun vuoro");
                        kentta.tarkistaTilanne();
                        kentta.repaint();
                    });
                    thread.start();
            }
        }  
        });

       
       JPanel nappipaneeli = new JPanel();
       nappipaneeli.setLayout(new BoxLayout(nappipaneeli, BoxLayout.Y_AXIS));
       nappipaneeli.setBorder(new EmptyBorder(new Insets(40, 60, 40, 60)));
       
       nappipaneeli.add(Box.createRigidArea(new Dimension(0, 50)));
       nappipaneeli.add(buttonPelaajaAloittaa);
       nappipaneeli.add(Box.createRigidArea(new Dimension(0, 20)));
       nappipaneeli.add(buttonBottiAloittaa);
       nappipaneeli.add(Box.createRigidArea(new Dimension(0, 50)));
       
       ulkopaneeli.add(kentta, CENTER);
       ulkopaneeli.add(nappipaneeli, EAST);

        pack();

        setTitle("TykkiPentago 1.0");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void paint(Graphics g) {
        
    }
    
}

class MyPanel extends JPanel implements MouseListener{
    
    private Lauta lauta;
    //private AI tekoaly;
    private Tekoaly tekoaly;
    private Siirto siirto;
    int size = 50;
    int koko = 100;
    private int uusiI = -1;
    private int uusiJ = -1;
    private String teksti = "Paina nappia aloittaaksesi peli";
    int kaantox[] = new int[] {koko/2, 2*koko, 5*koko, 13*koko/2, 2*koko, koko/2, 13*koko/2, 5*koko};
    int kaantoy[] = new int[] {2*koko, koko/2, koko/2, 2*koko, 13*koko/2, 5*koko, 5*koko, 13*koko/2};
    
    public MyPanel(Lauta lauta, Tekoaly tekoaly) {
        this.lauta = lauta;
        this.tekoaly = tekoaly;
        setBackground(new Color(30,144,255));
        addMouseListener(this);
    }
    
    public void setLauta(Lauta lauta) {
        this.lauta = lauta;
    }
    
    public void alusta() {
        this.uusiI = -1;
        this.uusiJ = -1;
    }
    
    public void setTeksti(String str) {
        this.teksti = str;
    }

    public Dimension getPreferredSize() {
        return new Dimension(730,730);
    }
    
    public void tarkistaTilanne() {
        if (lauta.tasapeli()) {
            this.setTeksti("Tasapeli!");
            GUI.kaynnissa = false;
        } else if (lauta.loytyyViidenSuora(0)) {
            this.setTeksti("Hävisit pelin!");
            GUI.kaynnissa = false;
        } else if (lauta.loytyyViidenSuora(1)) {
            this.setTeksti("Voitit pelin!");
            GUI.kaynnissa = false;
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g2.draw(new Line2D.Float(7*koko/2, koko - 10, 7*koko/2, 6*koko + 15));
        g2.draw(new Line2D.Float(koko-10, 7*koko/2, 6*koko+15, 7*koko/2));
        
        g.setFont(new Font("Serif", Font.BOLD, 30));
        g.drawString(teksti, 100, 710);
        
        g2.setStroke(new BasicStroke(7));
        g.setColor(Color.red);
        drawArrowDown(g, kaantox[0]-10, kaantoy[0]-30);
        drawArrowRight(g, kaantox[1]-20, kaantoy[1]-10);
        drawArrowLeft(g, kaantox[2]-45, kaantoy[2]-10);
        drawArrowDown(g, kaantox[3]+10, kaantoy[3]-30);
        drawArrowRight(g, kaantox[4]-25, kaantoy[4]+5);
        drawArrowUp(g, kaantox[5]-10, kaantoy[5]-50);
        drawArrowUp(g, kaantox[6]+10, kaantoy[6]-50);
        drawArrowLeft(g, kaantox[7]-45, kaantoy[7]+5);
        /*
        for (int i = 0; i < 8; i++) {
            g.setColor(Color.red);
            //g.fillOval(kaantox[i]-25, kaantoy[i]-25, 50, 50);
            drawArrow(g, kaantox[i]-25, kaantoy[i]-25);
        }
        */
        g.setColor(Color.black);
        for (int i = 1; i <= 6; i++) {
           for (int j = 1; j <= 6; j++) {
               if (lauta.symboliKohdassa(i, j) == ' ') {
                   g.setColor(Color.gray);
                   g.fillOval(koko*j - size/4, koko*i - size/4, size/2, size/2);
               } else if (lauta.symboliKohdassa(i, j) == 'X') {
                   g.setColor(Color.white);
                   g.fillOval(koko*j - size/2, koko*i - size/2, size, size);
               } else {
                   g.setColor(Color.black);
                   g.fillOval(koko*j -size/2, koko*i -size/2, size, size);
               }
               if (uusiI == i && uusiJ == j) {
                   g.setColor(Color.white);
                   g.fillOval(koko*j - size/2, koko*i - size/2, size, size);
               }
           }
       }
    }  

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (GUI.kaynnissa && GUI.pelaajanVuoro) {
            if (GUI.kaantovaihe) {
                int kaanto = 0;
                for (int i = 0; i < 8; i++) {
                    if (Math.abs(y-kaantoy[i]) < 50 && Math.abs(x-kaantox[i]) < 50) {
                        kaanto = i+1;
                    }
                }
                if (kaanto == 0) return;
                uusiI = -1;
                uusiJ = -1;
                siirto.setKaanto(kaanto);
                lauta.teeSiirto(1, siirto);
                this.setTeksti("Odota hetki, botti miettii");
                this.tarkistaTilanne();
                repaint();
                GUI.kaantovaihe = false;
                GUI.pelaajanVuoro = false;
                if (!GUI.kaynnissa) return;
                
                Thread thread = new Thread(() -> {
                    Lauta uusilauta = lauta.kopio();
                    lauta.tulostaLauta();
                    tekoaly.teeSiirto(uusilauta);
                    lauta.tulostaLauta();
                    GUI.pelaajanVuoro = true;
                    this.lauta = uusilauta;
                    this.setTeksti("Sinun vuoro");
                    this.tarkistaTilanne();
                    repaint();
                });
                thread.start();
                
                /*
                tekoaly.teeSiirto(lauta);
                GUI.pelaajanVuoro = true;
                repaint();
                */
            } else {
                Siirto siirto = null;
                for (int i = 1; i <= 6; i++) {
                    for (int j = 1; j <= 6; j++) {
                        if (Math.abs(y-koko*i) < 15 && Math.abs(x-koko*j) < 15) {
                            siirto = new Siirto(i,j,0,0);
                        }
                    }
                }
                if (siirto == null || !lauta.vapaa(siirto.getX(), siirto.getY())) return;
                this.siirto = siirto;
                uusiI = siirto.getX();
                uusiJ = siirto.getY();
                GUI.kaantovaihe = true;
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }
    
    public void drawArrowRight(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(30));
        g.drawLine(x, y, x+78, y);
        g2.setStroke(new BasicStroke(10));
        g.drawLine(x+70,y-30,x+100,y);
        g.drawLine(x+70,y+30,x+100,y);
    }
    
    public void drawArrowLeft(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(30));
        g.drawLine(x+22, y, x+100, y);
        g2.setStroke(new BasicStroke(10));
        g.drawLine(x,y,x+30,y-30);
        g.drawLine(x,y,x+30,y+30);
    }
    
    public void drawArrowDown(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(30));
        g.drawLine(x, y, x, y+78);
        g2.setStroke(new BasicStroke(10));
        g.drawLine(x-30,y+70,x,y+100);
        g.drawLine(x+30,y+70,x,y+100);
    }
    
    public void drawArrowUp(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(30));
        g.drawLine(x, y+22, x, y+100);
        g2.setStroke(new BasicStroke(10));
        g.drawLine(x,y,x-30,y+30);
        g.drawLine(x,y,x+30,y+30);
    }
}