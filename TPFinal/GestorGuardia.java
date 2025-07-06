package TPFinal;

import TPFinal.TDAs.Stack;
import TPFinal.Utils.Sintoma;

import java.util.*;

public class GestorGuardia {
    private Map<String, Paciente> pacientes;
    private Map<String, Especialidad> especialidades;
    private Stack<AtencionGuardia> cancelacionesRecientes;
    private Queue<AtencionInicial> filaEvaluacionInicial;
    private List<Enfermedad> registroEnfermedades;

    public GestorGuardia() {
        pacientes = new HashMap<>();
        cancelacionesRecientes = new Stack<>();
        filaEvaluacionInicial = new LinkedList<>();
        especialidades = new HashMap<>();
        registroEnfermedades = new ArrayList<>();
    }

    // --------------------------------
    // ----Gestión de Especialidad-----
    // --------------------------------

    public boolean registrarEspecialidad(Especialidad especialidad) {
        try {
            especialidades.put(especialidad.getId(), especialidad);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean eliminarEspecialidad(String idEspecialidad) {
        try {
            especialidades.remove(idEspecialidad);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // --------------------------------
    // ------Gestión de Paciente-------
    // --------------------------------

    public boolean registrarPaciente(Paciente paciente) {
        try {
            pacientes.put(paciente.getDni(), paciente);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean eliminarPaciente(String dni) {
        try {
            pacientes.remove(dni);
            List<String> keysEspecialidades = getEspecialidades().keySet().stream().toList();
            keysEspecialidades.forEach(key -> {
                Especialidad especialidad = especialidades.get(key);
                especialidad.getTurnos().removeIf(t -> t.getPaciente().getDni().equals(dni));
            });

            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // --------------------------------
    // ------Gestión Enfermedades------
    // --------------------------------

    public boolean agregarEnfermedad(String nombre, Set<Sintoma> sintomas, Especialidad especialidad, int prioridad) {
        try {
            Enfermedad nuevaEnfermedad = new Enfermedad(nombre, sintomas, especialidad, prioridad);
            registroEnfermedades.add(nuevaEnfermedad);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean eliminarEnfermedad(String nombre) {
        Enfermedad enfermedadEncontrada = registroEnfermedades.stream().filter(enfermedad -> enfermedad.getNombre().equals(nombre)).findFirst().orElse(null);
        if (enfermedadEncontrada == null) {
            return false;
        } else {
            registroEnfermedades.remove(enfermedadEncontrada);
            return true;
        }
    }

    // --------------------------------
    // ------Gestión de Medico---------
    // --------------------------------

    public boolean registrarMedico(String idEspecialidad, Medico medico) {
        try {
            Especialidad especialidadMedico = especialidades.get(idEspecialidad);
            if (especialidadMedico != null) {
                return especialidadMedico.registrarMedico(medico);
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean eliminarMedico(String idEspecialidad, String idMedico) {
        try {
            Especialidad especialidadMedico = especialidades.get(idEspecialidad);
            if (especialidadMedico != null) {
                return especialidadMedico.eliminarMedico(idMedico);
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    // --------------------------------
    // ------Gestión de Atención-------
    // ------------Inicial-------------
    // --------------------------------

    public AtencionInicial solicitarAtencionInicial(Paciente paciente) {
        if (paciente != null) {
            AtencionInicial nuevaSolicitud = new AtencionInicial(paciente);
            filaEvaluacionInicial.add(nuevaSolicitud);
            return nuevaSolicitud;
        } else {
            return null;
        }
    }

    public AtencionInicial atenderAtencionInicial() {
        if (filaEvaluacionInicial.isEmpty()) {
            return null;
        }

        return filaEvaluacionInicial.poll();
    }


    // --------------------------------
    // -----Gestión de Atención--------
    // ---------Especialidad-----------
    // --------------------------------

    public List<Enfermedad> obtenerCoincidenciaEnfermedades(Set<Sintoma> sintomas) {
        List<Enfermedad> enfermedadesOrdenadas = new ArrayList<>(registroEnfermedades);
        enfermedadesOrdenadas.sort((a, b) -> {
            double pa = a.calcularProbabilidad(sintomas);
            double pb = b.calcularProbabilidad(sintomas);
            return Double.compare(pb, pa);
        });

        return enfermedadesOrdenadas;
    }

    public Enfermedad obtenerEnfermedadMasProbable(Set<Sintoma> sintomas) {
        List<Enfermedad> enfermedadesProbables = obtenerCoincidenciaEnfermedades(sintomas);
        return enfermedadesProbables.get(0);
    }

    public AtencionGuardia solicitarAtencionEspecialidad(String dniPaciente, String idEspecialidad, int prioridad, Set<Sintoma> sintomas) {
        Paciente paciente = pacientes.get(dniPaciente);
        Especialidad especialidad = especialidades.get(idEspecialidad);

        if (paciente != null && especialidad != null) {
            return especialidad.agregarTurno(paciente, prioridad, sintomas);
        } else {
            return null;
        }
    }

    public AtencionGuardia atenderAtencionEspecialidad(String idEspecialidad) {
        Especialidad especialidad = especialidades.get(idEspecialidad);
        if (especialidad == null) {
            System.out.println("La Especialidad no existe");
            return null;
        }

        if (especialidad.getTurnos().isEmpty()) {
            System.out.println("La especialidad no tiene turnos");
            return null;
        }

        AtencionGuardia atencionGuardiaAtendido = especialidad.atenderPrimerTurno();
        if (atencionGuardiaAtendido != null) {
            Paciente pacienteAtendido = atencionGuardiaAtendido.getPaciente();
            pacienteAtendido.agregarTurnoAlHistorial(atencionGuardiaAtendido);
        }

        return atencionGuardiaAtendido;
    }

    public AtencionGuardia atenderAtencionMedico(String idEspecialidad, String idMedico) {
        Especialidad especialidad = especialidades.get(idEspecialidad);

        if (especialidad == null) {
            return null;
        }

        Medico medico = especialidad.getMedico(idMedico);

        if (medico == null || medico.getTurnos().isEmpty()) {
            return null;
        }

        AtencionGuardia atencionGuardiaAtendido = medico.atenderPrimerTurno();
        if (atencionGuardiaAtendido != null) {
            especialidad.eliminarTurno(atencionGuardiaAtendido);
        }

        return atencionGuardiaAtendido;
    }

    public boolean cancelarTurno(AtencionGuardia atencionGuardia) {
        Especialidad especialidad = atencionGuardia.getEspecialidad();
        if (especialidad.cancelarTurno(atencionGuardia)) {
            cancelacionesRecientes.push(atencionGuardia);
            return true;
        }

        return false;
    }

    public AtencionGuardia deshacerCancelacion() {
        if (!cancelacionesRecientes.isEmpty()) {
            AtencionGuardia atencionGuardia = cancelacionesRecientes.pop();

            Paciente paciente = atencionGuardia.getPaciente();
            int prioridad = atencionGuardia.getPrioridad();
            Especialidad especialidad = atencionGuardia.getEspecialidad();
            Set<Sintoma> sintomas = atencionGuardia.getSintomasPaciente();

            especialidad.agregarTurno(paciente, prioridad, sintomas);
            return atencionGuardia;
        }

        System.out.println("No hay turnos cancelados para recuperar.");
        return null;
    }

    // ------------------------------


    // ------------------------------
    // -----------Getters------------
    // ------------------------------

    public Map<String, Especialidad> getEspecialidades() {
        return this.especialidades;
    }

    public Paciente getPaciente(String dni) {
        return pacientes.get(dni);
    }

    public List<String> getEspecialidadesIds() {
        return new ArrayList<String>(this.especialidades.keySet());
    }

    public Queue<AtencionInicial> getFilaAtencionInicial() {
        return this.filaEvaluacionInicial;
    }

    public List<Enfermedad> getEnfermedades() {
        return registroEnfermedades;
    }

}
