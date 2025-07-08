package TPFinal;

import java.time.format.DateTimeFormatter;
import java.util.*;

import TPFinal.Utils.Sintoma;

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

    public static Set<Sintoma> obtenerSintomas(Scanner sc, String titulo) {
        Set<Sintoma> seleccionados = EnumSet.noneOf(Sintoma.class);
        Sintoma[] sintomas = Sintoma.values();

        int opcion = -1;
        do {
            clearScreen();
            System.out.println(titulo);

            for (int i = 0; i < sintomas.length; i++) {
                boolean marcado = seleccionados.contains(sintomas[i]);
                System.out.printf("%d. (%s) %s\n", i + 1, marcado ? "X" : " ", sintomas[i].getDescripcion());
            }

            System.out.println("0. Salir");
            System.out.print("Opción: ");

            try {
                opcion = sc.nextInt();
                sc.nextLine();

                if (opcion >= 1 && opcion <= sintomas.length) {
                    Sintoma s = sintomas[opcion - 1];
                    if (seleccionados.contains(s)) {
                        seleccionados.remove(s);
                    } else {
                        seleccionados.add(s);
                    }
                } else if (opcion != 0) {
                    System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        } while (opcion != 0);

        return seleccionados;
    }


    public static Especialidad elegirEspecialidad(Scanner sc, GestorGuardia gestor, String titulo) {
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

    public static Medico elegirMedico(Scanner sc, GestorGuardia gestor, String titulo) {
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

    public static AtencionGuardia elegirTurnoEspecialidad(Scanner sc, GestorGuardia gestor, String titulo) {
        Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿De qué especialidad es el turno?");
        if (especialidad == null) {
            return null;
        }
        if (especialidad.getTurnos().isEmpty()) {
            System.out.println("Esta especialidad aún no tiene turnos.");
            return null;
        }

        AtencionGuardia atencionGuardiaElegido = null;
        AtencionGuardia[] atencionesGuardia = especialidad.getTurnos().toArray(new AtencionGuardia[0]);
        System.out.println(titulo);

        for (int i = 0; i < atencionesGuardia.length; i++) {
            Paciente paciente = gestor.getPaciente(atencionesGuardia[i].getIdPaciente());
            Medico medico = especialidad.getMedico(atencionesGuardia[i].getIdMedico());
            System.out.println((i + 1) + ") Turno para " + paciente.getNombre() + " con " + medico.getNombre() + " ");
        }

        System.out.println("0) Salir");

        int opcionTurno = sc.nextInt();
        sc.nextLine();

        if (opcionTurno <= 0 || opcionTurno > atencionesGuardia.length) {
            System.out.println("Operación cancelada.");
        } else if (atencionesGuardia[opcionTurno - 1] != null) {
            atencionGuardiaElegido = atencionesGuardia[opcionTurno - 1];
        }

        return atencionGuardiaElegido;
    }

    // ------------------------------


    // ------------------------------
    // ---Gestión de especialidades--
    // ------------------------------

    public static void agregarEspecialidad(GestorGuardia gestor, Scanner sc) {
        System.out.println("¿Cuál es el nombre de la nueva especialidad que desea crear?");
        String nombreEspecialidad = sc.nextLine();
        Especialidad nuevaEspecialidad = new Especialidad(nombreEspecialidad);
        if (gestor.registrarEspecialidad(nuevaEspecialidad)) {
            System.out.println("¡La especialidad ha sido agregada con éxito!");
        } else {
            System.out.println("Ocurrió un error al agregar el especialidad.");
        }
    }

    public static void eliminarEspecialidad(GestorGuardia gestor, Scanner sc) {
        Especialidad especialidadAEliminar = elegirEspecialidad(sc, gestor, "Elija la especialidad que desea eliminar");

        if (especialidadAEliminar != null && gestor.eliminarEspecialidad(especialidadAEliminar.getId())) {
            System.out.println("¡La especialidad ha sido eliminada con éxito!");
        } else {
            System.out.println("La especialidad no ha sido eliminada.");
        }
    }

    public static void mostrarEspecialidades(GestorGuardia gestor) {
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

    public static void agregarPaciente(GestorGuardia gestor, Scanner sc) {
        System.out.println("¿Cuál es el DNI del paciente que desea registrar? Por favor ingrese el dni sin puntos. Por ejemplo, 44222333");
        String dniPaciente = sc.nextLine();
        System.out.println("¿Cuál es el nombre de la paciente que desea registrar?");
        String nombrePaciente = sc.nextLine();

        Paciente nuevoPaciente = new Paciente(nombrePaciente, dniPaciente);
        System.out.println("PACIENTE NEUVO:  " + nuevoPaciente);
        if (gestor.registrarPaciente(nuevoPaciente)) {
            System.out.println("¡El paciente " + nombrePaciente + " ha sido agregado con éxito!");
        } else {
            System.out.println("Ocurrió un error al agregar el paciente.");
        }
    }

    public static void eliminarPaciente(GestorGuardia gestor, Scanner sc) {
        System.out.println("Ingrese el dni del paciente que desea eliminar sin puntos (Por ejemplo 44222333):");
        String dniPaciente = sc.nextLine();
        if (gestor.eliminarPaciente(dniPaciente)) {
            System.out.println("¡El paciente ha sido eliminado con éxito!");
        } else {
            System.out.println("Ocurrió un error al eliminar el paciente.");
        }
    }

    public static void mostrarPaciente(GestorGuardia gestor, Scanner sc) {
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


    public static void agregarMedico(GestorGuardia gestor, Scanner sc) {
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

    public static void mostrarMedico(GestorGuardia gestor, Scanner sc) {
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

    public static void eliminarMedico(GestorGuardia gestor, Scanner sc) {
        Medico medicoElegido = elegirMedico(sc, gestor, "Elija el médico que desea eliminar");
        if (medicoElegido == null) {
            System.out.println("Error al elegir médico.");
            return;
        }

        gestor.eliminarMedicoYReasignar(medicoElegido.getIdEspecialidad(), medicoElegido.getId());

        if (!gestor.eliminarMedico(medicoElegido.getIdEspecialidad(), medicoElegido.getId())) {
            System.out.println("El médico no ha sido eliminado.");
        }
    }

    // ------------------------------


    // --------------------------------
    // ------Gestión de Guardia--------
    // --------------------------------

    public static void agregarColaPreDiagnostico(GestorGuardia gestor, Scanner sc) {
        System.out.println("Ingrese el dni del paciente que desea agregar a la cola para el pre diagnóstico sin puntos (Por ejemplo 44222333):");
        String dniPaciente = sc.nextLine();
        Paciente paciente = gestor.getPaciente(dniPaciente);
        if (paciente == null) {
            System.out.println("El paciente no fue encontrado. Revise el dni ingresado e intente nuevamente.");
            return;
        }

        AtencionInicial atencionInicial = gestor.solicitarAtencionInicial(dniPaciente);
        if (atencionInicial != null) {
            int pendientes = gestor.getFilaAtencionInicial().size() - 1;
            System.out.println("El paciente ha sido agregado a la cola para el pre diagnóstico. Actualmente hay " + pendientes + " personas adelante en la fila");
        } else {
            System.out.println("Ocurrió un error al agregar el paciente a la fila.");
        }
    }

    public static void atenderPreDiagnostico(GestorGuardia gestor, Scanner sc) {
        AtencionInicial atendido = gestor.atenderAtencionInicial();
        if (atendido == null || gestor.getPaciente(atendido.getIdPaciente()) == null) {
            System.out.println("No hay ninguna persona en la fila.");
            return;
        }

        Paciente paciente = gestor.getPaciente(atendido.getIdPaciente());
        Set<Sintoma> sintomas = obtenerSintomas(sc, ("¿Qué síntomas presenta el paciente " + paciente.getNombre() + "?"));

        if (sintomas.isEmpty()) {
            System.out.println("El paciente no cuenta con síntomas, cancelando operación.");
            return;
        }

        List<Enfermedad> enfermedadesPosibles = gestor.obtenerCoincidenciaEnfermedades(sintomas);
        Enfermedad enfermedadProbable = gestor.obtenerEnfermedadMasProbable(sintomas);
        if (!enfermedadesPosibles.isEmpty() && enfermedadProbable != null) {
            System.out.println("El sistema dice que en base a esos síntomas, la enfermedad más probables es:");
            for (Enfermedad enfermedad : enfermedadesPosibles) {
                System.out.println("->" + enfermedad.getNombre() + " (Urgencia " + enfermedad.getPrioridad() + ") - " + enfermedad.calcularProbabilidad(sintomas) + "% de probabilidad");
            }

            System.out.println("¿Asignar la indicada por el sistema? (S/N) ");
            String opcion = sc.nextLine();
            String idEspecialidad = enfermedadProbable.getIdEspecialidad();
            if (opcion.equalsIgnoreCase("S")) {
                AtencionGuardia generada = gestor.solicitarAtencionEspecialidad(paciente.getDni(), idEspecialidad, enfermedadProbable.getPrioridad(), sintomas);
                Especialidad especialidad = gestor.getEspecialidades().get(idEspecialidad);
                System.out.print("Se ha añadido al paciente a la cola de la especialidad " + especialidad.getNombre());
                System.out.print(" con prioridad " + generada.getPrioridad());
                System.out.print(" y los siguientes síntomas: ");
                generada.getSintomasPaciente().forEach(sintoma -> System.out.print(sintoma.getDescripcion() + ", "));
                System.out.println("\n");
                return;
            }

            System.out.println("¿Desea ingresar manualmente la especialidad y prioridad? (S/N)");
            opcion = sc.nextLine();
            if (opcion.equalsIgnoreCase("S")) {
                agregarAtencionEspecialidad(gestor, sc, paciente.getDni());
                return;
            }

            System.out.println("Opción inválida.");
        } else {
            agregarAtencionEspecialidad(gestor, sc, paciente.getDni());
        }
    }

    public static void agregarAtencionEspecialidad(GestorGuardia gestor, Scanner sc, String idPaciente) {
        Especialidad especialidadElegida = elegirEspecialidad(sc, gestor, "¿A qué especialidad desea asignarlo?");
        if (especialidadElegida == null) {
            System.out.println("Especialidad inválida, el paciente no ha sido agregado a la cola.");
            return;
        }

        if (especialidadElegida.getMedicos().isEmpty()) {
            System.out.println("Esta especialidad aún no tiene médicos, por favor agreguele médicos y vuelva a intentar.");
            return;
        }

        System.out.println("¿Cuál es la prioridad de la emergencia del paciente?\n1) Sin urgencia\n2) Urgencia Menor\n3) Urgencia\n4) Emergencia\n5) Resucitación");
        int prioridad = sc.nextInt();
        sc.nextLine();

        if (prioridad < 1 || prioridad > 5) {
            System.out.println("La prioridad ingresada no es valida. Intente nuevamente.");
            return;
        }

        AtencionGuardia atencionGuardiaGenerado = gestor.solicitarAtencionEspecialidad(idPaciente, especialidadElegida.getId(), prioridad, null);
        if (atencionGuardiaGenerado != null) {
            Medico medico = especialidadElegida.getMedico(atencionGuardiaGenerado.getIdMedico());
            System.out.println("Se agregó a la cola una consulta con el doctor " + medico.getNombre() + " con éxito con id " + atencionGuardiaGenerado.getId() + "!");
        } else {
            System.out.println("Ocurrió un error y el paciente no ha sido agregado. Por favor intente nuevamente más tarde.");
        }
    }

    public static void procesarAtencionEspecialidad(GestorGuardia gestor, Scanner sc) {
        Especialidad especialidadElegida = elegirEspecialidad(sc, gestor, "¿Para la guardia de qué especialidad desea atender el próximo turno?");
        if (especialidadElegida == null) {
            System.out.println("Especialidad inválida, la operación ha sido cancelada.");
            return;
        }

        if (especialidadElegida.getTurnos().isEmpty()) {
            System.out.println("No hay turnos pendientes en la especialidad.");
            return;
        }

        AtencionGuardia atencionGuardiaAtendido = gestor.atenderAtencionEspecialidad(especialidadElegida.getId());

        if (atencionGuardiaAtendido != null) {
            Paciente paciente = gestor.getPaciente(atencionGuardiaAtendido.getIdPaciente());
            Medico medico = especialidadElegida.getMedico(atencionGuardiaAtendido.getIdMedico());
            System.out.println("El turno del paciente " + paciente.getNombre() + " con el doctor " + medico.getNombre() + " de prioridad " + atencionGuardiaAtendido.getPrioridad() + " ha sido atendido con éxito y eliminado de la cola.");
        } else {
            System.out.println("Ocurrió un error. Por favor intente nuevamente.");
        }
    }

    public static void atenderTurnoMedico(GestorGuardia gestor, Scanner sc) {
        Medico medicoElegido = elegirMedico(sc, gestor, "¿Cuál médico atenderá un turno?");
        if (medicoElegido == null) {
            System.out.println("Médico inválido, la operación ha sido cancelada.");
            return;
        }


        if (gestor.atenderAtencionMedico(medicoElegido.getIdEspecialidad(), medicoElegido.getId()) != null) {
            System.out.println("El turno ha sido atendido con éxito.");
        } else {
            System.out.println("Ocurrió un error al atender el turno, por favor intente nuevamente.");
        }
    }

    public static void cancelarAtencion(GestorGuardia gestor, Scanner sc) {
        AtencionGuardia atencionGuardiaElegido = elegirTurnoEspecialidad(sc, gestor, "¿Qué turno desea cancelar?");
        if (atencionGuardiaElegido == null) {
            System.out.println("El turno no ha sido cancelado.");
            return;
        }

        if (gestor.cancelarTurno(atencionGuardiaElegido)) {
            System.out.println("El turno ha sido cancelado con éxito.");
        } else {
            System.out.println("Ocurrió un error, por favor vuelva a intentar más tarde.");
        }
    }

    public static void deshacerCancelarAtencion(GestorGuardia gestor, Scanner sc) {
        System.out.println("¿Desea recuperar el último turno cancelado?\n1)Sí\n2)No");
        int opcion = sc.nextInt();
        sc.nextLine();

        if (opcion != 1) {
            System.out.println("El turno cancelado no ha sido recuperado.");
            return;
        }

        AtencionGuardia atencionGuardiaRecuperado = gestor.deshacerCancelacion();
        if (atencionGuardiaRecuperado == null) {
            System.out.println("No se ha recuperado ningún turno.");
            return;
        }

        Especialidad especialidad = gestor.getEspecialidades().get(atencionGuardiaRecuperado.getIdEspecialidad());
        Medico medico = especialidad.getMedico(atencionGuardiaRecuperado.getIdMedico());

        System.out.println("El turno del paciente con DNI " + atencionGuardiaRecuperado.getIdPaciente() + " con el doctor " + medico.getNombre() + " de " + especialidad.getNombre() + " ha sido recuperado con éxito y fue agregado nuevamente a la cola.");
    }

    // ------------------------------


    // ------------------------------
    // --------Mostrar turnos--------
    // ------------------------------

    public static void mostrarFilaPreDiagnostico(GestorGuardia gestor) {
        Queue<AtencionInicial> filaActual = gestor.getFilaAtencionInicial();
        if (filaActual.isEmpty()) {
            System.out.println("La fila de pre diagnóstico está vacía.");
            return;
        }

        filaActual.forEach(atencionInicial -> {
            Paciente paciente = gestor.getPaciente(atencionInicial.getIdPaciente());
            System.out.println("| " + atencionInicial.getId() + " - Turno de " + paciente.getNombre() + " sacado a las " + atencionInicial.getFechaHoraAsignacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        });
    }

    public static void mostrarFilaEspecialidad(GestorGuardia gestor, Scanner sc) {
        Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿De la guardia de qué especialidad quiere ver los turnos?");
        if (especialidad == null) {
            System.out.println("La especialidad es inválida, la operación fue cancelada.");
            return;
        }

        PriorityQueue<AtencionGuardia> auxiliarAtencionesGuardia = new PriorityQueue<>(especialidad.getTurnos());

        if (auxiliarAtencionesGuardia.isEmpty()) {
            System.out.println("Aún no hay turnos en la especialidad.");
            return;
        }

        do {
            AtencionGuardia atencionGuardiaSacado = auxiliarAtencionesGuardia.poll();
            System.out.println(atencionGuardiaSacado.toString());
        } while (!auxiliarAtencionesGuardia.isEmpty());
    }

    public static void mostrarFilaMedico(GestorGuardia gestor, Scanner sc) {
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

    public static void mostrarMenuGestionTurnos(GestorGuardia gestor, Scanner sc) {
        int opcion;

        clearScreen();

        System.out.println("---------MENU GESTIÓN ATENCIÓN---------\n");
        System.out.println("1. Agregar a la fila de pre diagnóstico");
        System.out.println("2. Atender fila");
        System.out.println("3. Mostrar fila");
        System.out.println("4. Cancelar atención");
        System.out.println("5. Recuperar atención cancelada");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                agregarColaPreDiagnostico(gestor, sc);
                break;
            case 2:
                System.out.println("¿Desea atender al primer lugar de la fila de pre diagnóstico, de una especialidad o de un doctor puntual?");
                System.out.println("1. Atender primer lugar de pre diagnóstico");
                System.out.println("2. Atender primer lugar de una especialidad");
                System.out.println("2. Atender primer lugar de un médico");
                opcion = sc.nextInt();
                sc.nextLine();
                switch (opcion) {
                    case 1:
                        atenderPreDiagnostico(gestor, sc);
                        break;
                    case 2:
                        procesarAtencionEspecialidad(gestor, sc);
                        break;
                    case 3:
                        atenderTurnoMedico(gestor, sc);
                        break;
                    default:
                        System.out.println("Opción inválida. Regresando al menú principal.");
                        break;
                }
                break;
            case 3:
                System.out.println("¿Desea mostrar la fila de pre diagnóstico, de una especialidad o de un médico puntual?");
                System.out.println("1. Mostrar fila de pre diagnóstico");
                System.out.println("2. Mostrar fila de una especialidad");
                System.out.println("3. Mostrar fila de un médico");
                opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        mostrarFilaPreDiagnostico(gestor);
                        break;
                    case 2:
                        mostrarFilaEspecialidad(gestor, sc);
                        break;
                    case 3:
                        mostrarFilaMedico(gestor, sc);
                        break;
                    default:
                        System.out.println("Opción inválida. Regresando al menú principal.");
                        break;
                }

                break;
            case 4:
                cancelarAtencion(gestor, sc);
                break;
            case 5:
                deshacerCancelarAtencion(gestor, sc);
                break;
            case 0:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    public static void mostrarMenuGestionEnfermedades(GestorGuardia gestor, Scanner sc) {
        int opcion;

        clearScreen();

        System.out.println("---------MENU GESTIÓN ENFERMEDADES---------\n");
        System.out.println("1. Agregar una nueva enfermedad");
        System.out.println("2. Eliminar una enfermedad");
        System.out.println("3. Mostrar enfermedades");
        System.out.println("0. Volver al inicio");

        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                System.out.println("¿Cuál es el nombre de la nueva enfermedad?");
                String nombre = sc.nextLine();
                Set<Sintoma> sintomas = obtenerSintomas(sc, ("¿Qué síntomas presenta la enfermedad?"));
                Especialidad especialidad = elegirEspecialidad(sc, gestor, "¿A qué especialidad pertenece?");
                System.out.println("¿Cuál es la prioridad de la emergencia del paciente?\n1) Sin urgencia\n2) Urgencia Menor\n3) Urgencia\n4) Emergencia\n5) Resucitación");
                int prioridad = sc.nextInt();
                sc.nextLine();

                if (especialidad == null) {
                    System.out.println("Especialidad inválida, intente nuevamente.");
                    return;
                }

                if (prioridad < 1 || prioridad > 5) {
                    System.out.println("La prioridad ingresada no es valida. Intente nuevamente.");
                    return;
                }

                if (gestor.agregarEnfermedad(nombre, sintomas, especialidad.getId(), prioridad)) {
                    System.out.println("La enfermedad ha sido agregada con éxito.");
                } else {
                    System.out.println("La enfermedad no ha sido agregada, intente nuevamente.");
                }

                break;
            case 2:
                System.out.println("¿Qué enfermedad desea eliminar?");
                List<Enfermedad> enfermedades = gestor.getEnfermedades();
                for (int i = 0; i < enfermedades.size(); i++) {
                    System.out.println((i + 1) + ". " + enfermedades.get(i).getNombre());
                }
                System.out.println("0. Volver al inicio");

                int opcionEnfermedad = sc.nextInt();

                if (opcionEnfermedad == 0) {
                    return;
                }

                if (gestor.getEnfermedades().get(opcionEnfermedad - 1) == null) {
                    System.out.println("Ocurrió un error al eliminar la enfermedad, intente nuevamente.");
                    return;
                }

                gestor.eliminarEnfermedad(gestor.getEnfermedades().get(opcionEnfermedad - 1).getNombre());

                break;
            case 3:
                List<Enfermedad> enfermedadesAMostrar = gestor.getEnfermedades();
                if (enfermedadesAMostrar.isEmpty()) {
                    System.out.println("Aún no hay enfermedades registradas.");
                    return;
                }

                enfermedadesAMostrar.forEach(enfermedad -> {
                    System.out.println(" _______________________");
                    System.out.println("|  " + enfermedad.getNombre());
                    System.out.println("|______________________");
                    System.out.println("| Prioridad: " + enfermedad.getPrioridad());
                    System.out.println("| Sintomas: " + enfermedad.getSintomas());
                    System.out.println(" ______________________");
                    System.out.println("\n");
                });
                break;
            case 0:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public static void mostrarMenuGestionEspecialidades(GestorGuardia gestor, Scanner sc) {
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

    public static void mostrarMenuGestionMedicos(GestorGuardia gestor, Scanner sc) {
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

    public static void mostrarMenuGestionPacientes(GestorGuardia gestor, Scanner sc) {
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

    public static void mostrarMenuPrincipal(GestorGuardia gestor, Scanner sc) {
        int opcion;

        do {
            clearScreen();

            System.out.println("---------MENU PRINCIPAL---------\n");
            System.out.println("1. Gestión de Turnos");
            System.out.println("2. Gestión de Especialidades");
            System.out.println("3. Gestión de Médicos");
            System.out.println("4. Gestión de Pacientes");
            System.out.println("5. Gestión de Enfermedades");
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
                case 5:
                    mostrarMenuGestionEnfermedades(gestor, sc);
                    break;
                case 0:
                    Persistencia.guardarTodo(gestor);
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

    public static void generarDatosPrueba(GestorGuardia gestor) {
        Especialidad traumatologia = new Especialidad("Traumatología");
        Medico traumatologo1 = new Medico("Jorge Traumatólogo", traumatologia.getId());
        Medico traumatologo2 = new Medico("Mauro Piernaquebrada", traumatologia.getId());
        traumatologia.registrarMedico(traumatologo1);
        traumatologia.registrarMedico(traumatologo2);

        Especialidad otorrinolaringologia = new Especialidad("Otorrinolaringologia");
        Medico otorrinolaringologo1 = new Medico("Esteban Orejas", otorrinolaringologia.getId());
        otorrinolaringologia.registrarMedico(otorrinolaringologo1);

        Especialidad cardiologia = new Especialidad("Cardiología");
        Medico cardiologo1 = new Medico("Jorge Cardiologia", cardiologia.getId());
        cardiologia.registrarMedico(cardiologo1);

        Paciente paciente1 = new Paciente("Elen Fermo", "44333222");
        Paciente paciente2 = new Paciente("Sinvi Taminas", "44333223");
        Paciente paciente3 = new Paciente("Eldia Bético", "44333224");
        Paciente paciente4 = new Paciente("Laque Brada", "44333225");
        Paciente paciente5 = new Paciente("Franco Mercado", "1");

        gestor.registrarEspecialidad(traumatologia);
        gestor.registrarEspecialidad(otorrinolaringologia);
        gestor.registrarEspecialidad(cardiologia);

        gestor.registrarPaciente(paciente1);
        gestor.registrarPaciente(paciente2);
        gestor.registrarPaciente(paciente3);
        gestor.registrarPaciente(paciente4);
        gestor.registrarPaciente(paciente5);

        gestor.agregarEnfermedad("Accidente Cerebrovascular", Set.of(Sintoma.DOLOR_CABEZA, Sintoma.MAREO, Sintoma.SANGRADO_NARIZ, Sintoma.PERDIDA_CONOCIMIENTO), cardiologia.getId(), 5);
        gestor.agregarEnfermedad("Paro Cardiaco", Set.of(Sintoma.DOLOR_PECHO, Sintoma.MAREO, Sintoma.FATIGA), cardiologia.getId(), 5);
        gestor.agregarEnfermedad("Infección Auricular", Set.of(Sintoma.DOLOR_CABEZA, Sintoma.PERDIDA_OIDO, Sintoma.FIEBRE), otorrinolaringologia.getId(), 1);
        gestor.agregarEnfermedad("Me acabo de dar cuenta que no conozco muchas enfermedades", Set.of(Sintoma.CONGESTION, Sintoma.DOLOR_CABEZA), traumatologia.getId(), 1);

    }


    public static void main(String[] args) {
        GestorGuardia gestor = new GestorGuardia();
        Scanner scanner = new Scanner(System.in);

        Persistencia.cargarTodo(gestor);
        // generarDatosPrueba(gestor);

        mostrarMenuPrincipal(gestor, scanner);
    }
}
