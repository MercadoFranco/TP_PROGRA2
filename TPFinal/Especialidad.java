package TPFinal;

import java.time.LocalDateTime;
import java.util.*;

import static TPFinal.Utils.Sintoma;
import static TPFinal.Utils.generateId;

public class Especialidad {
    private Map<String, Medico> medicos;
    private String nombre;
    private String id;
    private PriorityQueue<AtencionGuardia> atencionesGuardia;

    public Especialidad(String nombre) {
        this.nombre = nombre;
        this.id = generateId();
        this.medicos = new HashMap<>();
        this.atencionesGuardia = new PriorityQueue<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Medico getMedico(String idMedico) {
        return medicos.get(idMedico);
    }

    public List<String> getMedicosIds() {
        return new ArrayList<String>(this.medicos.keySet());
    }

    public Map<String, Medico> getMedicos() {
        return this.medicos;
    }

    public boolean registrarMedico(Medico medico) {
        String idMedico = medico.getId();
        if (!medicos.containsKey(idMedico)) {
            medicos.put(idMedico, medico);
            return true;
        }
        return false;
    }

    public boolean eliminarMedico(String idMedico) {
        if (medicos.containsKey(idMedico)) {
            Collection<AtencionGuardia> turnosDelMedico = medicos.get(idMedico).getTurnos();
            medicos.remove(idMedico);
            atencionesGuardia.removeAll(turnosDelMedico);
            return true;
        }
        return false;
    }

    public AtencionGuardia agregarTurno(String idPaciente, int prioridad, Set<Sintoma> sintomas) {
        if (medicos.isEmpty()) {
            return null;
        }

        Medico medicoConMenosTurnos = null;
        int minTurnos = Integer.MAX_VALUE;

        for (Medico medico : medicos.values()) {
            int cantidadTurnos = medico.getTurnos().size();
            if (cantidadTurnos < minTurnos) {
                minTurnos = cantidadTurnos;
                medicoConMenosTurnos = medico;
            }
        }

        if (medicoConMenosTurnos == null) {
            return null;
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        AtencionGuardia nuevoAtencionesGuardia = new AtencionGuardia(idPaciente, medicoConMenosTurnos.getId(), id, fechaActual, prioridad, sintomas);
        medicoConMenosTurnos.agregarTurno(nuevoAtencionesGuardia);
        atencionesGuardia.offer(nuevoAtencionesGuardia);
        return nuevoAtencionesGuardia;
    }

    public AtencionGuardia atenderPrimerTurno() {
        if (atencionesGuardia.isEmpty()) {
            return null;
        }

        AtencionGuardia atencionGuardiaAtendido = atencionesGuardia.poll();
        if (atencionGuardiaAtendido != null) {
            Medico medicoQueAtendio = medicos.get(atencionGuardiaAtendido.getIdMedico());
            medicoQueAtendio.atenderPrimerTurno();
            return atencionGuardiaAtendido;
        }

        return null;
    }

    public boolean eliminarTurno(AtencionGuardia atencionGuardia) {
        if (!atencionesGuardia.contains(atencionGuardia)) {
            return false;
        }

        atencionesGuardia.remove(atencionGuardia);
        return true;
    }

    public boolean cancelarTurno(AtencionGuardia atencionGuardia) {
        if (!atencionesGuardia.contains(atencionGuardia)) {
            System.out.println("no hay turno");
            return false;
        }

        Medico medico = medicos.get(atencionGuardia.getIdMedico());
        if (medico.eliminarTurno(atencionGuardia)) {
            atencionesGuardia.remove(atencionGuardia);
            return true;
        }

        return false;
    }

    public PriorityQueue<AtencionGuardia> getTurnos() {
        return atencionesGuardia;
    }
}
