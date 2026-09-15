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
        aplicarLookAndFeelModerno();

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

    private static void aplicarLookAndFeelModerno() {
        try {
            java.awt.Color acento = new java.awt.Color(0, 120, 215);

            UIManager.put("nimbusBase", new java.awt.Color(51, 61, 74));
            UIManager.put("nimbusBlueGrey", new java.awt.Color(180, 188, 196));
            UIManager.put("control", new java.awt.Color(238, 240, 242));
            UIManager.put("nimbusFocus", acento);
            UIManager.put("nimbusSelectionBackground", acento);
            UIManager.put("nimbusSelection", acento);

            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        }
    }
}