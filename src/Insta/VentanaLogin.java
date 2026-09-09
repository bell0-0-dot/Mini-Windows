
package Insta;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ConfigInsta.ServicioArchivoInsta;
import Excepciones.ArchivoCorruptoException;
import Excepciones.PasswordIncorrectoException;
import Excepciones.CuentaDesactivadaException;
import Excepciones.UsuarioInexistenteException;
import java.io.IOException;




/**
 *
 * @author vasqu
 */
public class VentanaLogin extends JPanel{
    private JTextField campoUsername;
    private JPasswordField campoPassword;
    private JLabel labelError;
    private JLabel labelRegistro;
    private JButton botonIniciarS;

    public VentanaLogin() {
        construirInterfaz();
        configurarEventos();
    }

   
    private void construirInterfaz(){
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        
        ImageIcon icono = new ImageIcon("/Insta/Imagenes/InstaLogo.png");
         JLabel labelLogo = new JLabel(icono);
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 20, 10);
        this.add(labelLogo, gbc);
        
        JLabel labelUsuario = new JLabel("Usuario");
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 10, 2, 10);
        this.add(labelUsuario, gbc);
        
        campoUsername = new JTextField(20);
        gbc.gridy = 2;
        gbc.insets = new Insets(2, 10, 10, 10);
        this.add(campoUsername, gbc);
        
        JLabel labelPassword = new JLabel("Contraseña");
        gbc.gridy = 3;
        gbc.insets = new Insets(4, 10, 2, 10);
        this.add(labelPassword, gbc);
        
        campoPassword = new JPasswordField(20);
        gbc.gridy = 4;
        gbc.insets = new Insets(2, 10, 10, 10);
        this.add(campoPassword, gbc);
        
        botonIniciarS = new JButton("Iniciar sesión");
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 5, 10);
        this.add(botonIniciarS, gbc);
        
        labelRegistro = new JLabel("¿Deseas registrarte?");
        labelRegistro.setForeground(Color.BLUE);
        labelRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(2, 10, 10, 10);
        this.add(labelRegistro, gbc);
        
        labelError = new JLabel(" "); 
        labelError.setForeground(Color.RED);
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 10, 10);
        this.add(labelError, gbc);

    }
    
    private void configurarEventos() {
        botonIniciarS.addActionListener(e -> intentarLogin());

        labelRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Aquí, cuando integres CardLayout, cambiarás al panel de registro
                System.out.println("Ir a pantalla de registro");
            }
        });
    }
    
    private void intentarLogin() {
        String username = campoUsername.getText();
        char[] passwordChars = campoPassword.getPassword();
        String password = new String(passwordChars);

        try {
            UsuarioInsta usuario = ServicioArchivoInsta.validarLogin(username, password);
            SesionActual.getInstancia().iniciarSesion(usuario);
            labelError.setText(" ");
            System.out.println("Login exitoso: " + username);
            // Aquí, con CardLayout, cambiarías al PanelTimeline
            System.out.println("Logeado");
        } catch (UsuarioInexistenteException ex) {
            labelError.setText("El usuario no existe.");
        } catch (PasswordIncorrectoException ex) {
            labelError.setText("Contraseña incorrecta.");
        } catch (CuentaDesactivadaException ex) {
            labelError.setText("Esta cuenta está desactivada.");
        } catch (ArchivoCorruptoException | IOException ex) {
            labelError.setText("Error del sistema. Intenta de nuevo.");
        } finally {
            java.util.Arrays.fill(passwordChars, ' ');
        }
    }
    
}
