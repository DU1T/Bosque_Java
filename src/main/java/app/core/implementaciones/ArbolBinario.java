package app.core.implementaciones;

import app.core.models.NodoBinario;
import java.util.ArrayList;
import java.util.List;

public class ArbolBinario
{
    //Atributos
    private NodoBinario<Integer> raiz;
    private int modoRepetidos;


    public ArbolBinario(int modoRepetidos)
    {
        this.raiz = null;
        this.modoRepetidos = modoRepetidos;
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
        if (raiz == null)
        {
            return "Vacio";
        }
        if (esPerfectamenteEquilibrado(raiz))
        {
            return "Perfectamente Equilibrado";
        }
        if (esEquilibrado(raiz))
        {
            return "Equilibrado (AVL)";
        }

        return "No equilibrado";
    }

    //equilibrado si la diferencia subarboles es <= 1
    private boolean esEquilibrado(NodoBinario<Integer> nodo)
    {
        if (nodo == null)
        {
            return true;
        }
        int diff = Math.abs(calcularAltura(nodo.getIzquierdo()) - calcularAltura(nodo.getDerecho()));
        return diff <= 1 && esEquilibrado(nodo.getIzquierdo()) && esEquilibrado(nodo.getDerecho());
    }

    // Es perfecto si todos los niveles estan llenos
    private boolean esPerfectamenteEquilibrado(NodoBinario<Integer> nodo)
    {
        if (nodo == null)
        {
            return true;
        }
        int hIzq = calcularAltura(nodo.getIzquierdo());
        int hDer = calcularAltura(nodo.getDerecho());
        return hIzq == hDer && esPerfectamenteEquilibrado(nodo.getIzquierdo()) && esPerfectamenteEquilibrado(nodo.getDerecho());
    }

    //Getters
    public NodoBinario<Integer> getRaiz() {
        return raiz;
    }
}
