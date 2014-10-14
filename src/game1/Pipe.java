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
    
    static int movePipeRate = 7;
            
    int height;
    int width;
    
    static IColor col = new Green();
    
    //Constructor 
    Pipe(int width, int height){
        //puts the pipe in the middle right of the screen
        this.position = new Posn(width - (pipeWidth/2), height/2) ;
        this.height = height;
        this.width = width;
    }
    
    //Constructor 
    Pipe(Posn position, int height, int width)
    {
        this.position = position;
        this.height = height;
        this.width = width;
    }
    
    //Moves the pipe depending on whether the up or down key is pressed and 
    //returns a new pipe accordingly
    //If any other key is pressed, then it will return the current pipe
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
    
    
    //Looks at if the pipe's y is out of bound of the screen and if so,
    //returns a new pipe that meets either the upper bound or lower bound of the 
    //screen depending on where it was previously
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
    
    //Looks if the x coordinate of the bird and pipe meet
    private boolean touchHelperX(Bird bird){
        return ((bird.position.x + bird.diameter/2) >
                (this.position.x - this.pipeWidth/2));
    }
    
    //Looks if the y coordinates of the bird and pipe match 
    private boolean touchHelperY(Bird bird){
        return ((bird.position.y < this.position.y + this.pipeHeight/2 - bird.diameter/2)&&
                    (bird.position.y > this.position.y - this.pipeHeight/2 + bird.diameter/2));
    } 
    
    //returns an integer which correlates to whether a bird hs touched the pipe,
    //has failed to or has not yet reached the pipe
    public int touchedBird(Bird bird){
        if (touchHelperX(bird) && touchHelperY(bird)){
            return 1; //will return a 1 if the bird is in range of the correct x and y coordinates 
        } else if ((!(touchHelperY(bird))) && touchHelperX(bird)){
            return -1; //will return -1 if the bird has reached the same x
                       //coordinate but has not reached the same y coordinate
         }else
            return 0; //will return a 0 if the bird has not yet reached the pipe 
    }
    
    //Creates the image for the pipe, a filled green rectangle 
    WorldImage pipeImage(){
        return new RectangleImage(this.position, 
                this.pipeWidth, this.pipeHeight, this.col);
    }
    
}
