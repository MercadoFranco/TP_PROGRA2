package TPFinal;


import TPFinal.TDAs.LinkedList;

import java.util.PriorityQueue;

public class Medico {
    private String nombre;
    private String especialidad;
    private String id;
    private PriorityQueue<Turno> turnos;

    public Medico(String nombre, String especialidad, String id) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.id = id;
        this.turnos = new PriorityQueue<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public PriorityQueue<Turno> getTurnos() {
        return turnos;
    }

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }
}
