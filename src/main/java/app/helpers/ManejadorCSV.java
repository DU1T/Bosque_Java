package app.helpers;

import java.io.*;
import java.util.*;

public class ManejadorCSV
{
    public static List<Integer> leerEnteros(String ruta) {

        List<Integer> valores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea;

            while ((linea = br.readLine()) != null) {
                valores.addAll(ParserEnteros.desdeTexto(linea));
            }

        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
        }

        return valores;
    }
}
