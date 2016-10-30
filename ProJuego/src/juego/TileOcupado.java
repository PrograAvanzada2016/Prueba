package juego;
// TileOccupier.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A tile occupier can be a block, pickup, or a sprite,
   which is indicated by its type.

   A tile occupier has a unique name, a tile coordinate
   (xTile, yTile), and a coordinate relative to
   the top-left corner of the floor image (xDraw,yDraw)
   where it should be drawn.

   Drawing involves adding an offset to the occupier's floor
   coordinate since the floor will have moved as the player
   apparently moves. 

   A sprite draws itself, and must be positioned in
   the JPanel first.
*/

import java.awt.*;
import java.awt.image.*;


public class TileOcupado
{
  private String nombre; 
  private int tipo;      // BLOCK, PICKUP, or SPRITE
  private BufferedImage imagen;
  private int xTile, yTile;    // Corrdenadas del Tile
  private int xDraw, yDraw;    //Coordenadas relativas al piso.

  private TilesSprite sprite = null; //Se utilizara solamente cuando el Tile ocupado sea un SPRITE.


  public TileOcupado(String n, int t, int x, int y, BufferedImage im,
                          int comienzoFilaX, int comienzoFilaY, 
                          int xTileAncho, int yTileLargo)
  { nombre = n; 
    tipo = t;
    xTile = x; yTile = y;
    imagen = im;
    calcularPosicion(comienzoFilaX, comienzoFilaY, xTileAncho, yTileLargo);
  } 



  private void calcularPosicion(int comienzoFilaX, int comienzoFilaY, 
                          int xTileAncho, int yTileLargo) //Calcula las coordenadas (xDraw,yDraw) con relacion al PISO. NO LO ENTIENDO!

  { // Esquina superior izquierda de la imagen en relación con su mosaico
    int xImgDesplazamiento = xTileAncho/2 - imagen.getWidth()/2;    // in the middle
    int yImgDesplazamiento = yTileLargo - imagen.getHeight() - yTileLargo/5;
                   // up a little from bottom point of the diamond

    // top-left corner of image relative to floor image
    xDraw = comienzoFilaX + (xTile * xTileAncho) + xImgDesplazamiento;
    if (yTile%2 == 0)    // on an even row
      yDraw = comienzoFilaY + (yTile/2 * yTileLargo) + yImgDesplazamiento;
    else       // on an odd row
      yDraw = comienzoFilaY + ((yTile-1)/2 * yTileLargo) + yImgDesplazamiento;
  } // end of calcularPosicion()


  public String getNombre()
  { return nombre; }

  public int getTipo()
  {  return tipo; }


  public Point getPosicionTile()
  {  return new Point(xTile, yTile);  }


  public void agregarSprite(TilesSprite s) //Asigna el Sprite!
  { if (tipo == DesarrolloMapa.SPRITE)
      sprite = s;
  }


  public void dibujar(Graphics g, int xDesplazamiento, int yDesplazamiento)
  { if (tipo == DesarrolloMapa.SPRITE) {
      sprite.setPosicion( xDraw+xDesplazamiento, yDraw+yDesplazamiento);  // Posiciona en JPanel
      sprite.drawSprite(g);   //  dibujo sprite.
    }
    else      // Sino la entidad es un OBSTACULO.
      g.drawImage( imagen, xDraw+xDesplazamiento, yDraw+yDesplazamiento, null);
  }
  
}