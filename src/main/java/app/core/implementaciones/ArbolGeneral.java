package app.core.implementaciones;

import app.core.models.NodoGeneral;

import java.util.*;

public class ArbolGeneral
{
    private NodoGeneral<Integer> raiz;
    private boolean esBalanceado;
    private boolean esPerfecto;
    private int maxHijos = 3;

    //Constructor
    public ArbolGeneral() {
        this.raiz = null;
    }
    public void setMaxHijos(int maxHijos) {
        this.maxHijos = maxHijos;
    }
    //Metodos
    //Insertar
    //Si padre es null, es la raiz.
    public void insertar(Integer valor, Integer valorPadre)
    {

        if (raiz == null)
        {
            raiz = new NodoGeneral<>(valor);
            return;
        }

        ResultadoBusqueda resultado = buscarExistenteYPadre(raiz, valor, valorPadre);

        NodoGeneral<Integer> existente = resultado.getExistente();
        NodoGeneral<Integer> padre = resultado.getPadre();

        if (existente != null) //No duplicados
        {
            return;
        }

        if (padre != null)
        {
            padre.agregarHijo(new NodoGeneral<>(valor));
        }
    }
    public void insertarBalanceado(Integer valor) {

        if (raiz == null) {
            raiz = new NodoGeneral<>(valor);
            return;
        }

        Queue<NodoGeneral<Integer>> cola = new LinkedList<>();
        cola.add(raiz);

        while (!cola.isEmpty()) {

            NodoGeneral<Integer> actual = cola.poll();

            // Si este nodo tiene menos hijos que un limite (ej: 3 hijos maximo)
            if (actual.getHijos().size() < maxHijos) {
                actual.agregarHijo(new NodoGeneral<>(valor));
                return;
            }

            cola.addAll(actual.getHijos());
        }
    }
    //Buscar
    private static class ResultadoBusqueda {

        private NodoGeneral<Integer> existente;
        private NodoGeneral<Integer> padre;

        public ResultadoBusqueda(NodoGeneral<Integer> existente,
                                 NodoGeneral<Integer> padre) {
            this.existente = existente;
            this.padre = padre;
        }

        public NodoGeneral<Integer> getExistente() {
            return existente;
        }

        public NodoGeneral<Integer> getPadre() {
            return padre;
        }
    }
    private ResultadoBusqueda buscarExistenteYPadre(
            NodoGeneral<Integer> actual,
            Integer valor,
            Integer valorPadre) {

        if (actual == null) {
            return new ResultadoBusqueda(null, null);
        }

        NodoGeneral<Integer> existente = null;
        NodoGeneral<Integer> padre = null;

        if (actual.getDato().equals(valor)) {
            existente = actual;
        }

        if (actual.getDato().equals(valorPadre)) {
            padre = actual;
        }

        for (NodoGeneral<Integer> hijo : actual.getHijos()) {

            ResultadoBusqueda resultado = buscarExistenteYPadre(hijo, valor, valorPadre);

            if (resultado.getExistente() != null)
            {
                existente = resultado.getExistente();
            }

            if (resultado.getPadre() != null)
            {
                padre = resultado.getPadre();
            }
            if (existente != null && padre != null)
            {
                break;
            }
        }

        return new ResultadoBusqueda(existente, padre);
    }
    //Altura
    public int getAltura() {
        return calcularAltura(raiz);
    }
    private int calcularAltura(NodoGeneral<Integer> nodo)
    {
        if (nodo == null) return 0;
        if (nodo.esHoja()) return 1;
        int maxH = 0;
        for (NodoGeneral<Integer> hijo : nodo.getHijos())
        {
            maxH = Math.max(maxH, calcularAltura(hijo));
        }
        return 1 + maxH;
    }

