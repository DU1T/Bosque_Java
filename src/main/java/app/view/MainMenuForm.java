package app.view;

import app.controladores.ArbolController;
import app.controladores.TipoArbol;

import javax.swing.*;
import java.awt.*;

public class MainMenuForm {

    public JPanel PanelPrincipal;
    private JButton btnGeneral;
    private JButton btnBinario;
    private JButton btnExpresiones;
    private JButton btnExit;

    public MainMenuForm() {
        PanelPrincipal.setPreferredSize(new Dimension(900, 600));
        btnGeneral.addActionListener(e -> abrirGeneral());
        btnBinario.addActionListener(e -> abrirBinario());
        btnExpresiones.addActionListener(e -> abrirExpresion());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void abrirGeneral() {

        ArbolController controller =
                new ArbolController(TipoArbol.GENERAL);

        ArbolGeneralForm vista =
                new ArbolGeneralForm(controller);

        mostrarVentana(vista.PanelPrincipal, "Árbol General");
    }

    private void abrirBinario() {

        ArbolController controller =
                new ArbolController(TipoArbol.BINARIO);

        ArbolBinarioForm vista =
                new ArbolBinarioForm(controller);

        mostrarVentana(vista.PanelPrincipal, "Árbol Binario");
    }

    private void abrirExpresion() {

        ArbolController controller =
                new ArbolController(TipoArbol.EXPRESION);

        ArbolExpresionForm vista =
                new ArbolExpresionForm(controller);

        mostrarVentana(vista.PanelPrincipal, "Árbol de Expresiones");
    }

    private void mostrarVentana(JPanel panel, String titulo) {

        JFrame frame = new JFrame(titulo);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Sistema de Árboles");
            frame.setContentPane(new MainMenuForm().PanelPrincipal);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}