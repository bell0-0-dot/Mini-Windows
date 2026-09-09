
package Insta;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author vasqu
 */
public class LoginJFrame extends JFrame{

    public LoginJFrame() {
        this.setTitle("Instagram");
        try {
        Image iconoVentana = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Insta/Imagenes/LogoBarra.png"));
        this.setIconImage(iconoVentana);
    } catch (Exception e) {
        System.err.println("No se pudo cargar el icono de la ventana: " + e.getMessage());
}
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new VentanaLogin());
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
     try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("No se pudo cargar el LookAndFeel: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            LoginJFrame frame = new LoginJFrame();
            frame.setVisible(true);
        });
    }
    
}
