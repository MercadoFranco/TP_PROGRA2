package TPFinal;

import java.util.HashSet;
import java.util.Set;

import TPFinal.Utils.Sintoma;

public class Enfermedad {
    private String id;
    private String nombre;
    private String idEspecialidad;
    private Set<Sintoma> sintomas;
    private int prioridad;

    public Enfermedad(String nombre, Set<Sintoma> sintomas, String idEspecialidad, int prioridad) {
        this.nombre = nombre;
        this.sintomas = sintomas;
        this.idEspecialidad = idEspecialidad;
        this.prioridad = prioridad;
    }

    public Enfermedad() {
        this.id = "";
        this.nombre = "";
        this.sintomas = new HashSet<>();
        this.idEspecialidad = "";
        this.prioridad = 0;
    }

    public double calcularProbabilidad(Set<Sintoma> sintomasIngresados) {
        if (sintomas.isEmpty() && sintomasIngresados.isEmpty()) return 100.0;
        if (sintomas.isEmpty() || sintomasIngresados.isEmpty()) return 0.0;

        Set<Sintoma> interseccion = new HashSet<>(sintomas);
        interseccion.retainAll(sintomasIngresados);

        Set<Sintoma> union = new HashSet<>(sintomas);
        union.addAll(sintomasIngresados);

        return Math.round(((double) interseccion.size() / union.size()) * 100);
    }


    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Set<Sintoma> getSintomas() {
        return sintomas;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSintomas(Set<Sintoma> sintomas) {
        this.sintomas = sintomas;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setIdEspecialidad(String idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

}
