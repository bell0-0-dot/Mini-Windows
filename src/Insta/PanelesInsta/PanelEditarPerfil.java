/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.Rutas;
import ConfigInsta.ServicioArchivoInsta;
import Insta.Genero;
import Insta.NavegarInsta;
import Insta.SesionActual;
import Insta.UsuarioInsta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author vasqu
 */
public class PanelEditarPerfil extends JPanel{
    private NavegarInsta navegador;
    private UsuarioInsta usuarioActual;
    private AvatarCircular avatarPerfil;
    private JLabel lblFotoPerfil;
    private JButton btnCambiarFoto;
    private String rutaNuevaFoto = null;
    private JTextField txtNombre;
    private JComboBox<Genero> cbGenero;
    private JSpinner spEdad;
    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JButton btnGuardar;
    private JButton btnDesactivar;
    
    public PanelEditarPerfil(NavegarInsta navegador) {
        this.navegador = navegador;
        this.usuarioActual = (UsuarioInsta) SesionActual.getInstancia().getUserActual();

        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        inicializarInterfaz();
        cargarDatosUsuario();
    }
    
    private void inicializarInterfaz() {
        
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(200, 0));

        try {
            avatarPerfil = AvatarCircular.crear(usuarioActual.getUser(), 120);
        } catch (Exception e) {
            Image defaultImg = new ImageIcon(getClass().getResource("/Insta/Imagenes/UserIcon.png")).getImage();
            avatarPerfil = new AvatarCircular(defaultImg, 120);
        }
        avatarPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCambiarFoto = new JButton("Cambiar foto");
        btnCambiarFoto.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCambiarFoto.setForeground(new Color(0, 149, 246));
        btnCambiarFoto.setContentAreaFilled(false);
        btnCambiarFoto.setBorderPainted(false);
        btnCambiarFoto.setFocusPainted(false);
        btnCambiarFoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCambiarFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarFoto.addActionListener(e -> seleccionarNuevaFoto());

        panelIzquierdo.add(Box.createVerticalStrut(20));
        panelIzquierdo.add(avatarPerfil);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnCambiarFoto);

        
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        Font fontLabel = new Font("SansSerif", Font.BOLD, 13);
        Font fontInput = new Font("SansSerif", Font.PLAIN, 13);

   
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel lblNombre = new JLabel("Nombre completo:");
        lblNombre.setFont(fontLabel);
        panelDerecho.add(lblNombre, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNombre = new JTextField();
        txtNombre.setFont(fontInput);
        panelDerecho.add(txtNombre, gbc);


        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setFont(fontLabel);
        panelDerecho.add(lblGenero, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        cbGenero = new JComboBox<>(Genero.values());
        cbGenero.setFont(fontInput);
        cbGenero.setBackground(Color.WHITE);
        panelDerecho.add(cbGenero, gbc);

        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setFont(fontLabel);
        panelDerecho.add(lblEdad, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        spEdad = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
        spEdad.setFont(fontInput);
        panelDerecho.add(spEdad, gbc);

       
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(fontLabel);
        panelDerecho.add(lblUser, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUser = new JTextField();
        txtUser.setFont(fontInput);
        panelDerecho.add(txtUser, gbc);

       
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(fontLabel);
        panelDerecho.add(lblPass, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField();
        txtPassword.setFont(fontInput);
        panelDerecho.add(txtPassword, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        
        btnDesactivar = new JButton("Desactivar cuenta");
        btnDesactivar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnDesactivar.setForeground(new Color(237, 73, 86));
        btnDesactivar.setContentAreaFilled(false);
        btnDesactivar.setBorderPainted(false);
        btnDesactivar.setFocusPainted(false);
        btnDesactivar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDesactivar.addActionListener(e -> desactivarCuenta());

       
        btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(0, 149, 246));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true);
        btnGuardar.setContentAreaFilled(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(150, 35));
        btnGuardar.addActionListener(e -> guardarCambios());

        panelBotones.add(btnDesactivar);
        panelBotones.add(btnGuardar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panelDerecho.add(panelBotones, gbc);

      
        this.add(panelIzquierdo, BorderLayout.WEST);
        this.add(panelDerecho, BorderLayout.CENTER);
    }
    private void cargarDatosUsuario() {
        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre());
            cbGenero.setSelectedItem(usuarioActual.getGenero());
            spEdad.setValue(usuarioActual.getEdad());
            txtUser.setText(usuarioActual.getUser());
            txtPassword.setText(usuarioActual.getPassword());
        }
    }
    private void seleccionarNuevaFoto() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        FileDialog chooser = new FileDialog(parentFrame, "Seleccionar foto de perfil", FileDialog.LOAD);
        chooser.setFile("*.jpg;*.jpeg;*.png");
        chooser.setVisible(true);

        String directorio = chooser.getDirectory();
        String archivo = chooser.getFile();

        if (directorio != null && archivo != null) {
            File seleccionado = new File(directorio, archivo);
            rutaNuevaFoto = seleccionado.getAbsolutePath();

          
            Image nuevaImagen = new ImageIcon(rutaNuevaFoto).getImage();
            avatarPerfil.setImagen(nuevaImagen);
        }
    }
    private void guardarCambios() {
        String nuevoNombre = txtNombre.getText().trim();
        Genero nuevoGenero = (Genero) cbGenero.getSelectedItem();
        int nuevaEdad = (Integer) spEdad.getValue();
        String nuevoUser = txtUser.getText().trim();
        String nuevaPassword = new String(txtPassword.getPassword()).trim();

        if (nuevoNombre.isEmpty() || nuevoUser.isEmpty() || nuevaPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        usuarioActual.setNombre(nuevoNombre);
        usuarioActual.setGenero(nuevoGenero);
        usuarioActual.setEdad(nuevaEdad);
        usuarioActual.setUser(nuevoUser);
        usuarioActual.setPassword(nuevaPassword);

       
        if (rutaNuevaFoto != null) {
            try {
                File origen = new File(rutaNuevaFoto);
                String nombreArchivo = nuevoUser + "_profile.png";
                File destino = new File(Rutas.rutaFotoPerfil(nuevoUser, nombreArchivo), nombreArchivo);

                if (!destino.getParentFile().exists()) {
                    destino.getParentFile().mkdirs();
                }

                Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                usuarioActual.setArchivoFoto(nombreArchivo);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la foto de perfil: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            ServicioArchivoInsta.actualizarUsuario(usuarioActual);
            JOptionPane.showMessageDialog(this, "Perfil actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        }
    
    private void desactivarCuenta() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas desactivar tu cuenta?\nSe cerrará la sesión actual.",
            "Desactivar Cuenta",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
               
                usuarioActual.desactivarUser();

               
                ServicioArchivoInsta.actualizarUsuario(usuarioActual);

                JOptionPane.showMessageDialog(this, "Tu cuenta ha sido desactivada.", "Cuenta Desactivada", JOptionPane.INFORMATION_MESSAGE);

               
                SesionActual.getInstancia().cerrarSesion();
                navegador.cerrarSesion();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al desactivar la cuenta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

