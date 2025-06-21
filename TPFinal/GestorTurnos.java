package TPFinal;

import TPFinal.TDAs.Stack;

import java.util.*;

public class GestorTurnos {
    private Map<String, Paciente> pacientes;
    private Map<String, Especialidad> especialidades;
    private Stack<Turno> cancelacionesRecientes;

    public GestorTurnos() {
        pacientes = new HashMap<>();
        cancelacionesRecientes = new Stack<>();
        especialidades = new HashMap<>();
    }

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


    public Turno solicitarTurnoEspecialidad(String dniPaciente, String idEspecialidad, int prioridad) {
        Paciente paciente = pacientes.get(dniPaciente);
        Especialidad especialidad = especialidades.get(idEspecialidad);

        if (paciente != null && especialidad != null) {
            return especialidad.agregarTurno(paciente, prioridad);
        } else {
            return null;
        }
    }


    public Turno atenderTurnoEspecialidad(String idEspecialidad) {
        Especialidad especialidad = especialidades.get(idEspecialidad);
        if (especialidad == null) {
            System.out.println("La Especialidad no existe");
            return null;
        }

        if (especialidad.getTurnos().isEmpty()) {
            System.out.println("La especialidad no tiene turnos");
            return null;
        }

        Turno turnoAtendido = especialidad.atenderPrimerTurno();
        if (turnoAtendido != null) {
            Paciente pacienteAtendido = turnoAtendido.getPaciente();
            pacienteAtendido.agregarTurnoAlHistorial(turnoAtendido);
        }

        return turnoAtendido;
    }

    public Turno atenderTurnoMedico(String idEspecialidad, String idMedico) {
        Especialidad especialidad = especialidades.get(idEspecialidad);

        if (especialidad == null) {
            return null;
        }

        Medico medico = especialidad.getMedico(idMedico);

        if (medico == null || medico.getTurnos().isEmpty()) {
            return null;
        }

        Turno turnoAtendido = medico.atenderPrimerTurno();
        if (turnoAtendido != null) {
            especialidad.eliminarTurno(turnoAtendido);
        }

        return turnoAtendido;
    }

    public boolean cancelarTurno(Turno turno) {
        Especialidad especialidad = turno.getEspecialidad();
        if (especialidad.cancelarTurno(turno)) {
            cancelacionesRecientes.push(turno);
            return true;
        }

        return false;
    }

    public Turno deshacerCancelacion() {
        if (!cancelacionesRecientes.isEmpty()) {
            Turno turno = cancelacionesRecientes.pop();

            Paciente paciente = turno.getPaciente();
            int prioridad = turno.getPrioridad();
            Especialidad especialidad = turno.getEspecialidad();

            especialidad.agregarTurno(paciente, prioridad);
            return turno;
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

}
