package app.core.implementaciones;

import app.core.models.NodoBinario;


public class ArbolBinario
{
    //Atributos
    private NodoBinario<Integer> raiz;
    private boolean esBalanceado;
    private boolean esPerfecto;

    //Constructor
    public ArbolBinario()
    {
        this.raiz = null;
    }

    public void insertar(int valor)
    {
        raiz = insertarRecursivo(raiz, valor);
    }
    private NodoBinario<Integer> insertarRecursivo(NodoBinario<Integer> actual, int valor)
    {
        if (actual == null)
        {
            return new NodoBinario<>(valor);
        }
        if (valor < actual.getDato())
        {
            actual.setIzquierdo(insertarRecursivo(actual.getIzquierdo(), valor));
        }
        else if (valor > actual.getDato())
        {
            actual.setDerecho(insertarRecursivo(actual.getDerecho(), valor));
        }
        else
        {
            //Repetidos
            actual.setFrecuencia(actual.getFrecuencia() + 1);
            System.out.println("Valor " + valor + " repetido. Frecuencia actual: " + actual.getFrecuencia());
        }
        return actual;
    }

    public int getAltura()
    {
        return calcularAltura(raiz);
    }

    private int calcularAltura(NodoBinario<Integer> nodo)
    {
        if (nodo == null)
        {
            return 0;
        }
        return 1 + Math.max(calcularAltura(nodo.getIzquierdo()), calcularAltura(nodo.getDerecho()));
    }

    public String obtenerTipoEquilibrio() {

        if (raiz == null)return "Vacío";

        esBalanceado = true;
        esPerfecto = true;

        analizar(raiz);

        if (esPerfecto) return "Perfectamente Equilibrado";
        else if (esBalanceado) return "Equilibrado";

        return "No equilibrado";
    }

    private int analizar(NodoBinario<Integer> nodo) {

        if (nodo == null)
            return 0;

        int hIzq = analizar(nodo.getIzquierdo());
        int hDer = analizar(nodo.getDerecho());

        // Verificar equilibrio
        if (Math.abs(hIzq - hDer) > 1)
            esBalanceado = false;

        // Verificar perfeccion
        if (hIzq != hDer)
            esPerfecto = false;

        return 1 + Math.max(hIzq, hDer);
    }
    //Getters
    public NodoBinario<Integer> getRaiz() {
        return raiz;
    }
}
