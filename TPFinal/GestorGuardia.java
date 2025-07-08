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
            System.out.println("ERROR: " + e);
            return false;
        }
    }

    public boolean eliminarPaciente(String dni) {
        try {
            pacientes.remove(dni);
            List<String> keysEspecialidades = getEspecialidades().keySet().stream().toList();
            keysEspecialidades.forEach(key -> {
                Especialidad especialidad = especialidades.get(key);
                especialidad.getTurnos().removeIf(t -> t.getIdPaciente().equals(dni));
            });
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // --------------------------------
    // ------Gestión Enfermedades------
    // --------------------------------

    public boolean agregarEnfermedad(String nombre, Set<Sintoma> sintomas, String idEspecialidad, int prioridad) {
        try {
            Enfermedad nuevaEnfermedad = new Enfermedad(nombre, sintomas, idEspecialidad, prioridad);
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

    public boolean eliminarMedicoYReasignar(String idEspecialidad, String idMedico) {
        Especialidad especialidad = especialidades.get(idEspecialidad);
        if (especialidad == null) {
            System.out.println("La especialidad no existe.");
            return false;
        }

        Medico medicoEliminado = especialidad.getMedico(idMedico);
        if (medicoEliminado == null) {
            System.out.println("El médico no existe.");
            return false;
        }

        List<Medico> medicosDisponibles = new ArrayList<>();
        for (Medico m : especialidad.getMedicos().values()) {
            if (!m.getId().equals(idMedico)) {
                medicosDisponibles.add(m);
            }
        }

        if (medicosDisponibles.isEmpty()) {
            System.out.println("No hay otros médicos disponibles en la especialidad " + especialidad.getId());
            return false;
        }

        PriorityQueue<AtencionGuardia> turnosPendientes = medicoEliminado.getAtencionesGuardia();

        while (!turnosPendientes.isEmpty()) {
            AtencionGuardia turno = turnosPendientes.poll();
            Medico menosCargado = buscarMedicoConMenosTurnos(medicosDisponibles);
            turno.setIdMedico(menosCargado.getId());
            menosCargado.agregarTurno(turno);
        }

        especialidad.eliminarMedico(idMedico);
        System.out.println("Médico eliminado y turnos reasignados correctamente.");
        return true;
    }

    private Medico buscarMedicoConMenosTurnos(List<Medico> lista) {
        Medico seleccionado = null;
        int min = Integer.MAX_VALUE;
        for (Medico m : lista) {
            int size = m.getAtencionesGuardia().size();
            if (size < min) {
                min = size;
                seleccionado = m;
            }
        }
        return seleccionado;
    }

    // --------------------------------
    // ------Gestión de Atención-------
    // ------------Inicial-------------
    // --------------------------------

    public AtencionInicial solicitarAtencionInicial(String idPaciente) {
        Paciente paciente = pacientes.getOrDefault(idPaciente, null);
        if (paciente != null) {
            AtencionInicial nuevaSolicitud = new AtencionInicial(idPaciente);
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
        if (enfermedadesProbables.isEmpty()) {
            return null;
        } else {
            return enfermedadesProbables.get(0);
        }
    }

    public AtencionGuardia solicitarAtencionEspecialidad(String dniPaciente, String idEspecialidad, int prioridad, Set<Sintoma> sintomas) {
        Especialidad especialidad = especialidades.get(idEspecialidad);
        if (pacientes.get(dniPaciente) != null && especialidad != null) {
            return especialidad.agregarTurno(dniPaciente, prioridad, sintomas);
        } else {
            return null;
        }
    }

    public AtencionGuardia atenderAtencionEspecialidad(String idEspecialidad) {
        Especialidad especialidad = especialidades.get(idEspecialidad);
        if (especialidad == null || especialidad.getTurnos().isEmpty()) {
            return null;
        }

        AtencionGuardia atencionGuardiaAtendido = especialidad.atenderPrimerTurno();
        if (atencionGuardiaAtendido != null) {
            Paciente pacienteAtendido = pacientes.get(atencionGuardiaAtendido.getIdPaciente());

            pacienteAtendido.agregarTurnoAlHistorial(atencionGuardiaAtendido);
        }
        return atencionGuardiaAtendido;
    }

    public AtencionGuardia atenderAtencionMedico(String idEspecialidad, String idMedico) {
        Especialidad especialidad = especialidades.get(idEspecialidad);
        if (especialidad == null) return null;

        Medico medico = especialidad.getMedico(idMedico);
        if (medico == null || medico.getAtencionesGuardia().isEmpty()) return null;

        AtencionGuardia atencionGuardiaAtendido = medico.atenderPrimerTurno();
        if (atencionGuardiaAtendido != null) {
            especialidad.eliminarTurno(atencionGuardiaAtendido);
        }
        return atencionGuardiaAtendido;
    }

    public boolean cancelarTurno(AtencionGuardia atencionGuardia) {
        Especialidad especialidad = especialidades.get(atencionGuardia.getIdEspecialidad());
        if (especialidad.cancelarTurno(atencionGuardia)) {
            cancelacionesRecientes.push(atencionGuardia);
            return true;
        }
        return false;
    }

    public AtencionGuardia deshacerCancelacion() {
        if (!cancelacionesRecientes.isEmpty()) {
            AtencionGuardia atencionGuardia = cancelacionesRecientes.pop();
            int prioridad = atencionGuardia.getPrioridad();
            Especialidad especialidad = especialidades.get(atencionGuardia.getIdEspecialidad());
            Set<Sintoma> sintomas = atencionGuardia.getSintomasPaciente();
            especialidad.agregarTurno(atencionGuardia.getIdPaciente(), prioridad, sintomas);
            return atencionGuardia;
        }
        return null;
    }

    // -----------Getters------------

    public Map<String, Especialidad> getEspecialidades() {
        return this.especialidades;
    }

    public Map<String, Paciente> getPacientes() {
        return this.pacientes;
    }

    public Paciente getPaciente(String dni) {
        return pacientes.get(dni);
    }

    public List<String> getEspecialidadesIds() {
        return new ArrayList<>(this.especialidades.keySet());
    }

    public Queue<AtencionInicial> getFilaAtencionInicial() {
        return this.filaEvaluacionInicial;
    }

    public List<Enfermedad> getEnfermedades() {
        return registroEnfermedades;
    }

    // -----------Setters---------

    public void setPacientes(Map<String, Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public void setEspecialidades(Map<String, Especialidad> especialidades) {
        this.especialidades = especialidades;
    }

    public void setRegistroEnfermedades(List<Enfermedad> registroEnfermedades) {
        this.registroEnfermedades = registroEnfermedades;
    }
}
