package editordetexto;

import javax.swing.JFrame;

/**
 * Punto de entrada SOLO para probar el editor por separado, fuera de
 * Mini-Windows. Como GUIEditorTexto ahora es un JPanel (para poder
 * embeberse dentro de la ventana principal), aqui se envuelve en un
 * JFrame minimo unicamente para poder verlo en pantalla durante pruebas.
 */
public class EditorDeTexto {

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Prueba - Editor de Texto");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(900, 600);
        ventana.setLocationRelativeTo(null);
        ventana.add(new GUIEditorTexto());
        ventana.setVisible(true);
    }
}
