package app.core.recorridos;

import app.core.models.NodoBinario;

import java.util.ArrayList;
import java.util.List;

public class RecorridosExpresion
{
    // PREORDEN
    public static List<String> preorden(NodoBinario<String> raiz) {
        List<String> resultado = new ArrayList<>();
        preordenRec(raiz, resultado);
        return resultado;
    }

    private static void preordenRec(NodoBinario<String> nodo, List<String> resultado) {
        if (nodo == null) return;

        resultado.add(nodo.getDato());
        preordenRec(nodo.getIzquierdo(), resultado);
        preordenRec(nodo.getDerecho(), resultado);
    }

    // INORDEN SIMPLE
    public static List<String> inorden(NodoBinario<String> raiz) {
        List<String> resultado = new ArrayList<>();
        inordenRec(raiz, resultado);
        return resultado;
    }

    private static void inordenRec(NodoBinario<String> nodo, List<String> resultado) {
        if (nodo == null) return;

        inordenRec(nodo.getIzquierdo(), resultado);
        resultado.add(nodo.getDato());
        inordenRec(nodo.getDerecho(), resultado);
    }

    // POSTORDEN
    public static List<String> postorden(NodoBinario<String> raiz) {
        List<String> resultado = new ArrayList<>();
        postordenRec(raiz, resultado);
        return resultado;
    }

    private static void postordenRec(NodoBinario<String> nodo, List<String> resultado) {
        if (nodo == null) return;

        postordenRec(nodo.getIzquierdo(), resultado);
        postordenRec(nodo.getDerecho(), resultado);
        resultado.add(nodo.getDato());
    }

    // INORDEN CON PARENTESIS (RECOMENDADO PARA EXPRESIONES)
    public static String inordenConParentesis(NodoBinario<String> raiz) {
        return inordenParentesisRec(raiz);
    }

    private static String inordenParentesisRec(NodoBinario<String> nodo) {

        if (nodo == null) return "";

        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            return nodo.getDato();
        }

        return "(" +
                inordenParentesisRec(nodo.getIzquierdo()) +
                nodo.getDato() +
                inordenParentesisRec(nodo.getDerecho()) +
                ")";
    }
}
