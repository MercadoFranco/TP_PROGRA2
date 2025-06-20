package TPFinal;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.function.Function;

public class Menu<T> {

    // ------------------------------
    // ---Funciones útiles comunes---
    // ------------------------------

    public Especialidad elegirEspecialidad(Scanner sc, GestorTurnos gestor, String titulo) {
        List<String> keysEspecialidades = gestor.getEspecialidadesIds();
        Map<String, Especialidad> mapEspecialidades = gestor.getEspecialidades();
        Especialidad especialidadElegida;

        System.out.println(titulo);

        for (int i = 0; i < keysEspecialidades.size(); i++) {
            String key = keysEspecialidades.get(i);
            System.out.println((i + 1) + ") " + mapEspecialidades.get(key).getNombre());
        }

        System.out.println("-1) Salir");
        int opcionEspecialidad = sc.nextInt();

        if (opcionEspecialidad == -1) {
            System.out.println("Operación cancelada.");
            return null;
        } else if (!keysEspecialidades.get(opcionEspecialidad).isEmpty()) {
            String keyElegida = keysEspecialidades.get(opcionEspecialidad - 1);
            especialidadElegida = mapEspecialidades.get(keyElegida);

            return especialidadElegida;
        } else {
            System.out.println("El valor ingresado es inválido.");
        }

        return null;
    }

    public Medico elegirMedico(Scanner sc, GestorTurnos gestor, String titulo) {
        Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿A qué especialidad pertenece el médico?");

        if (especialidad == null) {
            return null;
        }

        List<String> keysMedicos = especialidad.getMedicosIds();
        Map<String, Medico> mapMedicos = especialidad.getMedicos();
        Medico medicoElegido;

        System.out.println(titulo);

        for (int i = 0; i < keysMedicos.size(); i++) {
            String key = keysMedicos.get(i);
            System.out.println((i + 1) + ") " + mapMedicos.get(key).getNombre());
        }

        System.out.println("-1) Salir");
        int opcionMedico = sc.nextInt();

        if (opcionMedico == -1) {
            System.out.println("Operación cancelada.");
            return null;
        } else if (!keysMedicos.get(opcionMedico).isEmpty()) {
            String keyElegida = keysMedicos.get(opcionMedico - 1);
            medicoElegido = mapMedicos.get(keyElegida);

            return medicoElegido;
        } else {
            System.out.println("El valor ingresado es inválido.");
        }

        return null;
    }

    public Turno elegirTurnoEspecialidad(Scanner sc, GestorTurnos gestor, String titulo) {
        Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿De qué especialidad es el turno?");
        if (especialidad == null) {
            return null;
        }

        Turno turnoElegido = null;
        Turno[] turnos = especialidad.getTurnos().toArray(new Turno[0]);
        System.out.println(titulo);

        for (int i = 0; i < turnos.length; i++) {
            System.out.println((i + 1) + ") Turno para " + turnos[i].getPaciente().getNombre() + " a las " + turnos[i].getFechaHora() + " con " + turnos[i].getMedico().getNombre() + " ");
        }

        System.out.println("-1) Salir");

        int opcionTurno = sc.nextInt();

        if (opcionTurno == -1) {
            System.out.println("Operación cancelada.");
        } else if (turnos[opcionTurno] != null) {
            turnoElegido = turnos[opcionTurno];
        }

        return turnoElegido;
    }

    // ------------------------------


    // ------------------------------
    // ---Gestión de especialidades--
    // ------------------------------

    public void agregarEspecialidad(GestorTurnos gestor, Scanner sc) {
        System.out.println("¿Cuál es el nombre de la nueva especialidad que desea crear?");
        String nombreEspecialidad = sc.nextLine();
        Especialidad nuevaEspecialidad = new Especialidad(nombreEspecialidad);
        if (gestor.registrarEspecialidad(nuevaEspecialidad)) {
            System.out.println("¡La especialidad ha sido agregada con éxito!");
        } else {
            System.out.println("Ocurrió un error al agregar el especialidad.");
        }
    }

    public void eliminarEspecialidad(GestorTurnos gestor, Scanner sc) {
        Especialidad especialidadAEliminar = elegirEspecialidad(sc, gestor, "Elija la especialidad que desea eliminar");

        if (especialidadAEliminar != null) {
            System.out.println("¡La especialidad ha sido eliminada con éxito!");
        } else {
            System.out.println("La especialidad no ha sido eliminada.");
        }
    }

    // ------------------------------


    // ------------------------------
    // -----Gestión de pacientes-----
    // ------------------------------

