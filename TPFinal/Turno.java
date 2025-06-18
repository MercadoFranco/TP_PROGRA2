package TPFinal;

import java.time.LocalDateTime;

public class Turno implements Comparable<Turno> {
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;
    private int prioridad; // 1 = urgente, 2 = normal, 3 = control
    private boolean cumplido = false;

    public Turno(Paciente paciente, Medico medico, LocalDateTime fechaHora, int prioridad) {
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.prioridad = prioridad;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void atender() {
        cumplido = true;
    }

    @Override
    public int compareTo(Turno otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }

    @Override
    public String toString() {
        return "[" + fechaHora + "] " + paciente.getNombre() + " con Dr. " + medico.getNombre() + " (prioridad: " + prioridad + ")";
    }
}
