package juego;


// TiledSprite.java ESTA CLASE SOLO SE UTILIZARA PARA SPRITES QUE SE MUEVAN EN LAS POSICIONES XTILE E YTILE.
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A TiledSprite object moves from one tile to another.
   Movement is limited to four compass directions (quadrants):
   NE, SE, SW, NW, and STILL for no movement.

   TiledSprite extends Sprite but does not use its (locx,locy)
   pixel coordinates. Instead it maintains its own current
   tile position (xTile, yTile).

   This approach means that a TiledSprite cannot move around 
   inside a tile, only move between tiles.

   The sprite's pixel position (i.e. its position in the JPanel)
   is updated by WorldDisplay just prior to the game being drawn
   in the JPanel. 
*/

import java.awt.*;


public class TilesSprite extends Sprite{
  // Las direcciones del sprite a moverse.
  public final static int NE = 0; //Noreste
  public final static int SE = 1; //Sudeste
  public final static int SO = 2; //Sudoeste
  public final static int NO = 3; //Noroeste
  public final static int QUIETO = 4; //Quieto

  public final static int NUM_DIRS = 4; //Numero de direcciones

  protected int xTile, yTile;    // Coordenadas del sprite
  protected DesarrolloMapa mapa;


  public TilesSprite(int x, int y, int w, int h,
                           CargaImagenes img, String nombre,
		                   DesarrolloMapa m)
  { super(0, 0, w, h, img, nombre); // pone en 0 la locación del Sprite. W y H son las dimensiones del panel
    setPosicion(0, 0);      // sin movimiento.
    mapa = m;

    if (!mapa.validarPosicionTile(x, y)) {  // Le pregunta al mundo si el Tile (X,Y) es valido
      System.out.println("La posición del Enemigo(" + x + "," + y +
                                        ") no es valida. Se setea por defecto(0,0)");
      x = 0; y = 0;
    }
    xTile = x; yTile = y; //Caso contrario, si es válido (X,Y) lo seteo.
  } 


  public void setPosicionTile(Point pt) //Setea locación del Tile dentro del mapa.
  { xTile = pt.x;
    yTile = pt.y;  
  }

  public Point getPosicionTile() //Nos da la posición del TILE
  {  return new Point(xTile, yTile);  }


  // METODOS DE MOVIMIENTO

  public Point tryMove(int quad) //Intenta moverse a la nueva posición
  {
    Point siguientePunto;
    if (quad == NE) //HAY QUE VER BIEN ESTO. No se entiende mucho.
      siguientePunto = (yTile%2 == 0)? new Point(xTile,yTile-1) : new Point(xTile+1,yTile-1);
    else if (quad == SE)
      siguientePunto = (yTile%2 == 0)? new Point(xTile,yTile+1) : new Point(xTile+1,yTile+1);
    else if (quad == SO)
      siguientePunto = (yTile%2 == 0)? new Point(xTile-1,yTile+1) : new Point(xTile,yTile+1);
    else if (quad == NO)
      siguientePunto = (yTile%2 == 0)? new Point(xTile-1,yTile-1) : new Point(xTile,yTile-1);
    else
      return null;

    if (mapa.validarPosicionTile(siguientePunto.x, siguientePunto.y))  //Si es valida esta posición en el mapa retorno el nuevo punto.
      return siguientePunto;
    else
     return null; //No es valida la posición nueva en el mapa.
  } 

  

  public int getDireccionRandom() //Retorno una dirección Randomn.
  {   return (int)(NUM_DIRS * Math.random());  }


  public int getCuadrante(Point p) /*Relacionado con la posición del sprite del Tile. 
  Indica al cuadrante que tiene que ir para llegar al point P (Adyacente a este TILE)*/

  { // System.out.println("tile: " + xTile + ", " + yTile);
    // System.out.println("to: " + p.x + ", " + p.y);

    if ((xTile == p.x) && (yTile == p.y))
      return QUIETO;   // Esta en el Tile P. QUIETO!

    if (yTile%2 == 0) {    // El tile se encuentra en un punto PAR
      if ((xTile == p.x) && (yTile-1 == p.y))
        return NE;
      if ((xTile == p.x) && (yTile+1 == p.y))
        return SE;
      if ((xTile-1 == p.x) && (yTile+1 == p.y))
        return SO;
      if ((xTile-1 == p.x) && (yTile-1 == p.y))
        return NO;
    }
    else {    // El tile se encuentra en un punto IMPAR
      if ((xTile+1 == p.x) && (yTile-1 == p.y))
        return NE;
      if ((xTile+1 == p.x) && (yTile+1 == p.y))
        return SE;
      if ((xTile == p.x) && (yTile+1 == p.y))
        return SO;
      if ((xTile == p.x) && (yTile-1 == p.y))
        return NO;
    }
    return -1;
  } 

} 