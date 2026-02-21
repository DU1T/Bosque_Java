package app.controladores;

import app.core.implementaciones.*;
import app.helpers.ParserEnteros;
import app.helpers.ManejadorCSV;
import java.util.List;

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
}
