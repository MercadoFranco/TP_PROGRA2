package TPFinal;

import java.time.LocalDateTime;

import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {
        GestorTurnos gestor = new GestorTurnos();
        Scanner scanner = new Scanner(System.in);

        gestor.registrarMedico("Juan Pérez", "Clínica Médica", "M001");
        gestor.registrarPaciente("Ana López", "12345678");

        int opcion;

        do {

            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Solicitar turno");
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
                    gestor.solicitarTurno(dni, idMedico, LocalDateTime.now(), prioridad);
                    System.out.println("Turno solicitado.");
                    break;

                case 2:
                    Turno atendido = gestor.atenderSiguiente();

                    if (atendido != null) {
                        System.out.println("Atendiendo: " + atendido);
                    } else {
                        System.out.println("No hay turnos pendientes.");
                    }
                    break;

                case 3:

                    System.out.print("DNI del paciente para cancelar turno: ");
                    String dniCancelar = scanner.nextLine();
                    boolean cancelado = gestor.cancelarTurno(dniCancelar);

                    if (cancelado) {
                        System.out.println("Turno cancelado.");
                    } else {
                        System.out.println("No se encontró un turno para ese paciente.");
                    }
                    break;

                case 4:

                    boolean deshecho = gestor.deshacerCancelacion();
                    if (deshecho) {
                        System.out.println("Cancelación deshecha. Turno restaurado.");
                    } else {
                        System.out.println("No hay cancelaciones recientes.");
                    }
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
