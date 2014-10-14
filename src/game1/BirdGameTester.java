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
public class BirdGameTester {
    
    
    static int testStart = 0;
    static int testPipe = 0;
    static Random rand = new Random();
    
    public static int randomGen(int start, int end){
        return (rand.nextInt((end - start) + 1) + start);
    }
    
    //tests the beginning of each level (which is the same start as the first level)
    public static void testStart() throws Exception {
        
        RunBirdGame game = new RunBirdGame(randomGen(1, 10));
        
        //checks if the pipe in the right position
        if (game.pipe.position != new Posn(game.screenWidth - game.pipe.pipeWidth/2,
                                          game.screenHeight/2)) {
            throw new Exception("The pipe isn't starting in the center"); 
        }
        
        //checks if the flock is empty - it should be 
        if (!game.flock.isEmpty()) {
            throw new Exception("The flock should be empty when the game starts");
        }
        
        //checks that the boolean value gameOver is false 
        if (!game.gameOver){
            throw new Exception("The game shouldn't be over at the start of a level");
        }
        
        testStart++;
        
    }
    
    //tests the up and down movement of the pipe
    public static void testPipe(String key) throws Exception {
        
        //creates a game which can be used to initalized a new game with the 
        //only change being a new pipe of random height 
        RunBirdGame preGame = new RunBirdGame();
        
        RunBirdGame game = new RunBirdGame(new Pipe(new Posn(preGame.pipe.position.x, 
                randomGen(0, preGame.screenHeight)), preGame.screenHeight, preGame.screenWidth),
                preGame.flock, preGame.level, preGame.frames, 
                preGame.birdsIn, preGame.birdsOut, preGame.gameOver);
        
        //the highest and lowest y coordinates the pipe can be at  
        int highY= game.pipe.pipeHeight/2;
        int lowY = game.screenHeight - (game.pipe.pipeHeight/2);
        
        //the highest distance the pipe will ever move
        int buffer = game.pipe.movePipeRate;
        
        //the coordinates before the key is pressed
        int beforeX = game.pipe.position.x;
        int beforeY = game.pipe.position.y;
        
        //the event
        game.onKeyEvent(key);
        
        //the coordinates after the key is pressed 
        int afterX = game.pipe.position.x;
        int afterY = game.pipe.position.y;
        
        if(key.equals("up")) {
            if (afterX != beforeX) {
                throw new Exception ("Your X is not supposed to change"); }
            if (beforeY < buffer){
                if (afterY != highY) {
                    throw new Exception ("Your pipe went above the screen, "
                            + "it should be at the highest position"); }
                else if (afterY != beforeY - buffer)
                    throw new Exception ("Your pipe didn't actually move the "
                    + "correct number of pixels up"); }
        }
        
        if(key.equals("down")) {
            if (afterX != beforeX) {
                throw new Exception ("Your X is not supposed to change"); }
            if (beforeY > game.screenHeight - buffer){
                if (afterY != lowY) {
                    throw new Exception ("Your pipe went below the screen, "
                            + "it should be at the lowest position"); }
                else if (afterY != beforeY + buffer)
                    throw new Exception ("Your pipe didn't actually move the "
                    + "correct number of pixels down"); }
        }
        
        testPipe++;
    }
    
    
    public static void main(String[] args){
        
    }
    
}
