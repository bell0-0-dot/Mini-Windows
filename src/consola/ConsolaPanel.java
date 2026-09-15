package consola;

import javax.swing.*;
import java.awt.*;

/**
 * @author gabri
 */
public class ConsolaPanel extends JPanel {

    private final InterpreteComandos interprete;
    private JTextArea consola;
    private JTextField entrada;
    private JLabel prompt;

    public ConsolaPanel(String rutaRaizUsuario) {
        SistemaArchivosConsola sistema = new SistemaArchivosConsola(rutaRaizUsuario);
        interprete = new InterpreteComandos(sistema);

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        construirAreaConsola();
        construirBarraEntrada();

        imprimirLinea(interprete.getRutaActual());
    }

    private void construirAreaConsola() {
        consola = new JTextArea();
        consola.setEditable(false);
        consola.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        consola.setForeground(Color.GREEN);
        consola.setBackground(Color.BLACK);
        consola.setLineWrap(true);
        consola.setWrapStyleWord(true);
        consola.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane scroll = new JScrollPane(consola);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.BLACK);

        add(scroll, BorderLayout.CENTER);
    }

    private void construirBarraEntrada() {
        JPanel panelEntrada = new JPanel(new BorderLayout());
        panelEntrada.setBackground(Color.BLACK);
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

        prompt = new JLabel(rutaMostrada() + "> ");
        prompt.setForeground(Color.GREEN);
        prompt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        entrada = new JTextField();
        entrada.setBackground(Color.BLACK);
        entrada.setForeground(Color.WHITE);
        entrada.setCaretColor(Color.WHITE);
        entrada.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        entrada.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        entrada.addActionListener(e -> ejecutarComando());

        panelEntrada.add(prompt, BorderLayout.WEST);
        panelEntrada.add(entrada, BorderLayout.CENTER);

        add(panelEntrada, BorderLayout.SOUTH);
    }

    private void ejecutarComando() {
        String comandoEscrito = entrada.getText().trim();
        if (comandoEscrito.isEmpty()) {
            return;
        }

        imprimirLinea(rutaMostrada() + "> " + comandoEscrito);

        String resultado = interprete.ejecutar(comandoEscrito);
        if (!resultado.isEmpty()) {
            imprimirLinea(resultado);
        }

        entrada.setText("");
        prompt.setText(rutaMostrada() + "> ");
        consola.setCaretPosition(consola.getDocument().getLength());
        entrada.requestFocusInWindow();
    }

    private String rutaMostrada() {
        return interprete.getRutaActual();
    }

    private void imprimirLinea(String texto) {
        consola.append(texto + "\n");
        consola.setCaretPosition(consola.getDocument().getLength());
    }
}