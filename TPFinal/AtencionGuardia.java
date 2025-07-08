package TPFinal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static TPFinal.Utils.Sintoma;
import static TPFinal.Utils.generateId;

public class AtencionGuardia implements Comparable<AtencionGuardia> {
    private String idPaciente;
    private String idMedico;
    private String idEspecialidad;
    private String id;
    private LocalDateTime fechaHora;
    private int prioridad; // 1 = urgente, 2 = normal, 3 = control
    private Set<Sintoma> sintomasPaciente;

    public AtencionGuardia(String idPaciente, String idMedico, String idEspecialidad, LocalDateTime fechaHora, int prioridad, Set<Sintoma> sintomasPaciente) {
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.idEspecialidad = idEspecialidad;
        this.fechaHora = fechaHora;
        this.prioridad = prioridad;
        this.id = generateId();
        this.sintomasPaciente = sintomasPaciente;
    }

    public AtencionGuardia(String idPaciente, String idMedico, String idEspecialidad, LocalDateTime fechaHora, int prioridad, Set<Sintoma> sintomasPaciente, String id) {
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.idEspecialidad = idEspecialidad;
        this.fechaHora = fechaHora;
        this.prioridad = prioridad;
        this.id = id;
        this.sintomasPaciente = sintomasPaciente;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getId() {
        return id;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public String getIdMedico() {
        return idMedico;
    }

    public String getIdEspecialidad() {
        return idEspecialidad;
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
        return "| " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " - Turno de paciente con DNI " + idPaciente + " con Dr. " + idMedico + " (prioridad: " + prioridad + ")";
    }

    public void setMedico(Medico menosCargado) {
        // TODO Auto-generated method stub

    }
}

