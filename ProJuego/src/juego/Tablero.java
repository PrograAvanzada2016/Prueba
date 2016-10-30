package juego;
// AlienTilesPanel.java //ACA ES DONDE SE DIBUJA EL JUEGO.
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The game's drawing surface. Uses active rendering to a JPanel
   with the help of Java 3D's timer.

   Set up the game world and sprites, and update and draw
   them every period nanosecs.

   The complexity of the game world: a moving tile floor, containing
   pickups, blocks, and moving sprites, is managed by a WorldDisplay
   object.

   The game begins with a simple introductory screen, which
   doubles as a help window during the course of play. When
   the help is shown, the game pauses.

   The game is controlled only from the keyboard, no mouse
   events are caught.

   The game finished (gameOver is set to true) either when the player
   has been hit the required number of times, or when he has picked
   up all the pickups (a cup, flower pot, and watch).
*/

import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import com.sun.j3d.utils.timer.J3DTimer;


public class Tablero extends JPanel implements Runnable
{
  private static final int ANCHO = 800;  //Tamaño de Panel.
  private static final int ALTO = 400; 

  private static final int NO_DELAYS_POR_RENDIMIENTO = 16;  //ESTO HAY QUE VER COMO SE USA
  /* Number of frames with a delay of 0 ms before the animation thread yields
     to other running threads. */
  
  private static final int MAX_FRAME_SKIPS = 5; //ESTO HAY QUE VER COMO SE USA
    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered
  
  private static final String IMG = "Imagenes.txt"; //Archivo de Imágenes

  private static final Color lightBlue = new Color(0.17f, 0.87f, 1.0f); //Color de fondo.

  private Thread animacion; // El hilo que genera la animación.
  private volatile boolean running = false;   // Se utiliza para stoppear la animación del hilo. Para el juego
  private volatile boolean pausa = false; //Pausa del juego. Comienza en False.

  private long period; // period between drawing in _nanosecs_
  
  private EnemigoTiles enemigo;

  //Entidades del Juego
  private DesarrolloMapa mapa;
  private JugadorSprite jugador;
  private EnemigoSprite enemigosp[]; 
  
  //Tiempos de juego activo
  private long tiempoInicioJuego;
  private int tiempoFinJuego;

  //Terminar el juego
  private volatile boolean finDelJuego = false;
  private int puntaje = 0;

  //Muestra mensajes
  private Font fuenteMensajes;
  private FontMetrics metricas;

  //Rendering de la salida
  private Graphics g; 
  private Image imagen = null;

  public Tablero(EnemigoTiles e, long period) //A MODO GENERAL, SETEA COSAS DE LA PANTALLA.  
  {
	
    enemigo = e; //e es un Enemigo.
    this.period = period;

    setDoubleBuffered(false); /*Esta técnica se conoce como doble buffer.
     Un buffer son los pixels en pantalla, el otro es el BufferedImage.
     Lo que se hace es dibujar sobre uno de ellos (el BufferedImage), evitando dibujar sobre la pantalla. 
     Cuando el dibujo sobre el BufferedImage está completo, se pega directamente encima de la pantalla. ESTA COMO FALSO.*/
    
    setBackground(Color.black);
    setPreferredSize( new Dimension(ANCHO, ALTO)); //Crea el tamaño del panel.

    setFocusable(true); /* JPanel reciba las notificaciones del teclado es necesario incluir la instrucción setFocusable(true)
    que permite que KeyboardExample reciba el foco. ESTO TAL VEZ NO SE NECESITE YA QUE NO HACEMOS NADA POR TECLADO.*/
    
    requestFocus();    // Gracias a la línea anterior, el JPanel ahora tiene el foco y recibe eventos claves.

	addKeyListener( new KeyAdapter() { //Escucha eventos claves del teclado. HAY QUE VER SI SE UTILIZA.
       public void keyPressed(KeyEvent e)
       { processKey(e);  }
     });

    // INICIAN LOS CARGADORES.
	
    CargaImagenes cargaImg = new CargaImagenes(IMG);

    // Crea el mapa, los jugadores y los enemigos.
    crearMapa(cargaImg); 

    // Mensajeria
    fuenteMensajes = new Font("SansSerif", Font.BOLD, 24);
    metricas = this.getFontMetrics(fuenteMensajes);
  }


  private void crearMapa(CargaImagenes cargaImg) //Crea el mapa con mapa, jugadores y Mounstros.
  {
    mapa = new DesarrolloMapa(cargaImg, this);  // Creo un nuevo mapa

    jugador = new JugadorSprite(7,12, ANCHO, ALTO, cargaImg, 
                                       mapa, this);  //Creo un nuevo jugador.

    enemigosp = new EnemigoSprite[4]; //Cantidad de Enemigos.

    enemigosp[0] = new EnemigoAStarSprite(10, 11, ANCHO, ALTO, cargaImg, mapa);
    enemigosp[1] = new EnemigoCuadranteSprite(6, 21, ANCHO, ALTO, cargaImg, mapa);
    enemigosp[2] = new EnemigoCuadranteSprite(14, 20, ANCHO, ALTO, cargaImg, mapa);
    enemigosp[3] = new EnemigoAStarSprite(34, 34, ANCHO, ALTO, cargaImg, mapa); //Su posición es ilegal. No pertenece al mapa (34,34).

    mapa.addSprites(jugador, enemigosp);  // Le agrego al mapa los sprites de jugador y enemigo.
  }

