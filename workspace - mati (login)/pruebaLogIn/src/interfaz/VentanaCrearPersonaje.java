package interfaz;
import model.Usuario;

import java.util.Properties;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaCrearPersonaje extends JFrame {
	
	Personaje personaje;
	VentanaMapa ventanaMapaJuego;
	
	static Properties propiedades;
	static PropertiesFile pf;
	
	private String nombrePersonaje;
	private String razaPersonaje;
	private String castaPersonaje;
	private JPanel contentPane;
	private JTextField textField;
	private Usuario usuario;
	
	
    
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//pf = new PropertiesFile();
					//propiedades = pf.getProperties();
					VentanaCrearPersonaje frame = new VentanaCrearPersonaje();
					frame.setVisible(true);
					//frame.setSize(Integer.parseInt(propiedades.getProperty("w")),Integer.parseInt(propiedades.getProperty("h")));
					frame.setSize(new Dimension(800,600));
					frame.setLocationRelativeTo(null);
					frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public VentanaCrearPersonaje() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\lukki\\workspace\\Interfaces\\src\\imagenes\\IconoVentana.jpg"));
		setTitle("Crear Personaje");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nombre del personaje");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(131, 37, 145, 50);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(313, 52, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblRaza = new JLabel("Raza");
		lblRaza.setForeground(new Color(255, 255, 255));
		lblRaza.setBounds(131, 98, 44, 14);
		contentPane.add(lblRaza);
		
		JComboBox comboRaza = new JComboBox();
		comboRaza.setBounds(313, 95, 86, 20);
		
		comboRaza.addItem("Seleccione");
		comboRaza.addItem("Humano");
		comboRaza.addItem("Orco");
		
		contentPane.add(comboRaza);
		
		JLabel lblCasta = new JLabel("Casta");
		lblCasta.setForeground(new Color(255, 255, 255));
		lblCasta.setBounds(131, 146, 44, 14);
		contentPane.add(lblCasta);
		
		JComboBox comboCasta = new JComboBox();
		comboCasta.setBounds(313, 143, 86, 20);
		
		comboCasta.addItem("Seleccione");
		comboCasta.addItem("Guerrero");
		comboCasta.addItem("Mago");
		comboCasta.addItem("Curandero");
		
		contentPane.add(comboCasta);
		
		JButton btnCrear = new JButton("Crear");
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					personaje = new Personaje(textField.getText(),comboRaza.getSelectedItem().toString(),comboCasta.getSelectedItem().toString()); //Lo puede guardar en la base.
					VentanaMapa ventanaMapa = new VentanaMapa(personaje);
					
				} catch (Exception e){
					e.printStackTrace();
				}
			ventanaMapaJuego.setVisible(true);
			ventanaMapaJuego.setSize(new Dimension(800,600));
			ventanaMapaJuego.setLocationRelativeTo(null);
			dispose();
				
			}
		});
		btnCrear.setBounds(313, 206, 89, 23);
		contentPane.add(btnCrear);
		
		JLabel lblImagen = new JLabel("");
		lblImagen.setIcon(new ImageIcon("C:\\Users\\lukki\\workspace\\Interfaces\\src\\imagenes\\BackgroundCrearPersonaje.jpeg"));
		lblImagen.setBounds(0, 0, 1776, 1050);
		contentPane.add(lblImagen);
		
	}
	
	public VentanaCrearPersonaje(JTextField text, JComboBox raza, JComboBox casta, Usuario user){
	   Personaje personaje = new Personaje();
	   personaje.nombrePersonaje = text.getText();
	   personaje.razaPersonaje = (String) raza.getSelectedItem();
	   personaje.castaPersonaje = (String) casta.getSelectedItem();
	   
	   usuario.setPersonajeJugador(personaje);
	   
	  }

	public String getNombrePersonaje() {
		return nombrePersonaje;
	}

	public void setNombrePersonaje(String nombrePersonaje) {
		this.nombrePersonaje = nombrePersonaje;
	}

	public String getRazaPersonaje() {
		return razaPersonaje;
	}

	public void setRazaPersonaje(String razaPersonaje) {
		this.razaPersonaje = razaPersonaje;
	}

	public String getCastaPersonaje() {
		return castaPersonaje;
	}

	public void setCastaPersonaje(String castaPersonaje) {
		this.castaPersonaje = castaPersonaje;
	}
	
	
	
	
}
