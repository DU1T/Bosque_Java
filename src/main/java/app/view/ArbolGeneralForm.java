package app.view;

import app.controladores.TipoEntrada;
import app.core.models.NodoGeneral;
import app.controladores.ArbolController;
import app.helpers.ParserEnteros;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class ArbolGeneralForm
{
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

    public ArbolGeneralForm(ArbolController controller)
    {
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

    private void insertarDatos()
    {

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

        if (!huboRepetidos)
        {
            mostrarConsola("Valores agregados correctamente.");
        }

        actualizarArbol();
        txtDatos.setText("");
    }

    private void buscarDato()
    {

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
    void cargarCSV()
    {

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
    private void eliminarNodo()
    {
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
        }
        else {
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

        if (nodo.esHoja())
        {

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
        }
        else {

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

        if (eliminado)
        {
            actualizarArbol();
            mostrarConsola("Nodo eliminado.");
        }
    }
    private void obtenerAltura()
    {
        if (arbolVacio()) {
            mostrarConsola("El arbol esta vacio.");
            return;
        }
        int altura = controller.getAlturaGeneral();
        mostrarConsola("Altura del arbol: " + altura);
    }

    private void obtenerNiveles()
    {
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
    private void iniciarRecorridos()
    {
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
        }
        else if (eleccion == 1) {
            mostrarConsola("=== RECORRIDO POSTORDEN ===");
            mostrarConsola(controller.getPostordenGeneral().toString());
        }
    }
    private void limpiarUI()
    {

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
    private void obtenerEquilibrio()
    {
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
}
