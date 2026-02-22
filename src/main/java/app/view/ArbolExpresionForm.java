package app.view;

import app.controladores.ArbolController;
import app.core.models.NodoBinario;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ArbolExpresionForm
{
        public JPanel PanelPrincipal;
        private JLabel LblTitulo;
        private JLabel LblSubtitulo;
        private JLabel LblValor;
        private JTextField txtDatos;
        private JButton btnInsertar;
        private JButton btnAltura;
        private JButton btnNiveles;
        private JButton btnRecorridos;
        private JLabel lblConsola;
        private JTree ViewArbol;
        private JButton btnLimpiar;
        private JTextArea txtConsola;

        private ArbolController controller;

    public ArbolExpresionForm(ArbolController controller) {
        this.controller = controller;
        PanelPrincipal.setPreferredSize(new Dimension(900, 600));
        btnInsertar.addActionListener(e -> agregar());
        txtDatos.addActionListener(e -> agregar()); // ENTER ejecuta también

        btnAltura.addActionListener(e -> altura());
        btnNiveles.addActionListener(e -> niveles());
        btnRecorridos.addActionListener(e -> recorridos());
        btnLimpiar.addActionListener(e -> limpiar());
    }
    private void agregar()
    {

        String expresion = txtDatos.getText().trim();

        if (expresion.isBlank()) {
            mostrar("Ingrese una expresion valida.");
            return;
        }

        try {
            controller.construirExpresion(expresion);

            actualizarArbol();

            double resultado = controller.evaluarExpresion();

            mostrar("Expresion construida correctamente.");
            mostrar("Resultado de la expresion: " + resultado);

            txtDatos.setText("");

        } catch (Exception ex) {
            mostrar("Error en la expresion: " + ex.getMessage());
        }
    }
    private void actualizarArbol() {

        NodoBinario<String> raiz = controller.getRaizExpresion();

        if (raiz == null) {
            ViewArbol.setModel(null);
            return;
        }

        DefaultMutableTreeNode root = construirNodoVisual(raiz);

        ViewArbol.setModel(new DefaultTreeModel(root));
        expandirTodo();
    }
    private void expandirTodo()
    {
        for (int i = 0; i < ViewArbol.getRowCount(); i++) {
            ViewArbol.expandRow(i);
        }
    }
    private DefaultMutableTreeNode construirNodoVisual(NodoBinario<String> nodo) {

        DefaultMutableTreeNode visual =
                new DefaultMutableTreeNode(nodo.getDato());

        if (nodo.getIzquierdo() != null)
            visual.add(construirNodoVisual(nodo.getIzquierdo()));

        if (nodo.getDerecho() != null)
            visual.add(construirNodoVisual(nodo.getDerecho()));

        return visual;
    }
    private void altura() {

        if (controller.estaVacioExpresion()) {
            mostrar("El arbol esta vacio.");
            return;
        }

        int h = controller.getAlturaExpresion();
        mostrar("Altura: " + h);
    }
    private void niveles() {

        if (controller.estaVacioExpresion()) {
            mostrar("El arbol esta vacio.");
            return;
        }

        Map<Integer, List<String>> niveles =
                controller.getNivelesExpresion();

        StringBuilder sb = new StringBuilder();

        niveles.forEach((nivel, lista) -> {
            sb.append("Nivel ")
                    .append(nivel)
                    .append(": ")
                    .append(lista)
                    .append("\n");
        });

        mostrar(sb.toString());
    }
    private void recorridos() {

        if (controller.estaVacioExpresion()) {
            mostrar("El arbol esta vacio.");
            return;
        }

        Object[] opciones = {"Preorden", "Inorden", "Postorden"};

        int eleccion = JOptionPane.showOptionDialog(
                PanelPrincipal,
                "Seleccione recorrido",
                "Recorridos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (eleccion == -1) return;

        switch (eleccion) {
            case 0 -> mostrar(controller.getPreordenExpresion().toString());
            case 1 -> mostrar(controller.getInordenExpresion());
            case 2 -> mostrar(controller.getPostordenExpresion().toString());
        }
    }
    private void limpiar() {

        controller.reset();
        ViewArbol.setModel(null);
        txtConsola.setText("Arbol de expresion reiniciado.");
    }
    private void mostrar(String mensaje) {
        txtConsola.append(mensaje + "\n");
    }
}
