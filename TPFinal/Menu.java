package TPFinal;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Menu {

    // ------------------------------
    // ---Funciones útiles comunes---
    // ------------------------------

    public static Especialidad elegirEspecialidad(Scanner sc, GestorTurnos gestor, String titulo) {
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

    public static Medico elegirMedico(Scanner sc, GestorTurnos gestor, String titulo) {
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

    public static Turno elegirTurnoEspecialidad(Scanner sc, GestorTurnos gestor, String titulo) {
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

    public static void agregarEspecialidad(GestorTurnos gestor, Scanner sc) {
        System.out.println("¿Cuál es el nombre de la nueva especialidad que desea crear?");
        String nombreEspecialidad = sc.nextLine();
        Especialidad nuevaEspecialidad = new Especialidad(nombreEspecialidad);
        if (gestor.registrarEspecialidad(nuevaEspecialidad)) {
            System.out.println("¡La especialidad ha sido agregada con éxito!");
        } else {
            System.out.println("Ocurrió un error al agregar el especialidad.");
        }
    }

    public static void eliminarEspecialidad(GestorTurnos gestor, Scanner sc) {
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

    public static void agregarPaciente(GestorTurnos gestor, Scanner sc) {
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

    public static void eliminarPaciente(GestorTurnos gestor, Scanner sc) {
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


    public static void agregarMedico(GestorTurnos gestor, Scanner sc) {
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

    public static void eliminarMedico(GestorTurnos gestor, Scanner sc) {
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

    public static void sacarTurnoEspecialidad(GestorTurnos gestor, Scanner sc) {
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

    public static void atenderTurnoEspecialidad(GestorTurnos gestor, Scanner sc) {
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

    public static void atenderTurnoMedico(GestorTurnos gestor, Scanner sc) {
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


        if (gestor.atenderTurnoMedico(especialidadElegida.getId(), medicoElegido.getId()) != null) {
            System.out.println("El turno ha sido atendido con éxito.");
        } else {
            System.out.println("Ocurrió un error al atender el turno, por favor intente nuevamente.");
        }
    }

    public static void cancelarTurno(GestorTurnos gestor, Scanner sc) {
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

    public static void deshacerCancelarTurno(GestorTurnos gestor, Scanner sc) {
        System.out.println("¿Desea recuperar el último turno cancelado?\n1)Sí\n2)No");
        int opcion = sc.nextInt();

        if (opcion != 1) {
            System.out.println("El turno cancelado no ha sido recuperado.");
            return;
        }

        Turno turnoRecuperado = gestor.deshacerCancelacion();
        if (turnoRecuperado == null) {
            System.out.println("No se ha recuperado ningún turno.");
            return;
        }

        System.out.println("El turno de " + turnoRecuperado.getPaciente().getNombre() + " con el doctor " + turnoRecuperado.getMedico().getNombre() + " de " + turnoRecuperado.getEspecialidad().getNombre() + " ha sido recuperado con éxito y fue agregado nuevamente a la cola.");
    }

    // ------------------------------


    // ------------------------------
    // --------Mostrar turnos--------
    // ------------------------------

    public static void mostrarTurnosPendientesEspecialidad(GestorTurnos gestor, Scanner sc) {
        Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿De la guardia de qué especialidad quiere ver los turnos?");
        if (especialidad == null) {
            System.out.println("La especialidad es inválida, la operación fue cancelada.");
            return;
        }

        PriorityQueue<Turno> turnos = especialidad.getTurnos();

        turnos.forEach(turno -> System.out.println(turno.toString()));
    }

    public static void mostrarTurnosPendientesMedico(GestorTurnos gestor, Scanner sc) {
        Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿A qué especialidad pertenece el médico cuyos turnos quiere ver?");
        if (especialidad == null) {
            System.out.println("Especialidad inválida. La operación será cancelada.");
            return;
        }

        Medico medico = elegirMedico(sc, gestor, "¿Los turnos pendientes de qué médico quiere ver?");
        if (medico == null) {
            System.out.println("Médico elegido inválido, la operación será cancelada.");
            return;
        }

        PriorityQueue<Turno> turnos = medico.getTurnos();
        turnos.forEach(turno -> System.out.println(turno.toString()));
    }

    // ------------------------------


    // ------------------------------
    // ------Funciones de Menus------
    // ------------------------------

    public static void mostrarMenuGestionTurnos(GestorTurnos gestor, Scanner sc) {
        int opcion;

        System.out.println("1. Crear turno para una especialidad");
        System.out.println("2. Atender turno de una especialidad");
        System.out.println("3. Atender turno de un médico");
        System.out.println("4. Mostrar turnos pendientes de una especialidad");
        System.out.println("5. Mostrar turnos pendientes de un médico");
        System.out.println("6. Cancelar turno");
        System.out.println("7. Recuperar turno cancelado");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                sacarTurnoEspecialidad(gestor, sc);
                break;
            case 2:
                atenderTurnoEspecialidad(gestor, sc);
                break;
            case 3:
                atenderTurnoMedico(gestor, sc);
                break;
            case 4:
                mostrarTurnosPendientesEspecialidad(gestor, sc);
                break;
            case 5:
                mostrarTurnosPendientesMedico(gestor, sc);
                break;
            case 6:
                cancelarTurno(gestor, sc);
                break;
            case 7:
                deshacerCancelarTurno(gestor, sc);
                break;
            case 0:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public static void mostrarMenuGestionEspecialidades(GestorTurnos gestor, Scanner sc) {
        int opcion;
        System.out.println("1. Agregar una nueva especialidad");
        System.out.println("2. Eliminar una especialidad");
        System.out.println("3. Mostrar especialidades");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                agregarEspecialidad(gestor, sc);
                break;
            case 2:
                eliminarEspecialidad(gestor, sc);
                break;
            case 3:
                // TODO:mostrarEspecialidades, nombres de especialidades con los médicos abajo o algo así
                break;
            case 0:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public static void mostrarMenuGestionMedicos(GestorTurnos gestor, Scanner sc) {
        int opcion;
        System.out.println("1. Agregar un nuevo médico");
        System.out.println("2. Eliminar un médico");
        System.out.println("3. Mostrar datos de un médico");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                agregarMedico(gestor, sc);
                break;
            case 2:
                eliminarMedico(gestor, sc);
                break;
            case 3:
                // TODO:mostrarMédico, datos del médico con el historial de turnos que atendió o algo así
                break;
            case 0:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public static void mostrarMenuGestionPacientes(GestorTurnos gestor, Scanner sc) {
        int opcion;

        System.out.println("1. Agregar un nuevo paciente");
        System.out.println("2. Eliminar un paciente");
        System.out.println("3. Mostrar datos de un paciente");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                agregarPaciente(gestor, sc);
                break;
            case 2:
                eliminarPaciente(gestor, sc);
                break;
            case 3:
                // TODO: mostrarPaciente, datos del paciente y listar los turnos historicos tomados o algo así
                break;
            case 0:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public static void mostrarMenuPrincipal(GestorTurnos gestor, Scanner sc) {
        int opcion;

        do {
            System.out.println("1. Gestión de Turnos");
            System.out.println("2. Gestión de Especialidades");
            System.out.println("3. Gestión de Médicos");
            System.out.println("4. Gestión de Pacientes");
            System.out.println("0. Salir");

            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    mostrarMenuGestionTurnos(gestor, sc);
                    break;
                case 2:
                    mostrarMenuGestionEspecialidades(gestor, sc);
                    break;
                case 3:
                    mostrarMenuGestionMedicos(gestor, sc);
                    break;
                case 4:
                    mostrarMenuGestionPacientes(gestor, sc);
                    break;
                case 0:
                    System.out.println("Gracias por usar la aplicación, son 40 usd.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        } while (opcion != 0);
    }

    public static void generarDatosPrueba(GestorTurnos gestor) {
        Especialidad traumatologia = new Especialidad("Traumatología");
        Medico traumatologo1 = new Medico("Jorge Traumatólogo", traumatologia.getId());
        Medico traumatologo2 = new Medico("Mauro Piernaquebrada", traumatologia.getId());
        traumatologia.registrarMedico(traumatologo1);
        traumatologia.registrarMedico(traumatologo2);

        Especialidad pediatria = new Especialidad("Pediatria");
        Medico pediatra1 = new Medico("Lape Diatra", pediatria.getId());
        pediatria.registrarMedico(pediatra1);

        Especialidad otorrinolaringologia = new Especialidad("Otorrinolaringologia");
        Medico otorrinolaringologo1 = new Medico("Esteban Orejas", otorrinolaringologia.getId());
        otorrinolaringologia.registrarMedico(otorrinolaringologo1);

        Paciente paciente1 = new Paciente("Elen Fermo", "44333222");
        Paciente paciente2 = new Paciente("Sinvi Taminas", "44333223");
        Paciente paciente3 = new Paciente("Eldia Bético", "44333224");
        Paciente paciente4 = new Paciente("Laque Brada", "44333225");

        gestor.registrarEspecialidad(traumatologia);
        gestor.registrarEspecialidad(pediatria);
        gestor.registrarEspecialidad(otorrinolaringologia);

        gestor.registrarPaciente(paciente1);
        gestor.registrarPaciente(paciente2);
        gestor.registrarPaciente(paciente3);
        gestor.registrarPaciente(paciente4);
    }


    public static void main(String[] args) {
        GestorTurnos gestor = new GestorTurnos();
        Scanner scanner = new Scanner(System.in);

        generarDatosPrueba(gestor);
        mostrarMenuPrincipal(gestor, scanner);
    }
}
