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
public class Pipe {
    
    Posn position;
    
    final int pipeHeight = 50;
    final int pipeWidth = 30;
    
    static int movePipeRate = 5;
            
    int height;
    int width;
    
    static IColor col = new Green();
    
    
    Pipe(int width, int height){
        this.position = new Posn(width - (pipeWidth/2), height/2) ;
        this.height = height;
        this.width = width;
    }
    
    private Pipe(Posn position, int height, int width)
    {
        //puts the pipe in the middle of the screen
        this.position = position;
        this.height = height;
        this.width = width;
    }
    
    public Pipe movePipe(String key)
    {
        if (key.equals("down"))
            return new Pipe(new Posn(this.position.x, this.position.y + movePipeRate), 
                            this.height, this.width);
        else if (key.equals("up"))
            return new Pipe(new Posn(this.position.x, this.position.y - movePipeRate), 
                            this.height, this.width);
        else 
            return this;
    }
    
    
    public Pipe pipeOutOfBounds()
    {
        if (this.position.y < pipeHeight/2)
            return new Pipe(new Posn(this.position.x, (pipeHeight/2)), 
                            this.height, this.width);
        else if (this.position.y > height - (pipeHeight/2))
            return new Pipe(new Posn(this.position.x, height - pipeHeight/2), 
                            this.height, this.width);
        else 
            return this;
    }
    
   
    private boolean touchHelperX(Bird bird){
        return ((bird.position.x + bird.radius/2) >
                (this.position.x - this.pipeWidth/2));
    }
    
    private boolean touchHelperY(Bird bird){
        return ((bird.position.y < this.position.y + this.pipeHeight/2 - bird.radius/2)&&
                    (bird.position.y > this.position.y - this.pipeHeight/2 + bird.radius/2));
    } 
    
    public int touchedBird(Bird bird){
        if (touchHelperX(bird) && touchHelperY(bird)){
            return 1; //will return a 1 if the bird is in range of the correct x and y coordinates 
        } else if ((!(touchHelperY(bird))) && touchHelperX(bird)){
            return -1; //will return -1 if the bird has reached the same x
                       //coordinate but has not reached the same y coordinate
         }else
            return 0; //will return a 0 if the bird has not yet reached the pipe 
    }
    
    WorldImage pipeImage(){
        return new RectangleImage(this.position, 
                this.pipeWidth, this.pipeHeight, this.col);
    }
    
}
