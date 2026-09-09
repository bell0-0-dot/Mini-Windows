package editordetexto;


import java.awt.Color;
import javax.swing.JColorChooser;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author vasqu
 */
public class SelectorColor {
    public Color elegirColor(Color Actual){
    
        return JColorChooser.showDialog(null, "Selecciona un color", Actual);
        
    }
        
}
