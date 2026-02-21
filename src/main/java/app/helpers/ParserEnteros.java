package app.helpers;

import java.util.*;

public class ParserEnteros
{
    public static List<Integer> desdeTexto(String texto) {

        List<Integer> lista = new ArrayList<>();

        if (texto == null || texto.isBlank()) return lista;

        String[] partes = texto.split(",");

        for (String parte : partes) {
            try {
                lista.add(Integer.parseInt(parte.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido: " + parte);
            }
        }
        return lista;
    }
}
