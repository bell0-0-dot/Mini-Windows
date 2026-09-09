/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editordetexto;

import javax.swing.JComboBox;

/**
 *
 * @author vasqu
 */
public class SelectorSize extends JComboBox<Integer>{
    private static final Integer[] size={8,9, 10, 11, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 72};

    public SelectorSize() {
        super(size);
        setSelectedItem(12);
    }
    public int getTamanoSeleccionado() {
        return (Integer) getSelectedItem();
    }
}
