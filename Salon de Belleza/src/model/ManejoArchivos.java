package model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ManejoArchivos {
/*

    public void EscribirUnProfesional(Profesional profesional) {
        try {
            List<Profesional> profesionales = LeerArchivoProfesionales();
            if (profesionales == null) {
                profesionales = new ArrayList<>(); // Si el archivo está vacío o no se pudo leer, inicializar la lista
            }
            profesionales.add(profesional);


            // Escribir la lista actualizada de profesionales en el archivo
            try (FileWriter fileWriter = new FileWriter("profesionales.json")) {
                Gson gson = new Gson();
                String json = gson.toJson(profesionales);
                fileWriter.write(json);
            }

            System.out.println("Profesional agregado exitosamente.");

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }*/

    public void EscribirUnProfesional(Profesional profesional) {
        try {
            // Leer los profesionales actuales (si existen)
            List<Profesional> profesionales = LeerArchivoProfesionales();

            // Escribir el nuevo profesional en el archivo
            try (FileWriter writer = new FileWriter("profesionales.json", true)) {
                Gson gson = new Gson();
                // Convertimos el objeto Profesional a JSON
                String json = gson.toJson(profesional);
                // Escribimos el objeto JSON seguido de un salto de línea
                if (profesionales.size() > 0) {
                    writer.write(System.lineSeparator()); // Solo agregar salto de línea si ya hay contenido
                }
                writer.write(json);
            }

            System.out.println("Profesional agregado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }


    public List<Profesional> LeerArchivoProfesionales() {
        try (FileReader fileReader = new FileReader("profesionales.json")) {
            Gson gson = new Gson();
            Type tipoListaProfesionales = new TypeToken<List<Profesional>>() {
            }.getType();
            List<Profesional> profesionales = gson.fromJson(fileReader, tipoListaProfesionales);

            // Si el archivo está vacío o no contiene datos válidos, devolver una lista vacía
            return (profesionales != null) ? profesionales : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, creando uno nuevo.");
            return new ArrayList<>(); // Si el archivo no existe, devolver una lista vacía
        } catch (JsonSyntaxException | IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return new ArrayList<>(); // Devolver una lista vacía en caso de error
    }
}

/*
    public void EscribirProfesional(Profesional profesional){
        try {
            List<Profesional> profesionales=LeerArchivoProfesionales();
            profesionales.add(profesional);
            FileWriter fileWriter = new FileWriter("profesionales.json");
            Gson gson = new Gson();
            String json = gson.toJson(profesionales);
            fileWriter.write(json);
            fileWriter.close();
        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Profesional> LeerArchivoProfesionales() {
        try {
            FileReader fileReader = new FileReader("profesionales.json");
            Gson gson = new Gson();
            Type tipoListaProfesionales = new TypeToken<List<Profesional>>() {
            }.getType();
            List<Profesional> profesionales = gson.fromJson(fileReader, tipoListaProfesionales);
            fileReader.close();
            return profesionales;
        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
*/
