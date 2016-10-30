package juego;
// WorldDisplay.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* WorldDisplay manages:
      - a moving tile floor, represented by a single GIF
      - no-go areas on the floor
      - blocks occupying certain tiles
      - pickups occupying certain tiles
      - the player and aliens sprites

   WorldDisplay manages the communication between the player and the
   sprites so that it can monitor/control the interactions.

   The 3 main data structures used here are:
      - an onstacles[][] array holding info on which tiles
        are no-gps or hold blocks.

      - a WorldItems objects which stores blocks, pickups, and
        sprites in row order to make them easier to draw with
        the correct z-ordering

      - a numPickups counter to record how many pickups ae still
        left to be picked up

    The methods fall into 5 main groups:
      - loading of floor info;           // info related to the floor image
      - loading of world objects info    // info about no-gos, blocks, pickups
      - pickups related
      - sprites related
      - others, e.g. draw()
*/

import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;


public class DesarrolloMapa
{
  private final static String MAPA_DIR = "Mapa/"; // Le indica que vaya a buscar los obstaculos a ese directorio

  // Tipo de tiles ocupantes
  public static final int OBSTACULO = 1;  //Bloque o arbol 
  public static final int SPRITE = 2; //Sprite es un personaje  

  // Tamaño del mapa en Tiles
  private int tamXTiles, tamYTiles;

  //ancho de píxel / altura de un tile (incluyendo cualquier espacio al lado del azulejo)
  private int anchoTile, alturaTile;

  private int filarParX, filarParY; // Comienzo de la primer fila. Empiezo desde arriba a la izquierda.
  private int filaImparX, filarImparY;   // Inicio de la primer fila IMPAR de coordenadas.
  
  private int desplazamientoX, desplazamientoY; //Desplazamiento del mapa.
  private CargaImagenes img; //Carga todas las imagenes. Tanto de Sprites como Mapa, obstaculos, etc.
  
  private Tablero tab; //Superficie de dibujo del juego.

  private BufferedImage imagenPiso;    // Imagen del piso

  private boolean obstaculos[][];    // En esta matriz voy a especificar los obstaculos

  private ItemsMapa items;   //Clase con los items que pueden aparecer en el mapa

  private JugadorSprite jugador;    // Los Sprites!
  private EnemigoSprite enemigo[];

  public DesarrolloMapa(CargaImagenes imagen, Tablero tablero)
  {
   img = imagen;
   tab = tablero;

   desplazamientoX = 0; desplazamientoY = 0;
   cargaInformacionPiso("informacionMapa.txt"); //Voy a tener el mapa con todos los objetos (Declarados en coordenadas. Va a trabajar con Jpanel).

   items = new ItemsMapa(anchoTile, alturaTile,
                             filarParX, filarParY, filaImparX, filarImparY);
   
   iniciarObstaculos(); // Con esta función hacemos un CLEAR de los obstaculos en nuestro mapa

   cargaObjetosAlMapa("objetosMapa.txt"); //Cargar los elementos al mapa.
  }  

  private void cargaInformacionPiso(String file)
  { 
    String archivo = MAPA_DIR + file; //Nombre del archivo.
    System.out.println("Leyendo Archivo" + archivo);
    try {
      InputStream entrada = this.getClass().getResourceAsStream(archivo);
      BufferedReader br = new BufferedReader( new InputStreamReader(entrada));
      // BufferedReader br = new BufferedReader( new FileReader(archivo));
      String linea;
      String[] tokens;
      while((linea = br.readLine()) != null) {
        if (linea.length() == 0)
          continue;
        if (linea.startsWith("//"))
          continue;

        // System.out.println("linea: " + line);
        tokens = linea.split("\\s+");
        if (tokens[0].equals("imagen"))
          imagenPiso = img.getImagen(tokens[1]);  // Cargar la imagen del PISO.
        else if (tokens[0].equals("tamTiles")) {   // Tamaño del mapa EN TILES.
          tamXTiles = getNumber(tokens[1]);
          tamYTiles = getNumber(tokens[2]);
        }
        else if (tokens[0].equals("dimTile")){ // Ancho y Largo de los Tiles
          anchoTile = getNumber(tokens[1]);
          alturaTile = getNumber(tokens[2]);
        }
        else if (tokens[0].equals("filaPar")){ //fila pares
          filarParX = getNumber(tokens[1]);
          filarParY = getNumber(tokens[2]);
        }
        else if (tokens[0].equals("filaImpar")){ //Fila Impar
          filaImparX = getNumber(tokens[1]);
          filarImparY = getNumber(tokens[2]);
        }
        else
          System.out.println("No se reconoce la línea" + linea);
      }
      br.close();
    } 
    catch (IOException e) 
    { System.out.println("Error leyendo el archivo:" + archivo);
      System.exit(1);
    }
  } 

