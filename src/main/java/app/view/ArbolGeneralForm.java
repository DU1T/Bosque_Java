package app.view;

import app.controladores.TipoEntrada;
import app.core.models.NodoGeneral;
import app.controladores.ArbolController;
import app.helpers.ParserEnteros;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class ArbolGeneralForm {
    public JPanel PanelPrincipal;
    private JLabel LblTitulo;
    private JLabel LblSubtitulo;
    private JLabel LblValor;
    private JTextField txtDatos;
    private JButton btnInsertar;
    private JButton btnCargaCSV;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JButton btnAltura;
    private JButton btnNiveles;
    private JButton btnRecorridos;
    private JButton btnEquilibrio;
    private JLabel lblConsola;
    private JTextArea txtConsola;
    private JTree ViewArbol;
    private JButton btnLimpiar;
    private ArbolController controller;

    public ArbolGeneralForm(ArbolController controller) {
        this.controller = controller;
        PanelPrincipal.setPreferredSize(new Dimension(900, 600));
        ViewArbol.setModel(null);
        btnInsertar.addActionListener(e -> insertarDatos());
        txtDatos.addActionListener(e -> insertarDatos());
        btnBuscar.addActionListener(e -> buscarDato());
        btnCargaCSV.addActionListener(e -> cargarCSV());
        btnEliminar.addActionListener(e -> eliminarNodo());
        btnAltura.addActionListener(e -> obtenerAltura());
        btnNiveles.addActionListener(e -> obtenerNiveles());
        btnRecorridos.addActionListener(e -> iniciarRecorridos());
        btnLimpiar.addActionListener(e -> limpiarUI());
        btnEquilibrio.addActionListener(e -> obtenerEquilibrio());
    }

    private boolean arbolVacio() {
        return controller.getArbolGeneral().getRaiz() == null;
    }

    private void insertarDatos() {

        String texto = txtDatos.getText().trim();

        if (texto.isEmpty()) {
            mostrarConsola("Ingrese un valor o lista.");
            return;
        }

        List<Integer> valores = ParserEnteros.desdeTexto(texto);

        if (valores.isEmpty()) {
            mostrarConsola("No se encontraron numeros válidos.");
            return;
        }

        // Si no hay raiz aun → el primero es raiz
        if (controller.getArbolGeneral().getRaiz() == null) {

            Integer raizValor = valores.get(0);

            controller.procesarEntrada(
                    TipoEntrada.MANUAL,
                    raizValor.toString(),
                    null
            );

            mostrarConsola("Raiz creada: " + raizValor);

            valores.remove(0);

            actualizarArbol();

            // Seleccionar automáticamente la raíz en el JTree
            if (ViewArbol.getModel() != null) {
                DefaultMutableTreeNode root =
                        (DefaultMutableTreeNode) ViewArbol.getModel().getRoot();
                TreePath path = new TreePath(root.getPath());
                ViewArbol.setSelectionPath(path);
            }

            if (valores.isEmpty()) {
                txtDatos.setText("");
                return;
            }
        }

        // Obtener nodo seleccionado como padre
        TreePath seleccion = ViewArbol.getSelectionPath();

        if (seleccion == null) {
            mostrarConsola("Seleccione un nodo padre en el arbol.");
            return;
        }

        DefaultMutableTreeNode nodoVisual =
                (DefaultMutableTreeNode) seleccion.getLastPathComponent();

        Integer padre = obtenerEnteroSeguro(nodoVisual.getUserObject().toString());

        if (padre == null) {
            mostrarConsola("Error interno al obtener nodo padre.");
            return;
        }

        boolean huboRepetidos = false;

        for (Integer v : valores) {

            // Verificar si ya existe
            if (controller.getArbolGeneral().buscar(v) != null) {
                mostrarConsola("Valor repetido ignorado: " + v);
                huboRepetidos = true;
                continue;
            }

            controller.procesarEntrada(
                    TipoEntrada.MANUAL,
                    v.toString(),
                    padre.toString()
            );
        }

        if (!huboRepetidos) {
            mostrarConsola("Valores agregados correctamente.");
        }

        actualizarArbol();
        txtDatos.setText("");
    }

    private void buscarDato() {

        Integer valor = obtenerEnteroSeguro(txtDatos.getText());

        if (valor == null) {
            mostrarConsola("Ingrese un numero valido.");
            return;
        }

        NodoGeneral<Integer> nodo =
                controller.getArbolGeneral().buscar(valor);

        if (nodo == null) {
            mostrarConsola("Valor no encontrado.");
            return;
        }

        resaltarNodo(valor);
        mostrarConsola("Valor encontrado: " + valor);
    }

    private Integer obtenerEnteroSeguro(String texto) {

        if (texto == null || texto.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void resaltarNodo(int valor) {

        if (ViewArbol.getModel() == null) return;

        DefaultMutableTreeNode root =
                (DefaultMutableTreeNode) ViewArbol.getModel().getRoot();

        Enumeration<?> e = root.breadthFirstEnumeration();


        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) e.nextElement();

            if (node.getUserObject().toString().equals(String.valueOf(valor))) {

                TreePath path = new TreePath(node.getPath());
                ViewArbol.setSelectionPath(path);
                ViewArbol.scrollPathToVisible(path);
                break;
            }
        }
    }

    void cargarCSV() {

        JFileChooser chooser = new JFileChooser();

        int opcion = chooser.showOpenDialog(PanelPrincipal);

        if (opcion == JFileChooser.APPROVE_OPTION) {

            File archivo = chooser.getSelectedFile();

            if (!archivo.getName().toLowerCase().endsWith(".csv")) {
                mostrarConsola("Seleccione un archivo CSV valido.");
                return;
            }

            controller.procesarEntrada(
                    TipoEntrada.CSV,
                    archivo.getAbsolutePath(),
                    null
            );

            actualizarArbol();
            mostrarConsola("CSV cargado correctamente.");
        }
    }

    private void eliminarNodo() {
        boolean eliminado = false;

        if (arbolVacio()) {
            mostrarConsola("El arbol esta vacio.");
            return;
        }
        Integer valor = null;

        String texto = txtDatos.getText().trim();

        if (!texto.isBlank()) {
            valor = obtenerEnteroSeguro(texto);

            if (valor == null) {
                mostrarConsola("Ingrese un numero valido.");
                return;
            }
        } else {
            TreePath seleccion = ViewArbol.getSelectionPath();

            if (seleccion != null) {
                DefaultMutableTreeNode nodo =
                        (DefaultMutableTreeNode) seleccion.getLastPathComponent();
                valor = Integer.parseInt(nodo.getUserObject().toString());
            }
        }

        if (valor == null) {
            mostrarConsola("Ingrese o seleccione un nodo.");
            return;
        }

        NodoGeneral<Integer> nodo =
                controller.getArbolGeneral().buscar(valor);

        if (nodo == null) {
            mostrarConsola("Nodo no encontrado.");
            return;
        }

        if (nodo.esHoja()) {

            int confirm = JOptionPane.showConfirmDialog(
                    PanelPrincipal,
                    "¿Eliminar hoja?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                controller.getArbolGeneral().eliminar(valor, false);
                eliminado = true;
            }
        } else {

            Object[] opciones = {"Podar rama", "Eliminar solo nodo"};

            int eleccion = JOptionPane.showOptionDialog(
                    PanelPrincipal,
                    "¿Cómo desea eliminar?",
                    "Eliminar",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (eleccion == -1) {
                return;
            }

            boolean reorganizar = eleccion == 1;
            controller.getArbolGeneral().eliminar(valor, reorganizar);
            eliminado = true;
        }

        if (eliminado) {
            actualizarArbol();
            mostrarConsola("Nodo eliminado.");
        }
    }

    private void obtenerAltura() {
        if (arbolVacio()) {
            mostrarConsola("El arbol esta vacio.");
            return;
        }
        int altura = controller.getAlturaGeneral();
        mostrarConsola("Altura del arbol: " + altura);
    }

    private void obtenerNiveles() {
        if (arbolVacio()) {
            mostrarConsola("El arbol esta vacio.");
            return;
        }

        Map<Integer, List<Integer>> niveles =
                controller.getNivelesGeneral();

        StringBuilder sb = new StringBuilder();

        niveles.forEach((nivel, lista) -> {
            sb.append("Nivel ")
                    .append(nivel)
                    .append(": ")
                    .append(lista)
                    .append("\n");
        });

        mostrarConsola(sb.toString());
    }

    private void iniciarRecorridos() {
        if (arbolVacio()) {
            mostrarConsola("El arbol esta vacio.");
            return;
        }
        Object[] opciones = {"Preorden", "Postorden"};

        int eleccion = JOptionPane.showOptionDialog(
                PanelPrincipal,
                "Seleccione tipo de recorrido",
                "Recorridos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
        if (eleccion == -1) {
            return;
        }
        if (eleccion == 0) {
            mostrarConsola("=== RECORRIDO PREORDEN ===");
            mostrarConsola(controller.getPreordenGeneral().toString());
        } else if (eleccion == 1) {
            mostrarConsola("=== RECORRIDO POSTORDEN ===");
            mostrarConsola(controller.getPostordenGeneral().toString());
        }
    }

    private void limpiarUI() {

        int confirm = JOptionPane.showConfirmDialog(
                PanelPrincipal,
                "Se borrara el arbol. ¿Continuar?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            controller.reset();
            ViewArbol.setModel(null);
            txtConsola.setText("");
            mostrarConsola("El arbol se ha borrado.");
        }
    }

    private void obtenerEquilibrio() {
        if (arbolVacio()) {
            mostrarConsola("El arbol esta vacio.");
            return;
        }
        String tipo = controller.getArbolGeneral().obtenerTipoEquilibrio();
        mostrarConsola("Tipo de equilibrio: " + tipo);
    }

    private void actualizarArbol() {

        NodoGeneral<Integer> raiz = controller.getArbolGeneral().getRaiz();

        if (raiz == null) {
            ViewArbol.setModel(null);
            return;
        }

        DefaultMutableTreeNode rootNode = construirNodoVisual(raiz);

        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        ViewArbol.setModel(model);

        expandirTodo();
    }

    private DefaultMutableTreeNode construirNodoVisual(NodoGeneral<Integer> nodo) {

        DefaultMutableTreeNode visual =
                new DefaultMutableTreeNode(nodo.getDato());

        for (NodoGeneral<Integer> hijo : nodo.getHijos()) {
            visual.add(construirNodoVisual(hijo));
        }

        return visual;
    }

    private void expandirTodo() {
        for (int i = 0; i < ViewArbol.getRowCount(); i++) {
            ViewArbol.expandRow(i);
        }
    }

    private void mostrarConsola(String mensaje) {
        txtConsola.append(mensaje + "\n");
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        PanelPrincipal = new JPanel();
        PanelPrincipal.setLayout(new GridLayoutManager(16, 4, new Insets(0, 0, 0, 0), -1, -1));
        LblTitulo = new JLabel();
        LblTitulo.setText("Arbol General");
        PanelPrincipal.add(LblTitulo, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        LblSubtitulo = new JLabel();
        LblSubtitulo.setText("Representacion VIsual");
        PanelPrincipal.add(LblSubtitulo, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        LblValor = new JLabel();
        LblValor.setText("Ingresa tus datos:");
        PanelPrincipal.add(LblValor, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnInsertar = new JButton();
        btnInsertar.setText("Agregar");
        PanelPrincipal.add(btnInsertar, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtDatos = new JTextField();
        PanelPrincipal.add(txtDatos, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnEliminar = new JButton();
        btnEliminar.setText("Eliminar Nodo");
        PanelPrincipal.add(btnEliminar, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAltura = new JButton();
        btnAltura.setText("Calcular Altura");
        PanelPrincipal.add(btnAltura, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNiveles = new JButton();
        btnNiveles.setText("Niveles");
        PanelPrincipal.add(btnNiveles, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRecorridos = new JButton();
        btnRecorridos.setText("Recorridos");
        PanelPrincipal.add(btnRecorridos, new GridConstraints(10, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblConsola = new JLabel();
        lblConsola.setText("Consola");
        PanelPrincipal.add(lblConsola, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEquilibrio = new JButton();
        btnEquilibrio.setText("Tipo de equilibrio");
        PanelPrincipal.add(btnEquilibrio, new GridConstraints(11, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBuscar = new JButton();
        btnBuscar.setText("Buscar");
        PanelPrincipal.add(btnBuscar, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCargaCSV = new JButton();
        btnCargaCSV.setText("Cargar .CSV");
        PanelPrincipal.add(btnCargaCSV, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        PanelPrincipal.add(scrollPane1, new GridConstraints(15, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtConsola = new JTextArea();
        txtConsola.setEditable(false);
        scrollPane1.setViewportView(txtConsola);
        final JScrollPane scrollPane2 = new JScrollPane();
        PanelPrincipal.add(scrollPane2, new GridConstraints(2, 0, 12, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ViewArbol = new JTree();
        scrollPane2.setViewportView(ViewArbol);
        btnLimpiar = new JButton();
        btnLimpiar.setText("Limpiar");
        PanelPrincipal.add(btnLimpiar, new GridConstraints(12, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return PanelPrincipal;
    }

}
