package app.view;
import app.controladores.ArbolController;
import app.controladores.TipoEntrada;
import app.core.models.NodoBinario;
import app.helpers.ParserEnteros;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ArbolBinarioForm
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
    private JTree ViewArbol;
    private JButton btnLimpiar;
    private JTextArea txtConsola;

    private ArbolController controller;

    public ArbolBinarioForm(ArbolController controller) {
        this.controller = controller;

        PanelPrincipal.setPreferredSize(new Dimension(900,600));
        ViewArbol.setModel(null);
        btnInsertar.addActionListener(e -> insertar());
        txtDatos.addActionListener(e -> insertar());
        btnCargaCSV.addActionListener(e -> cargarCSV());
        btnBuscar.addActionListener(e -> buscar());
        btnEliminar.addActionListener(e -> eliminar());
        btnAltura.addActionListener(e -> altura());
        btnNiveles.addActionListener(e -> niveles());
        btnRecorridos.addActionListener(e -> recorridos());
        btnEquilibrio.addActionListener(e -> equilibrio());
        btnLimpiar.addActionListener(e -> limpiar());
    }
    private boolean arbolVacio() {
        return controller.estaVacioBinario();
    }

    private void insertar() {

        String texto = txtDatos.getText().trim();

        if (texto.isBlank()) {
            mostrar("Ingrese uno o varios numeros.");
            return;
        }

        List<Integer> valores = ParserEnteros.desdeTexto(texto);

        if (valores.isEmpty()) {
            mostrar("No se encontraron numeros validos.");
            return;
        }

        valores.forEach(v ->
                controller.procesarEntrada(TipoEntrada.MANUAL, v.toString(), null)
        );

        actualizarArbol();
        mostrar("Valores insertados correctamente.");
        txtDatos.setText("");
    }

    private void buscar() {

        Integer valor = obtenerEnteroSeguro(txtDatos.getText());

        if (valor == null) {
            mostrar("Ingrese un numero valido.");
            return;
        }

        if (!controller.buscarBinario(valor)) {
            mostrar("Valor no encontrado.");
            return;
        }

        int frecuencia = controller.getFrecuenciaBinario(valor);

        resaltarNodo(valor);
        mostrar("Encontrado. Frecuencia: " + frecuencia);
        txtDatos.setText("");
    }

    private void eliminar() {

        if (arbolVacio()) {
            mostrar("El arbol esta vacio.");
            return;
        }

        Integer valor = null;
        String texto = txtDatos.getText().trim();

        //Si hay texto escrito
        if (!texto.isBlank()) {
            valor = obtenerEnteroSeguro(texto);

            if (valor == null) {
                mostrar("Ingrese un numero valido.");
                return;
            }
        }
        //Si no hay texto, usar selección del JTree
        else {
            TreePath seleccion = ViewArbol.getSelectionPath();

            if (seleccion != null) {
                DefaultMutableTreeNode nodo =
                        (DefaultMutableTreeNode) seleccion.getLastPathComponent();

                String textoNodo = nodo.getUserObject().toString();

                if (textoNodo.contains(" ")) {
                    textoNodo = textoNodo.substring(0, textoNodo.indexOf(" "));
                }

                valor = obtenerEnteroSeguro(textoNodo);
            }
        }

        if (valor == null) {
            mostrar("Ingrese o seleccione un nodo.");
            return;
        }

        NodoBinario<Integer> nodo = controller.getArbolBinario().buscar(valor);

        if (nodo == null) {
            mostrar("Nodo no encontrado.");
            return;
        }

        boolean tieneHijos =
                nodo.getIzquierdo() != null ||
                        nodo.getDerecho() != null;

        boolean eliminado = false;

        if (!tieneHijos) {
            // Nodo hoja
            eliminado = controller.eliminarBinario(valor);
        }
        else {

            Object[] opciones = {"Podar rama", "Eliminar solo nodo"};

            int eleccion = JOptionPane.showOptionDialog(
                    PanelPrincipal,
                    "¿Como desea eliminar?",
                    "Eliminar",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (eleccion == -1) return;

            boolean podar = (eleccion == 0);

            eliminado = controller.eliminarBinario(valor, podar);
        }

        if (eliminado) {
            actualizarArbol();
            mostrar("Nodo eliminado correctamente.");
            txtDatos.setText("");
        }
    }

    private void altura() {

        if (arbolVacio()) {
            mostrar("El arbol esta vacio.");
            return;
        }

        int h = controller.getAlturaBinario();
        mostrar("Altura: " + h);
    }

    private void niveles() {

        if (arbolVacio()) {
            mostrar("El arbol esta vacio.");
            return;
        }

        Map<Integer, List<Integer>> niveles =
                controller.getNivelesBinario();

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

        if (arbolVacio()) {
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
            case 0 -> mostrar(controller.getPreordenBinario().toString());
            case 1 -> mostrar(controller.getInordenBinario().toString());
            case 2 -> mostrar(controller.getPostordenBinario().toString());
        }
    }

    private void equilibrio() {

        if (arbolVacio()) {
            mostrar("El arbol esta vacio.");
            return;
        }

        mostrar("Tipo: " + controller.getTipoEquilibrioBinario());
    }

    private void cargarCSV() {

        JFileChooser chooser = new JFileChooser();

        int opcion = chooser.showOpenDialog(PanelPrincipal);

        if (opcion == JFileChooser.APPROVE_OPTION) {

            File archivo = chooser.getSelectedFile();

            if (!archivo.getName().toLowerCase().endsWith(".csv")) {
                mostrar("Seleccione un archivo CSV valido.");
                return;
            }

            controller.procesarEntrada(
                    TipoEntrada.CSV,
                    archivo.getAbsolutePath(),
                    null
            );

            actualizarArbol();
            mostrar("CSV cargado correctamente.");
        }
    }

    private void limpiar() {

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
            mostrar("Se ha borrado el arbol.");
        }
    }

    private void actualizarArbol() {

        NodoBinario<Integer> raiz =
                controller.getArbolBinario().getRaiz();

        if (raiz == null) {
            ViewArbol.setModel(null);
            return;
        }

        DefaultMutableTreeNode root =
                construirNodoVisual(raiz);

        ViewArbol.setModel(new DefaultTreeModel(root));
        expandirTodo();
    }

    private DefaultMutableTreeNode construirNodoVisual(NodoBinario<Integer> nodo) {

        String texto = nodo.getFrecuencia() > 1
                ? nodo.getDato() + " (" + nodo.getFrecuencia() + ")"
                : nodo.getDato().toString();

        DefaultMutableTreeNode visual =
                new DefaultMutableTreeNode(texto);

        if (nodo.getIzquierdo() != null)
            visual.add(construirNodoVisual(nodo.getIzquierdo()));

        if (nodo.getDerecho() != null)
            visual.add(construirNodoVisual(nodo.getDerecho()));

        return visual;
    }

    private void expandirTodo() {
        for (int i = 0; i < ViewArbol.getRowCount(); i++) {
            ViewArbol.expandRow(i);
        }
    }

    private void resaltarNodo(int valor) {

        if (ViewArbol.getModel() == null) return;

        DefaultMutableTreeNode root =
                (DefaultMutableTreeNode) ViewArbol.getModel().getRoot();

        var e = root.breadthFirstEnumeration();

        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) e.nextElement();

            if (node.toString().startsWith(String.valueOf(valor))) {
                TreePath path = new TreePath(node.getPath());
                ViewArbol.setSelectionPath(path);
                ViewArbol.scrollPathToVisible(path);
                break;
            }
        }
    }

    private Integer obtenerEnteroSeguro(String texto) {
        try {
            if (texto == null || texto.isBlank()) return null;
            return Integer.parseInt(texto.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private void mostrar(String mensaje) {
        txtConsola.append(mensaje + "\n");
    }
}