  private int getNumber(String token)
  { int num = 0;
    try {
      num = Integer.parseInt(token);
    }
    catch (NumberFormatException ex){ 
      System.out.println("Formato incorrecto " + token); 
    }
    return num;
  }

  // Carga de objetos al mundo. ESTA CLASE HAY QUE VERLA YA QUE NO SABEMOS AUN QUE OBJETOS VA A CONTENER EL MAPA.


  private void cargaObjetosAlMapa(String file) //N-> Objetos, B->Bloques/Obstaculos, P->Items
  { 
    String archivo = MAPA_DIR + file;
    System.out.println("Leyendo Archivo: " + archivo);
    try {
      InputStream entrada = this.getClass().getResourceAsStream(archivo);
      BufferedReader br = new BufferedReader( new InputStreamReader(entrada));
      // BufferedReader br = new BufferedReader( new FileReader(archivo));
      String linea;
      char ch;
      while((linea = br.readLine()) != null) {
        if (linea.length() == 0)  // blank line
          continue;
        if (linea.startsWith("//"))   // comment
          continue;
        ch = Character.toLowerCase( linea.charAt(0) );
        if (ch == 'o')  // Si es un objeto.
          obtenerObstaculos(linea.substring(1), br);   // skip 'n' at start
        else
          System.out.println("No se reconoce la línea: " + linea);
      }
      br.close();
    } 
    catch (IOException e) 
    { System.out.println("Error leyendo el archivo: " + archivo);
      System.exit(1);
    }
  } 


  private Point getCoord(String token) //Para obtener las coordenadas..
  {
    int x = 0;
    int y = 0;
    String[] result = token.split("-");
    if (result.length != 2)
      System.out.println("Coordenadas incorrectas en" + token);
    else {
      try {
        x = Integer.parseInt(result[0]);
        y = Integer.parseInt(result[1]);
      }
      catch (NumberFormatException ex){ 
        System.out.println("Formato incorrecto de coordenadas en" + token); 
      }
    }

    if (x >= tamXTiles) {
      System.out.println("Coordenada X mayor en" + token); 
      x = tamXTiles-1;
    }
    if (y >= tamYTiles) {
      System.out.println("Coordenada Y mayor en" + token); 
      x = tamYTiles-1;
    }

    return new Point(x,y);
  }



  private void obtenerObstaculos(String linea, BufferedReader br) //Obtengo los obstaculos
  {
    boolean resultado = false;
    StringTokenizer tokens = new StringTokenizer(linea);
    tokens.nextToken();   
    String nombreObstaculo = tokens.nextToken();
    BufferedImage imgObstaculo = img.getImagen(nombreObstaculo);

    try {
      while (!resultado) {
        linea = br.readLine();
        if (linea == null) { 
          System.out.println("Inexperada finalización de información de obstaculos");
          System.exit(1);
        }
        resultado = obtenerLineaObstaculos(linea, nombreObstaculo, imgObstaculo);
      }
    }
    catch (IOException e) 
    { System.out.println("Error leyendo la información de obstaculos");
      System.exit(1);
    }
  } 

