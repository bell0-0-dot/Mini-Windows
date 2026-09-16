
package Insta.PanelesInsta;

import Insta.PanelesInsta.PanelAuth;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import ConfigInsta.ServicioArchivoInsta;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import Excepciones.UsernameDuplicadoException;
import Excepciones.ArchivoCorruptoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import ConfigInsta.Rutas;
import Insta.Genero;
import Insta.SesionActual;
import Insta.UsuarioInsta;
/**
 *
 * @author vasqu
 */
public  class PanelRegistro extends PanelAuth{
    private JTextField campoNombre;
    private JComboBox<Genero> campoGenero;
    private JTextField campoUsername;
    private JPasswordField campoPassword;
    private JPasswordField campoConfirmarPassword;
    private JSpinner campoEdad;
    private JLabel fotoPerfil;
    private JLabel labelAgregarFoto;
    private JLabel labelError;
    private JLabel labelIniciarSesion;
    private static final GridBagConstraints gbcTarjeta = new GridBagConstraints();
    private File archivoFoto;
    private JButton btnRegistrar;





    public PanelRegistro(Consumer<String> navegar) {

        super(navegar);
        configurarEventos();

    }
    @Override
    protected void construirContenidoTarjeta(JPanel tarjeta){

        gbcTarjeta.gridx=0;
        gbcTarjeta.fill=GridBagConstraints.HORIZONTAL;
        gbcTarjeta.weightx=1.0;

        fotoPerfil=new JLabel();

        try{
            ImageIcon iconoOriginal=new ImageIcon(getClass().getResource("/Insta/Imagenes/UserIcon.png"));
            Image iconoUsuario=iconoOriginal.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            fotoPerfil.setIcon(new ImageIcon (iconoUsuario));
            fotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
            fotoPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            fotoPerfil.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8));

        }catch(Exception e){
            System.out.println("Error al cargar la imagen"+e.getMessage());
        }
        gbcTarjeta.gridy=0;
        gbcTarjeta.anchor= GridBagConstraints.CENTER;
        gbcTarjeta.insets = new Insets(10, 10, 2, 10);
        tarjeta.add(fotoPerfil, gbcTarjeta);

        labelAgregarFoto=new JLabel("Agregar foto de perfil");
        labelAgregarFoto.setForeground(new Color(0,149,246));
        labelAgregarFoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelAgregarFoto.setHorizontalAlignment(SwingConstants.CENTER);

        gbcTarjeta.gridy=1;
        gbcTarjeta.insets=new Insets(0,10,15,10);
        tarjeta.add(labelAgregarFoto,gbcTarjeta);

        campoNombre=new JTextField(20);
        campoNombre.setText("Nombre completo");
        campoNombre.setForeground(Color.GRAY);
        campoNombre.setPreferredSize(new Dimension(250, 35));
        campoNombre.addFocusListener(new java.awt.event.FocusAdapter (){
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (campoNombre.getText().equals("Nombre completo")) {
                campoNombre.setText("");
                campoNombre.setForeground(Color.BLACK);
            }
        }
         @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (campoNombre.getText().trim().isEmpty()) {
                campoNombre.setText("Nombre completo");
                campoNombre.setForeground(Color.GRAY);
            }
        }
    });
         gbcTarjeta.gridy=2;
         gbcTarjeta.insets=new Insets(5,10,8,10);
         tarjeta.add(campoNombre,gbcTarjeta);

        JPanel panelGeneroEdad = new JPanel(new GridBagLayout());
        panelGeneroEdad.setOpaque(false);
        campoGenero = new JComboBox<>(Genero.values());

        campoGenero.setBackground(Color.WHITE);
        campoGenero.setForeground(Color.BLACK);
        campoGenero.setPreferredSize(new Dimension(170, 35));
        panelGeneroEdad.add(campoGenero);

        campoEdad = new JSpinner();

        SpinnerNumberModel modeloEdad = new SpinnerNumberModel(
            18,  
            1,   
            100,
            1    
        );

        campoEdad.setModel(modeloEdad);
        GridBagConstraints gbcGenero = new GridBagConstraints();
        gbcGenero.gridx = 0;
        gbcGenero.gridy = 0;
        gbcGenero.fill = GridBagConstraints.HORIZONTAL;
        gbcGenero.weightx = 1.0;
        gbcGenero.insets = new Insets(0, 0, 0, 5);

        panelGeneroEdad.add(campoGenero, gbcGenero);
        campoEdad.setPreferredSize(new Dimension(75, 35));
        GridBagConstraints gbcEdad = new GridBagConstraints();
        gbcEdad.gridx = 1;
        gbcEdad.gridy = 0;
        gbcEdad.fill = GridBagConstraints.HORIZONTAL;
        gbcEdad.weightx = 0.0;

        panelGeneroEdad.add(campoEdad, gbcEdad);

        gbcTarjeta.gridx = 0;
        gbcTarjeta.gridy = 3;
        gbcTarjeta.fill = GridBagConstraints.HORIZONTAL;
        gbcTarjeta.weightx = 1.0;
        gbcTarjeta.insets = new Insets(5, 10, 8, 10);

        tarjeta.add(panelGeneroEdad, gbcTarjeta);

         campoUsername=new JTextField();
         campoUsername.setText("Usuario");
         campoUsername.setForeground(Color.GRAY);
         campoUsername.setPreferredSize(new Dimension(250,35));
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
         gbcTarjeta.gridy=4;
         gbcTarjeta.insets=new Insets(5,10,8,10);
         tarjeta.add(campoUsername, gbcTarjeta);


         JPanel panelPassword=new JPanel(new BorderLayout());
         panelPassword.setPreferredSize(new Dimension(250,35));
         panelPassword.setBackground(Color.WHITE);
         panelPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

         campoPassword = new JPasswordField(20);
         campoPassword.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
         char ecoOriginal = campoPassword.getEchoChar();
         campoPassword.setText("Contraseña");
         campoPassword.setForeground(Color.GRAY);
         campoPassword.setEchoChar((char) 0);

         JLabel labelOjo1=new JLabel();
         JLabel labelOjo2=new JLabel();
         try {
        ImageIcon ojo1Original = new ImageIcon(getClass().getResource("/Insta/Imagenes/ojo1.png"));
        Image ojo1 = ojo1Original.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        labelOjo1.setIcon(new ImageIcon(ojo1));

        ImageIcon ojo2Orriginal=new ImageIcon(getClass().getResource("/Insta/Imagenes/ojo2.png"));
        Image ojo2=ojo2Orriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        labelOjo2.setIcon(new ImageIcon(ojo2));

    } catch (Exception e) {
        System.err.println("No se pudo cargar la imagen: " + e.getMessage());
    }
        labelOjo1.setCursor(
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        labelOjo2.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        labelOjo1.setBorder(
            BorderFactory.createEmptyBorder(0, 5, 0, 8)
        );

        labelOjo2.setBorder(
            BorderFactory.createEmptyBorder(0, 5, 0, 8)
        ); 



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
         labelOjo1.addMouseListener(
    new java.awt.event.MouseAdapter() {

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {


        campoPassword.setEchoChar((char) 0);


        panelPassword.remove(labelOjo1);
        panelPassword.add(labelOjo2, BorderLayout.EAST);

        panelPassword.revalidate();
        panelPassword.repaint();
    }
});


    labelOjo2.addMouseListener(
    new java.awt.event.MouseAdapter() {

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {


        campoPassword.setEchoChar(ecoOriginal);


        panelPassword.remove(labelOjo2);
        panelPassword.add(labelOjo1, BorderLayout.EAST);

        panelPassword.revalidate();
        panelPassword.repaint();
    }
});


         panelPassword.add(campoPassword, BorderLayout.CENTER);
         panelPassword.add(labelOjo1, BorderLayout.EAST);

         gbcTarjeta.gridx=0;
         gbcTarjeta.gridy=5;
         gbcTarjeta.fill = GridBagConstraints.HORIZONTAL;
         gbcTarjeta.weightx = 1.0;
         gbcTarjeta.insets = new Insets(5, 10, 8, 10);

         tarjeta.add(panelPassword, gbcTarjeta);

         gbcTarjeta.gridx=0;
         gbcTarjeta.gridy=6;
         gbcTarjeta.fill = GridBagConstraints.HORIZONTAL;
         gbcTarjeta.weightx = 1.0;
         gbcTarjeta.insets = new Insets(5, 10, 8, 10);

         tarjeta.add(agregarCampoConfirmarPass(), gbcTarjeta);

        labelError=new JLabel();
        labelError.setHorizontalAlignment(SwingConstants.CENTER);
        labelError.setFont(new Font("Arial", Font.PLAIN, 12)); 

        gbcTarjeta.gridx = 0;
        gbcTarjeta.gridy = 7;
        gbcTarjeta.fill = GridBagConstraints.HORIZONTAL;
        gbcTarjeta.weightx = 1.0;
        gbcTarjeta.anchor = GridBagConstraints.CENTER;
        gbcTarjeta.insets = new Insets(0, 10, 5, 10);

        tarjeta.add(labelError, gbcTarjeta);



         btnRegistrar=new JButton("Registrar");
         btnRegistrar.setBackground(new Color(0,149,246));
         btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(250, 35));
        btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbcTarjeta.gridx = 0;
        gbcTarjeta.gridy = 8;
        gbcTarjeta.fill = GridBagConstraints.NONE;
        gbcTarjeta.weightx = 1.0;
        gbcTarjeta.anchor = GridBagConstraints.CENTER;
        gbcTarjeta.insets = new Insets(8, 10, 10, 10);

        tarjeta.add(btnRegistrar, gbcTarjeta);
        btnRegistrar();

        labelIniciarSesion=new JLabel("Ya tienes cuenta?. Inicia Sesión");
        labelIniciarSesion.setHorizontalAlignment(SwingConstants.CENTER);
        labelIniciarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelIniciarSesion.setForeground(new Color(0,149,246));
        labelIniciarSesion.setFont(new Font("Arial",Font.PLAIN,12));
        labelIniciars();

        gbcTarjeta.gridy=9;
        gbcTarjeta.fill = GridBagConstraints.NONE;
        gbcTarjeta.weightx = 1.0;
        gbcTarjeta.anchor = GridBagConstraints.CENTER;
        gbcTarjeta.insets = new Insets(8, 10, 10, 10);

         tarjeta.add(labelIniciarSesion,gbcTarjeta);
    }

    private JPanel agregarCampoConfirmarPass(){
        JPanel panelPassword=new JPanel(new BorderLayout());
         panelPassword.setPreferredSize(new Dimension(250,35));
         panelPassword.setBackground(Color.WHITE);
         panelPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

         campoConfirmarPassword = new JPasswordField(20);
         campoConfirmarPassword.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
         char ecoOriginal = campoConfirmarPassword.getEchoChar();
         campoConfirmarPassword.setText("Confirmar contraseña");
         campoConfirmarPassword.setForeground(Color.GRAY);
         campoConfirmarPassword.setEchoChar((char) 0);

         JLabel labelOjo1=new JLabel();
         JLabel labelOjo2=new JLabel();
         try {
        ImageIcon ojo1Original = new ImageIcon(getClass().getResource("/Insta/Imagenes/ojo1.png"));
        Image ojo1 = ojo1Original.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        labelOjo1.setIcon(new ImageIcon(ojo1));

        ImageIcon ojo2Orriginal=new ImageIcon(getClass().getResource("/Insta/Imagenes/ojo2.png"));
        Image ojo2=ojo2Orriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        labelOjo2.setIcon(new ImageIcon(ojo2));

    } catch (Exception e) {
        System.err.println("No se pudo cargar la imagen: " + e.getMessage());
    }
        labelOjo1.setCursor(
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        labelOjo2.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        labelOjo1.setBorder(
            BorderFactory.createEmptyBorder(0, 5, 0, 8)
        );

        labelOjo2.setBorder(
            BorderFactory.createEmptyBorder(0, 5, 0, 8)
        ); 



         campoConfirmarPassword.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            String passTexto = String.valueOf(campoConfirmarPassword.getPassword());
            if (passTexto.equals("Confirmar contraseña")) {
                campoConfirmarPassword.setText("");
                campoConfirmarPassword.setEchoChar(ecoOriginal);
                campoConfirmarPassword.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (campoConfirmarPassword.getPassword().length == 0) {
                campoConfirmarPassword.setText("Confirmar contraseña");
                campoConfirmarPassword.setEchoChar((char) 0);
                campoConfirmarPassword.setForeground(Color.GRAY);
            }
        }
    });
         labelOjo1.addMouseListener(
    new java.awt.event.MouseAdapter() {

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {


        campoConfirmarPassword.setEchoChar((char) 0);


        panelPassword.remove(labelOjo1);
        panelPassword.add(labelOjo2, BorderLayout.EAST);

        panelPassword.revalidate();
        panelPassword.repaint();
    }
});


    labelOjo2.addMouseListener(
    new java.awt.event.MouseAdapter() {

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {


        campoConfirmarPassword.setEchoChar(ecoOriginal);


        panelPassword.remove(labelOjo2);
        panelPassword.add(labelOjo1, BorderLayout.EAST);

        panelPassword.revalidate();
        panelPassword.repaint();
    }
});


         panelPassword.add(campoConfirmarPassword, BorderLayout.CENTER);
         panelPassword.add(labelOjo1, BorderLayout.EAST);

         return panelPassword;
    }


    private void labelIniciars(){
        labelIniciarSesion.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                limpiarCampos();
                navegar.accept("login");

            }
        });

    }

    private void btnRegistrar(){
        btnRegistrar.addActionListener((ActionEvent e) -> {
            String password1=String.valueOf(campoPassword.getPassword());
            String password2=String.valueOf(campoConfirmarPassword.getPassword());

            if(confirmarPassword(password1,password2)){
                if(validarPassword(password1)){
                    mostrarMsj("Contraseña válida",Color.GREEN);
                    intentarRegistro();
                }

            }
        });

    }
    private boolean confirmarPassword(String Password1, String Password2){
        if(Password1.equals("Contraseña")||Password1.isEmpty()){
            mostrarMsj("Ingrese una contraseña",Color.RED);
            return false;
        }else if(Password2.equals("Confirmar contraseña")|| Password2.isEmpty()){
            mostrarMsj("Confirma tu contraseña",Color.RED);
            return false;
        }else if(!Password1.equals(Password2)){
            mostrarMsj("Las contraseñas no coinciden",Color.RED);
            return false;
        }else{
            mostrarMsj("",Color.WHITE);
        }

        return true;
    }

    private boolean validarPassword(String Password){
        if(Password.length()<8){
            mostrarMsj("La contraseña debe tener 8 digitos",Color.RED);
            return false;
        }
        String simbolosPermitidos="!@#$%&*()_";

        boolean tieneNumero=false;
        boolean tieneMayuscula=false;
        boolean tieneSimbolo=false;

        for(char c:Password.toCharArray()){
            if(Character.isUpperCase(c)){
                tieneMayuscula=true;
            }

            if(Character.isDigit(c)){
                tieneNumero=true;
            }
            if(simbolosPermitidos.indexOf(c)!=-1){
                tieneSimbolo=true;
            }
        }

        if(!tieneMayuscula){
            mostrarMsj("La contraseña debe incluir una letra mayúscula",Color.RED);
            return false;
        }
        if(!tieneNumero){
            mostrarMsj("La contraseña debe incluir un digito numerico",Color.RED);
            return false;
        }

        if(!tieneSimbolo){
            mostrarMsj("<html>La contraseña debe tener<br>"
                    + "al menos un carácter especial</html>", Color.RED);
            return false;
        }

        return tieneNumero&&tieneMayuscula&&tieneSimbolo;

    }
    private void seleccionarFoto(){
        JFileChooser chooser = new JFileChooser();
    int resultado = chooser.showOpenDialog(this); 
    if (resultado == JFileChooser.APPROVE_OPTION) {
        archivoFoto = chooser.getSelectedFile();
        ImageIcon imagenElegida = new ImageIcon(archivoFoto.getAbsolutePath());
        Image escalada = imagenElegida.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        fotoPerfil.setIcon(new ImageIcon(escalada));
    }
    }

    private void configurarEventos(){
       MouseAdapter clickFoto = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            seleccionarFoto();
        }
    };
    fotoPerfil.addMouseListener(clickFoto);
    labelAgregarFoto.addMouseListener(clickFoto);

    }

    private boolean validarCamposObligatorios(String nombre,Genero genero, String username){
        if(nombre.isEmpty()||genero.equals("Selecciona_tu_genero")||username.isEmpty()){
            mostrarMsj("Completa todos los campos obligatorios",Color.RED);
            return false;
        }
        return true;
    }



    private void intentarRegistro(){
        String nombre=campoNombre.getText().trim();
        Genero genero=(Genero) campoGenero.getSelectedItem();
        String username=campoUsername.getText().trim();
        char[] passChars = campoPassword.getPassword();
        char[] confirmChars = campoConfirmarPassword.getPassword();
        String password = new String(passChars);
        String confirmacion = new String(confirmChars);
        int edad = (Integer) campoEdad.getValue();

        if(!validarCamposObligatorios(nombre, genero, username)){
            limpiarCampos(passChars, confirmChars);
            return;
        }
        if (!validarPassword(password)) {
            limpiarCampos(passChars, confirmChars);
            return;
        }

        if (!confirmarPassword(password, confirmacion)) {
            limpiarCampos(passChars, confirmChars);
            return;
        }
        String nombreArchivoFoto = (archivoFoto != null) ? archivoFoto.getName() : null;

        try{
            UsuarioInsta nuevo=ServicioArchivoInsta.registrarUsuario(nombre,
                    genero,edad, username, password, nombreArchivoFoto);
            guardarFotoPerfil(username);
            SesionActual.getInstancia().iniciarSesion(nuevo);
            mostrarMsj(" ",Color.RED);
            limpiarCampos();
            navegar.accept("panelApp");


        }catch(UsernameDuplicadoException ex){
            mostrarMsj("Nombre de usuario en uso", Color.RED);
        }catch(ArchivoCorruptoException | IOException ex){
            mostrarMsj("Error al registrar Usuario", Color.RED);
        }finally{
            limpiarCampos(passChars, confirmChars);
        }


    }
    private void guardarFotoPerfil(String username)throws IOException{
        if(archivoFoto==null){
            return;
        }
        String extension = obtenerExtension(archivoFoto.getName());
        String nombreDestino = "perfil" + extension;

        Path carpetaImagenes = Paths.get(Rutas.rutaImagenes(username));
        Files.createDirectories(carpetaImagenes); 

        Path origen = archivoFoto.toPath();
        Path destino = carpetaImagenes.resolve(nombreDestino);

        Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
    }
    private String obtenerExtension(String nombreArchivo){
        int posicion=nombreArchivo.lastIndexOf('.');
        String extension=nombreArchivo.substring(posicion);

        if(posicion==-1){
            return "";
        }
        return extension;
    }

    private void limpiarCampos(char[] a, char[] b){
        java.util.Arrays.fill(a, ' ');
        java.util.Arrays.fill(b, ' ');
    }


    private void mostrarMsj(String texto, Color color){
        labelError.setText(texto);
        labelError.setForeground(color);
    }

    @Override
    protected void limpiarCampos(){
        campoNombre.setText("Nombre completo");
        campoGenero.setSelectedIndex(0);
        campoUsername.setText("Usuario");
        campoPassword.setText("Contraseña");
        campoConfirmarPassword.setText("Confirmar contraseña");
        campoEdad.setValue(18); 
        archivoFoto = null;
        ImageIcon iconoOriginal=new ImageIcon(getClass().getResource("/Insta/Imagenes/UserIcon.png"));
        Image iconoUsuario=iconoOriginal.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        fotoPerfil.setIcon(new ImageIcon(iconoUsuario)); 
        labelError.setText(" ");
    }

}
