package juego;
// PlayerSprite.java ESTA CLASE REPRESENTA EL SPRITE DEL PLAYER.
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Represents a player as a subclass of TiledSprite.

   A player can pick things up. When it has picked up 
   everything, it has 'won'.

   If a player is 'hit' by an alien, it loses a life.
   When all of its lives are gone, the palyer has 'lost'.

   The player communicates with the WorldDisplay object 
   to find out about the world (e.g. if there is something
   to pick up on the current tile).

   Sound effects are attached to:
     * picking something up
     * failing to pick something up
     * being hit by an alien
     * hitting a no-go tile or block
*/

import java.awt.*;
import java.util.*;


public class JugadorSprite extends TilesSprite
{

  private Tablero tab;

  public JugadorSprite(int x, int y, int w, int h, CargaImagenes img, 
                                DesarrolloMapa mapa, Tablero tablero)
  { super(x, y, w, h, img, "still", mapa);
    tab = tablero;
  }


  public boolean tryPickup() //El usuario quiere agarrar un ITEM de la posición actual. NO SABEMOS SI LO VAMOS A USAR.
  {
    String pickupName;
    // System.out.println("pickup: " + getTileLoc() );
    if ((pickupName = world.overPickup( getTileLoc())) == null) {
      cargaSonidos.play("noPickup", false);     // Si no hay nada en ese Tile llama al sonido NOPICKUP.
      return false;
    }
    else {     // Encontró un ITEM!
      cargaSonidos.play("gotPickup", false); //Suena la canción que encontró un item
      world.removePickup(pickupName); //Indicarle al mapa que elimine un ITEM.
      return true;
    }
  }


  // METODOS RELACIONADOS AL ATAQUE DE UN MOUNSTRO. TAMPOCO SABEMOS SI LO VAMOS A USAR.


  public void hitByAlien() //Jugador golpeado por Mounstro.
  { cargaSonidos.play("hit", false);
    hitCount++;
    if (hitCount == MAX_HITS) // Si el HitCount es 3. Finaliza el juego ya que el player murio.
      tab.gameOver();
  }

  public String getHitStatus() //Estado del jugador.
  {  
    int livesLeft = MAX_HITS-hitCount; //estado de vidas al momento
    if (livesLeft <= 0)
      return "You're D*E*A*D"; 
    else if (livesLeft == 1)
      return "1 life left";
    else
      return  "" + livesLeft + " lives left";  
  }



  // 			METODOS DE MOVIMIENTO.

  public void mover(int cuadrante) //Este metodo actualiza el tile donde se encuentra el objeto cuando se mueve.
  {
    Point nuevoPunto = tryMove(cuadrante);
    if (nuevoPunto == null) {   // No se puede mover a esa posición.
      quedaQuieto(); //Le setea la imagen de Sprite QUIETO.
    }
    else {    // Movimiento posible
      setPosicionTile(nuevoPunto);    // Actualizo la locación del tiles donde esta el sprite 
      if (cuadrante == NE) 
        setImagen("ne"); //Voy asignandole las imagenes del jugador moviendose en NE
      else if (cuadrante == SE)
        setImagen("se"); //jugador moviendose en SE.. Etc.
      else if (cuadrante == SO)
        setImagen("sw");
      else // cuadrante == NW
        setImagen("nw");
    }
  } 


  public void quedaQuieto() //Seteo de imagen de sprite quieto.
  {  setImagen("still");    }

}