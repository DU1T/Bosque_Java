package app.helpers;

import java.util.Stack;

public class ConvertidorExp
{
    /**
     * Convierte una expresión matematica de Infix a Postfix (Notacion Polaca Inversa)
     */
    public static String infixToPostfix(String exp) {
        StringBuilder resultado = new StringBuilder();
        Stack<Character> pila = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            // Si es numero o punto decimal, lo extraemos completo
            if (Character.isDigit(c) || c == '.') {
                while (i < exp.length() && (Character.isDigit(exp.charAt(i)) || exp.charAt(i) == '.')) {
                    resultado.append(exp.charAt(i));
                    i++;
                }
                resultado.append(" "); // Separador
                i--; // Ajustamos el indice por el bucle while
            }
            else if (c == '(') {
                pila.push(c);
            }
            else if (c == ')') {
                while (!pila.isEmpty() && pila.peek() != '(') {
                    resultado.append(pila.pop()).append(" ");
                }
                if (!pila.isEmpty()) pila.pop(); // Sacar el '('
            }
            else if (esOperador(c)) {
                while (!pila.isEmpty() && precedencia(c) <= precedencia(pila.peek())) {
                    resultado.append(pila.pop()).append(" ");
                }
                pila.push(c);
            }
        }

        // Vaciamos el resto de la pila
        while (!pila.isEmpty()) {
            resultado.append(pila.pop()).append(" ");
        }
        return resultado.toString().trim();
    }

    public static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static int precedencia(char ch) {
        switch (ch) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
        }
        return -1;
    }
}
