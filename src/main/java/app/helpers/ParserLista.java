package app.helpers;

import java.util.*;

public class ParserLista
{
    public static List<Integer> convertir(String texto) {

        List<Integer> lista = new ArrayList<>();

        if (texto == null || texto.isEmpty()) return lista;

        String[] partes = texto.split(",");

        for (String p : partes) {
            try {
                lista.add(Integer.parseInt(p.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido: " + p);
            }
        }

        return lista;
    }
}
