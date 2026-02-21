package app.core.implementaciones;

import app.core.models.NodoGeneral;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

        ResultadoBusqueda resultado = buscarExistenteYPadre(raiz, valor, valorPadre);

        NodoGeneral<Integer> existente = resultado.getExistente();
        NodoGeneral<Integer> padre = resultado.getPadre();

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
            if (actual.getHijos().size() < 3) {
                actual.agregarHijo(new NodoGeneral<>(valor));
                return;
            }

            cola.addAll(actual.getHijos());
        }
    }
    public void insertarDesdeLista(String valores, boolean balanceado, Integer padre) {

        if (valores == null || valores.isEmpty()) return;

        String[] partes = valores.split(",");

        for (String parte : partes) {

            try {
                int numero = Integer.parseInt(parte.trim());

                if (balanceado)
                    insertarBalanceado(numero);
                else
                    insertar(numero, padre);

            } catch (NumberFormatException e) {
                System.out.println("Valor invalido: " + parte);
            }
        }
    }
    public void insertarDesdeCSV(String rutaArchivo, boolean balanceado, Integer padre) {

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {
                insertarDesdeLista(linea, balanceado, padre);
            }

        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
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

    public NodoGeneral<Integer> getRaiz()
    {
        return raiz;
    }

}
