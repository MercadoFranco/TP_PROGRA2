package TPFinal;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class Persistencia {

    public static void guardarPacientes(Map<String, Paciente> pacientes) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("pacientes.json"), pacientes);
            System.out.println("Objetos Persona guardados exitosamente en pacientes.json");
        } catch (IOException e) {
            System.err.println("Error al guardar los pacientes: " + e.getMessage());
        }
    }

    public static Map<String, Paciente> cargarPacientes() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Paciente> pacientes = objectMapper.readValue(new File("pacientes.json"),
                    objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Paciente.class));
            System.out.println("Objetos Paciente leídos exitosamente de pacientes.json");
            return pacientes;
        } catch (IOException e) {
            System.err.println("Error al leer los pacientes: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static void guardarEspecialidades(Map<String, Especialidad> especialidades) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("especialidades.json"), especialidades);
            System.out.println("Objetos Especialidad guardados exitosamente en especialidades.json");
        } catch (IOException e) {
            System.err.println("Error al guardar las especialidades: " + e.getMessage());
        }
    }

    public static Map<String, Especialidad> cargarEspecialidades() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Especialidad> especialidades = objectMapper.readValue(new File("especialidades.json"),
                    objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Especialidad.class));
            System.out.println("Objetos Especialidad leídos exitosamente de especialidades.json");
            return especialidades;
        } catch (IOException e) {
            System.err.println("Error al leer las especialidades: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static void guardarEnfermedades(List<Enfermedad> enfermedades) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("enfermedades.json"), enfermedades);
            System.out.println("Objetos enfermedad guardados exitosamente en enfermedades.json");
        } catch (IOException e) {
            System.err.println("Error al guardar las enfermedades: " + e.getMessage());
        }
    }

    public static List<Enfermedad> cargarEnfermedades() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Enfermedad> enfermedades = objectMapper.readValue(new File("enfermedades.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Enfermedad.class));
            System.out.println("Enfermedades leídas exitosamente de enfermedades.json");
            return enfermedades;
        } catch (IOException e) {
            System.err.println("Error al leer las enfermedades: " + e.getMessage());
            return new ArrayList<Enfermedad>();
        }
    }

    public static void guardarTodo(GestorGuardia gestor) {
        guardarPacientes(gestor.getPacientes());
        guardarEspecialidades(gestor.getEspecialidades());
        guardarEnfermedades(gestor.getEnfermedades());
    }

    public static void cargarTodo(GestorGuardia gestor) {
        gestor.setPacientes(cargarPacientes());
        gestor.setEspecialidades(cargarEspecialidades());
        gestor.setRegistroEnfermedades(cargarEnfermedades());
    }

}