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
    static int testBirdScreen = 0;
    static int testMoveDelete = 0;
    static int testUpLevel = 0;
    static int testBirdsIn = 0;
    
    static Random rand = new Random();
    
    public static int randomGen(int start, int end){
        return (rand.nextInt((end - start) + 1) + start);
    }
    
    //tests the beginning of each level (which is the same start as the first level)
    public static void testStart() throws Exception {
        
        RunBirdGame game = new RunBirdGame(randomGen(1, 100));
        
        //checks if the flock is empty - it should be 
        if (!game.flock.isEmpty()) {
            throw new Exception("The flock should be empty when the game starts");
        }
        
        //checks that the boolean value gameOver is false 
        if (game.gameOver){
            throw new Exception("The game shouldn't be over at the start of a level");
        }
        
        //checks BirdsIn
        if (game.birdsIn != 0){
            throw new Exception("BirdsIn should start at 0");
        }
        
        //checks BirdsOut 
        if (game.birdsOut != 0){
            throw new Exception("BirdsOut should start at 0");
        }
        
        //checks frames 
        if (game.frames != 0){
            throw new Exception("Frames should start at 0");
        }
        
        //checks that the pipe is center right
        if (game.pipe.position.equals(new Posn(game.screenWidth - game.pipe.pipeWidth/2,
                                          game.screenHeight/2))) {
            throw new Exception("The pipe isn't starting in the center"); 
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
        
        //the largest distance the pipe will ever move
        int moveDistance = game.pipe.movePipeRate;
        
        //the coordinates before the key is pressed
        int beforeX = game.pipe.position.x;
        int beforeY = game.pipe.position.y;
        
        //the event
        RunBirdGame newGame = game.onKeyEvent(key);
        
        //the coordinates after the key is pressed 
        int afterX = newGame.pipe.position.x;
        int afterY = newGame.pipe.position.y;
        
        
        if (key.equals("up")){
            if (afterX != beforeX) {
                throw new Exception ("Your X is not supposed to change"); }
            
            if (beforeY < highY + moveDistance){
                if (afterY != highY) { 
                    throw new Exception ("Your pipe went above the screen, "
                            + "it should be at the highest position - up"); 
                }
            }
            
            if (beforeY - moveDistance > lowY){
                if (afterY != lowY) {
                    System.out.println(beforeY);
                    System.out.println(afterY);
                    throw new Exception ("Your pipe was below the screen, "
                            + "it should be at the lowest position - up"); 
                }
            }
            
            if ((beforeY >= highY + moveDistance) && (beforeY - moveDistance <= lowY)) {
                if (afterY != (beforeY - moveDistance)) {
                    System.out.println(beforeY);
                    System.out.println(afterY);
                    throw new Exception ("Your pipe didn't actually move the "
                  + "correct number of pixels up - up"); }
            }
        }
        else if (key.equals("down")){
            if (afterX != beforeX) {
                throw new Exception ("Your X is not supposed to change"); }
            
            if (beforeY > lowY - moveDistance){
                if (afterY != lowY) {
                    throw new Exception ("Your pipe went below the screen, "
                            + "it should be at the lowest position - down"); 
                }
            }
            
            if (beforeY + moveDistance < highY){
                if (afterY != highY) {
                    throw new Exception ("Your pipe was above the screen, "
                            + "it should be at the highest position - down"); 
                }
            }
            
            if ((beforeY <= highY - moveDistance) && (beforeY + moveDistance >= lowY)) {
                if (afterY != (beforeY + moveDistance)) {
                    System.out.println(beforeY);
                    System.out.println(afterY);
                    throw new Exception ("Your pipe didn't actually move the "
                  + "correct number of pixels down - down"); }
            }
        }
        else {
            if (beforeY != afterY || beforeX != afterX)
                throw new Exception ("Something happened, but it shouldn't have");
        }
        
        testPipe++;
    }
    
    //tests that a bird is always created on the screen and that the 
    //entire bird will be on screen 
    public static void testBirdScreen() throws Exception {
        
        RunBirdGame preGame = new RunBirdGame();
        
        ArrayList<Bird> flock = preGame.flock;
        
        flock.add(new Bird(preGame.screenWidth, preGame.screenHeight, preGame.level));
        
        RunBirdGame game = new RunBirdGame(preGame.pipe, flock, preGame.level, preGame.frames,
            preGame.birdsIn, preGame.birdsOut, preGame.gameOver);
        
        int positionY = game.flock.get(0).position.y;
        int buffer = game.flock.get(0).diameter/2;
        
        //looks if the bird is within the y-range
        if (!((positionY >= buffer) && 
                (positionY <= game.screenHeight - buffer))){
            throw new Exception ("Your bird isn't on screen");
        }
        
        testBirdScreen++;
    }
    
    
    
    //tests whether the bird moves at the correct speed and whether it ends 
    //the game if it misses the pipe 
    public static void testMoveDelete() throws Exception {
        
        //creates a game to base the tested game from
        RunBirdGame preGame = new RunBirdGame(randomGen(1, 250));
        
        //creates a new pipe that is off screen so that any bird crossing 
        //the screen will result in a gameOver
        Pipe pipe = new Pipe(new Posn(preGame.pipe.position.x, 
                preGame.screenHeight + preGame.pipe.pipeHeight), 
                preGame.screenHeight, preGame.screenWidth);
        
        //adds a bird to the flock
        ArrayList<Bird> flock = preGame.flock;
        flock.add(new Bird(preGame.screenWidth, preGame.screenHeight, preGame.level));
        
        //creates a new game to run the test with
        RunBirdGame game = new RunBirdGame(pipe, flock, preGame.level, preGame.frames,
            preGame.birdsIn, preGame.birdsOut, preGame.gameOver);
        
        //stores the x coordinate of the bird before
        int beforeX = game.flock.get(0).position.x;
        
        //the action 
        RunBirdGame afterTick = game.onTick();
        
        
        if (beforeX > (game.screenWidth 
                - game.pipe.pipeWidth 
                - game.flock.get(0).rate
                - game.flock.get(0).diameter/2)) {
            if (!afterTick.gameOver){
                throw new Exception ("The bird went past the pipe and didn't touch it" + 
                        " , the game should be over");}
        }
        else {
            if (game.flock.get(0).position.y != afterTick.flock.get(0).position.y){
                throw new Exception ("The bird should not move up or down ");
            }
            
            if ((beforeX + afterTick.flock.get(0).rate) != (afterTick.flock.get(0).position.x)){
                
                throw new Exception ("The bird didn't move as much as it should have");
            }
        }
        testMoveDelete++;
    }
       
    
    //testing whether you can advance the level which also makes it so that 
    //we know birdsOut is adding correctly 
    public static void testUpLevel() throws Exception {
         
        RunBirdGame preGame = new RunBirdGame(randomGen(1, 100));
        
        ArrayList<Bird> flock = preGame.flock;
        flock.add(new Bird
            (new Posn(preGame.screenWidth - preGame.pipe.pipeWidth - 1, preGame.screenHeight/2),
            preGame.screenWidth, preGame.screenHeight, preGame.level));
        
        RunBirdGame game = new RunBirdGame(preGame.pipe, flock, preGame.level, preGame.frames,
            preGame.winNumber, preGame.winNumber - 1, preGame.gameOver);
        
        RunBirdGame afterTick = game.onTick();
        
        if (afterTick.level != game.level + 1){
            throw new Exception ("You aren't advancing the level properly");
        }
        testUpLevel++;
    }
    
    //testing that birdsIn is adding correctly
    public static void testBirdsIn() throws Exception {

          RunBirdGame preGame = new RunBirdGame();

          RunBirdGame game = new RunBirdGame(preGame.pipe, preGame.flock, 
                    preGame.level, preGame.frames, randomGen(0,20), 1, preGame.gameOver);

          RunBirdGame afterTick = game.onTick().onTick().onTick();

          if (game.birdsIn > game.winNumber - 1){
            if (afterTick.birdsIn != game.birdsIn){
                throw new Exception ("Your birds shouldn't be adding again");
            }
          }
          else {    
            if (afterTick.birdsIn != game.birdsIn + 1){
                throw new Exception ("Your birds aren't adding correctly");
            }
          }
          testBirdsIn++;
      }

    
    public static void main(String[] args) throws Exception {
        BirdGameTester tests = new BirdGameTester();
        
        for (int i = 0; i < 1000; i++){
            BirdGameTester.testStart();
            BirdGameTester.testPipe("up");
            BirdGameTester.testPipe("down");
            BirdGameTester.testPipe(" ");
            BirdGameTester.testBirdScreen();
            BirdGameTester.testMoveDelete();
            BirdGameTester.testUpLevel();
            BirdGameTester.testBirdsIn();
        }
        
        System.out.println("testStart ran sucessfully " + tests.testStart + (" times"));
        System.out.println("testPipe ran sucessfully " + tests.testPipe + (" times"));
        System.out.println("testBirdScreen ran sucessfully " + tests.testBirdScreen + (" times"));
        System.out.println("testMoveDelete ran sucessfully " + tests.testMoveDelete + (" times"));
        System.out.println("testUpLevel ran sucessfully " + tests.testUpLevel + (" times"));
        System.out.println("testBirdsIn ran sucessfully " + tests.testBirdsIn + (" times"));
        
            
        
    }
    
}
