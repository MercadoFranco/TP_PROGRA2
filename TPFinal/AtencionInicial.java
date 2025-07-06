package TPFinal;

import java.time.LocalDateTime;

import static TPFinal.Utils.generateId;

public class AtencionInicial {
    private String id;
    private Paciente paciente;
    private LocalDateTime fechaHoraAsignacion;
    private LocalDateTime fechaHoraAtendido;
    private String numero;

    public AtencionInicial(Paciente paciente) {
        this.id = generateId();
        this.paciente = paciente;
        this.fechaHoraAsignacion = LocalDateTime.now();
    }

    public void atender() {
        this.fechaHoraAtendido = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getFechaHoraAsignacion() {
        return fechaHoraAsignacion;
    }

    public LocalDateTime getFechaHoraAtendido() {
        return fechaHoraAtendido;
    }
}
