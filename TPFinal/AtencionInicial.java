package TPFinal;

import java.time.LocalDateTime;

import static TPFinal.Utils.generateId;

public class AtencionInicial {
    private String id;
    private String idPaciente;
    private LocalDateTime fechaHoraAsignacion;
    private LocalDateTime fechaHoraAtendido;
    private String numero;

    public AtencionInicial(String idPaciente) {
        this.id = generateId();
        this.idPaciente = idPaciente;
        this.fechaHoraAsignacion = LocalDateTime.now();
    }

    public void atender() {
        this.fechaHoraAtendido = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public LocalDateTime getFechaHoraAsignacion() {
        return fechaHoraAsignacion;
    }

    public LocalDateTime getFechaHoraAtendido() {
        return fechaHoraAtendido;
    }
}
