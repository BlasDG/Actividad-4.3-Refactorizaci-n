import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.*;

public class VentanaDepart extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// --- NUEVOS ATRIBUTOS Y CONSTANTES (Refactorización Actividad 4.6) ---
	private String existedepart = "DEPARTAMENTO EXISTE.";
	private String depar_error = "DEPARTAMENTO ERRÓNEO";
	private static final String NOEXISTEDEPART = "DEPARTAMENTO NO EXISTE.";
	// ---------------------------------------------------------------------

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
	Color c;

	public VentanaDepart(JFrame f) {
		setTitle("GESTIÓN DE DEPARTAMENTOS.");

		JPanel p0 = new JPanel();
		c = Color.CYAN;
		p0.add(titulo);
		p0.setBackground(c);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(lnum);
		p1.add(num);
		p1.add(consu);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		p2.add(lnom);
		p2.add(nombre);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout());
		p3.add(lloc);
		p3.add(loc);

		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout());
		c = Color.YELLOW;
		p4.add(balta);
		p4.add(borra);
		p4.add(modif);
		p4.setBackground(c);

		JPanel p5 = new JPanel();
		c = Color.PINK;
		p5.add(breset);
		p5.add(ver);
		p5.add(fin);
		p5.setBackground(c);

		JPanel p7 = new JPanel();
		p7.setLayout(new FlowLayout());
		p7.add(mensaje);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(p0); add(p1); add(p2); add(p3); add(p4); add(p5); add(p7);
		pack();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		balta.addActionListener(this);
		breset.addActionListener(this);
		fin.addActionListener(this);
		consu.addActionListener(this);
		borra.addActionListener(this);
		modif.addActionListener(this);
		ver.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int dep, confirm;

		// El botón ALTA
		if (e.getSource() == balta) {
			mensaje.setText(" has pulsado el boton alta");
			try {
				dep = Integer.parseInt(num.getText());
				if (dep > 0) {
					if (consultar(dep)) {
						mensaje.setText(existedepart); // Uso de atributo
					} else {
						mensaje.setText("NUEVO DEPARTAMENTO.");
						grabar(dep, nombre.getText(), loc.getText());
						mensaje.setText("NUEVO DEPARTAMENTO GRABADO.");
					}
				} else {
					mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
				}
			} catch (java.lang.NumberFormatException ex) {
				mensaje.setText(depar_error); // Uso de atributo
			} catch (IOException ex2) {
				mensaje.setText("ERRORRR EN EL FICHERO. Fichero no existe. (ALTA)");
			}
		}

		// El botón CONSULTAR
		if (e.getSource() == consu) {
			mensaje.setText(" has pulsado el boton consultar");
			try {
				dep = Integer.parseInt(num.getText());
				if (dep > 0) {
					if (consultar(dep)) {
						mensaje.setText(existedepart); // Uso de atributo
						visualiza(dep);
					} else {
						mensaje.setText(NOEXISTEDEPART); // Uso de constante
						nombre.setText(" "); loc.setText(" ");
					}
				} else {
					mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
				}
			} catch (java.lang.NumberFormatException ex) {
				mensaje.setText(depar_error); // Uso de atributo
			} catch (IOException ex2) {
				mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (ALTA)");
			}
		}

		// El botón BORRAR
		if (e.getSource() == borra) {
			mensaje.setText(" has pulsado el boton Borrar");
			try {
				dep = Integer.parseInt(num.getText());
				if (dep > 0) {
					if (consultar(dep)) {
						mensaje.setText(existedepart); // Uso de atributo
						visualiza(dep);
						confirm = JOptionPane.showConfirmDialog(this, "ESTAS SEGURO DE BORRAR...", "AVISO BORRADO.",
								JOptionPane.OK_CANCEL_OPTION);
						if (confirm == 0) {
							borrar(dep);
							mensaje.setText(" REGISTRO BORRADOO: " + dep);
							nombre.setText(" "); loc.setText(" ");
						}
					} else {
						mensaje.setText(NOEXISTEDEPART); // Uso de constante
						nombre.setText(" "); loc.setText(" ");
					}
				} else {
					mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
				}
			} catch (java.lang.NumberFormatException ex) {
				mensaje.setText(depar_error); // Uso de atributo
			} catch (IOException ex2) {
				mensaje.setText("ERRORRR EN EL FICHERO. Fichero no existe. (BORRAR)");
			}
		}

		// El botón MODIFICAR
		if (e.getSource() == modif) {
			mensaje.setText(" has pulsado el boton Modificar.");
			try {
				dep = Integer.parseInt(num.getText());
				if (dep > 0) {
					if (consultar(dep)) {
						mensaje.setText(existedepart); // Uso de atributo
						confirm = JOptionPane.showConfirmDialog(this, "ESTAS SEGURO DE MODIFICAR...", "AVISO MODIFICACIÓN.",
								JOptionPane.OK_CANCEL_OPTION);
						if (confirm == 0) {
							modificar(dep);
							mensaje.setText(" REGISTRO MODIFICADO: " + dep);
						}
					} else {
						mensaje.setText(NOEXISTEDEPART); // Uso de constante
						nombre.setText(" "); loc.setText(" ");
					}
				} else {
					mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
				}
			} catch (java.lang.NumberFormatException ex) {
				mensaje.setText(depar_error); // Uso de atributo
			} catch (IOException ex2) {
				mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (MODIFICAR)");
			}
		}

		if (e.getSource() == fin) {
			System.exit(0);
		}

		if (e.getSource() == ver) {
			try {
				mensaje.setText("Visualizando el fichero por la consolaa.....");
				verporconsola();
			} catch (IOException e1) {
				System.out.println("ERRROR AL LEEERRRRRR AleatorioDep.dat");
			}
		}

		if (e.getSource() == breset) {
			mensaje.setText(" has pulsado el boton limpiar..");
			num.setText(" "); nombre.setText(" ");
			loc.setText(" ");
		}
	}

	// ... (El resto de métodos: verporconsola, consultar, visualiza, borrar, modificar, grabar permanecen igual)

	public void verporconsola() throws IOException {
		String nom = "", locStr = ""; int dep = 0; long pos;
		File fichero = new File("AleatorioDep.dat");
		RandomAccessFile file = new RandomAccessFile(fichero, "r");
		char cad[] = new char[10], aux;
		if (file.length() > 0) {
			pos = 0;
			System.out.println(" ------------------------------------------");
			System.out.println(" - - - VISUALIZO POR CONSOLAAAAA ");
			for (;;) {
				file.seek(pos);
				dep = file.readInt();
				for (int i = 0; i < cad.length; i++) {
					aux = file.readChar();
					cad[i] = aux;
				}
				nom = new String(cad);
				for (int i = 0; i < cad.length; i++) {
					aux = file.readChar();
					cad[i] = aux;
				}
				locStr = new String(cad);
				System.out.println("DEP: " + dep + ", Nombre: " + nom + ", Localidad: " + locStr);
				pos = pos + 44;
				if (file.getFilePointer() == file.length()) break;
			}
			file.close();
			System.out.println(" ------------------------------------------");
		} else {
			System.out.println(" ---------FICHERO VACÍO --------------------");
		}
	}

	boolean consultar(int dep) throws IOException {
		long pos; int depa;
		File fichero = new File("AleatorioDep.dat");
		RandomAccessFile file = new RandomAccessFile(fichero, "r");
		try {
			pos = 44 * (dep - 1);
			if (file.length() == 0) return false;
			file.seek(pos);
			depa = file.readInt();
			file.close();
			if (depa > 0) return true;
			else return false;
		} catch (IOException ex2) {
			return false;
		}
	}

	void visualiza(int dep) {
		String nom = "", loca = ""; long pos; int depa;
		File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "r");
			pos = 44 * (dep - 1);
			file.seek(pos);
			depa = file.readInt();
			char nom1[] = new char[10], aux, loc1[] = new char[10];
			for (int i = 0; i < 10; i++) {
				aux = file.readChar();
				nom1[i] = aux;
			}
			for (int i = 0; i < 10; i++) {
				aux = file.readChar();
				loc1[i] = aux;
			}
			nom = new String(nom1);
			loca = new String(loc1);
			nombre.setText(nom);
			loc.setText(loca);
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void borrar(int dep) {
		String nom = "", loca = ""; StringBuffer buffer = null;
		long pos; File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			pos = 44 * (dep - 1);
			file.seek(pos);
			file.writeInt(0);
			buffer = new StringBuffer(nom);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			buffer = new StringBuffer(loca);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void modificar(int dep) {
		String nom = "", loca = ""; StringBuffer buffer = null;
		long pos; File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			pos = 44 * (dep - 1);
			file.seek(pos);
			file.writeInt(dep);
			nom = nombre.getText();
			loca = loc.getText();
			buffer = new StringBuffer(nom);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			buffer = new StringBuffer(loca);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void grabar(int dep, String nom, String loc) {
		long pos; StringBuffer buffer = null;
		File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			pos = 44 * (dep - 1);
			file.seek(pos);
			file.writeInt(dep);
			buffer = new StringBuffer(nom);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			buffer = new StringBuffer(loc);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}