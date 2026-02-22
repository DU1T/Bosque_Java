package app.core.implementaciones;

import app.core.models.NodoBinario;
import app.helpers.ConvertidorExp; // Importamos tu nueva clase
import java.util.Stack;
import java.util.*;

public class ArbolExpresion
{
    private NodoBinario<String> raiz;

    public ArbolExpresion() {
        this.raiz = null;
    }

    //construccion utilizadon helpers
    public void construirDesdeExpresion(String expresion)
    {
        // UsamosConvertidorExp para limpiar y transformar el texto
        String postfix = ConvertidorExp.infixToPostfix(expresion);

        //Construimos el arbol con la cadena ya procesada
        this.raiz = construirArbolPostfix(postfix);
    }

    private NodoBinario<String> construirArbolPostfix(String postfix)
    {
        Stack<NodoBinario<String>> pila = new Stack<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens)
        {
            if (token.isEmpty()) continue;

            // Verificamos si es operador usando la logica generica (string de longitud 1)
            if (token.length() == 1 && ConvertidorExp.esOperador(token.charAt(0)))
            {
                NodoBinario<String> nodo = new NodoBinario<>(token);
                nodo.setDerecho(pila.pop());   // El primero que sale va a la derecha
                nodo.setIzquierdo(pila.pop()); // El segundo va a la izquierda
                pila.push(nodo);
            } else {
                // Es un operando (número)
                pila.push(new NodoBinario<>(token));
            }
        }
        return pila.isEmpty() ? null : pila.pop();
    }

    //Evaluacion matematica
    public double evaluar() {
        if (raiz == null) {
            throw new IllegalStateException("El arbol esta vacio.");
        }
        return evaluarRecursivo(raiz);
    }

    private double evaluarRecursivo(NodoBinario<String> nodo) {

        // Si es hoja -> es numero
        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            return Double.parseDouble(nodo.getDato());
        }

        double izquierda = evaluarRecursivo(nodo.getIzquierdo());
        double derecha = evaluarRecursivo(nodo.getDerecho());

        String operador = nodo.getDato();

        switch (operador) {
            case "+":
                return izquierda + derecha;

            case "-":
                return izquierda - derecha;

            case "*":
                return izquierda * derecha;

            case "/":
                if (derecha == 0) {
                    throw new ArithmeticException("Division entre cero detectada.");
                }
                return izquierda / derecha;

            default:
                throw new IllegalArgumentException("Operador desconocido: " + operador);
        }
    }

    //Altura
    public int getAltura() {
        return calcularAltura(raiz);
    }

    private int calcularAltura(NodoBinario<String> nodo) {
        if (nodo == null) return 0;
        return 1 + Math.max(calcularAltura(nodo.getIzquierdo()), calcularAltura(nodo.getDerecho()));
    }
    public Map<Integer, List<String>> obtenerNiveles() {

        Map<Integer, List<String>> niveles = new LinkedHashMap<>();

        if (raiz == null) return niveles;

        Queue<NodoBinario<String>> cola = new LinkedList<>();
        Queue<Integer> nivelCola = new LinkedList<>();

        cola.add(raiz);
        nivelCola.add(0);

        while (!cola.isEmpty()) {

            NodoBinario<String> actual = cola.poll();
            int nivel = nivelCola.poll();

            niveles.putIfAbsent(nivel, new ArrayList<>());
            niveles.get(nivel).add(actual.getDato());

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


    public NodoBinario<String> getRaiz() { return raiz; }
}
