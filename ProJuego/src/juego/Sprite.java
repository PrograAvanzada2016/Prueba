package juego;
// Sprite.java ACA VAMOS A DESCRIBIR VELOCIDAD, POSICIÓN, ETC. DE TODOS LOS SPRITES
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A Sprite has a position, velocity (in terms of steps),
   an image, and can be deactivated.

   The sprite's image is managed with an ImagesLoader object,
   and an ImagesPlayer object for looping.

   The images stored until the image 'name' can be looped
   through by calling loopImage(), which uses an
   ImagesPlayer object.

   ------------------
   ADDED: getImage()

*/

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;


public class Sprite 
{
  // Tamaños de pasos por defecto. (Velocidad del Sprite).
  private static final int pasosEnX = 5; 
  private static final int pasosEnY = 5;

  private static final int TAM = 12;   //TAM para cuando no hay imagen.

  // Todo lo necesario para cargar las imagenes.
  private CargaImagenes img;
  private String nombreImagen;
  private BufferedImage imagen;
  private int ancho, largo;     // Dimensiones de las imagenes de los sprites.

  private ImagenesJugador jugador;  // Trae todo el conjunto de imagenes de un sprite y le da funcionalidad.
  private boolean enMovimiento;

  private int pAncho, pLargo;   // Dimensiones del panel

  private boolean estaActivo = true; // Un sprite se actualiza y se dibuja sólo cuando está activo     

  // protected vars
  protected int posicionXSprite, posicionYSprite; //Locación del Sprite.
  protected int cantPasosX, cantPasosY;// Los pasos que se va a mover el Sprite.



  public Sprite(int x, int y, int w, int h, CargaImagenes imagen, String nombre) 
  { 
    posicionXSprite = x; posicionYSprite = y;
    pAncho = w; pLargo = h;
    cantPasosX = pasosEnX; cantPasosY = pasosEnY;

    img = imagen;
    setImagen(nombre);
  }


  public void setImagen(String nombre) //Setea una imagen al sprite.
  {
    nombreImagen = nombre;
    imagen = img.getImagen(nombreImagen);
    if (imagen == null) {
      System.out.println("No hay una imagen asociada para el sprite " + nombreImagen);
      ancho = TAM;
      largo = TAM;
    }
    else {
      ancho = imagen.getWidth();
      largo = imagen.getHeight();
    }
    jugador = null; // Como el jugador es NULL (O sea, no hay jugador)
    enMovimiento = false; // No va a haber movimiento (loop).
  }

  public BufferedImage getImagen() //Nuevo Aliens.
  { if (enMovimiento)
      return jugador.getImagenActual();
    else
      return imagen;
  } 

  public void imagenJugadorEnMovimiento(int periodoAnimacion, double duracionSecuencia) //Pone en LOOP a la imagen del jugador indicando que comenzó a jugar.
  {
    if (img.cantImagenes(nombreImagen) > 1) {
      jugador = null;   //Hacemos un clear del jugador anterior.
      jugador = new ImagenesJugador(nombreImagen, periodoAnimacion, duracionSecuencia,
                                       true, img);
      enMovimiento = true; //Comienza a jugar este jugador.
    }
    else
      System.out.println(nombreImagen + " No es una secuencia de imagenes");
  }


  public void jugadorParado() //Dejó e jugar el jugador, ya no esta activo. SIN LOOP.
  {
    if (enMovimiento) {
      jugador.parado();
      enMovimiento = false;
    }
  } 

  public int getAncho()    // of the sprite's image
  {  return ancho;  }

  public int getLargo()   // of the sprite's image
  {  return largo;  }

  public int getPAncho()   // of the enclosing panel
  {  return pAncho;  }

  public int getPLargo()  // of the enclosing panel
  {  return pLargo;  }


  public boolean estaActivo() 
  {  return estaActivo;  }

  public void setActivo(boolean a) 
  {  estaActivo = a;  }

  public void setPosicion(int x, int y)
  {  posicionXSprite = x; posicionYSprite = y;  }

  public void translate(int xDist, int yDist)
  {  posicionXSprite += xDist;  posicionYSprite += yDist;  }

  public int getXPosicionSprite()
  {  return posicionXSprite;  }

  public int getYPosicionSprite()
  {  return posicionYSprite;  }


  public void seteaPosicion(int cantPasosX, int cantPasosY) //Setea posición al sprite.
  {  this.cantPasosX = cantPasosX; this.cantPasosY = cantPasosY; }

  public int getPasosEnX()
  {  return cantPasosX;  }

  public int getPasosEnY()
  {  return cantPasosY;  }


  public Rectangle getRectangulo()
  {  return  new Rectangle(posicionXSprite, posicionYSprite, ancho, largo);  }


  public void actualizarSprite()
  // move the sprite
  {
    if (estaActivo()) {
      posicionXSprite += cantPasosX;
      posicionYSprite += cantPasosY;
      if (enMovimiento)
        jugador.updateTick();  // update the player
    }
  } // end of updateSprite()



  public void drawSprite(Graphics g) 
  {
    if (estaActivo()) { //¿Esta el sprite activo?
      if (imagen == null) {   // El sprite no tiene imagen
        g.setColor(Color.yellow);   // draw a yellow circle instead
        g.fillOval(posicionXSprite, posicionYSprite, TAM, TAM);
        g.setColor(Color.black);
      }
      else {
        if (enMovimiento) //Si esta en movimiento lo dibuja
          imagen = jugador.getImagenActual();
        g.drawImage(imagen, posicionXSprite, posicionYSprite, null); //Aca lo dibuja
      }
    }
  } 

} 
