package TPFinal;

import java.util.*;

import TPFinal.TDAs.Stack;

public class Menu {

    // ------------------------------
    // ---Funciones útiles comunes---
    // ------------------------------

    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }


    public static Especialidad elegirEspecialidad(Scanner sc, GestorTurnos gestor, String titulo) {
        if (gestor.getEspecialidades().isEmpty()) {
            System.out.println("Aún no hay especialidades registradas.");
            return null;
        }

        List<String> keysEspecialidades = gestor.getEspecialidadesIds();
        Map<String, Especialidad> mapEspecialidades = gestor.getEspecialidades();
        Especialidad especialidadElegida;

        System.out.println(titulo);

        for (int i = 0; i < keysEspecialidades.size(); i++) {
            String key = keysEspecialidades.get(i);
            System.out.println((i + 1) + ") " + mapEspecialidades.get(key).getNombre());
        }

        System.out.println("0) Salir");
        int opcionEspecialidad = sc.nextInt();
        sc.nextLine();

        if (opcionEspecialidad == 0) {
            System.out.println("Operación cancelada.");
            return null;
        } else if (opcionEspecialidad > 0 && opcionEspecialidad <= keysEspecialidades.size() && !keysEspecialidades.get(opcionEspecialidad - 1).isEmpty()) {
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

        if (especialidad.getMedicos().isEmpty()) {
            System.out.println("Esta especialidad aún no tiene médicos.");
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

        System.out.println("0) Salir");
        int opcionMedico = sc.nextInt();
        sc.nextLine();

        if (opcionMedico == 0) {
            System.out.println("Operación cancelada.");
            return null;
        } else if (opcionMedico > 0 && opcionMedico <= keysMedicos.size() && !keysMedicos.get(opcionMedico - 1).isEmpty()) {
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
        if (especialidad.getTurnos().isEmpty()) {
            System.out.println("Esta especialidad aún no tiene turnos.");
            return null;
        }

        Turno turnoElegido = null;
        Turno[] turnos = especialidad.getTurnos().toArray(new Turno[0]);
        System.out.println(titulo);

        for (int i = 0; i < turnos.length; i++) {
            System.out.println((i + 1) + ") Turno para " + turnos[i].getPaciente().getNombre() + " a las " + turnos[i].getFechaHora() + " con " + turnos[i].getMedico().getNombre() + " ");
        }

        System.out.println("0) Salir");

        int opcionTurno = sc.nextInt();
        sc.nextLine();

        if (opcionTurno <= 0 || opcionTurno > turnos.length) {
            System.out.println("Operación cancelada.");
        } else if (turnos[opcionTurno - 1] != null) {
            turnoElegido = turnos[opcionTurno - 1];
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

        if (especialidadAEliminar != null && gestor.eliminarEspecialidad(especialidadAEliminar.getId())) {
            System.out.println("¡La especialidad ha sido eliminada con éxito!");
        } else {
            System.out.println("La especialidad no ha sido eliminada.");
        }
    }

    public static void mostrarEspecialidades(GestorTurnos gestor) {
        List<Especialidad> especialidades = gestor.getEspecialidades().values().stream().toList();

        if (especialidades.isEmpty()) {
            System.out.println("Aún no hay especialidades creadas.");
            return;
        }

        especialidades.forEach(e -> {
            List<Medico> medicos = e.getMedicos().values().stream().toList();
            System.out.println("-------------------------------------------------");
            System.out.println("|              " + e.getNombre());
            System.out.println("-------------------------------------------------");
            if (medicos.isEmpty()) {
                System.out.println("|        Esta especialidad aún no tiene médicos.");
            } else {
                medicos.forEach(m -> System.out.println("| " + m.getId() + " | " + m.getNombre()));
            }
            System.out.println("-------------------------------------------------\n");
        });
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

        Paciente nuevoPaciente = new Paciente(nombrePaciente, dniPaciente);
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

    public static void mostrarPaciente(GestorTurnos gestor, Scanner sc) {
        System.out.println("Ingrese el dni del paciente que desea ver sin puntos (Por ejemplo 44222333):");
        String dniPaciente = sc.nextLine();
        Paciente paciente = gestor.getPaciente(dniPaciente);
        if (paciente == null) {
            System.out.println("El paciente no fue encontrado. Revise el dni ingresado e intente nuevamente.");
            return;
        }

        System.out.println("--------------------------------");
        System.out.println("|  DNI  : " + paciente.getDni());
        System.out.println("|  Nombre: " + paciente.getNombre());
        System.out.println("|-------------------------------");
        System.out.println("|       Turnos históricos");
        System.out.println("|-------------------------------");
        paciente.mostrarTurnosHistoricos();
        System.out.println("|-------------------------------");
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

    public static void mostrarMedico(GestorTurnos gestor, Scanner sc) {
        Medico medico = elegirMedico(sc, gestor, "¿Qué médico quiere ver?");
        if (medico == null) {
            System.out.println("Médico inválido, volviendo al menú principal.");
            return;
        }

        System.out.println("--------------------------------");
        System.out.println("|  ID  : " + medico.getId());
        System.out.println("|  Nombre: " + medico.getNombre());
        System.out.println("|-------------------------------");
        System.out.println("|       Turnos Pendientes");
        System.out.println("|-------------------------------");
        medico.mostrarTurnosPendientes();
        System.out.println("|-------------------------------");
        System.out.println("|       Turnos históricos");
        System.out.println("|-------------------------------");
        medico.mostrarTurnosHistoricos();
        System.out.println("|-------------------------------");
    }

    public static void eliminarMedico(GestorTurnos gestor, Scanner sc) {
        Medico medicoElegido = elegirMedico(sc, gestor, "Elija el médico que desea eliminar");

        if (medicoElegido != null && gestor.eliminarMedico(medicoElegido.getIdEspecialidad(), medicoElegido.getId())) {
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

        if (especialidadElegida.getMedicos().isEmpty()) {
            System.out.println("Esta especialidad aún no tiene médicos, por favor agreguele médicos y vuelva a intentar.");
            return;
        }

        System.out.println("Ingrese el dni del paciente que desea sacar turno sin puntos (Por ejemplo 44222333):");
        String dniPaciente = sc.nextLine();
        Paciente paciente = gestor.getPaciente(dniPaciente);
        if (paciente == null) {
            System.out.println("El paciente no fue encontrado. Revise el dni ingresado e intente nuevamente.");
            return;
        }
        System.out.println("Sacando un turno para el paciente " + paciente.getNombre());
        System.out.println("¿Cuál es la prioridad de la emergencia del paciente?\n1) Urgente\n2) Normal\n3) Control");
        int prioridad = sc.nextInt();
        sc.nextLine();

        if (prioridad < 1 || prioridad > 3) {
            System.out.println("La prioridad ingresada no es valida. Intente nuevamente.");
            return;
        }

        Turno turnoGenerado = gestor.solicitarTurnoEspecialidad(dniPaciente, especialidadElegida.getId(), prioridad); //especialidadElegida.agregarTurno(paciente, prioridad);
        if (turnoGenerado != null) {
            System.out.println("Se generó un turno con el doctor " + turnoGenerado.getMedico().getNombre() + " con éxito con id " + turnoGenerado.getId() + "!");
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

        if (especialidadElegida.getTurnos().isEmpty()) {
            System.out.println("No hay turnos pendientes en la especialidad.");
            return;
        }

        Turno turnoAtendido = gestor.atenderTurnoEspecialidad(especialidadElegida.getId());
        if (turnoAtendido != null) {
            System.out.println("El turno del paciente " + turnoAtendido.getPaciente().getNombre() + " con el doctor " + turnoAtendido.getMedico().getNombre() + " de prioridad " + turnoAtendido.getPrioridad() + " ha sido atendido con éxito y eliminado de la cola.");
        } else {
            System.out.println("Ocurrió un error. Por favor intente nuevamente.");
        }
    }

    public static void atenderTurnoMedico(GestorTurnos gestor, Scanner sc) {
        Medico medicoElegido = elegirMedico(sc, gestor, "¿Cuál médico atenderá un turno?");
        if (medicoElegido == null) {
            System.out.println("Médico inválido, la operación ha sido cancelada.");
            return;
        }


        if (gestor.atenderTurnoMedico(medicoElegido.getIdEspecialidad(), medicoElegido.getId()) != null) {
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
        sc.nextLine();

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

        PriorityQueue<Turno> auxiliarTurnos = new PriorityQueue<>(especialidad.getTurnos());

        if (auxiliarTurnos.isEmpty()) {
            System.out.println("Aún no hay turnos en la especialidad.");
        } else {
            do {
                Turno turnoSacado = auxiliarTurnos.poll();
                System.out.println(turnoSacado.toString());
            } while (!auxiliarTurnos.isEmpty());
        }
    }

    public static void mostrarTurnosPendientesMedico(GestorTurnos gestor, Scanner sc) {
        Medico medico = elegirMedico(sc, gestor, "¿Los turnos pendientes de qué médico quiere ver?");
        if (medico == null) {
            System.out.println("Médico elegido inválido, la operación será cancelada.");
            return;
        }
        medico.mostrarTurnosPendientes();
    }

    // ------------------------------


    // ------------------------------
    // ------Funciones de Menus------
    // ------------------------------

    public static void mostrarMenuGestionTurnos(GestorTurnos gestor, Scanner sc) {
        int opcion;

        clearScreen();

        System.out.println("---------MENU GESTIÓN TURNOS---------\n");
        System.out.println("1. Sacar un turno");
        System.out.println("2. Atender turno");
        System.out.println("3. Mostrar turnos pendientes");
        System.out.println("4. Cancelar turno");
        System.out.println("5. Recuperar turno cancelado");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                sacarTurnoEspecialidad(gestor, sc);
                break;
            case 2:
                System.out.println("¿Desea atender el primer turno de una especialidad o de un doctor puntual?");
                System.out.println("1. Atender turno de una especialidad");
                System.out.println("2. Atender turno de un médico");
                opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        atenderTurnoEspecialidad(gestor, sc);
                        break;
                    case 2:
                        atenderTurnoMedico(gestor, sc);
                        break;
                    default:
                        System.out.println("Opción inválida. Regresando al menú principal.");
                        break;
                }
                break;
            case 3:
                System.out.println("¿Desea mostrar los turnos pendientes de una especialidad o de un doctor puntual?");
                System.out.println("1. Mostrar turnos de una especialidad");
                System.out.println("2. Mostrar turnos de un médico");
                opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        mostrarTurnosPendientesEspecialidad(gestor, sc);
                        break;
                    case 2:
                        mostrarTurnosPendientesMedico(gestor, sc);
                        break;
                    default:
                        System.out.println("Opción inválida. Regresando al menú principal.");
                        break;
                }
                break;
            case 4:
                cancelarTurno(gestor, sc);
                break;
            case 5:
                deshacerCancelarTurno(gestor, sc);
                break;
            case 0:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    public static void mostrarMenuGestionEspecialidades(GestorTurnos gestor, Scanner sc) {
        int opcion;

        clearScreen();

        System.out.println("---------MENU GESTIÓN ESPECIALIDADES---------\n");
        System.out.println("1. Agregar una nueva especialidad");
        System.out.println("2. Eliminar una especialidad");
        System.out.println("3. Mostrar especialidades");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                agregarEspecialidad(gestor, sc);
                break;
            case 2:
                eliminarEspecialidad(gestor, sc);
                break;
            case 3:
                mostrarEspecialidades(gestor);
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

        clearScreen();

        System.out.println("---------MENU GESTIÓN MÉDICOS---------\n");
        System.out.println("1. Agregar un nuevo médico");
        System.out.println("2. Eliminar un médico");
        System.out.println("3. Mostrar datos de un médico");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                agregarMedico(gestor, sc);
                break;
            case 2:
                eliminarMedico(gestor, sc);
                break;
            case 3:
                mostrarMedico(gestor, sc);
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

        clearScreen();

        System.out.println("---------MENU GESTIÓN PACIENTES---------\n");
        System.out.println("1. Agregar un nuevo paciente");
        System.out.println("2. Eliminar un paciente");
        System.out.println("3. Mostrar datos de un paciente");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                agregarPaciente(gestor, sc);
                break;
            case 2:
                eliminarPaciente(gestor, sc);
                break;
            case 3:
                mostrarPaciente(gestor, sc);
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
            clearScreen();

            System.out.println("---------MENU PRINCIPAL---------\n");
            System.out.println("1. Gestión de Turnos");
            System.out.println("2. Gestión de Especialidades");
            System.out.println("3. Gestión de Médicos");
            System.out.println("4. Gestión de Pacientes");
            System.out.println("0. Salir");

            opcion = sc.nextInt();
            sc.nextLine();

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
            System.out.println("<Presione enter para continuar>");
            sc.nextLine();
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
        Paciente paciente5 = new Paciente("Franco Mercado", "1");

        gestor.registrarEspecialidad(traumatologia);
        gestor.registrarEspecialidad(pediatria);
        gestor.registrarEspecialidad(otorrinolaringologia);

        gestor.registrarPaciente(paciente1);
        gestor.registrarPaciente(paciente2);
        gestor.registrarPaciente(paciente3);
        gestor.registrarPaciente(paciente4);
        gestor.registrarPaciente(paciente5);
    }


    public static void main(String[] args) {
        GestorTurnos gestor = new GestorTurnos();
        Scanner scanner = new Scanner(System.in);

        generarDatosPrueba(gestor);
        mostrarMenuPrincipal(gestor, scanner);
    }
}
