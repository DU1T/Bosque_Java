package app.core.implementaciones;

import app.core.models.NodoGeneral;
import java.util.*;

public class ArbolGeneral
{
    private NodoGeneral<Integer> raiz;
    private boolean esBalanceado;
    private boolean esPerfecto;

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

        NodoGeneral<Integer>[] resultado = buscarExistenteYPadre(raiz, valor, valorPadre);

        NodoGeneral<Integer> existente = resultado[0];
        NodoGeneral<Integer> padre = resultado[1];

        if (existente != null)
        {
            existente.setFrecuencia(existente.getFrecuencia() + 1);
            return;
        }

        if (padre != null)
        {
            padre.agregarHijo(new NodoGeneral<>(valor));
        }
    }

    //Buscar
    private NodoGeneral<Integer>[] buscarExistenteYPadre(
            NodoGeneral<Integer> actual,
            Integer valor,
            Integer valorPadre) {

        NodoGeneral<Integer> existente = null;
        NodoGeneral<Integer> padre = null;

        if (actual == null)
        {
            return new NodoGeneral[]{null, null};
        }

        if (actual.getDato().equals(valor))
        {
            existente = actual;
        }

        if (actual.getDato().equals(valorPadre))
        {
            padre = actual;
        }

        for (NodoGeneral<Integer> hijo : actual.getHijos())
        {

            NodoGeneral<Integer>[] resultado =
                    buscarExistenteYPadre(hijo, valor, valorPadre);

            if (resultado[0] != null) existente = resultado[0];

            if (resultado[1] != null) padre = resultado[1];
        }

        return new NodoGeneral[]{existente, padre};
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

    public NodoGeneral<Integer> getRaiz()
    {
        return raiz;
    }

}