 //Esto es para cuando detecta CERRAR o para ayuda, por el momento lo saco. AL SACARLO ME TIRA ERROR, VER PORQUE.
  private void processKey(KeyEvent e) //Lee los eventos del teclado.
  {
    int keyCode = e.getKeyCode();
    if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) ||
        (keyCode == KeyEvent.VK_END) ||
        ((keyCode == KeyEvent.VK_C) && e.isControlDown()) )
      running = false; //Si la tecla presionada es ESC, Q, End o Control C. El juego deja de correr.
    
    if (keyCode == KeyEvent.VK_H) { //Esto es para la ayuda...
      if (showHelp) {  // help being shown
        showHelp = false;  // switch off
        pausa = false;
      }
      else {  // help not being shown
       showHelp = true;    // show it
       pausa = true;    // pausa may already be true
      }
    } 

    // Teclas del juego. HAY QUE MODIFICAR PARA QUE SEA CON CLICK.
    if (!pausa && !finDelJuego) {
      // Se mueven los personajes según la teclada presionada. HAY QUE MODIFICARLO PARA QUE SEA CON CLICK.
      if (keyCode == KeyEvent.VK_NUMPAD7)
        jugador.mover(TilesSprite.NO);   // Mover personaje Noroeste.
      else if (keyCode == KeyEvent.VK_NUMPAD9)
        jugador.mover(TilesSprite.NE);   // Mover personaje Noreste.
      else if (keyCode == KeyEvent.VK_NUMPAD3)
        jugador.mover(TilesSprite.SE);   // Mover personaje Sudeste.
      else if (keyCode == KeyEvent.VK_NUMPAD1)
        jugador.mover(TilesSprite.SO);   // Mover personaje Sudoeste.
      else if (keyCode == KeyEvent.VK_NUMPAD5)
        jugador.quedaQuieto();           // queda quieto
      else if (keyCode == KeyEvent.VK_NUMPAD2)
        jugador.tryPickup();      // Agarra un ITEM de la posición actual
    }
  }


  public void finDelJuego() //Finaliza el juego porque no hay más ITEMS en el mapa.
  { 
    if (!finDelJuego) {
      finDelJuego = true; 
      puntaje = (int) ((J3DTimer.getValue() - tiempoInicioJuego)/1000000000L);
    }
  }

  public void addNotify() //¿?
  // wait for the JPanel to be added to the JFrame before starting
  { super.addNotify();   // creates the peer
    comenzarJuego();         // Comienzan los hilos. Comienza el juego
  }


  private void comenzarJuego() //Inicializo los hilos.
  { 
    if (animacion == null || !running) { //Si no esta iniciado, lo inicio.
      animacion = new Thread(this);
	  animacion.start();
    }
  }
    

  //				MÉTODOS DE CICLO DE VIDA DEL JUEGO!
  
  public void pauseGame() //JFrame desactivado.
  { pausa = true;   } //Juego despausado.


  public void stopGame() //JFrame o VENTANA cerrada.
  {  running = false;   } //Hilo de running en OFF.

  public void run() /* Los fotogramas de la animación se dibujan dentro del bucle while. Esto es el RUN del juego, HAY QUE VERLO.*/
  
    long tiempoAnterior, tiempoDespues, diferenciaTiempo, sleepTime;
    long duranteTiempoDeSleep = 0L;
    int noDelays = 0;
    long exceso = 0L;

    tiempoInicioJuego = J3DTimer.getValue();
    tiempoAnterior = tiempoInicioJuego;

	running = true;

	while(running) {
	  actualizarJuego();
      renderizarJuego();
      pintarPantalla();

      tiempoDespues = J3DTimer.getValue();
      diferenciaTiempo = tiempoDespues - tiempoAnterior;
      sleepTime = (period - diferenciaTiempo) - duranteTiempoDeSleep;  

      if (sleepTime > 0) {   // some time left in this cycle
        try {
          Thread.sleep(sleepTime/1000000L);  // nano -> ms
        }
        catch(InterruptedException ex){}
        duranteTiempoDeSleep = (J3DTimer.getValue() - tiempoDespues) - sleepTime;
      }
      else {    // sleepTime <= 0; the frame took longer than the period
        exceso -= sleepTime;  // store excess time value
        duranteTiempoDeSleep = 0L;

        if (++noDelays >= NO_DELAYS_POR_RENDIMIENTO) {
          Thread.yield();   // give another thread a chance to run
          noDelays = 0;
        }
      }

      tiempoAnterior = J3DTimer.getValue();

      /* If frame animation is taking too long, update the game state
         without rendering it, to get the updates/sec nearer to
         the required FPS. */
      int skips = 0;
      while((exceso > period) && (skips < MAX_FRAME_SKIPS)) {
        exceso -= period;
	    actualizarJuego();    // update state but don't render
        skips++;
      }
	}
    System.exit(0);   // so window disappears
  }


  private void actualizarJuego()  //Actualizo Enemigos en el mundo.
  { 
    if (!pausa && !finDelJuego) {
      for(int i=0; i < enemigosp.length; i++)
        enemigosp[i].actualizar();
    } 
  }


  private void renderizarJuego()
  {
    if (imagen == null){
      imagen = createImage(ANCHO, ALTO);
      if (imagen == null) {
        System.out.println("Imagen es nulo");
        return;
      }
      else
        g = imagen.getGraphics();
    }

    // con g seteo el color de fondo y creo el rectangulo de la pantalla.
    g.setColor(lightBlue);
    g.fillRect(0, 0, ANCHO, ALTO);

    // Dibujo los elementos del juego. Es importante el ORDEN.
    mapa.dibujar(g);  
  }

  private void pintarPantalla() //¿?
  // use active rendering to put the buffered image on-screen
  { 
    Graphics g;
    try {
      g = this.getGraphics();
      if ((g != null) && (imagen != null))
        g.drawImage(imagen, 0, 0, null);
      // Sync the display on some systems.
      // (on Linux, this fixes event queue problems)
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }
    catch (Exception e)
    { System.out.println("Error de contextos de Gráficos " + e);  }
  }

} 
