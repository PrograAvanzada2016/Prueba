package juego;


// AlienQuadSprite.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A subclass of AlienSprite.
   It overrides:
      void playerHasMoved()   -- called when the player has moved
	  void move()             -- move the alien

   This alien calculates a quadrant direction for the sprite 
   (NE, SE, SW, NW). The calculation is repeated made whenever 
   the player moves.

   The direction is obtained by finding the nearest pickup
   point to the player, then finding that pickup's quadrant
   position relative to the alien.

   This gives the alien a 'pickup guarding' behaviour, where an
   alien moves to guard the pickup that the player is heading
   towards.
*/

import java.awt.*;


public class EnemigoCuadranteSprite extends EnemigoSprite
{
  // quadrant direction for the sprite
  private int cuadranteActual;
  

  public EnemigoCuadranteSprite(int x, int y, int w, int h, CargaImagenes img,
                                       DesarrolloMapa mapa)
  {  super(x, y, w, h, img, mapa); 
     cuadranteActual = getDireccionRandom();  // random starting quad direction
  } 

  private int calcularCuadrante(Point puntoItem) /* Aproximadamente calcula un cuadrante	comparando
  el punto del item con la posición del Mounstro*/
  {
    if ((puntoItem.x > xTile) && (puntoItem.y > yTile))
      return SE;
    else if ((puntoItem.x > xTile) && (puntoItem.y < yTile))
      return NE;
    else if ((puntoItem.x < xTile) && (puntoItem.y > yTile))
      return SO;
    else
      return NO;
   }

  protected void mover() /*Trata de moverse en la dirección de cuadranteActual.
  						Si el camino esta bloqueado elige otro randown*/
  
  { int cuadrante = cuadranteActual;
    Point nuevoPunto;
    while ((nuevoPunto = tryMove(cuadrante)) == null)
      cuadrante = getDireccionRandom(); 
      // the loop could repeat for a while, but it should eventually find a way
    setMovimiento(nuevoPunto, cuadrante);
  }


}
