package app.core.implementaciones;

import app.core.models.NodoBinario;
import java.util.*;


public class ArbolBinario
{
    //Atributos
    private NodoBinario<Integer> raiz;
    private boolean esBalanceado;
    private boolean esPerfecto;

    //Constructor
    public ArbolBinario()
    {
        this.raiz = null;
    }

    public void insertar(int valor)
    {
        raiz = insertarRecursivo(raiz, valor);
    }
    private NodoBinario<Integer> insertarRecursivo(NodoBinario<Integer> actual, int valor)
    {
        if (actual == null)
        {
            return new NodoBinario<>(valor);
        }
        if (valor < actual.getDato())
        {
            actual.setIzquierdo(insertarRecursivo(actual.getIzquierdo(), valor));
        }
        else if (valor > actual.getDato())
        {
            actual.setDerecho(insertarRecursivo(actual.getDerecho(), valor));
        }
        else
        {
            //Repetidos
            actual.setFrecuencia(actual.getFrecuencia() + 1);
            System.out.println("Valor " + valor + " repetido. Frecuencia actual: " + actual.getFrecuencia());
        }
        return actual;
    }

    public int getAltura()
    {
        return calcularAltura(raiz);
    }

    private int calcularAltura(NodoBinario<Integer> nodo)
    {
        if (nodo == null)
        {
            return 0;
        }
        return 1 + Math.max(calcularAltura(nodo.getIzquierdo()), calcularAltura(nodo.getDerecho()));
    }

    public String obtenerTipoEquilibrio() {

        if (raiz == null)return "Vacío";

        esBalanceado = true;
        esPerfecto = true;

        analizar(raiz);

        if (esPerfecto) return "Perfectamente Equilibrado";
        else if (esBalanceado) return "Equilibrado";

        return "No equilibrado";
    }

    private int analizar(NodoBinario<Integer> nodo) {

        if (nodo == null)
            return 0;

        int hIzq = analizar(nodo.getIzquierdo());
        int hDer = analizar(nodo.getDerecho());

        // Verificar equilibrio
        if (Math.abs(hIzq - hDer) > 1)
            esBalanceado = false;

        // Verificar perfeccion
        if (hIzq != hDer)
            esPerfecto = false;

        return 1 + Math.max(hIzq, hDer);
    }
    public NodoBinario<Integer> buscar(int valor) {
        return buscarRec(raiz, valor);
    }

    private NodoBinario<Integer> buscarRec(NodoBinario<Integer> actual, int valor) {
        if (actual == null) return null;

        if (valor == actual.getDato())
            return actual;

        if (valor < actual.getDato())
            return buscarRec(actual.getIzquierdo(), valor);
        else
            return buscarRec(actual.getDerecho(), valor);
    }

    public boolean eliminar(int valor) {
        if (buscar(valor) == null) return false;
        raiz = eliminarRecursivo(raiz, valor);
        return true;
    }
    public boolean eliminar(int valor, boolean podar) {

        if (buscar(valor) == null) return false;

        if (podar) {
            raiz = eliminarPodando(raiz, valor);
        } else {
            raiz = eliminarRecursivo(raiz, valor);
        }

        return true;
    }
    private NodoBinario<Integer> eliminarPodando(NodoBinario<Integer> actual, int valor) {

        if (actual == null) return null;

        if (valor < actual.getDato()) {
            actual.setIzquierdo(eliminarPodando(actual.getIzquierdo(), valor));
        }
        else if (valor > actual.getDato()) {
            actual.setDerecho(eliminarPodando(actual.getDerecho(), valor));
        }
        else {
            // Poda completa
            return null;
        }

        return actual;
    }
    private NodoBinario<Integer> eliminarRecursivo(NodoBinario<Integer> actual, int valor) {

        if (actual == null) return null;

        if (valor < actual.getDato()) {
            actual.setIzquierdo(eliminarRecursivo(actual.getIzquierdo(), valor));
        }
        else if (valor > actual.getDato()) {
            actual.setDerecho(eliminarRecursivo(actual.getDerecho(), valor));
        }
        else {
            // Nodo encontrado

            //Si tiene frecuencia mayor a 1, solo decrementamos
            if (actual.getFrecuencia() > 1) {
                actual.setFrecuencia(actual.getFrecuencia() - 1);
                return actual;
            }

            //Sin hijos
            if (actual.getIzquierdo() == null && actual.getDerecho() == null) {
                return null;
            }

            //Un solo hijo
            if (actual.getIzquierdo() == null) {
                return actual.getDerecho();
            }

            if (actual.getDerecho() == null) {
                return actual.getIzquierdo();
            }

            //Dos hijos
            NodoBinario<Integer> sucesor = obtenerMinimo(actual.getDerecho());

            actual.setDato(sucesor.getDato());
            actual.setFrecuencia(sucesor.getFrecuencia());

            // Eliminar el sucesor original
            actual.setDerecho(
                    eliminarRecursivo(actual.getDerecho(), sucesor.getDato())
            );
        }

        return actual;
    }

    public Map<Integer, List<Integer>> obtenerNiveles() {

        Map<Integer, List<Integer>> niveles = new LinkedHashMap<>();

        if (raiz == null) return niveles;

        Queue<NodoBinario<Integer>> cola = new LinkedList<>();
        Queue<Integer> nivelCola = new LinkedList<>();

        cola.add(raiz);
        nivelCola.add(0);

        while (!cola.isEmpty()) {

            NodoBinario<Integer> actual = cola.poll();
            int nivel = nivelCola.poll();

            niveles.putIfAbsent(nivel, new ArrayList<>());

            // Si quieres mostrar frecuencia:
            for (int i = 0; i < actual.getFrecuencia(); i++) {
                niveles.get(nivel).add(actual.getDato());
            }

            if (actual.getIzquierdo() != null) {
                cola.add(actual.getIzquierdo());
                nivelCola.add(nivel + 1);
            }

            if (actual.getDerecho() != null) {
                cola.add(actual.getDerecho());
                nivelCola.add(nivel + 1);
            }
        }

        return niveles;
    }

    private NodoBinario<Integer> obtenerMinimo(NodoBinario<Integer> nodo) {
        while (nodo.getIzquierdo() != null) {
            nodo = nodo.getIzquierdo();
        }
        return nodo;
    }
    //Getters
    public NodoBinario<Integer> getRaiz() {
        return raiz;
    }
}
