package TPFinal;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Especialidad {
    private Map<String, Medico> medicos;
    private String nombre;
    private String id;

    public Especialidad(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
        this.medicos = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Medico getMedico(String idMedico) { return medicos.get(idMedico); }

    public void registrarMedico(String nombre, String especialidad, String id) {
        if (!medicos.containsKey(id)) {
            medicos.put(id, new Medico(nombre, especialidad, id));
        }
    }

    public void agregarTurno() {
        // TODO: Acá debería revisar qué médico tiene menos turnos y agregarle el turno directo a ese.
    }
}
