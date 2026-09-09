package editordetexto;

import persistencia.*;
import editordetexto.excepciones.*;
import base.FileChooserUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;

public class GUIEditorTexto extends JPanel {
    private JTextPane areaTexto;
    private JComboBox<String> Fuentes;
    private JComboBox<Integer> Tamanos;
    private JToggleButton btnBOLD, btnITALIC, btnSUBRAYAR, btnTACHAR;
    private JLabel labelEstado;
    private JLabel labelArchivoActual;

    private final GestorFormato gestorFormato = new GestorFormato();
    private final EdtWrite escritorBinario = new EdtWrite();
    private final EdtRead lectorBinario = new EdtRead();

    private File archivoActual = null;
    private final File carpetaRaizUsuario; 
    private final File carpetaInicial;  

    private JTextPane paneActivo;

    private final Color AZUL_OSCURO = new Color(26, 37, 48);
    private final Color AZUL_ACCENT = new Color(44, 62, 80);

    public GUIEditorTexto() {
        this(null);
    }

    public GUIEditorTexto(String rutaCarpetaUsuario) {
        if (rutaCarpetaUsuario != null) {
            this.carpetaRaizUsuario = new File(rutaCarpetaUsuario);
            File misDocumentos = new File(carpetaRaizUsuario, "Mis Documentos");
            this.carpetaInicial = misDocumentos.exists() ? misDocumentos : carpetaRaizUsuario;
        } else {
            this.carpetaRaizUsuario = null;
            this.carpetaInicial = null;
        }

        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(BarraMenu(), BorderLayout.NORTH);
        panelSuperior.add(BarraHerramientas(), BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        areaTexto = new JTextPane();
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 12));
        paneActivo = areaTexto;

