package juego;
// WorldItems.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* WorldItems maintains a list of TileOccupier objects ordered by tile row.
   When the objects are drawn, the correct z-ordering is
   enforced, so objects in the 'foreground' are drawn in
   front of those further back.

   A TileOccupier can be a block, pickup, or sprite.

   A pickups is deleted from WorldItems when it is picked up by 
   the player.

   Sprites are added temporarily while drawing is carried out, then
   removed afterwards. This is because sprites move around, changing
   their tile position, which will affect where they are placed in
   the list.
*/

import java.awt.*;
import java.util.*;
import java.awt.image.*;


public class ItemsMapa
{
	
  private int tileWidth, tileHeight; //ancho de píxel / altura de un tile (incluyendo cualquier espacio al lado del azulejo)
  private int evenRowX, evenRowY; // Comienzo de la primer fila. Empiezo desde arriba a la izquierda.
  private int oddRowX, oddRowY; // Inicio de la primer fila IMPAR de coordenadas.

  private ArrayList items; //ArrayList de items. Este ArrayList va a contener objetos de tipo TileOccupier.
  
  public ItemsMapa(int w, int h, int erX, int erY,
                                  int orX, int orY)
  { tileWidth = w; tileHeight = h;
    evenRowX = erX; evenRowY = erY;
    oddRowX = orX; oddRowY = orY;
    items = new ArrayList();
  }

  public void agregarItem(String name, int type, int x, int y, BufferedImage im)
   /*  BASICAMENTE LO QUE NOS DICE ACÁ ES QUE CUANDO CREAMOS UN TILEOCCUPIER VAMOS A TENER LAS 
     COORDENADAS DE DONDE VA A ESTAR UN OBSTACULO Y VAMOS A PODER GRAFICARLO CON JPANEL.
  */
  {
    TileOcupado toc; //Le creo un objeto de Tile Ocupado.
    if (y%2 == 0) // even row (Fila incluida.. Esto lo hace por si es par. Lleva filas y columnas PARES e IMPARES
      toc = new TileOcupado(name, type, x, y, im, 
                                    evenRowX, evenRowY,
                                    tileWidth, tileHeight);
    else
      toc = new TileOcupado(name, type, x, y, im, //La FILA Y COLUMNA es impar.
                                    oddRowX, oddRowY,
                                    tileWidth, tileHeight);
    rowInsert(toc, x, y);
  } 


  private void rowInsert(TileOcupado toc, int x, int y) //Inserta los elementos en el mapa. Los elementos a almacenar de manera secuencial.
  {
    TileOcupado item;
    Point itemPt; //Es un punto con coordenadas X e Y.
    int i = 0;
    while(i < items.size()) {
      item = (TileOcupado) items.get(i);
      itemPt = item.getTileLoc();
      if (y < itemPt.y)   
        break;
      else if ((y == itemPt.y) && (x < itemPt.x))
        break;
      i++;
    }
    items.add(i, toc); //ArraysList de Tiles Ocupados.
  }

  public void dibujar(Graphics g, int xOffset, int yOffset)
  
  /* Dibujar cada elemento. Dado que los artículos están en orden consecutivo, se
   * dibujaran en orden
   */
  {
    TileOcupado item;
    for(int i = 0; i < items.size(); i++) {
      item = (TileOcupado) items.get(i); //Voy trayendo los items que tengo en la lista
      item.dibujar(g, xOffset, yOffset);    // Grafico y desplazo
    }
  }



  //         Sprites en el MAPA

  public void posicionSprites(JugadorSprite ps, EnemigoSprite[] aliens) //Posiciono Sprites en el Mapa
  {
    posnSprite("bob", ps);    // Creo un Personaje que se llama bob y le doy el sprite correspondiente.
    for(int i = 0; i < aliens.length; i++) //Crea aliens!
      posnSprite("alien "+i, aliens[i]);
  }


  private void posnSprite(String name, TilesSprite tSprite)
  {
    Point sPt = tSprite.getTileLoc();

    TileOcupado toc;
    if (sPt.y%2 == 0) // even row
      toc = new TileOcupado(name, DesarrolloMapa.SPRITE, 
                                    sPt.x, sPt.y, tSprite.getImage(),
                                    evenRowX, evenRowY,
                                    tileWidth, tileHeight);
    else
      toc = new TileOcupado(name, DesarrolloMapa.SPRITE, 
                                    sPt.x, sPt.y, tSprite.getImage(), 
                                    oddRowX, oddRowY,
                                    tileWidth, tileHeight);

    toc.addSpriteRef(tSprite); 
    rowInsert(toc, sPt.x, sPt.y);
  }


  public void removerSprites() //Remueve Sprites
  {
    TileOcupado item;
    int i = 0;
    while(i < items.size()) {
      item = (TileOcupado) items.get(i);
      if (item.getType() == DesarrolloMapa.SPRITE)
        items.remove(i);
      else
        i++;
    }
  }



  // -------------------------- pickup related ------------------


  public String findPickupName(Point pt) //Si el punto PT posee un ITEM devuelvo el nombre sino NULL.
  {
    TileOcupado item;
    for(int i=0; i < items.size(); i++) {
      item = (TileOcupado) items.get(i);
      if ((item.getType() == DesarrolloMapa.PICKUP) && 
          (pt.equals( item.getTileLoc())))    //si lo que hay en el tile es un item y es igual a la locación lo devuelvo sino NULL
        return item.getName();
    }
    return null;
  }


  public boolean removePickup(String name)
  // Attempt to delete the named pickup
  {
    TileOcupado item;
    for(int i=0; i < items.size(); i++) {
      item = (TileOcupado) items.get(i);
      if ((item.getType() == DesarrolloMapa.PICKUP) && 
          (name.equals(item.getName()))) {    // find the named pickup
        items.remove(i);
        return true;
      }
    }
    return false;
  }  // end of removePickup()


}

