package TPFinal;

import java.util.LinkedList;

public class Medico {

    private String nombre;

    private String especialidad;

    private String id;

    private LinkedList<Turno> turnosDelDia;

    public Medico(String nombre, String especialidad, String id) {

        this.nombre = nombre;

        this.especialidad = especialidad;
this.id = id;

        this.turnosDelDia = new LinkedList<>();

    }

    public String getId() {

        return id;

    }

    public String getNombre() {

        return nombre;

    }

    public LinkedList<Turno> getTurnosDelDia() {

        return turnosDelDia;

    }

    public void agregarTurno(Turno turno) {

        turnosDelDia.add(turno);

    }

}
 