    public void agregarPaciente(GestorTurnos gestor, Scanner sc) {
        System.out.println("¿Cuál es el DNI del paciente que desea registrar? Por favor ingrese el dni sin puntos. Por ejemplo, 44222333");
        String dniPaciente = sc.nextLine();
        System.out.println("¿Cuál es el nombre de la paciente que desea registrar?");
        String nombrePaciente = sc.nextLine();

        Paciente nuevoPaciente = new Paciente(dniPaciente, nombrePaciente);
        if (gestor.registrarPaciente(nuevoPaciente)) {
            System.out.println("¡El paciente " + nombrePaciente + " ha sido agregado con éxito!");
        } else {
            System.out.println("Ocurrió un error al agregar el paciente.");
        }
    }

    public void eliminarPaciente(GestorTurnos gestor, Scanner sc) {
        System.out.println("Ingrese el dni del paciente que desea eliminar sin puntos (Por ejemplo 44222333):");
        String dniPaciente = sc.nextLine();
        if (gestor.eliminarPaciente(dniPaciente)) {
            System.out.println("¡El paciente ha sido eliminado con éxito!");
        } else {
            System.out.println("Ocurrió un error al eliminar el paciente.");
        }
    }

    // ------------------------------


    // ------------------------------
    // ------Gestión de médicos------
    // ------------------------------


    public void agregarMedico(GestorTurnos gestor, Scanner sc) {
        System.out.println("¿Cuál es el nombre del medico que desea agregar?");
        String nombreMedico = sc.nextLine();

        Especialidad especialidadMedico = elegirEspecialidad(sc, gestor, "¿A qué especialidad pertenecerá el doctor que desea agregar?");
        if (especialidadMedico != null) {
            Medico nuevoMedico = new Medico(nombreMedico, especialidadMedico.getId());
            if (gestor.registrarMedico(especialidadMedico.getId(), nuevoMedico)) {
                System.out.println("¡El medico ha sido agregado con éxito!");
            } else {
                System.out.println("El médico no ha sido agregado.");
            }
        } else {
            System.out.println("El medico no ha sido agregado.");
        }
    }

    public void eliminarMedico(GestorTurnos gestor, Scanner sc) {
        Medico medicoElegido = elegirMedico(sc, gestor, "Elija el médico que desea eliminar");

        if (medicoElegido != null) {
            System.out.println("¡El médico ha sido eliminado con éxito!");
        } else {
            System.out.println("El médico no ha sido eliminado.");
        }
    }

    // ------------------------------


    // ------------------------------
    // ------Gestión de turnos-------
    // ------------------------------

    public void sacarTurnoEspecialidad(GestorTurnos gestor, Scanner sc) {
        Especialidad especialidadElegida = elegirEspecialidad(sc, gestor, "¿Para la guardia de qué especialidad desea sacar turno?");
        if (especialidadElegida == null) {
            System.out.println("Especialidad inválida, el turno no ha sido generado.");
            return;
        }

        System.out.println("Ingrese el dni del paciente que desea sacar turno sin puntos (Por ejemplo 44222333):");
        String dniPaciente = sc.nextLine();
        Paciente paciente = gestor.getPaciente(dniPaciente);
        if (paciente == null) {
            System.out.println("El paciente no fue encontrado. Revise el dni ingresado e intente nuevamente.");
            return;
        }

        System.out.println("¿Cuál es la prioridad de la emergencia del paciente?\n1) Urgente\2) Normal\n3) Control");
        int prioridad = sc.nextInt();
        if (prioridad < 1 || prioridad > 3) {
            System.out.println("La prioridad ingresada no es valida. Intente nuevamente.");
            return;
        }

        Turno turnoGenerado = gestor.solicitarTurnoEspecialidad(dniPaciente, especialidadElegida.getId(), prioridad); //especialidadElegida.agregarTurno(paciente, prioridad);
        if (turnoGenerado != null) {
            System.out.println("El turno ha sido agregado con éxito con id " + turnoGenerado.getId() + "!");
        } else {
            System.out.println("Ocurrió un error y el turno no ha sido generado. Por favor intente nuevamente más tarde.");
        }
    }

    public void atenderTurnoEspecialidad(GestorTurnos gestor, Scanner sc) {
        Especialidad especialidadElegida = elegirEspecialidad(sc, gestor, "¿Para la guardia de qué especialidad desea atender el próximo turno?");
        if (especialidadElegida == null) {
            System.out.println("Especialidad inválida, la operación ha sido cancelada.");
            return;
        }

        if (gestor.atenderTurnoEspecialidad(especialidadElegida.getId())) {
            System.out.println("El turno ha sido atendido con éxito y eliminado de la cola.");
        } else {
            System.out.println("Ocurrió un error. Por favor intente nuevamente.");
        }
    }

