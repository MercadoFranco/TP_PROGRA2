package TPFinal;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Persistencia {

    // --- Utils ----
    public static String stringifySintomas(Set<Utils.Sintoma> sintomas) {
        StringBuilder sintomaString = new StringBuilder();
        // Formato: Sintoma|Sintoma|Sintoma
        for (Utils.Sintoma sintoma : sintomas) {
            sintomaString.append(sintoma.name()).append("|");
        }
        return sintomaString.toString();
    }

    public static Set<Utils.Sintoma> parseSintomas(String sintomas) {
        Set<Utils.Sintoma> sintomaSet = new HashSet<>();
        String[] sintomasArray = sintomas.split("\\|");
        for (String sintoma : sintomasArray) {
            sintomaSet.add(Utils.Sintoma.valueOf(sintoma));
        }
        return sintomaSet;
    }

    public static String stringifyAtencionGuardia(List<AtencionGuardia> atencionesGuardia) {
        StringBuilder atencionString = new StringBuilder();
        // Formato: id, idPaciente, idEspecialidad, idMedico, fechaHora, prioridad, Sintomas.
        for (AtencionGuardia atencionGuardia : atencionesGuardia) {
            atencionString
                    .append(atencionGuardia.getId()).append(',')
                    .append(atencionGuardia.getIdPaciente()).append(',')
                    .append(atencionGuardia.getIdEspecialidad()).append(',')
                    .append(atencionGuardia.getIdMedico()).append(',')
                    .append(atencionGuardia.getFechaHora()).append(',')
                    .append(atencionGuardia.getPrioridad()).append(',')
                    .append(stringifySintomas(atencionGuardia.getSintomasPaciente())).append(',')
                    .append('\n');
        }
        return atencionString.toString();
    }

    public static List<AtencionGuardia> parseAtencionGuardia(String string) {
        List<AtencionGuardia> atencionesGuardia = new ArrayList<>();
        try {
            List<String> lines = Arrays.asList(string.split("\n"));
            lines.forEach(line -> {
                List<String> params = Arrays.asList(line.split(","));
                String id = params.get(0);
                String idPaciente = params.get(1);
                String idEspecialidad = params.get(2);
                String idMedico = params.get(3);
                LocalDateTime fechaHora = LocalDateTime.parse(params.get(4));
                int prioridad = Integer.parseInt(params.get(5));
                String sintomasPaciente = params.get(6);
                AtencionGuardia parseada = new AtencionGuardia(idPaciente, idMedico, idEspecialidad, fechaHora, prioridad, parseSintomas(sintomasPaciente), id);

                atencionesGuardia.add(parseada);
            });
        } catch (RuntimeException e) {
            return null;
        }

        return atencionesGuardia;
    }

    public static String stringifyEnfermedad(Enfermedad enfermedad) {
        return enfermedad.getNombre() + ',' +
                stringifySintomas(enfermedad.getSintomas()) + ',' +
                enfermedad.getIdEspecialidadAsociada() + ',' +
                enfermedad.getPrioridad() +
                '\n';
    }

    public static Enfermedad parseEnfermedad(String string) {
        List<String> params = Arrays.asList(string.split(","));
        String nombre = params.get(0);
        String sintomas = params.get(1);
        String idEspecialidad = params.get(2);
        int prioridad = Integer.parseInt(params.get(3));

        return new Enfermedad(nombre, parseSintomas(sintomas), idEspecialidad, prioridad);
    }


    // --- Pacientes ---
    public static void guardarPacientes(Map<String, Paciente> pacientes, String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Paciente p : pacientes.values()) {
                // Formato: DNI, Nombre, [historialTurnos]

                bw.write(p.getDni() + "," + p.getNombre());
                bw.newLine();
            }
        }
    }

    public static Map<String, Paciente> cargarPacientes(String archivo) throws IOException {
        Map<String, Paciente> pacientes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] cols = linea.split(",");
                if (cols.length >= 2) {
                    String dni = cols[0];
                    String nombre = cols[1];
                    pacientes.put(dni, new Paciente(dni, nombre));
                }
            }
        }
        return pacientes;
    }

    // --- Médicos ---
    public static void guardarMedicos(Map<String, Medico> medicos, String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Medico m : medicos.values()) {
                // Formato: ID,Nombre
                bw.write(m.getId() + "," + m.getNombre());
                bw.newLine();
            }
        }
    }

    public static Map<String, Medico> cargarMedicos(String archivo) throws IOException {
        Map<String, Medico> medicos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] cols = linea.split(",");
                if (cols.length >= 2) {
                    String id = cols[0];
                    String nombre = cols[1];
                    medicos.put(id, new Medico(id, nombre));
                }
            }
        }
        return medicos;
    }

    // --- Especialidades ---
    public static void guardarEspecialidades(Map<String, Especialidad> especialidades, String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Especialidad e : especialidades.values()) {
                // Formato: NombreEspecialidad
                bw.write(e.getNombre());
                bw.newLine();
            }
        }
    }

    public static Map<String, Especialidad> cargarEspecialidades(String archivo) throws IOException {
        Map<String, Especialidad> especialidades = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String nombre = linea.trim();
                especialidades.put(nombre, new Especialidad(nombre));
            }
        }
        return especialidades;
    }

