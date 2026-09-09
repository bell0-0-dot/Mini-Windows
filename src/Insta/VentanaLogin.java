
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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;




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
    private Image Fondo;

    public VentanaLogin() {
        this.Fondo=new ImageIcon(getClass().getResource("/Insta/Imagenes/FondoLogin.jpg")).getImage();
        setPreferredSize(new java.awt.Dimension(800, 600));
        setFocusable(true);
        construirInterfaz();
        configurarEventos();
    }
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); 
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (Fondo != null) {
           
            g.drawImage(Fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

   
    private void construirInterfaz(){
        this.setLayout(new GridLayout(1, 2, 20, 0));
        this.setOpaque(false);
        
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setOpaque(false);

        GridBagConstraints gbcIzq = new GridBagConstraints();
        gbcIzq.gridx = 0;
        gbcIzq.anchor = GridBagConstraints.NONE;
        

        
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Insta/Imagenes/InstaLogo.png")); 
            Image logoImg = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel labelLogo = new JLabel(new ImageIcon(logoImg));
            gbcIzq.gridy = 0;
            gbcIzq.anchor = GridBagConstraints.NORTH;
            gbcIzq.insets = new Insets(0, 10, 5, 10);
            gbcIzq.weighty = 0.0;
            panelIzquierdo.add(labelLogo, gbcIzq);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo de Instagram");
        }
        gbcIzq.anchor = GridBagConstraints.CENTER;
       
        JLabel labelSlogan = new JLabel("<html><div style='text-align: center;'>Mira los momentos cotidianos de<br><b style="
                + "'color:#E1306C; font-size:16px;'>tus mejores amigos.</b></div></html>");
        labelSlogan.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbcIzq.gridy = 1;
        gbcIzq.insets = new Insets(5, 10, 20, 10);
        panelIzquierdo.add(labelSlogan, gbcIzq);

        
        try {
            ImageIcon promoIcon = new ImageIcon(getClass().getResource("/Insta/Imagenes/InstaPromo.png"));
            Image promoImg = promoIcon.getImage().getScaledInstance(350, 300, Image.SCALE_SMOOTH);
            JLabel labelImagenPromo = new JLabel(new ImageIcon(promoImg));
            gbcIzq.gridy = 2;
            gbcIzq.insets = new Insets(10, 10, 10, 10);
            panelIzquierdo.add(labelImagenPromo, gbcIzq);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen promocional");
        }


        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        JPanel tarjetaLogin = new JPanel(new GridBagLayout()) {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

       
        g2.setColor(new Color(255, 255, 255, 170)); 
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); 

        
        g2.setColor(new Color(255, 255, 255, 220));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

        g2.dispose();
        super.paintComponent(g);
    }
};
        tarjetaLogin.setOpaque(false);
        tarjetaLogin.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); 

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
        
        
        campoPassword.addFocusListener(new java.awt.event.FocusAdapter (){
            public void focusGained(java.awt.event.FocusEvent e){
                String passTexto = String.valueOf(campoPassword.getPassword());
                if (passTexto.equals("Contraseña")) {
                campoPassword.setText("");
                campoPassword.setEchoChar(ecoOriginal); 
                campoPassword.setForeground(Color.BLACK);
        }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campoPassword.getText().trim().isEmpty()) {
                    campoPassword.setText("Contraseña");
                    campoPassword.setForeground(Color.GRAY);
                }
            }
    });
       JPanel panelPasswordContainer = new JPanel(new BorderLayout());
        panelPasswordContainer.setPreferredSize(new Dimension(250, 35));
        panelPasswordContainer.setBackground(Color.WHITE);
        panelPasswordContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); 

        campoPassword = new JPasswordField();
        campoPassword.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); 
        campoPassword.setBackground(Color.WHITE);

        campoPassword.setText("Contraseña");
        campoPassword.setForeground(Color.GRAY);
        campoPassword.setEchoChar((char) 0);

        
        JLabel labelojo1 = new JLabel();
        try {
            ImageIcon ojo1Original = new ImageIcon(getClass().getResource("/Insta/Imagenes/ojo1.png"));
            Image ojo1 = ojo1Original.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            labelojo1.setIcon(new ImageIcon(ojo1));
            labelojo1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            labelojo1.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8)); 
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen del ojo: " + e.getMessage());
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

        
        GridBagConstraints gbcDer = new GridBagConstraints();
        gbcDer.gridx = 0;
        gbcDer.gridy = 0;
        panelDerecho.add(tarjetaLogin, gbcDer);
        this.add(panelIzquierdo);
        this.add(panelDerecho);
        

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
