package TPFinal;

import java.time.LocalDateTime;
import java.util.*;

public class Especialidad {
    private Map<String, Medico> medicos;
    private String nombre;
    private String id;
    private PriorityQueue<Turno> turnos;

    public Especialidad(String nombre) {
        this.nombre = nombre;
        this.id = UUID.randomUUID().toString();
        this.medicos = new HashMap<>();
        this.turnos = new PriorityQueue<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Medico getMedico(String idMedico) { return medicos.get(idMedico); }

    public boolean registrarMedico(Medico medico) {
        String idMedico = medico.getId();
        if (!medicos.containsKey(idMedico)) {
            medicos.put(idMedico, medico);
            System.out.println("El médico ha sido registrado con éxito.");
            return true;
        }
        System.out.println("Ocurrió un error al registrar el medico.");
        return false;
    }

    public boolean eliminarMedico(String idMedico) {
        if (medicos.containsKey(idMedico)) {
            Collection<Turno> turnosDelMedico = medicos.get(idMedico).getTurnos();
            medicos.remove(idMedico);
            turnos.removeAll(turnosDelMedico);
            return true;
        }
        return false;
    }

    public Turno agregarTurno(Paciente paciente, int prioridad) {
        if (medicos.isEmpty()) {
            System.out.println("Aún no existen médicos en esta especialidad. Por favor primero cree alguno.");
            return null;
        }

        Medico medicoConMenosTurnos = null;
        int minTurnos = Integer.MAX_VALUE;

        for (Medico medico : medicos.values()) {
            int cantidadTurnos = medico.getTurnos().size();
            if (cantidadTurnos < minTurnos) {
                minTurnos = cantidadTurnos;
                medicoConMenosTurnos = medico;
            }
        }

        if (medicoConMenosTurnos == null) {
            System.out.println("Ocurrió un error al buscar un turno.");
            return null;
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        Turno nuevoTurno = new Turno(paciente, medicoConMenosTurnos, this, fechaActual, prioridad);
        medicoConMenosTurnos.agregarTurno(nuevoTurno);
        turnos.add(nuevoTurno);
        return nuevoTurno;
    }

    public boolean eliminarTurno(String idTurno) {
        Turno turnoAEliminar = turnos.stream().filter(t -> t.getId().equals(idTurno)).findFirst().orElse(null);
        if (turnoAEliminar == null) {
            return false;
        }

        turnos.remove(turnoAEliminar);
        return true;
    }
}
