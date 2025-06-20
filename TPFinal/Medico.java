package TPFinal;

import TPFinal.TDAs.Stack;

import java.util.PriorityQueue;
import java.util.UUID;

public class Medico {
    private String nombre;
    private String idEspecialidad;
    private String id;
    private PriorityQueue<Turno> turnos;
    private Stack<Turno> historicoTurnos;

    public Medico(String nombre, String idEspecialidad) {
        this.nombre = nombre;
        this.idEspecialidad = idEspecialidad;
        this.id = UUID.randomUUID().toString();
        this.turnos = new PriorityQueue<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdEspecialidad() {
        return idEspecialidad;
    }

    public PriorityQueue<Turno> getTurnos() {
        return turnos;
    }

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }

    public Turno atenderPrimerTurno() {
        historicoTurnos.push(turnos.peek());
        return turnos.poll();
    }

    public boolean eliminarTurno(Turno turno) {
        return turnos.remove(turno);
    }
}
