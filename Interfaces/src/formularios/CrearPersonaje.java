package formularios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class CrearPersonaje extends JFrame {
	
	private String nombrePersonaje;
	private String razaPersonaje;
	private String castaPersonaje;
	private JPanel contentPane;
	private JTextField textField;
	
    
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CrearPersonaje frame = new CrearPersonaje();
					frame.setVisible(true);
					frame.setSize(new Dimension(800,600));
					frame.setLocationRelativeTo(null);
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
	public CrearPersonaje() {
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
		
		JLabel lblRaza = DefaultComponentFactory.getInstance().createLabel("Raza");
		lblRaza.setForeground(new Color(255, 255, 255));
		lblRaza.setBounds(131, 98, 44, 14);
		contentPane.add(lblRaza);
		
		JComboBox raza = new JComboBox();
		raza.setBounds(313, 95, 86, 20);
		
		raza.addItem("");
		raza.addItem("Humano");
		raza.addItem("Olfo");
		
		contentPane.add(raza);
		
		JLabel lblCasta = DefaultComponentFactory.getInstance().createLabel("Casta");
		lblCasta.setForeground(new Color(255, 255, 255));
		lblCasta.setBounds(131, 146, 44, 14);
		contentPane.add(lblCasta);
		
		JComboBox casta = new JComboBox();
		casta.setBounds(313, 143, 86, 20);
		
		casta.addItem("");
		casta.addItem("Guerrero");
		casta.addItem("Mago");
		casta.addItem("Curandero");
		
		contentPane.add(casta);
		
		JButton btnCrear = new JButton("Crear");
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OtraInterfaz otra = new OtraInterfaz();
				try{
					CrearPersonaje personaje = new CrearPersonaje(textField,raza,casta);
					
				} catch (Exception e){
					e.printStackTrace();
				}
			setVisible(false);
			otra.setVisible(true);
			otra.setSize(new Dimension(800,600));
			otra.setLocationRelativeTo(null);
				
			}
		});
		btnCrear.setBounds(313, 206, 89, 23);
		contentPane.add(btnCrear);
		
		JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("New JGoodies label");
		lblNewJgoodiesLabel.setIcon(new ImageIcon("C:\\Users\\lukki\\workspace\\Interfaces\\src\\imagenes\\BackgroundCrearPersonaje.jpeg"));
		lblNewJgoodiesLabel.setBounds(0, 0, 1776, 1050);
		contentPane.add(lblNewJgoodiesLabel);
		
	}
	
	public CrearPersonaje(JTextField text, JComboBox raza, JComboBox casta){
	   CrearPersonaje personaje = new CrearPersonaje();
	   personaje.nombrePersonaje = text.getText();
	   personaje.razaPersonaje = (String) raza.getSelectedItem();
	   personaje.castaPersonaje = (String) casta.getSelectedItem();
	   
	   System.out.print(personaje.nombrePersonaje + " " + personaje.razaPersonaje + " " + personaje.castaPersonaje);
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