    public void atenderTurnoDoctor(GestorTurnos gestor, Scanner sc) {
        Especialidad especialidadElegida = elegirEspecialidad(sc, gestor, "¿De qué especialidad es el doctor que desea atender un turno?");
        if (especialidadElegida == null) {
            System.out.println("Especialidad inválida, la operación ha sido cancelada.");
            return;
        }

        Medico medicoElegido = elegirMedico(sc, gestor, "¿Cuál médico atenderá un turno?");
        if (medicoElegido == null) {
            System.out.println("Médico inválido, la operación ha sido cancelada.");
            return;
        }

        Turno turnoAtendido = medicoElegido.atenderPrimerTurno();
        if (turnoAtendido == null) {
            System.out.println("Ha ocurrido un error al atender el turno. Por favor intente nuevamente.");
            return;
        }

        if (especialidadElegida.eliminarTurno(turnoAtendido)) {
            System.out.println("El turno ha sido atendido con éxito.");
        } else {
            System.out.println("Ocurrió un error al atender el turno, por favor intente nuevamente.");
        }
    }

    public void cancelarTurno(GestorTurnos gestor, Scanner sc) {
        Turno turnoElegido = elegirTurnoEspecialidad(sc, gestor, "¿Qué turno desea cancelar?");
        if (turnoElegido == null) {
            System.out.println("El turno no ha sido cancelado.");
            return;
        }

        if (gestor.cancelarTurno(turnoElegido)) {
            System.out.println("El turno ha sido cancelado con éxito.");
        } else {
            System.out.println("Ocurrió un error, por favor vuelva a intentar más tarde.");
        }
    }

    public void deshacerCancelarTurno(GestorTurnos gestor, Scanner sc) {
        Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿De qué especialidad desea recuperar el último turno deshecho?");

        if (especialidad == null) {
            System.out.println("Especialidad inválida, la operación ha sido cancelada.");
            return;
        }

        Turno turnoRecuperado = gestor.deshacerCancelacion();
        if (turnoRecuperado == null) {
            System.out.println("No se ha recuperado ningún turno.");
            return;
        }

        System.out.println("El turno de " + turnoRecuperado.getPaciente().getNombre() + " con el doctor " + turnoRecuperado.getMedico().getNombre() + " de " + turnoRecuperado.getEspecialidad().getNombre() + " ha sido recuperado con éxito y fue agregado nuevamente a la cola.");
    }


    public static void main(String[] args) {
        GestorTurnos gestor = new GestorTurnos();
        Scanner scanner = new Scanner(System.in);

        //Medico nuevoMedico = new Medico("Juan Pérez", "Clínica Médica", "M001")

        // gestor.registrarMedico(nuevoMedico);
        //gestor.registrarPaciente("Ana López", "12345678");

        int opcion;

        do {

            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Gestión de Turnos");
            System.out.println("2. Atender siguiente turno");
            System.out.println("3. Cancelar turno de un paciente");
            System.out.println("4. Deshacer última cancelación");
            System.out.println("0. Salir");

            System.out.print("Opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("DNI del paciente: ");
                    String dni = scanner.nextLine();
                    System.out.print("ID del médico: ");
                    String idMedico = scanner.nextLine();
                    System.out.print("Prioridad (1=Urgente, 2=Normal, 3=Control): ");
                    int prioridad = scanner.nextInt();
                    //gestor.solicitarTurno(dni, idMedico, LocalDateTime.now(), prioridad);
                    System.out.println("Turno solicitado.");
                    break;

                case 2:
                    /*Turno atendido = gestor.atenderSiguiente();

                    if (atendido != null) {
                        System.out.println("Atendiendo: " + atendido);
                    } else {
                        System.out.println("No hay turnos pendientes.");
                    }*/
                    break;

                case 3:
                    /*
                    System.out.print("DNI del paciente para cancelar turno: ");
                    String dniCancelar = scanner.nextLine();
                    boolean cancelado = gestor.cancelarTurno(dniCancelar);

                    if (cancelado) {
                        System.out.println("Turno cancelado.");
                    } else {
                        System.out.println("No se encontró un turno para ese paciente.");
                    }

                     */
                    break;

                case 4:
                    /*
                    boolean deshecho = gestor.deshacerCancelacion();
                    if (deshecho) {
                        System.out.println("Cancelación deshecha. Turno restaurado.");
                    } else {
                        System.out.println("No hay cancelaciones recientes.");
                    }

                     */
                    break;

                case 0:

                    System.out.println("Saliendo del sistema.");
                    break;

                default:

                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
}
