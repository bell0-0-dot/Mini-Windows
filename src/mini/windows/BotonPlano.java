/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mini.windows;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author gabri
 */
public class BotonPlano extends JButton {
 
    private final Color colorFondo;
    private final Color colorFondoHover;
 
    public BotonPlano(String texto, Icon icono, Color colorFondo, Color colorFondoHover, Color colorTexto) {
        super(texto, icono);
        this.colorFondo = colorFondo;
        this.colorFondoHover = colorFondoHover;
 
        setForeground(colorTexto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(getFont().deriveFont(Font.PLAIN, 12f));
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        Color fondo = (getModel().isRollover() || getModel().isPressed()) ? colorFondoHover : colorFondo;
        if (fondo != null) {
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        }
        g2.dispose();
 
        super.paintComponent(g);
    }
}
 