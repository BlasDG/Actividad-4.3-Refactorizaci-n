import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.*;

public class VentanaDepart extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// --- REFACTORIZACIÓN ACTIVIDAD 4.6: Atributos y Constantes ---
	// Se extraen las cadenas de texto a atributos de clase y constantes.
	private String existedepart = "DEPARTAMENTO EXISTE.";
	private String depar_error = "DEPARTAMENTO ERRÓNEO";
	private static final String NOEXISTEDEPART = "DEPARTAMENTO NO EXISTE.";

	// Componentes de la Interfaz
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

		// Paneles de la interfaz
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

		// Registro de Eventos
		balta.addActionListener(this);
		breset.addActionListener(this);
		fin.addActionListener(this);
		consu.addActionListener(this);
		borra.addActionListener(this);
		modif.addActionListener(this);
		ver.addActionListener(this);
	}

	// --- REFACTORIZACIÓN ACTIVIDAD 4.7: actionPerformed simplificado ---
	// Se extrae la lógica de cada botón a métodos individuales.
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == balta) {
			altadepart();
		} else if (e.getSource() == consu) {
			consuldepart();
		} else if (e.getSource() == borra) {
			borradepart();
		} else if (e.getSource() == modif) {
			modifdepart();
		} else if (e.getSource() == fin) {
			System.exit(0);
		} else if (e.getSource() == ver) {
			try {
				mensaje.setText("Visualizando el fichero por la consolaa.....");
				verporconsola();
			} catch (IOException e1) {
				System.out.println("ERRROR AL LEEERRRRRR AleatorioDep.dat");
			}
		} else if (e.getSource() == breset) {
			mensaje.setText(" has pulsado el boton limpiar..");
			num.setText(" ");
			nombre.setText(" ");
			loc.setText(" ");
		}
	}

	// --- MÉTODOS EXTRAÍDOS (Extract Method) ---

	private void altadepart() {
		mensaje.setText(" has pulsado el boton alta");
		try {
			int dep = Integer.parseInt(num.getText());
			if (dep > 0) {
				if (consultar(dep)) {
					mensaje.setText(existedepart);
				} else {
					mensaje.setText("NUEVO DEPARTAMENTO.");
					grabar(dep, nombre.getText(), loc.getText());
					mensaje.setText("NUEVO DEPARTAMENTO GRABADO.");
				}
			} else {
				mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
			}
		} catch (java.lang.NumberFormatException ex) {
			mensaje.setText(depar_error);
		} catch (IOException ex2) {
			mensaje.setText("ERRORRR EN EL FICHERO. Fichero no existe. (ALTA)");
		}
	}

	private void consuldepart() {
		mensaje.setText(" has pulsado el boton consultar");
		try {
			int dep = Integer.parseInt(num.getText());
			if (dep > 0) {
				if (consultar(dep)) {
					mensaje.setText(existedepart);
					visualiza(dep);
				} else {
					mensaje.setText(NOEXISTEDEPART);
					nombre.setText(" "); loc.setText(" ");
				}
			} else {
				mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
			}
		} catch (java.lang.NumberFormatException ex) {
			mensaje.setText(depar_error);
		} catch (IOException ex2) {
			mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (ALTA)");
		}
	}

	private void borradepart() {
		mensaje.setText(" has pulsado el boton Borrar");
		try {
			int dep = Integer.parseInt(num.getText());
			if (dep > 0) {
				if (consultar(dep)) {
					mensaje.setText(existedepart);
					visualiza(dep);
					int confirm = JOptionPane.showConfirmDialog(this, "ESTAS SEGURO DE BORRAR...", "AVISO BORRADO.",
							JOptionPane.OK_CANCEL_OPTION);
					if (confirm == 0) {
						borrar(dep);
						mensaje.setText(" REGISTRO BORRADOO: " + dep);
						nombre.setText(" "); loc.setText(" ");
					}
				} else {
					mensaje.setText(NOEXISTEDEPART);
					nombre.setText(" "); loc.setText(" ");
				}
			} else {
				mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
			}
		} catch (java.lang.NumberFormatException ex) {
			mensaje.setText(depar_error);
		} catch (IOException ex2) {
			mensaje.setText("ERRORRR EN EL FICHERO. Fichero no existe. (BORRAR)");
		}
	}

	private void modifdepart() {
		mensaje.setText(" has pulsado el boton Modificar.");
		try {
			int dep = Integer.parseInt(num.getText());
			if (dep > 0) {
				if (consultar(dep)) {
					mensaje.setText(existedepart);
					int confirm = JOptionPane.showConfirmDialog(this, "ESTAS SEGURO DE MODIFICAR...", "AVISO MODIFICACIÓN.",
							JOptionPane.OK_CANCEL_OPTION);
					if (confirm == 0) {
						modificar(dep);
						mensaje.setText(" REGISTRO MODIFICADO: " + dep);
					}
				} else {
					mensaje.setText(NOEXISTEDEPART);
					nombre.setText(" "); loc.setText(" ");
				}
			} else {
				mensaje.setText("DEPARTAMENTO DEBE SER MAYOR QUE 0");
			}
		} catch (java.lang.NumberFormatException ex) {
			mensaje.setText(depar_error);
		} catch (IOException ex2) {
			mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (MODIFICAR)");
		}
	}

	// --- MÉTODOS DE ACCESO A DATOS ---

	public void verporconsola() throws IOException {
		String nom = "", localidad = ""; int dep = 0; long pos;
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
				localidad = new String(cad);
				System.out.println("DEP: " + dep + ", Nombre: " + nom + ", Localidad: " + localidad);
				pos = pos + 44;
				if (file.getFilePointer() == file.length()) break;
			}
			file.close();
			System.out.println(" ------------------------------------------");
		} else {
			System.out.println(" ---------FICHERO VACIÍO --------------------");
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
		long pos; int depa;
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
			nombre.setText(new String(nom1));
			loc.setText(new String(loc1));
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void borrar(int dep) {
		long pos; File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			pos = 44 * (dep - 1);
			file.seek(pos);
			file.writeInt(0);
			StringBuffer buffer = new StringBuffer("");
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			file.writeChars(buffer.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void modificar(int dep) {
		long pos; File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			pos = 44 * (dep - 1);
			file.seek(pos);
			file.writeInt(dep);
			StringBuffer buffer = new StringBuffer(nombre.getText());
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			buffer = new StringBuffer(loc.getText());
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void grabar(int dep, String nom, String local) {
		long pos; File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			pos = 44 * (dep - 1);
			file.seek(pos);
			file.writeInt(dep);
			StringBuffer buffer = new StringBuffer(nom);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			buffer = new StringBuffer(local);
			buffer.setLength(10);
			file.writeChars(buffer.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}