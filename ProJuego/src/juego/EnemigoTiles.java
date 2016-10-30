package juego;
// AlienTiles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* AlienTiles is a basic isometric game consisting of one player
   who must pick up 3 items (a cup, flower pot, and a watch)
   before 4 nasty aliens have hit him 3 times.

   The player is moved using the number pad keys: 7, 9, 3, 1, 5,
   and 2, as explained in the help screen.

   The aliens are of two types: those that actively chase after
   the player (AlienAStarSprite objects) and those that congregate
   around the pickup that the player is heading towards 
   (AlienQuadSprite objects). 

   The AlienAStarSprite class uses A* pathfinding to chase the player.

   The playing area is an isometric map containing 'no-go' tiles
   which the sprites cannot move on to. Some tiles 
   contain 'blocks' which also stop the sprites.

   When a sprite moves behind a block, the sprite is suitably obscured.

   There are various sound effects, and background music.

   The Player sprite does not actually move relative to the game's
   JPanel. Instead the tile map, pickups, blocks, and aliens are 
   repositioned. 

   The tile map graphic is a single GIF, not multiple tile images.

   -----
   Pausing/Resuming/Quiting are controlled via the frame's window
   listener methods.

   Active rendering is used to update the JPanel. See WormP for
   another example, with additional statistics generation.

   Using Java 3D's timer: J3DTimer.getValue()
     *  nanosecs rather than millisecs for the period

   The MidisLoader, ClipsLoader, and ImagesLoader
   classes are used for music and image manipulation.

   The player and tyhe alien sprites are subclasses of the 
   Sprite class discussed in chapter 6.
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class EnemigoTiles extends JFrame implements WindowListener
{
  private static int DEFAULT_FPS = 40; 

  private Tablero tab; //Donde se dibuja el juego

  public EnemigoTiles(long periodo)
  { super("EnemigoTiles");

    Container c = getContentPane();    // BorderLayout que utiliza.
    tab = new Tablero(this, periodo);
    c.add(tab, "Centro");

    addWindowListener( this );
    pack();
    setResizable(false);
    setVisible(true);
  }


  // ----------------- window listener methods -------------

  public void windowDeactivated(WindowEvent e) 
  { tab.pauseGame();  }

  public void windowIconified(WindowEvent e) 
  {  tab.pauseGame(); }


  public void windowClosing(WindowEvent e)
  {  tab.stopGame();  
  }

  public void windowClosed(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

  // ----------------------------------------------------

  public static void main(String args[])
  { 
    long periodo = (long) 1000.0/DEFAULT_FPS;
    // System.out.println("fps: " + DEFAULT_FPS + "; periodo: " + periodo + " ms");
    new EnemigoTiles(periodo*1000000L);    // ms --> nanosecs 
  }

}


