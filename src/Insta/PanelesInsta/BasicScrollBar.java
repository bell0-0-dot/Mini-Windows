/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author vasqu
 */
public class BasicScrollBar extends BasicScrollBarUI{
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(200, 200, 200); 
        this.trackColor = Color.WHITE;               
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return crearBotonVacio();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return crearBotonVacio();
    }

    private JButton crearBotonVacio() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
        g.setColor(thumbColor);
        g.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(trackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
}
