package TPFinal;

import java.util.HashSet;
import java.util.Set;

import TPFinal.Utils.Sintoma;

public class Enfermedad {
    private String nombre;
    private String idEspecialidadAsociada;
    private Set<Sintoma> sintomas;
    private int prioridad;

    public Enfermedad(String nombre, Set<Sintoma> sintomas, String idEspecialidad, int prioridad) {
        this.nombre = nombre;
        this.sintomas = sintomas;
        this.idEspecialidadAsociada = idEspecialidad;
        this.prioridad = prioridad;
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


    public String getNombre() {
        return nombre;
    }

    public Set<Sintoma> getSintomas() {
        return sintomas;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getIdEspecialidadAsociada() {
        return idEspecialidadAsociada;
    }

}
