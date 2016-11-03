package interfaz;
import model.Usuario;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Usuario;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class VentanaInicio extends JFrame {

	private JPanel contentPane;
	private Usuario usuario;
	private JTextArea textArea;
	private boolean cancelar;
	private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    
    static Properties propiedades;
	static PropertiesFile pf;
	
	public VentanaCrearPersonaje ventanaCrearPersonaje;

	public VentanaInicio(Usuario user) {
		initComponents();
		usuario = user;
		textArea.setText("Bienvenido "+usuario.getNombre());
		cerrar();
		if(usuario.getPersonajeJugador() != null){
			VentanaMapa ventanaMapa = new VentanaMapa(usuario);
			ventanaMapa.setVisible(true);
			dispose();
		}
		else{
			ventanaCrearPersonaje.setVisible(true);
			dispose();
		}
				
		try {
            Socket socket = new Socket("localhost", 4444);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public void initComponents(){
		pf = new PropertiesFile();
		propiedades = pf.getProperties();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Integer.parseInt(propiedades.getProperty("w")),Integer.parseInt(propiedades.getProperty("h")));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(123, 62, 149, 100);
		contentPane.add(textArea);
	}
	
	private void cerrarConexion() throws IOException{
	    int resp = JOptionPane.showConfirmDialog(this, "¿Desea salir del programa?");
	    cancelar = false;
	    if (resp==JOptionPane.YES_OPTION){
	        
	            salida.writeObject("fin");
	            JOptionPane.showMessageDialog(this, "Conexion con Servidor cerrada correctamente");
	            System.exit(0);
	        
	       
	    }
	    else{
	        if (resp==JOptionPane.NO_OPTION || resp==JOptionPane.CANCEL_OPTION || resp==JOptionPane.CLOSED_OPTION){
	             cancelar=true;
	        }
	     }
	}
	    
	    private void cerrar() {
	       try{
	            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	            addWindowStateListener(new WindowAdapter(){
	                public void windowClosing(WindowEvent e){
	                    try {
	                        cerrarConexion();
	                    } catch (IOException ex) {
	                        ex.printStackTrace();;
	                    }
	                    }
	                }
	            );    
	            this.setVisible(true);
	            }catch (Exception e){   
	            }
	        }
}
