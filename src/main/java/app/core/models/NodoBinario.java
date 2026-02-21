package app.core.models;


import java.util.ArrayList;
import java.util.List;

public class NodoBinario<T> //T de Type
{
    //Atributos
    private T dato;
    private int frecuencia;
    private NodoBinario<T> izquierdo;
    private NodoBinario<T> derecho;

    //Constructor
    public NodoBinario(T dato) {
        this.dato = dato;
        this.frecuencia = 1;
    }
    //Getters y Setters
    public void setDato(T dato)
    {
        this.dato = dato;
    }
    public void setIzquierdo(NodoBinario<T> izquierdo)
    {
        this.izquierdo = izquierdo;
    }
    public void setDerecho(NodoBinario<T> derecho)
    {
        this.derecho = derecho;
    }
    public void setFrecuencia(int frecuencia)
    {
        this.frecuencia = frecuencia;
    }
    public T getDato()
    {
        return dato;
    }
    public int getFrecuencia()
    {
        return frecuencia;
    }
    public NodoBinario<T> getIzquierdo()
    {
        return izquierdo;
    }
    public NodoBinario<T> getDerecho()
    {
        return derecho;
    }
}
