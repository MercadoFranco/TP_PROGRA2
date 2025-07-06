package TPFinal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static TPFinal.Utils.Sintoma;
import static TPFinal.Utils.generateId;

public class AtencionGuardia implements Comparable<AtencionGuardia> {
    private Paciente paciente;
    private Medico medico;
    private Especialidad especialidad;
    private String id;
    private LocalDateTime fechaHora;
    private int prioridad; // 1 = urgente, 2 = normal, 3 = control
    private Set<Sintoma> sintomasPaciente;

    public AtencionGuardia(Paciente paciente, Medico medico, Especialidad especialidad, LocalDateTime fechaHora, int prioridad, Set<Sintoma> sintomasPaciente) {
        this.paciente = paciente;
        this.medico = medico;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.prioridad = prioridad;
        this.id = generateId();
        this.sintomasPaciente = sintomasPaciente;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public String getFechaHora() {
        return fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public Set<Sintoma> getSintomasPaciente() {
        return sintomasPaciente;
    }

    @Override
    public int compareTo(AtencionGuardia otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }

    @Override
    public String toString() {
        return "| " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " - Turno de " + paciente.getNombre() + " con Dr. " + medico.getNombre() + " (prioridad: " + prioridad + ")";
    }
}
