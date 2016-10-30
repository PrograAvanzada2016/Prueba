package juego;
// ImagesPlayer.java //IMPORTANTE!!! Esta clase utiliza ImagesLoader (Tipo N/S/G para cargar las imagenes en tiras o grupos etc.)
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* ImagesPLayer is aimed at displaying the sequence of images
   making up a 'n', 's', or 'g' image file, as loaded by
   ImagesLoader.

   The ImagesPlayer constructor is supplied with the
   intended duration for showing the entire sequence
   (seqDuration). This is used to calculate showPeriod,
   the amount of time each image should be shown before
   the next image is displayed.

   The animation period (animPeriod) input argument states
   how often the ImagesPlayer's updateTick() method will be
   called. The intention is that updateTick() will be called periodically
   from the update() method in the top-level animation framework.

   The current animation time is calculated when updateTick()
   is called, and used to calculate imPosition, imPosition
   specifies which image should be returned when getCurrentImage() 
   is called.

   The ImagesPlayer can be set to cycle, stop, resume, or restart
   at a given image position.

   When the sequence finishes, a callback, sequenceEnded(), can
   be invoked in a specified object implementing the 
   ImagesPlayerWatcher interface.

*/

import java.awt.image.*;


public class ImagenesJugador
{
  private String nombreImagen;
  private boolean isRepeating, personajeNoFueCreado; //personajeNoFueCreado seria como una bandera que indica si se creo o no el personaje.
  private CargaImagenes img;

  private int periodoAnimacion; //Periodo de la animación.
  
  private long tiempoTotalAnimacion; //El tiempo total de la animacion.

  private int muestraPeriodo;// Periodo de la imagen actual. 
  
  private double duracionSecuencia; // Secuencia completa de duración. Se utiliza para calcular el periodo de la imagen actual

  private int cantImagenes;
  private int posicionImagen;     // position of current displayable image

  private ImagesPlayerWatcher watcher = null; //La variable Watcher nos indicara cuando un jugador llegue al final de su secuencia. VERRR!!


  public ImagenesJugador(String n, int a, double d, 
                                boolean isr, CargaImagenes imagen) 
  {
    nombreImagen = n;
    periodoAnimacion = a; 
    duracionSecuencia = d;
    isRepeating = isr;
    img = imagen;

    tiempoTotalAnimacion = 0L;

    if (duracionSecuencia < 0.5) {
      System.out.println("Prohibido: El mínimo de secuencia es 0.5");
      duracionSecuencia = 0.5;
    }

    if (!img.verificaCargaImagen(nombreImagen)) { //si o si el player debe tener una imagen asociada. Si no tiene se le asigna posición -1 y personajeNoFueCreado TRUE.
      System.out.println(nombreImagen + " no es reconocido por el ImagesLoader");
      cantImagenes = 0;
      posicionImagen = -1;
      personajeNoFueCreado = true; //Como no hay personaje que mostrar... Ponemos el personajeNoFueCreado en TRUE.
    }
    else {
      cantImagenes = img.cantImagenes(nombreImagen); //Si tiene imagen se le asigna posición 0 y personajeNoFueCreado FALSE.
      posicionImagen = 0;
      personajeNoFueCreado = false;
      muestraPeriodo = (int) (1000 * duracionSecuencia / cantImagenes); //Periodo en el que va a estar disponible este sprite.
    }
  } 

 //HAY QUE VERLO!!!!!! 
  
  public void updateTick()
  /* We assume that this method is called every periodoAnimacion ms */
  {
    if (!personajeNoFueCreado) {
      // update total animation time, modulo the animation sequence duration
      tiempoTotalAnimacion = (tiempoTotalAnimacion + periodoAnimacion) % (long)(1000 * duracionSecuencia);

      // calculate current displayable image position
      posicionImagen = (int) (tiempoTotalAnimacion / muestraPeriodo);   // in range 0 to num-1
      if ((posicionImagen == cantImagenes-1) && (!isRepeating)) {  // at end of sequence
        personajeNoFueCreado = true;   // stop at this image
        if (watcher != null)
          watcher.finalizoSecuencia(nombreImagen);   // call callback
      }
    }
  }  // end of updateTick()



  public BufferedImage getImagenActual()
  { if (cantImagenes != 0)
      return img.getImage(nombreImagen, posicionImagen); 
    else
      return null; 
  } // end of getImagenActual()


  public int getPosicionActual()
  {  return posicionImagen;  }



  public void setWatcher(ImagesPlayerWatcher w)
  {  watcher = w;  }
   

  public void parado() //Pone al jugador fuera del loop ( sin jugar).
  /* updateTick() calls will no longer update the
     total animation time or posicionImagen. */
  {  personajeNoFueCreado = true;  } //El personaje ya no existe más.


  public boolean estaParado()
  {  return personajeNoFueCreado;  }


  public boolean finalizaSecuencia()
  // are we at the last image and not cycling through them?
  {  return ((posicionImagen == cantImagenes-1) && (!isRepeating));  }



  public void restartPersonaje(int posicionActual)
  /* Start showing the images again, starting with image number
     imPosn. This requires a resetting of the animation time as 
     well. */
  {
    if (cantImagenes != 0) {
      if ((posicionActual < 0) || (posicionActual > cantImagenes-1)) {
        System.out.println("Reiniciando fuera de rango, comenzando en 0");
        posicionActual = 0;
      }

      posicionImagen = posicionActual;
      // calculate a suitable animation time
      tiempoTotalAnimacion = (long) posicionImagen * muestraPeriodo;
      personajeNoFueCreado = false;
    }
  }  // end of restartPersonaje()


  public void resumen()
  // start at previous image position
  { 
    if (cantImagenes != 0)
      personajeNoFueCreado = false;
  } 


} // end of ImagesPlayer class
