package app.core.recorridos;
import app.core.models.NodoGeneral;
import java.util.ArrayList;
import java.util.List;

public class RecorridosGeneral
{
    public static List<Integer> preorden(NodoGeneral<Integer> raiz) {
        List<Integer> resultado = new ArrayList<>();
        preordenRec(raiz, resultado);
        return resultado;
    }
    private static void preordenRec(NodoGeneral<Integer> nodo, List<Integer> resultado) {
        if (nodo == null) return;

        resultado.add(nodo.getDato());

        for (NodoGeneral<Integer> hijo : nodo.getHijos()) {
            preordenRec(hijo, resultado);
        }
    }

    public static List<Integer> postorden(NodoGeneral<Integer> raiz) {
        List<Integer> resultado = new ArrayList<>();
        postordenRec(raiz, resultado);
        return resultado;
    }

    private static void postordenRec(NodoGeneral<Integer> nodo, List<Integer> resultado) {
        if (nodo == null) return;

        for (NodoGeneral<Integer> hijo : nodo.getHijos()) {
            postordenRec(hijo, resultado);
        }

        resultado.add(nodo.getDato());
    }
}
