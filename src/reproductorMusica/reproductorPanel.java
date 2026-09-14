package reproductorMusica;

import base.ListaEnlazada;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class reproductorPanel extends JPanel {

    private File carpetaRaizUsuario;
    private File carpetaActual;
    private ListaEnlazada<File> musicaCarpetaActual;
    private Cancion cancionActual;
    
    private Reproductor reproductor = new Reproductor();
    private Timer timerProgreso;
    private int segundoActual = 0;

    private JList<Cancion> listaCanciones;
    private JLabel imagenCancion;
    private JLabel labelTituloCancion;
    private JLabel labelArtistaCancion;
    private JSlider playbackSlider;
    private JButton btnAnterior;
    private JButton btnPlayPause;
    private JButton btnSiguiente;

    private ImageIcon iconPlay;
    private ImageIcon iconPause;

    public reproductorPanel(String rutaRaizUsuario) {
        this.carpetaRaizUsuario = new File(rutaRaizUsuario);
        setLayout(new BorderLayout());

        iconPlay = cargarIconoControl("play.png", 24, 24);
        iconPause = cargarIconoControl("pause.png", 24, 24);

        reproductor.setAlTerminarCancion(() -> siguienteCancion());

        add(construirPanelListaMusica(), BorderLayout.CENTER);
        add(construirPanelCancionActual(), BorderLayout.EAST);

            timerProgreso = new Timer(1000, e -> {
            if (reproductor.isReproduciendo() && !playbackSlider.getValueIsAdjusting()) {
                segundoActual++;
                if (segundoActual <= playbackSlider.getMaximum()) {
                    playbackSlider.setValue(segundoActual);
                }
            }
        });
    }

    private JPanel construirPanelListaMusica() {
        DefaultListModel<Cancion> modelo = new DefaultListModel<>();
        listaCanciones = new JList<>(modelo);

        listaCanciones.setCellRenderer(new elementoCancionRender());
        listaCanciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCanciones.setFixedCellHeight(60);

        File carpetaMusica = new File(carpetaRaizUsuario, "Musica");
        if (carpetaMusica.exists()) {
            cargarCarpeta(carpetaMusica);
        } else {
            carpetaMusica.mkdir();
            cargarCarpeta(carpetaMusica);
        }

        for (int i = 0; i < musicaCarpetaActual.length(); i++) {
            modelo.addElement(new Cancion(musicaCarpetaActual.obtenerEn(i)));
        }

        listaCanciones.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Cancion seleccionada = listaCanciones.getSelectedValue();
                if (seleccionada != null) {
                    reproducirSeleccionada(seleccionada);
                }
            }
        });

        JPanel panelListaPrincipal = new JPanel(new BorderLayout());
        panelListaPrincipal.add(new JScrollPane(listaCanciones));

        return panelListaPrincipal;
    }

    private JPanel construirPanelCancionActual() {
        JPanel panelCancionActual = new JPanel(new BorderLayout(10, 10));
        panelCancionActual.setPreferredSize(new Dimension(300, 0));
        panelCancionActual.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JPanel panelInfoCentral = new JPanel();
        panelInfoCentral.setLayout(new BoxLayout(panelInfoCentral, BoxLayout.Y_AXIS));

        imagenCancion = new JLabel(cargarImagen());
        imagenCancion.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelTituloCancion = new JLabel("Selecciona una canción");
        labelTituloCancion.setFont(new Font("Arial", Font.BOLD, 16));
        labelTituloCancion.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelArtistaCancion = new JLabel("---");
        labelArtistaCancion.setFont(new Font("Arial", Font.PLAIN, 13));
        labelArtistaCancion.setForeground(Color.GRAY);
        labelArtistaCancion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfoCentral.add(Box.createVerticalStrut(10));
        panelInfoCentral.add(imagenCancion);
        panelInfoCentral.add(Box.createVerticalStrut(15));
        panelInfoCentral.add(labelTituloCancion);
        panelInfoCentral.add(Box.createVerticalStrut(5));
        panelInfoCentral.add(labelArtistaCancion);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));

        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (cancionActual != null) {
                    segundoActual = playbackSlider.getValue();
                    reproductor.saltarA(segundoActual);
                    btnPlayPause.setIcon(iconPause);
                    timerProgreso.start();
                }
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        btnAnterior = new JButton(cargarIconoControl("previous.png", 20, 20));
        btnPlayPause = new JButton(iconPlay);
        btnSiguiente = new JButton(cargarIconoControl("next.png", 20, 20));

        estilarBoton(btnAnterior);
        estilarBoton(btnPlayPause);
        estilarBoton(btnSiguiente);

        btnPlayPause.addActionListener(e -> togglePlayPause());
        btnSiguiente.addActionListener(e -> siguienteCancion());
        btnAnterior.addActionListener(e -> anteriorCancion());

        panelBotones.add(btnAnterior);
        panelBotones.add(btnPlayPause);
        panelBotones.add(btnSiguiente);

        panelInferior.add(playbackSlider);
        panelInferior.add(Box.createVerticalStrut(10));
        panelInferior.add(panelBotones);

        panelCancionActual.add(panelInfoCentral, BorderLayout.CENTER);
        panelCancionActual.add(panelInferior, BorderLayout.SOUTH);

        return panelCancionActual;
    }

    private void reproducirSeleccionada(Cancion cancion) {
        this.cancionActual = cancion;
        labelTituloCancion.setText(cancion.getTitulo());
        labelArtistaCancion.setText(cancion.getArtista());
        imagenCancion.setIcon(cargarImagen());

        segundoActual = 0;
        playbackSlider.setMinimum(0);
        playbackSlider.setMaximum(cancion.getDuracionSegundos());
        playbackSlider.setValue(0);

        reproductor.cargarCancion(cancion);
        btnPlayPause.setIcon(iconPause);
        timerProgreso.start();
    }

    private void togglePlayPause() {
        if (cancionActual == null) return;

        reproductor.alternarPlayPausa();

        if (reproductor.isReproduciendo()) {
            btnPlayPause.setIcon(iconPause);
            timerProgreso.start();
        } else {
            btnPlayPause.setIcon(iconPlay);
            timerProgreso.stop();
        }
    }

    private void siguienteCancion() {
        int index = listaCanciones.getSelectedIndex();
        int total = listaCanciones.getModel().getSize();
        if (total == 0) return;

        if (index < total - 1) {
            listaCanciones.setSelectedIndex(index + 1);
        } else {
            listaCanciones.setSelectedIndex(0);
        }
    }

    private void anteriorCancion() {
        int index = listaCanciones.getSelectedIndex();
        int total = listaCanciones.getModel().getSize();
        if (total == 0) return;

        if (index > 0) {
            listaCanciones.setSelectedIndex(index - 1);
        } else {
            listaCanciones.setSelectedIndex(total - 1);
        }
    }

    private ImageIcon cargarIconoControl(String nombreArchivo, int ancho, int alto) {
        try {
            var resource = getClass().getResource("/reproductorMusica/res/" + nombreArchivo);
            if (resource != null) {
                ImageIcon original = new ImageIcon(resource);
                Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                return new ImageIcon(escalada);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + nombreArchivo);
        }
        return null;
    }

    private ImageIcon cargarImagen() {
        try {
            BufferedImage imagen;
            if (cancionActual != null && cancionActual.getPortada() != null) {
                imagen = cancionActual.getPortada();
            } else {
                var resource = getClass().getResourceAsStream("/reproductorMusica/res/DefaultSongCover.png");
                if (resource != null) {
                    imagen = ImageIO.read(resource);
                } else {
                    return null;
                }
            }
            Image imagenEscalada = imagen.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenEscalada);

        } catch (Exception e) {
            return null;
        }
    }

    private void estilarBoton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void cargarCarpeta(File carpeta) {
        carpetaActual = carpeta;
        musicaCarpetaActual = new ListaEnlazada<>();

        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (!archivo.isDirectory() && esCancion(archivo)) {
                    musicaCarpetaActual.insertarFinal(archivo);
                }
            }
        }
    }

    private boolean esCancion(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".mp3");
    }

    private class elementoCancionRender extends JPanel implements ListCellRenderer<Cancion> {
        private JLabel lblPortada = new JLabel();
        private JLabel lblTitulo = new JLabel();
        private JLabel lblAutor = new JLabel();

        public elementoCancionRender() {
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JPanel panelTextos = new JPanel(new GridLayout(2, 1));
            panelTextos.setOpaque(false);

            lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
            lblAutor.setFont(new Font("Arial", Font.PLAIN, 12));
            lblAutor.setForeground(Color.GRAY);

            panelTextos.add(lblTitulo);
            panelTextos.add(lblAutor);

            add(lblPortada, BorderLayout.WEST);
            add(panelTextos, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Cancion> list, Cancion cancion,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            lblTitulo.setText(cancion.getTitulo());
            lblAutor.setText(cancion.getArtista());

            if (cancion.getPortada() != null) {
                Image imgEscalada = cancion.getPortada().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                lblPortada.setIcon(new ImageIcon(imgEscalada));
            } else {
                lblPortada.setIcon(null);
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                lblTitulo.setForeground(list.getSelectionForeground());
                lblAutor.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                lblTitulo.setForeground(list.getForeground());
                lblAutor.setForeground(Color.GRAY);
            }
            return this;
        }
    }
    
    public void detener() {
    if (timerProgreso != null && timerProgreso.isRunning()) {
        timerProgreso.stop();
    }
    if (reproductor != null) {
        reproductor.detener();
    }
}
}