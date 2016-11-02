package formularios;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class OtraInterfaz extends JFrame {

	private JPanel contentPane;

	public OtraInterfaz(Personaje p){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Personaje");
		lblNewLabel.setBounds(40, 29, 200, 50);
		lblNewLabel.setText(p.getNombrePersonaje());
		contentPane.add(lblNewLabel);
		
		JLabel lblRaza = new JLabel("Raza");
		lblRaza.setBounds(40, 85, 200, 50);
		lblRaza.setText(p.getRazaPersonaje());
		contentPane.add(lblRaza);
		
		JLabel lblCasta = new JLabel("Casta");
		lblCasta.setBounds(40, 166, 200, 50);
		lblCasta.setText(p.getCastaPersonaje());
		contentPane.add(lblCasta);
	}

}
