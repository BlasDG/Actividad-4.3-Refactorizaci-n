import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.*;

public class VentanaDepart extends JFrame implements ActionListener, InterfaceVentanaDepart {
	private static final long serialVersionUID = 1L;

	private String existedepart = "DEPARTAMENTO EXISTE.";
	private String depar_error = "DEPARTAMENTO ERRÓNEO";
	private static final String NOEXISTEDEPART = "DEPARTAMENTO NO EXISTE.";

	JTextField num = new JTextField(10);
	JTextField nombre = new JTextField(25);
	JTextField loc = new JTextField(25);
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
		JPanel p2 = new JPanel(); p2.add(lnom); p2.add(nombre);
		JPanel p3 = new JPanel(); p3.add(lloc); p3.add(loc);
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
		// Llamadas actualizadas con el parámetro String "PRUEBA" exigido en la actividad 4.8
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
			try { verporconsola(); } catch (IOException e1) { System.out.println("Error consola"); }
		} else if (e.getSource() == breset) {
			num.setText(" "); nombre.setText(" "); loc.setText(" ");
			mensaje.setText(" has pulsado el boton limpiar..");
		}
	}

	// --- MÉTODOS CON FIRMA CAMBIADA (Actividad 4.8) ---

	public int altadepart(String p) {
		mensaje.setText("Ejecutando: " + p);
		try {
			int dep = Integer.parseInt(num.getText());
			if (dep > 0) {
				if (consultar(dep)) {
					mensaje.setText(existedepart);
					return 0;
				} else {
					grabar(dep, nombre.getText(), loc.getText());
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
			int dep = Integer.parseInt(num.getText());
			if (dep > 0 && consultar(dep)) {
				visualiza(dep);
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
			int dep = Integer.parseInt(num.getText());
			if (dep > 0 && consultar(dep)) {
				int confirm = JOptionPane.showConfirmDialog(this, "¿BORRAR?", "AVISO", JOptionPane.OK_CANCEL_OPTION);
				if (confirm == 0) {
					borrar(dep);
					mensaje.setText("REGISTRO BORRADO");
					return 1;
				}
			}
		} catch (Exception ex) { mensaje.setText(depar_error); }
		return 0;
	}

	public int modifdepart(String p) {
		mensaje.setText("Ejecutando: " + p);
		try {
			int dep = Integer.parseInt(num.getText());
			if (dep > 0 && consultar(dep)) {
				modificar(dep);
				mensaje.setText("REGISTRO MODIFICADO");
				return 1;
			}
		} catch (Exception ex) { mensaje.setText(depar_error); }
		return 0;
	}

	// --- MÉTODOS CON REFACTORIZACIÓN "INLINE" (Actividad 4.8) ---
	// Se elimina la variable 'fichero' y se integra en el constructor como pide imagen_3.png

	public void verporconsola() throws IOException {
		// Ejemplo de Inline aplicado:
		RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "r");
		if (file.length() > 0) {
			for (long pos = 0; pos < file.length(); pos += 44) {
				file.seek(pos);
				int d = file.readInt();
				char[] n = new char[10], l = new char[10];
				for(int i=0; i<10; i++) n[i]=file.readChar();
				for(int i=0; i<10; i++) l[i]=file.readChar();
				System.out.println("ID: " + d + " Nom: " + new String(n) + " Loc: " + new String(l));
			}
		}
		file.close();
	}

	boolean consultar(int dep) throws IOException {
		// Inline aplicado:
		RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "r");
		try {
			if (file.length() == 0) return false;
			file.seek(44 * (dep - 1));
			int depa = file.readInt();
			file.close();
			return depa > 0;
		} catch (Exception ex) { return false; }
	}

	void visualiza(int dep) {
		try {
			// Inline aplicado:
			RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "r");
			file.seek(44 * (dep - 1));
			file.readInt();
			char[] n = new char[10], l = new char[10];
			for(int i=0; i<10; i++) n[i]=file.readChar();
			for(int i=0; i<10; i++) l[i]=file.readChar();
			nombre.setText(new String(n));
			loc.setText(new String(l));
			file.close();
		} catch (Exception e) {}
	}

	void borrar(int dep) {
		try {
			// Inline aplicado:
			RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "rw");
			file.seek(44 * (dep - 1));
			file.writeInt(0); // Marcar como borrado
			file.close();
		} catch (Exception e) {}
	}

	void modificar(int dep) {
		try {
			// Inline aplicado:
			RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "rw");
			file.seek(44 * (dep - 1));
			file.writeInt(dep);
			StringBuffer b = new StringBuffer(nombre.getText()); b.setLength(10);
			file.writeChars(b.toString());
			b = new StringBuffer(loc.getText()); b.setLength(10);
			file.writeChars(b.toString());
			file.close();
		} catch (Exception e) {}
	}

	void grabar(int dep, String nom, String local) {
		try {
			// Inline aplicado:
			RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "rw");
			file.seek(44 * (dep - 1));
			file.writeInt(dep);
			StringBuffer b = new StringBuffer(nom); b.setLength(10);
			file.writeChars(b.toString());
			b = new StringBuffer(local); b.setLength(10);
			file.writeChars(b.toString());
			file.close();
		} catch (Exception e) {}
	}

	public class claseAnidada {
		void entrada() {
			System.out.println("Método entrada.");
		}

		String salida(int d) {
			System.out.println("Salida.");
			return "Salida el " + d;
		}
	} // fin clase anidada
}