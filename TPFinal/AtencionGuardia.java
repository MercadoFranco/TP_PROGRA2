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
    private int prioridad; // 1 = urgente, 2 = normal, 3 = control
    private Set<Sintoma> sintomasPaciente;

    public AtencionGuardia(String idPaciente, String idMedico, String idEspecialidad, int prioridad, Set<Sintoma> sintomasPaciente) {
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.idEspecialidad = idEspecialidad;
        this.prioridad = prioridad;
        this.id = generateId();
        this.sintomasPaciente = sintomasPaciente;
    }

    public AtencionGuardia(String idPaciente, String idMedico, String idEspecialidad, LocalDateTime fechaHora, int prioridad, Set<Sintoma> sintomasPaciente, String id) {
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.idEspecialidad = idEspecialidad;
        this.prioridad = prioridad;
        this.id = id;
        this.sintomasPaciente = sintomasPaciente;
    }

    public AtencionGuardia() {
        this.idPaciente = "";
        this.idMedico = "";
        this.idEspecialidad = "";
        this.prioridad = 0;
        this.id = "";
        this.sintomasPaciente = null;
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


    public Set<Sintoma> getSintomasPaciente() {
        return sintomasPaciente;
    }

    @Override
    public int compareTo(AtencionGuardia otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }

    @Override
    public String toString() {
        return "| - Turno de paciente con DNI " + idPaciente + " con Dr. " + idMedico + " (prioridad: " + prioridad + ")";
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setIdMedico(String idMedico) {
        this.idMedico = idMedico;
    }

    public void setIdEspecialidad(String idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public void setSintomasPaciente(Set<Sintoma> sintomasPaciente) {
        this.sintomasPaciente = sintomasPaciente;
    }
}

