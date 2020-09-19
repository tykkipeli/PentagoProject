/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PentagoProject.pelikomponentit;


/**
 *
 * @author matilaol
 */
public class Siirto implements Comparable<Siirto> {
    private int x;
    private int y;
    private int kaanto;
    private int value;

    public Siirto(int x, int y, int kaanto, int value) {
        this.x = x;
        this.y = y;
        this.kaanto = kaanto;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getKaanto() {
        return kaanto;
    }

    public void setKaanto(int kaanto) {
        this.kaanto = kaanto;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Siirto toinen) {
        return this.value - toinen.value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Siirto other = (Siirto) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.kaanto != other.kaanto) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
    
    
}
