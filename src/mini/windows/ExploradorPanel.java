package mini.windows;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExploradorPanel extends JPanel {

    private JTree arbol;
    private JList<File> lista;
    private DefaultListModel<File> modeloLista;

    private File carpetaRaizUsuario;
    private ComparadorArchivos.Criterio criterioActual = ComparadorArchivos.Criterio.NOMBRE;

    private File carpetaMostrada;

    private File elementoSeleccionado;

    private File archivoCopiado;

    private JButton botonOrganizar;

    public ExploradorPanel(String rutaRaizUsuario) {
        this.carpetaRaizUsuario = new File(rutaRaizUsuario);
        this.carpetaMostrada = carpetaRaizUsuario;
        setLayout(new BorderLayout());

        construirArbol();
        construirLista();

        JSplitPane divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(arbol), new JScrollPane(lista));
        divisor.setDividerLocation(230);

        add(construirBarraHerramientas(), BorderLayout.NORTH);
        add(divisor, BorderLayout.CENTER);

        refrescarLista();
    }

    private void construirArbol() {
        DefaultMutableTreeNode nodoRaiz = construirNodo(carpetaRaizUsuario);
        arbol = new JTree(new DefaultTreeModel(nodoRaiz));

        arbol.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                File seleccionado = archivoDelNodoSeleccionado();
                if (seleccionado == null) return;

                if (seleccionado.isDirectory()) {
                    carpetaMostrada = seleccionado;
                    elementoSeleccionado = seleccionado;
                } else {
                    carpetaMostrada = seleccionado.getParentFile();
                    elementoSeleccionado = seleccionado;
                }
                refrescarLista();
            }
        });
    }

    private DefaultMutableTreeNode construirNodo(File carpetaOArchivo) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(new NodoArchivo(carpetaOArchivo));

        File[] hijos = carpetaOArchivo.listFiles();
        if (hijos != null) {
            List<File> carpetas = new ArrayList<>();
            List<File> archivos = new ArrayList<>();

            for (File hijo : hijos) {
                if (hijo.isDirectory()) carpetas.add(hijo);
                else archivos.add(hijo);
            }

            ComparadorArchivos.ordenar(carpetas, criterioActual);
            ComparadorArchivos.ordenar(archivos, criterioActual);

            for (File carpeta : carpetas) {
                nodo.add(construirNodo(carpeta));
            }
            for (File archivo : archivos) {
                nodo.add(new DefaultMutableTreeNode(new NodoArchivo(archivo)));
            }
        }
        return nodo;
    }

    private File archivoDelNodoSeleccionado() {
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
        if (nodo == null) return null;
        return ((NodoArchivo) nodo.getUserObject()).getArchivo();
    }

    private void construirLista() {
        modeloLista = new DefaultListModel<>();
        lista = new JList<>(modeloLista);
        lista.setCellRenderer(new RenderizadorArchivo());

        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lista.getSelectedValue() != null) {
                elementoSeleccionado = lista.getSelectedValue();
            }
        });

        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    File seleccionado = lista.getSelectedValue();
                    if (seleccionado != null && seleccionado.isDirectory()) {
                        carpetaMostrada = seleccionado;
                        elementoSeleccionado = null;
                        refrescarLista();
                    }
                }
            }
        });
    }

    private void refrescarLista() {
        modeloLista.clear();

        File[] hijos = carpetaMostrada.listFiles();
        if (hijos != null) {
            List<File> carpetas = new ArrayList<>();
            List<File> archivos = new ArrayList<>();

            for (File hijo : hijos) {
                if (hijo.isDirectory()) carpetas.add(hijo);
                else archivos.add(hijo);
            }

            ComparadorArchivos.ordenar(carpetas, criterioActual);
            ComparadorArchivos.ordenar(archivos, criterioActual);

            for (File c : carpetas) {
                modeloLista.addElement(c);
            }
            for (File a : archivos) {
                modeloLista.addElement(a);
            }
        }
    }

    public void refrescar() {
        DefaultMutableTreeNode nuevoRaiz = construirNodo(carpetaRaizUsuario);
        arbol.setModel(new DefaultTreeModel(nuevoRaiz));
        refrescarLista();
    }

    public File getArchivoSeleccionado() {
        return elementoSeleccionado;
    }

    public JTree getArbol() {
        return arbol;
    }

    private JToolBar construirBarraHerramientas() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);

        botonOrganizar = new JButton("Organizar");
        botonOrganizar.addActionListener(e -> organizarCarpetaSeleccionada());
        barra.add(botonOrganizar);

        barra.addSeparator();

        barra.add(new JLabel("Ordenar por:"));
        JComboBox<ComparadorArchivos.Criterio> comboOrden =
                new JComboBox<>(ComparadorArchivos.Criterio.values());
        comboOrden.addActionListener(e -> {
            criterioActual = (ComparadorArchivos.Criterio) comboOrden.getSelectedItem();
            refrescar();
        });
        barra.add(comboOrden);

        barra.addSeparator();

        JButton botonNuevaCarpeta = new JButton("Nueva carpeta");
        botonNuevaCarpeta.addActionListener(e -> crearCarpeta());
        barra.add(botonNuevaCarpeta);

        JButton botonNuevoArchivo = new JButton("Nuevo archivo");
        botonNuevoArchivo.addActionListener(e -> crearArchivo());
        barra.add(botonNuevoArchivo);

        JButton botonRenombrar = new JButton("Renombrar");
        botonRenombrar.addActionListener(e -> renombrarSeleccionado());
        barra.add(botonRenombrar);

        JButton botonCopiar = new JButton("Copiar");
        botonCopiar.addActionListener(e -> copiarSeleccionado());
        barra.add(botonCopiar);

        JButton botonPegar = new JButton("Pegar");
        botonPegar.addActionListener(e -> pegar());
        barra.add(botonPegar);

        JButton botonEliminar = new JButton("Eliminar");
        botonEliminar.addActionListener(e -> eliminarSeleccionado());
        barra.add(botonEliminar);

        return barra;
    }

    private void organizarCarpetaSeleccionada() {
        File carpetaObjetivo = obtenerCarpetaDeTrabajo();

        botonOrganizar.setEnabled(false);
        botonOrganizar.setText("Organizando...");

        new Thread(new OrganizadorTarea(carpetaObjetivo, tarea -> {
            botonOrganizar.setEnabled(true);
            botonOrganizar.setText("Organizar");
            refrescar();
            mostrarResumenOrganizacion(tarea);
        })).start();
    }

    private void mostrarResumenOrganizacion(OrganizadorTarea tarea) {
        String mensaje = "Organización completada:\n"
                + "Imágenes: " + tarea.getImagenesOrganizadas().length() + "\n"
                + "Documentos: " + tarea.getDocumentosOrganizados().length() + "\n"
                + "Música: " + tarea.getMusicaOrganizada().length();

        JOptionPane.showMessageDialog(this, mensaje, "Organizar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearCarpeta() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva carpeta:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        boolean exito = GestorArchivosFS.crearCarpeta(obtenerCarpetaDeTrabajo(), nombre.trim());
        if (!exito) {
            JOptionPane.showMessageDialog(this, "No se pudo crear la carpeta (¿ya existe?).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        refrescar();
    }

    private void crearArchivo() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del nuevo archivo (con extensión, ej. notas.txt):");
        if (nombre == null || nombre.trim().isEmpty()) return;

        try {
            boolean exito = GestorArchivosFS.crearArchivo(obtenerCarpetaDeTrabajo(), nombre.trim());
            if (!exito) {
                JOptionPane.showMessageDialog(this, "No se pudo crear el archivo (¿ya existe?).",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        refrescar();
    }

    private void renombrarSeleccionado() {
        File seleccionado = getArchivoSeleccionado();
        if (seleccionado == null || seleccionado.equals(carpetaRaizUsuario)) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo o carpeta para renombrar.");
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", seleccionado.getName());
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

        boolean exito = GestorArchivosFS.renombrar(seleccionado, nuevoNombre.trim());
        if (!exito) {
            JOptionPane.showMessageDialog(this, "No se pudo renombrar (¿el nombre ya existe?).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        elementoSeleccionado = null;
        refrescar();
    }

    private void copiarSeleccionado() {
        File seleccionado = getArchivoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo o carpeta para copiar.");
            return;
        }
        archivoCopiado = seleccionado;
        JOptionPane.showMessageDialog(this, "\"" + seleccionado.getName() + "\" copiado. Selecciona el destino y presiona Pegar.");
    }

    private void pegar() {
        if (archivoCopiado == null) {
            JOptionPane.showMessageDialog(this, "No hay nada copiado todavía.");
            return;
        }

        File carpetaDestino = obtenerCarpetaDeTrabajo();
        try {
            GestorArchivosFS.copiar(archivoCopiado, carpetaDestino);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "No se pudo pegar", JOptionPane.ERROR_MESSAGE);
        }
        refrescar();
    }

    private void eliminarSeleccionado() {
        File seleccionado = getArchivoSeleccionado();
        if (seleccionado == null || seleccionado.equals(carpetaRaizUsuario)) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo o carpeta para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + seleccionado.getName() + "\" definitivamente?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            GestorArchivosFS.eliminar(seleccionado);
            elementoSeleccionado = null;
            refrescar();
        }
    }

    private File obtenerCarpetaDeTrabajo() {
        if (elementoSeleccionado != null && elementoSeleccionado.isDirectory()) {
            return elementoSeleccionado;
        }
        return carpetaMostrada;
    }

    private static class NodoArchivo {
        private final File archivo;

        NodoArchivo(File archivo) {
            this.archivo = archivo;
        }

        File getArchivo() {
            return archivo;
        }

        @Override
        public String toString() {
            return archivo.getName();
        }
    }

    private static class RenderizadorArchivo extends DefaultListCellRenderer {
        private static final FileSystemView VISTA_SISTEMA = FileSystemView.getFileSystemView();

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof File) {
                File archivo = ((File) value).getAbsoluteFile();
                setText(archivo.getName());

                Icon icono = VISTA_SISTEMA.getSystemIcon(archivo);
                if (icono == null) {
                    icono = archivo.isDirectory()
                            ? UIManager.getIcon("FileView.directoryIcon")
                            : UIManager.getIcon("FileView.fileIcon");
                }
                setIcon(icono);
            }
            return this;
        }
    }
}
