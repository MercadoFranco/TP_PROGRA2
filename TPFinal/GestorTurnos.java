package TPFinal;

import TPFinal.TDAs.Stack;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GestorTurnos {
    private Map<String, Paciente> pacientes;
    private Map<String, Especialidad> especialidades;
    private PriorityQueue<Turno> colaTurnos; // Creo que esto en realidad debería ir en cada especialidad, no una para todo
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


    public void registrarPaciente(String nombre, String dni) {
        if (!pacientes.containsKey(dni)) {
            pacientes.put(dni, new Paciente(nombre, dni));
        }
    }


    public void solicitarTurnoEspecialidad(String dniPaciente, String idEspecialidad, LocalDateTime fechaHora, int prioridad) {
        Paciente paciente = pacientes.get(dniPaciente);
        Especialidad especialidad = especialidades.get(idEspecialidad);

        // TODO: Acá deberíamos revisar entre todos los médicos de la especialidad, cuál es el que tiene menos turnos, para asignárselo automáticamente.
        Medico medico = especialidad.getMedico("1");

        if (paciente != null && medico != null) {
            Turno turno = new Turno(paciente, medico, especialidad, fechaHora, prioridad);
            especialidad.agregarTurno(paciente, prioridad); // TODO: implementar para que la especialidad le agregue el turno al médico
            paciente.agregarTurnoAlHistorial(turno);
        } else {
            System.out.println("Especialidad o paciente no encontrado.");
        }
    }


    public Turno atenderSiguiente() {
        if (!colaTurnos.isEmpty()) {
            Turno turnoAtendido = colaTurnos.poll();
            // TODO: Implementar la lógica de atender a través de la clase especialidad, liberando el turno del médico
            //       en cuestión
        }

        return null;
    }


    public boolean cancelarTurno(String dniPaciente) {
        Iterator<Turno> it = colaTurnos.iterator();

        while (it.hasNext()) {
            Turno t = it.next();
            if (t.getPaciente().getDni().equals(dniPaciente)) {
                cancelacionesRecientes.push(t);
                it.remove();
                // TODO: También remover del listado de turnos de cada doctor y capaz de cada paciente.
                return true;
            }
        }

        return false;
    }


    public boolean deshacerCancelacion() {
        if (!cancelacionesRecientes.isEmpty()) {
            Turno turno = cancelacionesRecientes.pop();
            colaTurnos.add(turno);
            // TODO: También volver a agregar a la cola de los doctores y capaz de los pacientes también.
            return true;
        }

        return false;
    }


    public void mostrarTurnosEnEspera() {
        System.out.println("📋 Turnos en espera:");

        for (Turno t : colaTurnos) {
            System.out.println(t);
        }

    }

    public Map<String, Especialidad> getEspecialidades() {
        return this.especialidades;
    }

    public List<String> getEspecialidadesIds() {
        return new ArrayList<String>(this.especialidades.keySet());
    }

}
