/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editordetexto;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 *
 * @author gabri
 */
public class TablaPanel extends JPanel{
    
    private final int filas;
    private final int columnas;
    private final JTextPane[][] celdasUI;
    private final GestorFormato gestorFormato;
    
    public TablaPanel(int filas, int columnas, GestorFormato gestorFormato) {
        this(filas, columnas, gestorFormato, null);
    }
    
    public TablaPanel(Tabla tablaExistente, GestorFormato gestorFormato) {
        this(tablaExistente.getFilas(), tablaExistente.getColumnas(), gestorFormato, tablaExistente);
    }
    
    private TablaPanel(int filas, int columnas, GestorFormato gestorFormato, Tabla tablaExistente) {
        this.filas = filas;
        this.columnas = columnas;
        this.gestorFormato = gestorFormato;
        this.celdasUI = new JTextPane[filas][columnas];

        setLayout(new GridLayout(filas, columnas, 2, 2));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                JTextPane celda = new JTextPane();
                celda.setPreferredSize(new Dimension(100, 30));
                celda.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                if (tablaExistente != null) {
                    cargarContenidoCelda(celda, tablaExistente.getCelda(f, c));
                }

                celdasUI[f][c] = celda;
                add(celda);
            }
        }
    }
    
    private void cargarContenidoCelda(JTextPane celda, Celda datosCelda) {
        StyledDocument doc = celda.getStyledDocument();
        for (Fragmento frag : datosCelda.getContenido()) {
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
                doc.insertString(doc.getLength(), frag.getTexto(), attrs);
            } catch (BadLocationException ignored) {}
        }
    }
    
    public Tabla getTabla() {
        Tabla tabla = new Tabla(filas, columnas);
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                JTextPane panelCelda = celdasUI[f][c];
                StyledDocument doc = panelCelda.getStyledDocument();
                Celda celda = new Celda();

                int offset = 0;
                int length = doc.getLength();
                while (offset < length) {
                    Element elem = doc.getCharacterElement(offset);
                    int end = elem.getEndOffset();
                    int fragLen = Math.min(end, length) - offset;
                    try {
                        String texto = doc.getText(offset, fragLen);
                        FormatoTexto fmt = gestorFormato.extraerFormato(elem.getAttributes());
                        celda.addFragmento(new Fragmento(texto, fmt));
                    } catch (BadLocationException ignored) {}
                    offset += fragLen;
                }
                tabla.setCelda(f, c, celda);
            }
        }
        return tabla;
    }
    
    public JTextPane[][] getCeldasUI() {
        return celdasUI;
    }
}
