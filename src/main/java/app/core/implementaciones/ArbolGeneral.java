package app.core.implementaciones;

import app.core.models.NodoGeneral;
import java.util.*;

public class ArbolGeneral
{
    private NodoGeneral<Integer> raiz;

    //Constructor
    public ArbolGeneral() {
        this.raiz = null;
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
        // Manejo de repetidos por frecuencia
        NodoGeneral<Integer> existente = buscarRecursivo(raiz, valor);
        if (existente != null)
        {
            existente.setFrecuencia(existente.getFrecuencia() + 1);
            return;
        }
        // Si no existe, buscamos al padre
        NodoGeneral<Integer> nodoPadre = buscarRecursivo(raiz, valorPadre);
        if (nodoPadre != null)
        {
            nodoPadre.agregarHijo(new NodoGeneral<>(valor));
        }
    }

    //Buscar
    public NodoGeneral<Integer> buscar(Integer valor)
    {
        return buscarRecursivo(raiz, valor);
    }
    private NodoGeneral<Integer> buscarRecursivo(NodoGeneral<Integer> actual, Integer valor)
    {
        if (actual == null) return null;
        if (actual.getDato().equals(valor)) return actual;

        for (NodoGeneral<Integer> hijo : actual.getHijos())
        {
            NodoGeneral<Integer> encontrado = buscarRecursivo(hijo, valor);
            if (encontrado != null) return encontrado;
        }
        return null;
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

        if (verificarPerfecto(raiz) != -1)
        {
            return "Perfectamente Equilibrado";
        }
        if (verificarEquilibrado(raiz) != -1)
        {
            return "Equilibrado";
        }
        return "No equilibrado";
    }
    private int verificarPerfecto(NodoGeneral<Integer> nodo)
    {
        if (nodo == null) return 0;
        if (nodo.esHoja()) return 1;

        int alturaRef = -1;

        for (NodoGeneral<Integer> hijo : nodo.getHijos())
        {
            int h = verificarPerfecto(hijo);
            if (h == -1) return -1; // Si un hijo no es perfecto, todo el árbol falla

            if (alturaRef == -1) {
                alturaRef = h; // El primer hijo define la altura que los demás deben igualar
            } else if (alturaRef != h) {
                return -1; // Rompe la perfección si hay alturas distintas
            }
        }
        return alturaRef + 1;
    }
    //equilibrado si la diferencia subarboles es <= 1
    private int verificarEquilibrado(NodoGeneral<Integer> nodo)
    {
        if (nodo == null) return 0;
        if (nodo.esHoja()) return 1;

        int minH = Integer.MAX_VALUE;
        int maxH = Integer.MIN_VALUE;

        for (NodoGeneral<Integer> hijo : nodo.getHijos()) {
            int h = verificarEquilibrado(hijo);
            if (h == -1) return -1; // Propagamos el fallo hacia arriba

            minH = Math.min(minH, h);
            maxH = Math.max(maxH, h);
        }

        if (maxH - minH > 1) return -1; // Diferencia mayor a 1, no está equilibrado

        return maxH + 1;
    }

    public NodoGeneral<Integer> getRaiz()
    {
        return raiz;
    }

}
