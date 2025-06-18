package TPFinal;

import TPFinal.TDAs.LinkedList;

public class Paciente {
    private String nombre;
    private String dni;
    private LinkedList<Turno> historialTurnos;

    public Paciente(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
        this.historialTurnos = new LinkedList<>();
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public LinkedList<Turno> getHistorialTurnos() {
        return historialTurnos;
    }

    public void agregarTurnoAlHistorial(Turno turno) {
        historialTurnos.add(turno);
    }
}