  private boolean obtenerLineaObstaculos(String linea, String nombreObstaculo, BufferedImage img) //Obtengo las lineas de bloques del archivo
  { StringTokenizer tokens = new StringTokenizer(linea);
    String token;
    Point coord;
    while (tokens.hasMoreTokens()) {
      token = tokens.nextToken();
      if (token.equals("#"))   // Finaliza de leer obstaculos
        return true;
      coord = getCoord(token);
      items.agregarItem( nombreObstaculo, OBSTACULO, coord.x, coord.y, img); //Lo guardo en ItemsMapa.
      obstaculos[coord.x][coord.y] = true;
      // System.out.println("Added " + nombreObstaculo + cantBloques + 
      //                            " at (" + coord.x + "," + coord.y + ")");
    }
    return false;
  }


  //					OTROS METODOS!


  private void iniciarObstaculos() //Inicio los obstaculos en el mapa
  {
    obstaculos = new boolean[tamXTiles][tamYTiles];
    for(int i=0; i < tamXTiles; i++)
      for(int j=0; j < tamYTiles; j++)
        obstaculos[i][j] = false;
  }


  public boolean validarPosicionTile(int x, int y)   // Si el (X,Y) están en el mapa y no hay obstaculo Creo el sprite!
  {
    if ((x < 0) || (x >= tamXTiles) || (y < 0) || (y >= tamYTiles))
      return false;
    if (obstaculos[x][y])
      return false;
    return true;
  }


  public void dibujar(Graphics g) //Función encargada de dibujar el mundo. IMPORTANTE!!
  { g.drawImage(imagenPiso, desplazamientoX, desplazamientoY, null);   // Dibujo el Piso en el desplazamientoX = 0 y desplazamientoY = 0.
    items.posicionSprites(jugador, enemigo);         // Agrego UN jugador y un FOR con el size de enemigo.
    items.dibujar(g, desplazamientoX, desplazamientoY);               // Grafico los ITEMS en el juego.
    items.removerSprites();                         // Si quedó algún sprite anterior.. Lo remuevo!
  }

  // 				Métodos relacionados a los ITEMS. VER COMO LO TIENEN PENSADO.

  public String overPickup(Point pt) //¿Hay un item en el tile seleccionado?
  { return items.findPickupName(pt);   }


  public void removePickup(String name) //Remueve el ITEM encontrado del mapa.
  { 
    if (items.removePickup(name)) {  // intenta removerlo.
      cantITems--;
      if (cantITems == 0)   // No hay mas items en el MAPA. FINALIZA EL JUEGO.
        tab.gameOver();
    }
    else
      System.out.println("No se puede eliminar ya que no se reconoce el ITEM" + name);
  }


  public boolean hasPickupsLeft()
  // called by AlienQuadSprite
  {  return cantITems != 0;  }



//				SPRITES EN EL MAPA.


  public void agregarSprite(JugadorSprite js, EnemigoSprite es[]) //Agrega el jugador y el enemigo.
  { jugador = js;
    enemigo = es;
  }

  public Point getPosicionJugador() //Envia ubicación a los enemigo del jugador.
  {  return jugador.getPosicionTile(); }


  public void hitByAlien() //Golpeado por un Enemigo.
  { jugador.hitByAlien(); }

  private void actualizaDesplazamiento(int cuadrante) //Actualiza el desplazamiento del mapa.
  {
    if (cuadrante == TilesSprite.SO) {   // SE MUEVE AL CONTRARIO Y EN DIAGONAL DE DONDE SE MOVIO EL PERSONAJE.
      desplazamientoX += anchoTile/2;
      desplazamientoY -= alturaTile/2;
    }
    else if (cuadrante == TilesSprite.NO) {
      desplazamientoX += anchoTile/2;
      desplazamientoY += alturaTile/2;
    }
    else if (cuadrante == TilesSprite.NE) {
      desplazamientoX -= anchoTile/2;
      desplazamientoY += alturaTile/2;
    }
    else if (cuadrante == TilesSprite.SE) {
      desplazamientoX -= anchoTile/2;
      desplazamientoY -= alturaTile/2;
    }
    else if (cuadrante == TilesSprite.QUIETO) {
    }
    else
      System.out.println("No se detecta el cuadrante al que desea moverse.");
  }


}
