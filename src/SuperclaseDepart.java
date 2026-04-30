import javax.swing.JTextField;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SuperclaseDepart {
    // Se mueven los campos aquí para que sean accesibles por la subclase
    // Usamos 'protected' para que VentanaDepart pueda verlos
    protected JTextField nombre = new JTextField(25);
    protected JTextField loc = new JTextField(25);

    // Métodos extraídos según la actividad 4.11
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

    public void visualiza(int dep) {
        try {
            RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "r");
            file.seek(44 * (dep - 1));
            file.readInt(); // Saltar ID
            char[] n = new char[10], l = new char[10];
            for(int i=0; i<10; i++) n[i]=file.readChar();
            for(int i=0; i<10; i++) l[i]=file.readChar();
            nombre.setText(new String(n));
            loc.setText(new String(l));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}