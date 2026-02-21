package app.controladores;

import app.core.implementaciones.*;
import app.core.recorridos.*;
import app.helpers.ParserEnteros;
import app.helpers.ManejadorCSV;
import java.util.List;
import java.util.Map;

public class ArbolController
{
    private ArbolBinario arbolBinario;
    private ArbolGeneral arbolGeneral;
    private ArbolExpresion arbolExpresion;

    private TipoArbol tipoArbol;

    public ArbolController(TipoArbol tipoArbol)
    {
        this.tipoArbol = tipoArbol;

        switch (tipoArbol) {
            case BINARIO:
                arbolBinario = new ArbolBinario();
                break;
            case GENERAL:
                arbolGeneral = new ArbolGeneral();
                break;
            case EXPRESION:
                arbolExpresion = new ArbolExpresion();
                break;
        }
    }

    //Metodos

    //Insertar
    public void procesarEntrada(TipoEntrada tipoEntrada, String dato, String datoExtra)
    {
        switch (tipoArbol)
        {
            case BINARIO:
                procesarBinario(tipoEntrada, dato);
                break;

            case GENERAL:
                procesarGeneral(tipoEntrada, dato, datoExtra);
                break;

            case EXPRESION:
                if (tipoEntrada == TipoEntrada.EXPRESION) {
                    arbolExpresion.construirDesdeExpresion(dato);
                }
                break;
        }
    }

    // BINARIO
    private void procesarBinario(TipoEntrada tipoEntrada, String dato)
    {

        switch (tipoEntrada) {

            case MANUAL:
                arbolBinario.insertar(Integer.parseInt(dato));
                break;

            case LISTA:
                List<Integer> lista = ParserEnteros.desdeTexto(dato);
                lista.forEach(arbolBinario::insertar);
                break;

            case CSV:
                List<Integer> desdeCSV = ManejadorCSV.leerEnteros(dato);
                desdeCSV.forEach(arbolBinario::insertar);
                break;
        }
    }
    public void eliminarBinario(int valor) {
        arbolBinario.eliminar(valor);
    }

    public boolean buscarBinario(int valor) {
        return arbolBinario.buscar(valor) != null;
    }

    public List<Integer> getPreordenBinario() {
        return RecorridosBinario.preorden(arbolBinario.getRaiz());
    }

    public List<Integer> getInordenBinario() {
        return RecorridosBinario.inorden(arbolBinario.getRaiz());
    }

    public List<Integer> getPostordenBinario() {
        return RecorridosBinario.postorden(arbolBinario.getRaiz());
    }

    public int getAlturaBinario() {
        return arbolBinario.getAltura();
    }

    public Map<Integer, List<Integer>> getNivelesBinario() {
        return arbolBinario.obtenerNiveles();
    }

    // GENERAL
    private void procesarGeneral(TipoEntrada tipoEntrada, String dato, String padre)
    {

        switch (tipoEntrada) {

            case MANUAL:
                arbolGeneral.insertar(
                        Integer.parseInt(dato),
                        padre == null ? null : Integer.parseInt(padre)
                );
                break;

            case LISTA:
                List<Integer> lista = ParserEnteros.desdeTexto(dato);
                lista.forEach(arbolGeneral::insertarBalanceado);
                break;

            case CSV:
                List<Integer> desdeCSV = ManejadorCSV.leerEnteros(dato);
                desdeCSV.forEach(arbolGeneral::insertarBalanceado);
                break;
        }
    }
    public List<Integer> getPreordenGeneral() {
        return RecorridosGeneral.preorden(arbolGeneral.getRaiz());
    }

    public List<Integer> getPostordenGeneral() {
        return RecorridosGeneral.postorden(arbolGeneral.getRaiz());
    }

    public int getAlturaGeneral() {
        return arbolGeneral.getAltura();
    }

    public Map<Integer, List<Integer>> getNivelesGeneral() {
        return arbolGeneral.obtenerNiveles();
    }

    //Expresion
    public List<String> getPreordenExpresion() {
        return RecorridosExpresion.preorden(arbolExpresion.getRaiz());
    }

    public List<String> getPostordenExpresion() {
        return RecorridosExpresion.postorden(arbolExpresion.getRaiz());
    }

    public String getInordenExpresion() {
        return RecorridosExpresion.inordenConParentesis(arbolExpresion.getRaiz());
    }

    public int getAlturaExpresion() {
        return arbolExpresion.getAltura();
    }

    public double evaluarExpresion() {
        return arbolExpresion.evaluar();
    }

    // GETTERS
    public ArbolBinario getArbolBinario() {
        return arbolBinario;
    }

    public ArbolGeneral getArbolGeneral() {
        return arbolGeneral;
    }

    public ArbolExpresion getArbolExpresion() {
        return arbolExpresion;
    }
    public TipoArbol getTipoArbol() {
        return tipoArbol;
    }
}
