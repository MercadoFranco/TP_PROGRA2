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

    public Medico getMedico(String idMedico) {
        return medicos.get(idMedico);
    }

    public List<String> getMedicosIds() {
        return new ArrayList<String>(this.medicos.keySet());
    }

    public Map<String, Medico> getMedicos() {
        return this.medicos;
    }

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
            return null;
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        Turno nuevoTurno = new Turno(paciente, medicoConMenosTurnos, this, fechaActual, prioridad);
        medicoConMenosTurnos.agregarTurno(nuevoTurno);
        turnos.add(nuevoTurno);
        return nuevoTurno;
    }

    public Turno atenderPrimerTurno() {
        if (turnos.isEmpty()) {
            return null;
        }

        Turno turnoAtendido = turnos.poll();
        if (turnoAtendido != null) {
            Medico medicoQueAtendio = turnoAtendido.getMedico();
            medicoQueAtendio.atenderPrimerTurno();
            return turnoAtendido;
        }

        return null;
    }

    public boolean eliminarTurno(Turno turno) {
        if (!turnos.contains(turno)) {
            return false;
        }

        turnos.remove(turno);
        return true;
    }

    public boolean cancelarTurno(Turno turno) {
        if (!turnos.contains(turno)) {
            return false;
        }

        Medico medico = turno.getMedico();
        if (medico.eliminarTurno(turno)) {
            turnos.remove(turno);
        }

        return false;
    }

    public PriorityQueue<Turno> getTurnos() {
        return turnos;
    }
}
