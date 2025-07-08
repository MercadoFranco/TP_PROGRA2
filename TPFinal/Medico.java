package TPFinal;

import TPFinal.TDAs.Stack;

import java.util.PriorityQueue;

import static TPFinal.Utils.generateId;

public class Medico {
    private String nombre;
    private String idEspecialidad;
    private String id;
    private PriorityQueue<AtencionGuardia> atencionesGuardia;
    private Stack<AtencionGuardia> historicoTurnos;

    public Medico(String nombre, String idEspecialidad) {
        this.nombre = nombre;
        this.idEspecialidad = idEspecialidad;
        this.id = generateId();
        this.atencionesGuardia = new PriorityQueue<>();
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

    public PriorityQueue<AtencionGuardia> getTurnos() {
        return atencionesGuardia;
    }

    public Stack<AtencionGuardia> getHistoricoTurnos() { return historicoTurnos; }

    public void agregarTurno(AtencionGuardia atencionGuardia) {
        atencionesGuardia.offer(atencionGuardia);
    }

    public AtencionGuardia atenderPrimerTurno() {
        historicoTurnos.push(atencionesGuardia.peek());
        return atencionesGuardia.poll();
    }

    public boolean eliminarTurno(AtencionGuardia atencionGuardia) {
        return atencionesGuardia.remove(atencionGuardia);
    }

    public void mostrarTurnosPendientes() {
        PriorityQueue<AtencionGuardia> auxiliarAtencionesGuardia = new PriorityQueue<AtencionGuardia>(atencionesGuardia);

        if (auxiliarAtencionesGuardia.isEmpty()) {
            System.out.println("| El médico no tiene turnos.");
        } else {
            do {
                AtencionGuardia atencionGuardiaSacado = auxiliarAtencionesGuardia.poll();
                System.out.println("| " + atencionGuardiaSacado.toString());
            } while (!auxiliarAtencionesGuardia.isEmpty());
        }
    }

    public void mostrarTurnosHistoricos() {
        Stack<AtencionGuardia> auxiliarTurnos = new Stack<>(historicoTurnos);

        if (auxiliarTurnos.isEmpty()) {
            System.out.println("| El médico aún no ha tomado ningún turno.");
        } else {
            do {
                AtencionGuardia atencionGuardiaSacado = auxiliarTurnos.pop();
                System.out.println("| " + atencionGuardiaSacado.toString());
            } while (!auxiliarTurnos.isEmpty());
        }
    }
}