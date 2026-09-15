/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mini.windows;

import Excepciones.ArchivoCorruptoException;
import base.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 *
 * @author gabri
 */
public class LoginFrame extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoPassword;

    public LoginFrame() {
        super("Mini-Windows - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 200);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setContentPane(construirFondo());
    }

    private JPanel construirFondo() {
        Image wallpaper = RecursosUI.cargarImagen("WallpaperLogin.png");

        JPanel fondo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (wallpaper != null) {
                    g.drawImage(wallpaper, 0, 0, getWidth(), getHeight(), this);
                } else {
                    Graphics2D g2 = (Graphics2D) g;
                    GradientPaint degradado = new GradientPaint(
                            0, 0, new Color(20, 45, 75),
                            0, getHeight(), new Color(0, 90, 140));
                    g2.setPaint(degradado);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        fondo.add(construirTarjetaLogin());
        return fondo;
    }

    private JPanel construirTarjetaLogin() {
        JPanel tarjeta = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 235));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(30, 35, 30, 35));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Mini-Windows", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        titulo.setForeground(new Color(30, 40, 55));
        c.gridx = 0; 
        c.gridy = 0; 
        c.gridwidth = 2;
        tarjeta.add(titulo, c);
        c.gridwidth = 1;

        c.gridx = 0; 
        c.gridy = 1;
        tarjeta.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        campoUsuario = new JTextField(15);
        tarjeta.add(campoUsuario, c);

        c.gridx = 0; 
        c.gridy = 2;
        tarjeta.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        campoPassword = new JPasswordField(15);
        tarjeta.add(campoPassword, c);

        JButton botonEntrar = new BotonPlano("Iniciar sesión", null,
                new Color(0, 120, 215), new Color(0, 99, 177), Color.WHITE);
        botonEntrar.setPreferredSize(new Dimension(0, 34));
        botonEntrar.addActionListener(e -> intentarLogin());
        c.gridx = 0; 
        c.gridy = 3; 
        c.gridwidth = 2;
        c.insets = new Insets(16, 6, 6, 6);
        tarjeta.add(botonEntrar, c);

        return tarjeta;
    }

    private void intentarLogin() {
        String username = campoUsuario.getText().trim();
        String password = new String(campoPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa usuario y contraseña.");
            return;
        }

        try {
            Usuario usuario = GestorArchivos.buscarUsuario(username);

            if (usuario == null) {
                mostrarErrorYPreguntar("No existe ese usuario.");
                return;
            }
            if (!usuario.isActivo()) {
                JOptionPane.showMessageDialog(this, "Esta cuenta está desactivada.",
                        "Cuenta desactivada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!usuario.verificarPassword(password)) {
                mostrarErrorYPreguntar("Contraseña incorrecta.");
                return;
            }

            SesionActual.iniciarSesion(usuario);
            new VentanaPrincipal().setVisible(true);
            dispose();

        } catch (ArchivoCorruptoException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer el archivo de usuarios: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarErrorYPreguntar(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(this,
                mensaje + "\n¿Deseas crear una cuenta nueva?",
                "Error de inicio de sesión",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            abrirCreacionDeCuenta();
        }
    }

    private void abrirCreacionDeCuenta() {
        String nuevoUsuario = JOptionPane.showInputDialog(this, "Nuevo username:");
        if (nuevoUsuario == null || nuevoUsuario.trim().isEmpty()) return;

        String nuevaPassword = JOptionPane.showInputDialog(this, "Nueva contraseña:");
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) return;

        try {
            GestorArchivos.crearUsuario(nuevoUsuario.trim(), nuevaPassword, false);
            JOptionPane.showMessageDialog(this, "Cuenta creada. Ahora puedes iniciar sesión.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo crear la cuenta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}