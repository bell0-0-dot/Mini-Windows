/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mini.windows;

import Excepciones.ArchivoCorruptoException;
import base.Usuario;
 
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author gabri
 */
public class LoginFrame extends JFrame{
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
 
    public LoginFrame() {
        super("Mini-Windows - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 200);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
 
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
 
        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        campoUsuario = new JTextField(15);
        panel.add(campoUsuario, c);
 
        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        campoPassword = new JPasswordField(15);
        panel.add(campoPassword, c);
 
        JButton botonEntrar = new JButton("Iniciar sesión");
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        panel.add(botonEntrar, c);
 
        botonEntrar.addActionListener(e -> intentarLogin());
 
        add(panel);
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
