package app.core.implementaciones;

import app.core.models.NodoBinario;
import app.helpers.ConvertidorExp; // Importamos tu nueva clase
import java.util.Stack;

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
        if (raiz == null) throw new IllegalStateException("El arbol está vacio.");
        return evaluarRecursivo(raiz);
    }
    private double evaluarRecursivo(NodoBinario<String> nodo) {
        if (nodo == null) return 0;

        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            return Double.parseDouble(nodo.getDato());
        }

        double izq = evaluarRecursivo(nodo.getIzquierdo());
        double der = evaluarRecursivo(nodo.getDerecho());

        switch (nodo.getDato()) {
            case "+": return izq + der;
            case "-": return izq - der;
            case "*": return izq * der;
            case "/": return izq / der;
            default:
                throw new IllegalArgumentException("Operador desconocido: " + nodo.getDato());
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

    public NodoBinario<String> getRaiz() { return raiz; }
}