        JPanel panelEscribir = new JPanel(new GridBagLayout());
        panelEscribir.setBackground(new Color(220, 224, 230));
        areaTexto.setPreferredSize(new Dimension(650, 800));
        areaTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 185, 190)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelEscribir.add(areaTexto);
        JScrollPane scrollenTexto = new JScrollPane(panelEscribir);
        add(scrollenTexto, BorderLayout.CENTER);

        labelEstado = new JLabel("Palabras: 0");
        labelEstado.setForeground(Color.WHITE);
        labelEstado.setFont(new Font("SansSerif", Font.BOLD, 12));

        labelArchivoActual = new JLabel("Documento sin guardar");
        labelArchivoActual.setForeground(Color.WHITE);
        labelArchivoActual.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(AZUL_OSCURO);
        panelEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelEstado.add(labelArchivoActual, BorderLayout.WEST);
        panelEstado.add(labelEstado, BorderLayout.EAST);
        add(panelEstado, BorderLayout.SOUTH);

        areaTexto.addCaretListener(e -> actualizarEstadosYBorde());
        areaTexto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                paneActivo = areaTexto;
                actualizarEstadosYBorde();
            }
        });
    }

    private JMenuBar BarraMenu() {
        JMenuBar menu = new JMenuBar();
        menu.setBackground(AZUL_OSCURO);
        menu.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(Color.WHITE);
        menuArchivo.setFont(new Font("SansSerif", Font.BOLD, 12));

        JMenuItem nuevoItem = new JMenuItem("Nuevo");
        JMenuItem abrirItem = new JMenuItem("Abrir");
        JMenuItem guardarItem = new JMenuItem("Guardar");
        JMenuItem guardarComoItem = new JMenuItem("Guardar como...");

        nuevoItem.addActionListener(e -> nuevoDocumento());
        abrirItem.addActionListener(e -> abrirArchivo());
        guardarItem.addActionListener(e -> guardarArchivo());
        guardarComoItem.addActionListener(e -> guardarComoArchivo());

        menuArchivo.add(nuevoItem);
        menuArchivo.add(abrirItem);
        menuArchivo.add(guardarItem);
        menuArchivo.add(guardarComoItem);
        menu.add(menuArchivo);

        return menu;
    }

    private JToolBar BarraHerramientas() {
        JToolBar herramientas = new JToolBar();
        herramientas.setFloatable(false);
        herramientas.setBackground(AZUL_ACCENT);
        herramientas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lblFuente = new JLabel("Fuente: ");
        lblFuente.setForeground(Color.WHITE);
        lblFuente.setFont(new Font("SansSerif", Font.BOLD, 12));

        String[] arregloFuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Fuentes = new JComboBox<>(arregloFuentes);
        Fuentes.setSelectedItem("Arial");
        Fuentes.setMaximumSize(new Dimension(150, 30));
        Fuentes.addActionListener(e -> {
            if (Fuentes.getSelectedItem() != null) {
                gestorFormato.aplicarFuente(paneActivo, (String) Fuentes.getSelectedItem());
            }
        });

        Integer[] arregloTamanos = {8, 9, 10, 11, 12, 14, 16, 18, 24, 36, 48, 72};
        Tamanos = new JComboBox<>(arregloTamanos);
        Tamanos.setSelectedItem(12);
        Tamanos.setMaximumSize(new Dimension(60, 30));
        Tamanos.addActionListener(e -> {
            if (Tamanos.getSelectedItem() != null) {
                gestorFormato.aplicarTamano(paneActivo, (Integer) Tamanos.getSelectedItem());
            }
        });

        btnBOLD = crearBotonToggle("B", Font.BOLD);
        btnITALIC = crearBotonToggle("I", Font.ITALIC);
        btnSUBRAYAR = crearBotonToggle("S", Font.PLAIN);
        btnTACHAR = crearBotonToggle("T", Font.PLAIN);

        btnBOLD.addActionListener(e -> {
            gestorFormato.aplicarNegrita(paneActivo);
            actualizarEstadosYBorde();
        });

        btnITALIC.addActionListener(e -> {
            gestorFormato.aplicarCursiva(paneActivo);
            actualizarEstadosYBorde();
        });

        btnSUBRAYAR.addActionListener(e -> {
            gestorFormato.aplicarSubrayado(paneActivo);
            actualizarEstadosYBorde();
        });

        btnTACHAR.addActionListener(e -> {
            gestorFormato.aplicarTachado(paneActivo);
            actualizarEstadosYBorde();
        });

        JButton btnColor = crearBoton("Color");
        btnColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color de fuente:", Color.BLACK);
            if (color != null) {
                gestorFormato.aplicarColor(paneActivo, color);
            }
        });

        JButton btnTabla = crearBoton("Insertar Tabla");
        btnTabla.addActionListener(e -> mostrarDialogoTabla());

        herramientas.add(lblFuente);
        herramientas.add(Fuentes);
        herramientas.addSeparator();
        herramientas.add(Tamanos);
        herramientas.addSeparator();
        herramientas.add(btnBOLD);
        herramientas.add(btnITALIC);
        herramientas.add(btnSUBRAYAR);
        herramientas.add(btnTACHAR);
        herramientas.addSeparator();
        herramientas.add(btnColor);
        herramientas.addSeparator();
        herramientas.add(btnTabla);

        return herramientas;
    }

    private JToggleButton crearBotonToggle(String texto, int estilo) {
        JToggleButton btn = new JToggleButton(texto);
        btn.setFont(new Font("SansSerif", estilo, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_OSCURO);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_OSCURO);
        btn.setFocusPainted(false);
        return btn;
    }

    private void actualizarEstadosYBorde() {
        int inicio = paneActivo.getSelectionStart();
        int fin = paneActivo.getSelectionEnd();

        btnBOLD.setSelected(gestorFormato.esNegritaActiva(paneActivo, inicio, fin));
        btnITALIC.setSelected(gestorFormato.esCursivaActiva(paneActivo, inicio, fin));
        btnSUBRAYAR.setSelected(gestorFormato.esSubrayadoActivo(paneActivo, inicio, fin));
        btnTACHAR.setSelected(gestorFormato.esTachadoActivo(paneActivo, inicio, fin));

        StyledDocument doc = paneActivo.getStyledDocument();
        int pos = (inicio == fin) ? Math.max(0, inicio - 1) : inicio;
        AttributeSet attr = doc.getCharacterElement(pos).getAttributes();

        String fuenteActual = StyleConstants.getFontFamily(attr);
        int tamanoActual = StyleConstants.getFontSize(attr);

        if (fuenteActual != null && !fuenteActual.equals(Fuentes.getSelectedItem())) {
            Fuentes.setSelectedItem(fuenteActual);
        }

        if (tamanoActual > 0 && !Integer.valueOf(tamanoActual).equals(Tamanos.getSelectedItem())) {
            Tamanos.setSelectedItem(tamanoActual);
        }

        String texto = areaTexto.getText().trim();
        int palabras = texto.isEmpty() ? 0 : texto.split("\\s+").length;
        labelEstado.setText("Palabras: " + palabras);
    }

    private void mostrarDialogoTabla() {
        JTextField campoFilas = new JTextField("2", 5);
        JTextField campoCols = new JTextField("2", 5);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Filas:"));
        panel.add(campoFilas);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Columnas:"));
        panel.add(campoCols);

        int result = JOptionPane.showConfirmDialog(this, panel, "Insertar Tabla", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int filas = Integer.parseInt(campoFilas.getText());
                int cols = Integer.parseInt(campoCols.getText());
                insertarComponenteTabla(filas, cols);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void insertarComponenteTabla(int filas, int cols) {
        TablaPanel panel = new TablaPanel(filas, cols, gestorFormato);
        registrarFocoCeldas(panel);
        areaTexto.insertComponent(panel);
    }

    private void registrarFocoCeldas(TablaPanel panel) {
        JTextPane[][] celdas = panel.getCeldasUI();
        for (JTextPane[] fila : celdas) {
            for (JTextPane celda : fila) {
                celda.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        paneActivo = celda;
                        actualizarEstadosYBorde();
                    }
                });
                celda.addCaretListener(e -> {
                    if (paneActivo == celda) actualizarEstadosYBorde();
                });
            }
        }
    }

    private JFileChooser crearSelectorArchivos() {
        if (carpetaRaizUsuario == null) {
            return new JFileChooser();
        }
        JFileChooser selector = FileChooserUtil.crearRestringido(carpetaRaizUsuario);
        selector.setCurrentDirectory(carpetaInicial);
        return selector;
    }

    private void nuevoDocumento() {
        areaTexto.setText("");
        paneActivo = areaTexto;
        archivoActual = null;
        labelArchivoActual.setText("Documento sin guardar");
    }

    private void abrirArchivo() {
        JFileChooser escogerArchivo = crearSelectorArchivos();
        escogerArchivo.setFileFilter(new FileNameExtensionFilter("Documentos .edt", "edt"));

        if (escogerArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = escogerArchivo.getSelectedFile();
            try {
                Documento doc = lectorBinario.abrir(archivo);
                renderizarDocumento(doc);

                archivoActual = archivo;
                labelArchivoActual.setText(archivoActual.getName());

                JOptionPane.showMessageDialog(this, "Archivo cargado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (ExtensionInvalidaException | ArchivoCorruptoException | ArchivoTruncadoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error de E/S al abrir el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarArchivo() {
        if (archivoActual == null) {
            guardarComoArchivo();
        } else {
            ejecutarGuardado(archivoActual);
        }
    }

    private void guardarComoArchivo() {
        JFileChooser escogerArchivo = crearSelectorArchivos();
        escogerArchivo.setFileFilter(new FileNameExtensionFilter("Documentos .edt", "edt"));

        if (escogerArchivo.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = escogerArchivo.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(Constantes.EXTENSION)) {
                archivo = new File(archivo.getAbsolutePath() + Constantes.EXTENSION);
            }
            ejecutarGuardado(archivo);
        }
    }

    private void ejecutarGuardado(File archivo) {
        try {
            Documento doc = construirDocumentoDesdeGUI();
            escritorBinario.guardar(doc, archivo);

            archivoActual = archivo;
            labelArchivoActual.setText(archivoActual.getName());

            JOptionPane.showMessageDialog(this, "Documento guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExtensionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Extensión inválida", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Documento construirDocumentoDesdeGUI() {
        Documento doc = new Documento();
        StyledDocument styledDoc = areaTexto.getStyledDocument();
        int offset = 0;
        int length = styledDoc.getLength();

        while (offset < length) {
            Element elem = styledDoc.getCharacterElement(offset);
            AttributeSet attr = elem.getAttributes();
            Object componente = attr.getAttribute(StyleConstants.ComponentAttribute);

            if (componente instanceof TablaPanel) {
                doc.addBloque(((TablaPanel) componente).getTabla());
                offset = elem.getEndOffset();
                continue;
            }

            int end = elem.getEndOffset();
            int fragmentLength = Math.min(end, length) - offset;

            try {
                String subTexto = styledDoc.getText(offset, fragmentLength);
                FormatoTexto fmt = gestorFormato.extraerFormato(attr);
                doc.addBloque(new Fragmento(subTexto, fmt));
            } catch (BadLocationException ignored) {}

            offset += fragmentLength;
        }

        return doc;
    }

    private void renderizarDocumento(Documento doc) {
        areaTexto.setText("");
        StyledDocument styledDoc = areaTexto.getStyledDocument();

        for (Object bloque : doc.getBloques()) {
            if (bloque instanceof Fragmento) {
                Fragmento frag = (Fragmento) bloque;
                FormatoTexto fmt = frag.getFormato();

                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setForeground(attrs, fmt.getColor());
                StyleConstants.setBold(attrs, fmt.isNegrita());
                StyleConstants.setItalic(attrs, fmt.isCursiva());
                StyleConstants.setUnderline(attrs, fmt.isSubrayado());
                StyleConstants.setStrikeThrough(attrs, fmt.isTachado());
                StyleConstants.setFontFamily(attrs, fmt.getFuente());
                StyleConstants.setFontSize(attrs, fmt.getsize());

                try {
                    styledDoc.insertString(styledDoc.getLength(), frag.getTexto(), attrs);
                } catch (BadLocationException ignored) {}

            } else if (bloque instanceof Tabla) {
                TablaPanel panel = new TablaPanel((Tabla) bloque, gestorFormato);
                registrarFocoCeldas(panel);
                areaTexto.setCaretPosition(styledDoc.getLength());
                areaTexto.insertComponent(panel);
            }
        }
    }
}