//    // --- Enfermedades ---
//    public static void guardarEnfermedades(Map<String, Enfermedad> enfermedades, String archivo) throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
//            for (Enfermedad enf : enfermedades.values()) {
//                // Formato: ID,Nombre
//                bw.write(enf.getId() + "," + enf.getNombre());
//                bw.newLine();
//            }
//        }
//    }
//
//    public static Map<String, Enfermedad> cargarEnfermedades(String archivo) throws IOException {
//        List<Enfermedad> enfermedades = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
//            String linea;
//            while ((linea = br.readLine()) != null) {
//                String[] cols = linea.split(",");
//                if (cols.length >= 2) {
//                    String nombre = cols[1];
//                    Set<Utils.Sintoma> sintomas = new HashSet<Utils.Sintoma>();
//                    String idEspecialidad = cols[3];
//                    int prioridad = Integer.parseInt(cols[4]);
//                    enfermedades.add(new Enfermedad(nombre, sintomas, idEspecialidad, prioridad));
//                }
//            }
//        }
//        return enfermedades;
//    }
//
//    // --- Atenciones en Guardia ---
//    public static void guardarAtenciones(List<AtencionGuardia> atenciones, String archivo) throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
//            for (AtencionGuardia ag : atenciones) {
//                // Formato: IDAtencion,PacienteDNI,MedicoID,Especialidad,Prioridad,FechaHora
//                bw.write(ag.getId() + ","
//                        + ag.getIdPaciente() + ","
//                        + ag.getMedico().getId() + ","
//                        + ag.getEspecialidad().getNombre() + ","
//                        + ag.getPrioridad() + ","
//                        + ag.getFechaHora().toString());
//                bw.newLine();
//            }
//        }
//    }
//
//    public static List<AtencionGuardia> cargarAtenciones(String archivo,
//                                                         Map<String, Paciente> pacientes,
//                                                         Map<String, Medico> medicos,
//                                                         Map<String, Especialidad> especialidades) throws IOException {
//        List<AtencionGuardia> atenciones = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
//            String linea;
//            while ((linea = br.readLine()) != null) {
//                String[] cols = linea.split(",");
//                if (cols.length >= 6) {
//                    String id = cols[0];
//                    Paciente p = pacientes.get(cols[1]);
//                    Medico m = medicos.get(cols[2]);
//                    Especialidad e = especialidades.get(cols[3]);
//                    int prioridad = Integer.parseInt(cols[4]);
//                    LocalDateTime fecha = LocalDateTime.parse(cols[5]);
//                    atenciones.add(new AtencionGuardia(id, p, m, e, prioridad, fecha));
//                }
//            }
//        }
//        return atenciones;
//    }
//
//    // Método de conveniencia para guardar todo de GestorGuardia
//    public static void guardarTodo(GestorGuardia gestor) throws IOException {
//        guardarPacientes(gestor.getPacientes(), "pacientes.csv");
//        guardarMedicos(gestor.getMedicos(), "medicos.csv");
//        guardarEspecialidades(gestor.getEspecialidades(), "especialidades.csv");
//        guardarEnfermedades(gestor.getEnfermedades(), "enfermedades.csv");
//        guardarAtenciones(gestor.getAtenciones(), "atenciones.csv");
//    }
//
//    // Método de conveniencia para cargar todo en GestorGuardia
//    public static void cargarTodo(GestorGuardia gestor) throws IOException {
//        Map<String, Paciente> pacs = cargarPacientes("pacientes.csv");
//        gestor.setPacientes(pacs);
//        Map<String, Medico> meds = cargarMedicos("medicos.csv");
//        gestor.setMedicos(meds);
//        Map<String, Especialidad> esps = cargarEspecialidades("especialidades.csv");
//        gestor.setEspecialidades(esps);
//        Map<String, Enfermedad> enfs = cargarEnfermedades("enfermedades.csv");
//        gestor.setEnfermedades(enfs);
//        List<AtencionGuardia> ats = cargarAtenciones("atenciones.csv", pacs, meds, esps);
//        gestor.setAtenciones(ats);
//    }
}