package TPFinal;

import java.time.LocalDateTime;

import java.util.HashMap;

import java.util.Iterator;

import java.util.Map;

import java.util.PriorityQueue;

import java.util.Stack;

public class GestorTurnos {

    private Map<String, Paciente> pacientes;

    private Map<String, Medico> medicos;

    private PriorityQueue<Turno> colaTurnos;

    private Stack<Turno> cancelacionesRecientes;

    public GestorTurnos() {

        pacientes = new HashMap<>();

        medicos = new HashMap<>();

        colaTurnos = new PriorityQueue<>();

        cancelacionesRecientes = new Stack<>();

    }

    

    public void registrarPaciente(String nombre, String dni) {

        if (!pacientes.containsKey(dni)) {

            pacientes.put(dni, new Paciente(nombre, dni));

        }

    }

    

    public void registrarMedico(String nombre, String especialidad, String id) {

        if (!medicos.containsKey(id)) {

            medicos.put(id, new Medico(nombre, especialidad, id));

        }

    }

    

    public void solicitarTurno(String dniPaciente, String idMedico, LocalDateTime fechaHora, int prioridad) {

        Paciente paciente = pacientes.get(dniPaciente);

        Medico medico = medicos.get(idMedico);

        if (paciente != null && medico != null) {

            Turno turno = new Turno(paciente, medico, fechaHora, prioridad);

            colaTurnos.add(turno);

            medico.agregarTurno(turno);

            paciente.agregarTurnoAlHistorial(turno);

        } else {

            System.out.println("Médico o paciente no encontrado.");

        }

    }

    

    public Turno atenderSiguiente() {

        if (!colaTurnos.isEmpty()) {

            return colaTurnos.poll();

        }

        return null;

    }

    

    public boolean cancelarTurno(String dniPaciente) {

        Iterator<Turno> it = colaTurnos.iterator();

        while (it.hasNext()) {

            Turno t = it.next();

            if (t.getPaciente().getDni().equals(dniPaciente)) {

                cancelacionesRecientes.push(t);

                it.remove();

                return true;

            }

        }

        return false;

    }

    

    public boolean deshacerCancelacion() {

        if (!cancelacionesRecientes.isEmpty()) {

            Turno turno = cancelacionesRecientes.pop();

            colaTurnos.add(turno);

            return true;

        }

        return false;

    }

    

    public void mostrarTurnosEnEspera() {

        System.out.println("📋 Turnos en espera:");

        for (Turno t : colaTurnos) {

            System.out.println(t);

        }

    }

}
 
 