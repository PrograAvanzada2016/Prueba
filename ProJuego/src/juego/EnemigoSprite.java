package juego;
// AlienSprite.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Represents an alien as a subclass of TiledSprite.

   A subclass of AlienSprite will overide:
      void playerHasMoved()   -- called when the player has moved
	  void move()             -- move the alien
*/

import java.awt.*;


public class EnemigoSprite extends TilesSprite
{
  private final static int UPDATE_FREQ = 30;

  private int actualizarContador = 0;


  public EnemigoSprite(int x, int y, int w, int h, CargaImagenes img, DesarrolloMapa mapa)
  { super(x, y, w, h, img, "baddieStill", mapa);  } //BadieStill hace referencia al SPRITE del Alien QUIETO.


  public void playerHasMoved(Point playerLoc) /* Le avisa a los aliens que el jugador se movio.
   (El desarrollo esta en AlienQuadSprite)*/
  {  }

//Este método no entiendo muy bien que es lo que hace!
  public void actualizar() //Actualiza a los Aliens. Intentan golpear al jugador. Si el jugador no puede ser golpeado se mueve
  /* Called by AlienTilesPanel to update the alien.

     Try to hit the player. If the player cannot be hit then move.

     The alien will only be updated every UPDATE_FREQ calls to update()
     thereby slowing it down, otherwise it responds to quickly to
     the player's movements.
  */
  { actualizarContador = (actualizarContador+1)%UPDATE_FREQ;
    if (actualizarContador == 0) {   // reduced update frequency
      if (!hitPlayer()) 
        movimientoRandom();
    }
  }

/*        LO COMENTO YA QUE NO SE SI SE VA A UTILIZAR 

  private boolean hitPlayer() //Si el jugador se encuentra en el mismo Tile que el Alien será golpeado.
  {
    Point playerLoc = world.getPlayerLoc();
    if (playerLoc.equals( getTileLoc() )) { //¿No falta pasar el objeto del Alien?
      world.hitByAlien();   // Retorno TRUE ya que el jugador fue golpeado!
      return true;
    }
    return false;
  }
*/

  protected void movimientoRandom() //Movimiento Randomn. Se aplica cuando no puede hacer el movimiento que quiere hacer.
  {
    int cuadrante = getDireccionRandom();
    Point nuevoPunto;
    while ((nuevoPunto = tryMove(cuadrante)) == null)  //Si al intentar moverse devuelve NULL
      cuadrante = getDireccionRandom(); //Le asigno una posición Randomn.
      // the loop could repeat for a while, 
      // but it should eventually find a direction
    setMovimiento(nuevoPunto, cuadrante);
  }



  protected void setMovimiento(Point nuevoPunto, int cuadrante) /*Seteo nueva posición del movimiento al
   Alien y cambia el sprite dependiendo de la posición.*/
  
  { if (mapa.validarPosicionTile(nuevoPunto.x, nuevoPunto.y)) {   // Valido si el nuevo punto se encuentra dentro del mapa.
      setPosicionTile(nuevoPunto); //Si es así le seteo la locación dentro del mapa.
      if ((cuadrante == NE) || (cuadrante == SE)) //Dependiendo donde se mueva seteo sprites diferentes.
        setImagen("baddieRight");
      else if ((cuadrante == SO) || (cuadrante == NO))
        setImagen("baddieLeft");
      else 
        System.out.println("No se reconoce el cuadrante del Enemigo: " + cuadrante);
    }
    else
      System.out.println("El enemigo no puede moverse a " + nuevoPunto.x + ", " + nuevoPunto.y + ")");
  }


}
