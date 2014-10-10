/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game1;

import java.awt.Color;
import java.util.*;
import javalib.funworld.*;
import javalib.colors.*;
import javalib.worldcanvas.*;
import javalib.worldimages.*;

/**
 *
 * @author AmandaMa
 */
public class runBirdGame extends World {
    
    static final int screenWidth = 600;
    static final int screenHeight = 150;
   
    //WorldCanvas canvas = new WorldCanvas(screenWidth, screenHeight);
    
    static final int winNumber = 10;
    
    int level; 
    int frames;
    int birdsIn;
    int birdsOut;
    
    Pipe pipe;
    ArrayList<Bird> flock;
    
    boolean gameOver;
    
    
    public runBirdGame(){
        this(1);
    }
    
    public runBirdGame(int level){
        super();
        this.pipe = new Pipe (screenWidth, screenHeight);
        this.level = level; 
        this.flock = new ArrayList();
        this.frames = 0;
        this.birdsIn = 0;
        this.birdsOut = 0;
        this.gameOver = false;
    }
    
    private runBirdGame(Pipe pipe, ArrayList<Bird> flock, int level, int frames,
            int birdsIn, int birdsOut, boolean gameOver){
        super();
        this.pipe = pipe;
        this.flock = flock;
        this.level = level;
        this.frames = frames;
        this.birdsIn = birdsIn;
        this.birdsOut = birdsOut;
        this.gameOver = gameOver;
        
    }
            
    
    public World onKeyEvent (String ke) {
        if (ke.equals("up") || (ke.equals("down")))
            return new runBirdGame(pipe.movePipe(ke).pipeOutOfBounds(), flock, level, frames,
            birdsIn, birdsOut, gameOver);
        else 
            return this;
        
    }
    
    public WorldEnd worldEnds(){
            if(gameOver){
                return new WorldEnd(true, new OverlayImages(this.makeImage(),
                    new TextImage(new Posn(screenWidth/2,screenHeight/2), 
                            ("Game Over: You've reached level " + this.level),
                            20, new White())));
            }
            else return new WorldEnd(false, this.makeImage());
        }
    
    
  
    
    public World onTick(){
        ArrayList<Bird> newFlock = new ArrayList();        
        int newBirdsIn = this.birdsIn;
        int newBirdsOut = this.birdsOut;
        
        
        //copies all the information in the flock to the new flock 
        for (int i = 0; i < flock.size(); i++) {            
           
            Bird newBird = flock.get(i).moveBird();                        
            
            int pipeTouch = pipe.touchedBird(newBird);            
            
            if (pipeTouch == 1) {
                // bird isn't in newFlock and a bird has made it through safely
                newBirdsOut += 1;
            } else if (pipeTouch == -1) {
                //Bird hasn't touched the screen 
                gameOver = true;
            } else {                
                // bird is in newFlock
                newFlock.add(newBird);
            }     
        }
        
        //adds a bird every 20 frames 
        if (this.frames % (30 - level * 3) == 1 ){
            if (newBirdsIn < winNumber){
                newFlock.add(new Bird(screenWidth, screenHeight, level));
               newBirdsIn += 1; 
            }
        }
        
        
        //if there have been 15 birds that have entered an left the screen 
        //then it returns a new game with the level increased by 1
        if (newBirdsIn == winNumber && newBirdsOut == winNumber)
            return new runBirdGame(level+1);
        else
            return new runBirdGame(pipe, newFlock, level, frames+1, newBirdsIn, newBirdsOut, gameOver);
    }
    
    private WorldImage background(){
        return new RectangleImage(new Posn(screenWidth/2, screenHeight/2),
                            screenWidth, screenHeight, new Black());
    }
    /*
    private WorldImage wallImage(){
        return new RectangleImage(new Posn(screenWidth - (pipe.pipeWidth/2), screenHeight/2),
                            screenWidth, screenHeight, new Orange());
    }*/
    
    private WorldImage birdImage(ArrayList<Bird> flock, int counter){
        if (flock.isEmpty())
            return background();
        else if (counter == -1)
            return background();
        else 
            return (new OverlayImages (birdImage(flock, counter - 1),flock.get(counter).birdImage()));
            
    }
    
    public WorldImage scoreImage(){
        return new TextImage(new Posn(65, 15), 
                            ("Level: " + this.level + "  Saved Birds: " + this.birdsOut ),
                            10, new White());
    }
    
    public WorldImage makeImage(){
       return new OverlayImages((birdImage(flock, flock.size() - 1)),
               new OverlayImages(pipe.pipeImage() , scoreImage()));
    }
    
    
    public static void main( String[] args ) {
        runBirdGame game = new runBirdGame();
        
        game.bigBang(600, 150, 0.15);
    }
    
}