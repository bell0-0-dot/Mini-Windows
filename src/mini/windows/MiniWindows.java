/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mini.windows;

import javax.swing.*;

/**
 *
 * @author vasqu
 */
public class MiniWindows {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        //System.out.println(Rutas.RUTA_RAIZ);
        
        try {
            GestorArchivos.inicializarSistema();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al inicializar el sistema: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }    
}
