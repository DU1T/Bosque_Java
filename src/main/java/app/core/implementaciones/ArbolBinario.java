package app.core.implementaciones;

import app.core.models.NodoBinario;
import java.util.ArrayList;
import java.util.List;

public class ArbolBinario
{
    //Atributos
    private NodoBinario<Integer> raiz;
    private int modoRepetidos;

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

    public String obtenerTipoEquilibrio()
    {
        if (raiz == null) return "Vacío";
        if (verificarPerfecto(raiz) != -1) return "Perfectamente Equilibrado";
        if (verificarEquilibrado(raiz) != -1) return "Equilibrado";
        return "No equilibrado";
    }

    //equilibrado si la diferencia subarboles es <= 1
    private int verificarEquilibrado(NodoBinario<Integer> nodo)
    {
        if (nodo == null) return 0;

        int hIzq = verificarEquilibrado(nodo.getIzquierdo());
        if (hIzq == -1) return -1;

        int hDer = verificarEquilibrado(nodo.getDerecho());
        if (hDer == -1) return -1;

        if (Math.abs(hIzq - hDer) > 1) return -1; // El nodo actual rompe el equilibrio

        return 1 + Math.max(hIzq, hDer);
    }

    // Es perfecto si todos los niveles estan llenos
    private int verificarPerfecto(NodoBinario<Integer> nodo) {
        if (nodo == null) return 0;

        int hIzq = verificarPerfecto(nodo.getIzquierdo());
        if (hIzq == -1) return -1;

        int hDer = verificarPerfecto(nodo.getDerecho());
        if (hDer == -1) return -1;

        if (hIzq != hDer) return -1; // Para ser perfecto, izquierda y derecha DEBEN ser iguales

        return 1 + hIzq;
    }
    //Getters
    public NodoBinario<Integer> getRaiz() {
        return raiz;
    }
}
