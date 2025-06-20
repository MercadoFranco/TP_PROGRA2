package TPFinal;

import java.time.LocalDateTime;
import java.util.UUID;

public class Turno implements Comparable<Turno> {
    private Paciente paciente;
    private Medico medico;
    private Especialidad especialidad;
    private String id;
    private LocalDateTime fechaHora;
    private int prioridad; // 1 = urgente, 2 = normal, 3 = control
    private boolean cumplido;

    public Turno(Paciente paciente, Medico medico, Especialidad especialidad, LocalDateTime fechaHora, int prioridad) {
        this.paciente = paciente;
        this.medico = medico;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.prioridad = prioridad;
        this.id = UUID.randomUUID().toString();
        this.cumplido = false;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getId() { return id; }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public Especialidad getEspecialidad() { return especialidad; }

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
