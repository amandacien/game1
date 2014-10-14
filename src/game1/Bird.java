/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game1;

import java.awt.Color;
import java.util.Random;
import javalib.funworld.*;
import javalib.colors.*;
import javalib.worldcanvas.*;
import javalib.worldimages.*;

/**
 *
 * @author AmandaMa
 */
public class Bird {

    Posn position;
    
    final int diameter = 10;
    int rate; 
    int height;
    int width;
    
    static IColor col = new Yellow();
    
    static Random rand = new Random();
    
    //Constructor 
    Bird(int width, int height, int level)
    {
        this.height = height;
        this.width = width;
        this.rate = level * 3;
        this.position = new Posn (0, randomY(height));
    }
    
    //Constructor 
    //makes private later
    Bird (Posn position, int width, int height, int rate){
        this.position = position;
        this.height = height;
        this.width = width;
        this.rate = rate;
    }
    
    //creates a random Y point at which the bird enters from 
    //makes sure that a bird will be fully on screen
    private int randomY (int height) {
        return rand.nextInt(height - (diameter * 2)) + diameter;
    }
    
    //returns a bird's x position to be (rate * 3) more than
    //where is was presently
    public Bird moveBird()
    {
        return new Bird(new Posn(this.position.x + rate, this.position.y), 
                this.height, this.width, this.rate);
    }
    
    //creates a image which will represent the bird in the world, a yellow
    //filled circle with a diameter of 10
    WorldImage birdImage(){
        return new DiskImage(this.position, this.diameter, this.col);
    }

    
}
