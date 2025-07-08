package TPFinal;

import TPFinal.TDAs.Stack;

public class Paciente {
    private String nombre;
    private String dni;
    private Stack<AtencionGuardia> historialTurnos;

    public Paciente(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
        this.historialTurnos = new Stack<AtencionGuardia>();
    }

    public Paciente() {
        this.nombre = "";
        this.dni = "";
        this.historialTurnos = new Stack<AtencionGuardia>();
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public Stack<AtencionGuardia> getHistorialTurnos() {
        return historialTurnos;
    }

    public void mostrarTurnosHistoricos() {
        Stack<AtencionGuardia> auxiliarTurnos = new Stack<>(historialTurnos);

        if (auxiliarTurnos.isEmpty()) {
            System.out.println("| El Paciente aún no ha pedido ningún turno.");
        } else {
            do {
                AtencionGuardia atencionGuardiaSacado = auxiliarTurnos.pop();
                System.out.println("| " + atencionGuardiaSacado.toString());
            } while (!auxiliarTurnos.isEmpty());
        }
    }

    public void agregarTurnoAlHistorial(AtencionGuardia atencionGuardia) {
        historialTurnos.push(atencionGuardia);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setHistorialTurnos(Stack<AtencionGuardia> historialTurnos) {
        this.historialTurnos = historialTurnos;
    }
}
