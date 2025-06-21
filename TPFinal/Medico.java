package TPFinal;

import TPFinal.TDAs.Stack;

import java.util.PriorityQueue;

import static TPFinal.Utils.generateId;

public class Medico {
    private String nombre;
    private String idEspecialidad;
    private String id;
    private PriorityQueue<Turno> turnos;
    private Stack<Turno> historicoTurnos;

    public Medico(String nombre, String idEspecialidad) {
        this.nombre = nombre;
        this.idEspecialidad = idEspecialidad;
        this.id = generateId();
        this.turnos = new PriorityQueue<>();
        this.historicoTurnos = new Stack<>();
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

    public Stack<Turno> getHistoricoTurnos() { return historicoTurnos; }

    public void agregarTurno(Turno turno) {
        turnos.offer(turno);
    }

    public Turno atenderPrimerTurno() {
        historicoTurnos.push(turnos.peek());
        return turnos.poll();
    }

    public boolean eliminarTurno(Turno turno) {
        return turnos.remove(turno);
    }

    public void mostrarTurnosPendientes() {
        PriorityQueue<Turno> auxiliarTurnos = new PriorityQueue<Turno>(turnos);

        if (auxiliarTurnos.isEmpty()) {
            System.out.println("| El médico no tiene turnos.");
        } else {
            do {
                Turno turnoSacado = auxiliarTurnos.poll();
                System.out.println("| " + turnoSacado.toString());
            } while (!auxiliarTurnos.isEmpty());
        }
    }

    public void mostrarTurnosHistoricos() {
        Stack<Turno> auxiliarTurnos = new Stack<>(historicoTurnos);

        if (auxiliarTurnos.isEmpty()) {
            System.out.println("| El médico aún no ha tomado ningún turno.");
        } else {
            do {
                Turno turnoSacado = auxiliarTurnos.pop();
                System.out.println("| " + turnoSacado.toString());
            } while (!auxiliarTurnos.isEmpty());
        }
    }
}