    //Equilibrio
    public String obtenerTipoEquilibrio()
    {

        if (raiz == null) return "Vacio";

        esBalanceado = true;
        esPerfecto = true;

        analizar(raiz);

        if (esPerfecto) return "Perfectamente Equilibrado";
        else if (esBalanceado) return "Equilibrado";

        return "No equilibrado";

    }
    private int analizar(NodoGeneral<Integer> nodo) {

        if (nodo == null) return 0;

        if (nodo.esHoja()) return 1;

        int minH = Integer.MAX_VALUE;
        int maxH = Integer.MIN_VALUE;

        for (NodoGeneral<Integer> hijo : nodo.getHijos())
        {

            int h = analizar(hijo);

            minH = Math.min(minH, h);
            maxH = Math.max(maxH, h);
        }

        // Verificar equilibrio
        if (maxH - minH > 1)
        {
            esBalanceado = false;
        }

        // Verificar perfeccion
        if (maxH != minH)
        {
            esPerfecto = false;
        }

        return maxH + 1;
    }

    public boolean eliminar(Integer valor, boolean reorganizar)
    {
        if (raiz == null) return false;

        //Caso especial
        if (raiz.getDato().equals(valor))
        {
            if (!reorganizar)
            {
                // Poda total
                raiz = null;
            }
            else
            {
                if (raiz.getHijos().isEmpty())
                {
                    raiz = null;
                }
                else
                {
                    // Primer hijo será el sucesor
                    NodoGeneral<Integer> nuevaRaiz = raiz.getHijos().get(0);

                    // Los demás hijos pasan a ser hijos del sucesor
                    for (int i = 1; i < raiz.getHijos().size(); i++)
                    {
                        nuevaRaiz.agregarHijo(raiz.getHijos().get(i));
                    }

                    raiz = nuevaRaiz;
                }
            }

            return true;
        }

        return eliminarRecursivo(raiz, valor, reorganizar);
    }
    private boolean eliminarRecursivo(
            NodoGeneral<Integer> actual,
            Integer valor,
            boolean reorganizar)
    {
        List<NodoGeneral<Integer>> hijos = actual.getHijos();

        for (int i = 0; i < hijos.size(); i++)
        {
            NodoGeneral<Integer> hijo = hijos.get(i);

            if (hijo.getDato().equals(valor))
            {
                if (!reorganizar)
                {
                    hijos.remove(i); // PODA
                }
                else
                {
                    hijos.remove(i);

                    if (!hijo.getHijos().isEmpty())
                    {
                        // Primer hijo como sucesor
                        NodoGeneral<Integer> sucesor = hijo.getHijos().get(0);

                        // Los demas hijos pasan al sucesor
                        for (int j = 1; j < hijo.getHijos().size(); j++)
                        {
                            sucesor.agregarHijo(hijo.getHijos().get(j));
                        }

                        // Insertar sucesor en la misma posicion
                        hijos.add(i, sucesor);
                    }
                }

                return true;
            }

            if (eliminarRecursivo(hijo, valor, reorganizar))
            {
                return true;
            }
        }

        return false;
    }
    public Map<Integer, List<Integer>> obtenerNiveles() {

        Map<Integer, List<Integer>> niveles = new LinkedHashMap<>();

        if (raiz == null) return niveles;

        Queue<NodoGeneral<Integer>> cola = new LinkedList<>();
        Queue<Integer> nivelesCola = new LinkedList<>();

        cola.add(raiz);
        nivelesCola.add(0);

        while (!cola.isEmpty()) {

            NodoGeneral<Integer> actual = cola.poll();
            int nivel = nivelesCola.poll();

            niveles.putIfAbsent(nivel, new ArrayList<>());
            niveles.get(nivel).add(actual.getDato());

            for (NodoGeneral<Integer> hijo : actual.getHijos()) {
                cola.add(hijo);
                nivelesCola.add(nivel + 1);
            }
        }

        return niveles;
    }
    //Busqueda publica
    public NodoGeneral<Integer> buscar(Integer valor) {
        return buscarRecursivo(raiz, valor);
    }

    private NodoGeneral<Integer> buscarRecursivo(
            NodoGeneral<Integer> actual,
            Integer valor) {

        if (actual == null) return null;

        if (actual.getDato().equals(valor)) {
            return actual;
        }

        for (NodoGeneral<Integer> hijo : actual.getHijos()) {
            NodoGeneral<Integer> encontrado = buscarRecursivo(hijo, valor);
            if (encontrado != null) return encontrado;
        }

        return null;
    }

    public NodoGeneral<Integer> getRaiz()
    {
        return raiz;
    }

}
