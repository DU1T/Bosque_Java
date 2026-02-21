package app.core.recorridos;

import app.core.models.NodoBinario;
import java.util.ArrayList;
import java.util.List;

public class RecorridosBinario
{
    public static List<Integer> preorden(NodoBinario<Integer> raiz) {
        List<Integer> resultado = new ArrayList<>();
        preordenRec(raiz, resultado);
        return resultado;
    }

    private static void preordenRec(NodoBinario<Integer> nodo, List<Integer> resultado) {
        if (nodo == null) return;

        resultado.add(nodo.getDato());
        preordenRec(nodo.getIzquierdo(), resultado);
        preordenRec(nodo.getDerecho(), resultado);
    }

    public static List<Integer> inorden(NodoBinario<Integer> raiz) {
        List<Integer> resultado = new ArrayList<>();
        inordenRec(raiz, resultado);
        return resultado;
    }

    private static void inordenRec(NodoBinario<Integer> nodo, List<Integer> resultado) {
        if (nodo == null) return;

        inordenRec(nodo.getIzquierdo(), resultado);
        resultado.add(nodo.getDato());
        inordenRec(nodo.getDerecho(), resultado);
    }

    public static List<Integer> postorden(NodoBinario<Integer> raiz) {
        List<Integer> resultado = new ArrayList<>();
        postordenRec(raiz, resultado);
        return resultado;
    }

    private static void postordenRec(NodoBinario<Integer> nodo, List<Integer> resultado) {
        if (nodo == null) return;

        postordenRec(nodo.getIzquierdo(), resultado);
        postordenRec(nodo.getDerecho(), resultado);
        resultado.add(nodo.getDato());
    }
}
