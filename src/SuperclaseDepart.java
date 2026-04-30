import javax.swing.JTextField;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SuperclaseDepart {
    // Atributos protegidos para que VentanaDepart pueda acceder a ellos
    protected JTextField nombre = new JTextField(25);
    protected JTextField loc = new JTextField(25);

    // Método para grabar un registro (Refactorizado con Inline)
    public void grabar(int dep, String nom, String local) {
        try {
            RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "rw");
            file.seek(44 * (dep - 1));
            file.writeInt(dep);
            StringBuffer b = new StringBuffer(nom); b.setLength(10);
            file.writeChars(b.toString());
            b = new StringBuffer(local); b.setLength(10);
            file.writeChars(b.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para visualizar un registro en los campos de texto
    public void visualiza(int dep) {
        try {
            RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "r");
            file.seek(44 * (dep - 1));
            file.readInt(); // Saltar el entero del ID
            char[] n = new char[10], l = new char[10];
            for(int i=0; i<10; i++) n[i] = file.readChar();
            for(int i=0; i<10; i++) l[i] = file.readChar();
            nombre.setText(new String(n).trim());
            loc.setText(new String(l).trim());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para modificar (basado en la lógica de grabar)
    public void modificar(int dep) {
        grabar(dep, nombre.getText(), loc.getText());
    }

    // Método para borrar (pone el ID a 0)
    public void borrar(int dep) {
        try {
            RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "rw");
            file.seek(44 * (dep - 1));
            file.writeInt(0);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}