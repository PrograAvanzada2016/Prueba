package formularios;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class OtraInterfaz extends JFrame {
	
	public OtraInterfaz (CrearPersonaje p){
		String nombrePersonaje = p.getNombrePersonaje();
		String castaPersonaje = p.getCastaPersonaje();
		String razaPersonaje = p.getRazaPersonaje();
		
	}

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OtraInterfaz frame = new OtraInterfaz();
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
	public OtraInterfaz() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Personaje");
		lblNewLabel.setBounds(40, 29, 200, 50);
		contentPane.add(lblNewLabel);
		
		JLabel lblRaza = new JLabel("Raza");
		lblRaza.setBounds(40, 85, 200, 50);
		contentPane.add(lblRaza);
		
		JLabel lblCasta = new JLabel("Casta");
		lblCasta.setBounds(40, 166, 200, 50);
		contentPane.add(lblCasta);
	}

}
