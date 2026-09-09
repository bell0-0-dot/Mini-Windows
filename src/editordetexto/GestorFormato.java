/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editordetexto;

import java.awt.Color;
import javax.swing.text.Element;
import javax.swing.text.AttributeSet;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author vasqu
 */
public class GestorFormato {
    public void aplicarColor(JTextPane texto, Color color){
        
        MutableAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setForeground(atributos, color);
        aplicarAtributo(texto, atributos);
    }
    public void aplicarNegrita(JTextPane area) {
        
        int inicio = area.getSelectionStart();
        int fin = area.getSelectionEnd();
        boolean esNegrita = !esNegritaActiva(area, inicio, fin);

        MutableAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setBold(atributos, esNegrita);
        aplicarAtributo(area, atributos);
    }
    
    public void aplicarCursiva(JTextPane area) {
        int inicio = area.getSelectionStart();
        int fin = area.getSelectionEnd();
        boolean esCursiva = !esCursivaActiva(area, inicio, fin);

        MutableAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setItalic(atributos, esCursiva);
        aplicarAtributo(area, atributos);
    }
    
     public void aplicarSubrayado(JTextPane area) {
        int inicio = area.getSelectionStart();
        int fin = area.getSelectionEnd();
        boolean activo = !esSubrayadoActivo(area, inicio, fin);

        MutableAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setUnderline(atributos, activo);
        aplicarAtributo(area, atributos);
    }
    
     public void aplicarTachado(JTextPane area) {
        int inicio = area.getSelectionStart();
        int fin = area.getSelectionEnd();
        boolean activo = !esTachadoActivo(area, inicio, fin);

        MutableAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setStrikeThrough(atributos, activo);
        aplicarAtributo(area, atributos);
    }

    public void aplicarFuente(JTextPane area, String fuente) {
        MutableAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setFontFamily(atributos, fuente);
        aplicarAtributo(area, atributos);
    }

    public void aplicarTamano(JTextPane area, int size) {
        MutableAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setFontSize(atributos, size);
        aplicarAtributo(area, atributos);
    }

    private void aplicarAtributo(JTextPane area, MutableAttributeSet atributos) {
        int inicio = area.getSelectionStart();
        int fin = area.getSelectionEnd();

        if (inicio == fin) {
            
            area.setCharacterAttributes(atributos, false);
            return;
        }

        StyledDocument doc = area.getStyledDocument();
        doc.setCharacterAttributes(inicio, fin - inicio, atributos, false);
    }
    
    public boolean esNegritaActiva(JTextPane area, int inicio, int fin) {
        AttributeSet atributos = obtenerAtributoBase(area, inicio, fin);
        return StyleConstants.isBold(atributos);
    }
    public boolean esCursivaActiva(JTextPane area, int inicio, int fin) {
        AttributeSet attr = obtenerAtributoBase(area, inicio, fin);
        return StyleConstants.isItalic(attr);
    }
    
     public boolean esSubrayadoActivo(JTextPane area, int inicio, int fin) {
        AttributeSet attr = obtenerAtributoBase(area, inicio, fin);
        return StyleConstants.isUnderline(attr);
    }

    public boolean esTachadoActivo(JTextPane area, int inicio, int fin) {
        AttributeSet attr = obtenerAtributoBase(area, inicio, fin);
        return StyleConstants.isStrikeThrough(attr);
    }

    private AttributeSet obtenerAtributoBase(JTextPane area, int inicio, int fin) {
        StyledDocument doc = area.getStyledDocument();
        int pos = (inicio == fin) ? Math.max(0, inicio - 1) : inicio;
        Element elem = doc.getCharacterElement(pos);
        return elem.getAttributes();
    }
    
     public FormatoTexto extraerFormato(AttributeSet attr) {
        FormatoTexto formato = new FormatoTexto();
        formato.setColor(StyleConstants.getForeground(attr));
        formato.setNegrita(StyleConstants.isBold(attr));
        formato.setCursiva(StyleConstants.isItalic(attr));
        formato.setSubrayado(StyleConstants.isUnderline(attr));
        formato.setTachado(StyleConstants.isStrikeThrough(attr));
        formato.setFuente(StyleConstants.getFontFamily(attr));
        formato.setsize(StyleConstants.getFontSize(attr));
        return formato;
    }

}
