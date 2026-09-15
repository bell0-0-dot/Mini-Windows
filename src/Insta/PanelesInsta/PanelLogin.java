
package Insta.PanelesInsta;

import Insta.PanelesInsta.PanelAuth;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
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
import Insta.SesionActual;
import Insta.UsuarioInsta;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import Insta.NavegarInsta;



/**
 *
 * @author vasqu
 */
public class PanelLogin extends PanelAuth{
   
    private JTextField campoUsername;
    private JPasswordField campoPassword;
    private JLabel labelRegistro;
    private JButton botonIniciarS;
    

    public PanelLogin(Consumer <String> navegar) {
        super(navegar);
       
        setPreferredSize(new java.awt.Dimension(800, 600));
        setFocusable(true);
        configurarEventos();
    }
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); 
    }
    
    
    @Override
    protected void construirContenidoTarjeta(JPanel tarjetaLogin) {
    GridBagConstraints gbcTarjeta = new GridBagConstraints();
    gbcTarjeta.gridx = 0;
    gbcTarjeta.fill = GridBagConstraints.HORIZONTAL;
    gbcTarjeta.weightx = 1.0;

    JLabel labelTitulo = new JLabel("Iniciar sesión en Instagram", SwingConstants.CENTER);
    labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
    gbcTarjeta.gridy = 0;
    gbcTarjeta.insets = new Insets(10, 10, 20, 10);
    tarjetaLogin.add(labelTitulo, gbcTarjeta);

    campoUsername = new JTextField(20);
    campoUsername.setText("Usuario");
    campoUsername.setForeground(Color.GRAY);
    campoUsername.setPreferredSize(new Dimension(250, 35));
    campoUsername.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (campoUsername.getText().equals("Usuario")) {
                campoUsername.setText("");
                campoUsername.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (campoUsername.getText().trim().isEmpty()) {
                campoUsername.setText("Usuario");
                campoUsername.setForeground(Color.GRAY);
            }
        }
    });
    gbcTarjeta.gridy = 1;
    gbcTarjeta.insets = new Insets(5, 10, 8, 10);
    tarjetaLogin.add(campoUsername, gbcTarjeta);

    campoPassword = new JPasswordField(20);
    campoPassword.setPreferredSize(new Dimension(250, 35));
    char ecoOriginal = campoPassword.getEchoChar();
    campoPassword.setText("Contraseña");
    campoPassword.setForeground(Color.GRAY);
    campoPassword.setEchoChar((char) 0);

    JPanel panelPasswordContainer = new JPanel(new BorderLayout());
    panelPasswordContainer.setPreferredSize(new Dimension(250, 35));
    panelPasswordContainer.setBackground(Color.WHITE);
    panelPasswordContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

    campoPassword.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    campoPassword.setBackground(Color.WHITE);

    JLabel labelojo1 = new JLabel();
    try {
        ImageIcon ojo1Original = new ImageIcon(getClass().getResource("/Insta/Imagenes/ojo1.png"));
        Image ojo1 = ojo1Original.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        labelojo1.setIcon(new ImageIcon(ojo1));
        labelojo1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelojo1.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8));
    } catch (Exception e) {
        System.err.println("No se pudo cargar la imagen: " + e.getMessage());
    }

    campoPassword.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            String passTexto = String.valueOf(campoPassword.getPassword());
            if (passTexto.equals("Contraseña")) {
                campoPassword.setText("");
                campoPassword.setEchoChar(ecoOriginal);
                campoPassword.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (campoPassword.getPassword().length == 0) {
                campoPassword.setText("Contraseña");
                campoPassword.setEchoChar((char) 0);
                campoPassword.setForeground(Color.GRAY);
            }
        }
    });

    panelPasswordContainer.add(campoPassword, BorderLayout.CENTER);
    panelPasswordContainer.add(labelojo1, BorderLayout.EAST);

    gbcTarjeta.gridy = 2;
    gbcTarjeta.insets = new Insets(5, 10, 12, 10);
    tarjetaLogin.add(panelPasswordContainer, gbcTarjeta);

    botonIniciarS = new JButton("Iniciar sesión");
    botonIniciarS.setPreferredSize(new Dimension(250, 35));
    botonIniciarS.setBackground(new Color(0, 149, 246));
    botonIniciarS.setForeground(Color.WHITE);
    gbcTarjeta.gridy = 3;
    gbcTarjeta.insets = new Insets(10, 10, 10, 10);
    tarjetaLogin.add(botonIniciarS, gbcTarjeta);

    labelRegistro = new JLabel("¿No tienes cuenta? Regístrate", SwingConstants.CENTER);
    labelRegistro.setForeground(new Color(0, 149, 246));
    labelRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    gbcTarjeta.gridy = 4;
    gbcTarjeta.insets = new Insets(10, 10, 5, 10);
    tarjetaLogin.add(labelRegistro, gbcTarjeta);

    labelError = new JLabel(" ", SwingConstants.CENTER);
    labelError.setForeground(Color.RED);
    gbcTarjeta.gridy = 5;
    tarjetaLogin.add(labelError, gbcTarjeta);
    }
    
    private void configurarEventos() {
        botonIniciarS.addActionListener(e -> intentarLogin()
                
        );

        labelRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                limpiarCampos();
                navegar.accept("registro");
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
            limpiarCampos();
            
            navegar.accept("panelApp");
            
            
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
    @Override
    protected void limpiarCampos(){
        campoUsername.setText("Usuario");
        campoUsername.setForeground(Color.GRAY);
        campoPassword.setText("Contraseña");
        campoPassword.setEchoChar((char) 0);
        campoPassword.setForeground(Color.GRAY);
        labelError.setText(" ");
}
    
}
