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


    public boolean atenderTurnoEspecialidad(String idEspecialidad) {
        Especialidad especialidad = especialidades.get(idEspecialidad);
        if (especialidad == null) {
            return false;
        }

        if (especialidad.getTurnos().isEmpty()) {
            return false;
        }

        Turno turnoAtendido = especialidad.atenderPrimerTurno();
        if (turnoAtendido != null) {
            Paciente pacienteAtendido = pacientes.get(idEspecialidad);
            pacienteAtendido.agregarTurnoAlHistorial(turnoAtendido);
        }

        return turnoAtendido != null;
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

        return null;
    }




    /*


    public void mostrarTurnosEnEspera() {
        System.out.println("📋 Turnos en espera:");

        for (Turno t : colaTurnos) {
            System.out.println(t);
        }

    }*/

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
