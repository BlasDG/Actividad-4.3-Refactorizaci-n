import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.*;

public class VentanaDepart extends SuperclaseDepart implements ActionListener, InterfaceVentanaDepart {

	private String existedepart = "DEPARTAMENTO EXISTE.";
	private String depar_error = "DEPARTAMENTO ERRÓNEO";
	private static final String NOEXISTEDEPART = "DEPARTAMENTO NO EXISTE.";

	// El campo num es específico de esta ventana
	JTextField num = new JTextField(10);

	JLabel mensaje = new JLabel(" ----------------------------- ");
	JLabel titulo = new JLabel("GESTIÓN DE DEPARTAMENTOS.");
	JLabel lnum = new JLabel("NUMERO DEPARTAMENTO:");
	JLabel lnom = new JLabel("NOMBRE:");
	JLabel lloc = new JLabel("LOCALIDAD:");

	JButton balta = new JButton("Insertar Depar.t");
	JButton consu = new JButton("Consultar Depart.");
	JButton borra = new JButton("Borrar Depart.");
	JButton breset = new JButton("Limpiar datos.");
	JButton modif = new JButton("Modificar Departamento.");
	JButton ver = new JButton("Ver por consola.");
	JButton fin = new JButton("CERRAR");

	public VentanaDepart(JFrame f) {
		setTitle("GESTIÓN DE DEPARTAMENTOS.");
		JPanel p0 = new JPanel(); p0.add(titulo); p0.setBackground(Color.CYAN);
		JPanel p1 = new JPanel(); p1.add(lnum); p1.add(num); p1.add(consu);
		JPanel p2 = new JPanel(); p2.add(lnom); p2.add(nombre); // Heredado
		JPanel p3 = new JPanel(); p3.add(lloc); p3.add(loc);    // Heredado
		JPanel p4 = new JPanel(); p4.add(balta); p4.add(borra); p4.add(modif); p4.setBackground(Color.YELLOW);
		JPanel p5 = new JPanel(); p5.add(breset); p5.add(ver); p5.add(fin); p5.setBackground(Color.PINK);
		JPanel p7 = new JPanel(); p7.add(mensaje);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(p0); add(p1); add(p2); add(p3); add(p4); add(p5); add(p7);
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		balta.addActionListener(this); breset.addActionListener(this); fin.addActionListener(this);
		consu.addActionListener(this); borra.addActionListener(this); modif.addActionListener(this);
		ver.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == balta) {
			altadepart("PRUEBA");
		} else if (e.getSource() == consu) {
			consuldepart("PRUEBA");
		} else if (e.getSource() == borra) {
			borradepart("PRUEBA");
		} else if (e.getSource() == modif) {
			modifdepart("PRUEBA");
		} else if (e.getSource() == fin) {
			System.exit(0);
		} else if (e.getSource() == ver) {
			try {
				// Ejemplo de uso de claseAnidada con cast a la interfaz si fuera necesario
				claseAnidada ej = new claseAnidada();
				ej.entrada();
				verporconsola();
			} catch (IOException e1) { System.out.println("Error consola"); }
		} else if (e.getSource() == breset) {
			num.setText(""); nombre.setText(""); loc.setText("");
			mensaje.setText(" has pulsado el boton limpiar..");
		}
	}

	// --- IMPLEMENTACIÓN DE InterfaceVentanaDepart ---

	public int altadepart(String p) {
		mensaje.setText("Ejecutando: " + p);
		try {
			int dep = Integer.parseInt(num.getText().trim());
			if (dep > 0) {
				if (consultar(dep)) {
					mensaje.setText(existedepart);
					return 0;
				} else {
					grabar(dep, nombre.getText(), loc.getText()); // Método de superclase
					mensaje.setText("NUEVO DEPARTAMENTO GRABADO.");
					return 1;
				}
			}
		} catch (Exception ex) { mensaje.setText(depar_error); }
		return 0;
	}

	public int consuldepart(String p) {
		mensaje.setText("Ejecutando: " + p);
		try {
			int dep = Integer.parseInt(num.getText().trim());
			if (dep > 0 && consultar(dep)) {
				visualiza(dep); // Método de superclase
				mensaje.setText(existedepart);
				return 1;
			} else {
				mensaje.setText(NOEXISTEDEPART);
			}
		} catch (Exception ex) { mensaje.setText(depar_error); }
		return 0;
	}

	public int borradepart(String p) {
		mensaje.setText("Ejecutando: " + p);
		try {
			int dep = Integer.parseInt(num.getText().trim());
			if (dep > 0 && consultar(dep)) {
				int confirm = JOptionPane.showConfirmDialog(this, "¿BORRAR?", "AVISO", JOptionPane.OK_CANCEL_OPTION);
				if (confirm == 0) {
					borrar(dep); // Método de superclase
					mensaje.setText("REGISTRO BORRADO");
					nombre.setText(""); loc.setText("");
					return 1;
				}
			}
		} catch (Exception ex) { mensaje.setText(depar_error); }
		return 0;
	}

	public int modifdepart(String p) {
		mensaje.setText("Ejecutando: " + p);
		try {
			int dep = Integer.parseInt(num.getText().trim());
			if (dep > 0 && consultar(dep)) {
				modificar(dep); // Método de superclase
				mensaje.setText("REGISTRO MODIFICADO");
				return 1;
			}
		} catch (Exception ex) { mensaje.setText(depar_error); }
		return 0;
	}

	public void verporconsola() throws IOException {
		RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "r");
		if (file.length() > 0) {
			for (long pos = 0; pos < file.length(); pos += 44) {
				file.seek(pos);
				int d = file.readInt();
				if (d <= 0) continue; // Saltar borrados
				char[] n = new char[10], l = new char[10];
				for(int i=0; i<10; i++) n[i]=file.readChar();
				for(int i=0; i<10; i++) l[i]=file.readChar();
				System.out.println("ID: " + d + " Nom: " + new String(n).trim() + " Loc: " + new String(l).trim());
			}
		}
		file.close();
	}

	public boolean consultar(int dep) throws IOException {
		try (RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "r")) {
			if (file.length() == 0) return false;
			file.seek(44 * (dep - 1));
			int depa = file.readInt();
			return depa > 0;
		} catch (Exception ex) { return false; }
	}
}