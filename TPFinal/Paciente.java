package TPFinal;

import TPFinal.TDAs.Stack;

public class Paciente {
    private String nombre;
    private String dni;
    private Stack<Turno> historialTurnos;

    public Paciente(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
        this.historialTurnos = new Stack<Turno>();
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public Stack<Turno> getHistorialTurnos() {
        return historialTurnos;
    }

    public void mostrarTurnosHistoricos() {
        Stack<Turno> auxiliarTurnos = new Stack<>(historialTurnos);

        if (auxiliarTurnos.isEmpty()) {
            System.out.println("| El Paciente aún no ha pedido ningún turno.");
        } else {
            do {
                Turno turnoSacado = auxiliarTurnos.pop();
                System.out.println("| " + turnoSacado.toString());
            } while (!auxiliarTurnos.isEmpty());
        }
    }

    public void agregarTurnoAlHistorial(Turno turno) {
        historialTurnos.push(turno);
    }
}
