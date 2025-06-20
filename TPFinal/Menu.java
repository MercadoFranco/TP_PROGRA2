package TPFinal;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class Menu<T> {

    public void MenuTurnos(Scanner sc) {
        int opcion = sc.nextInt();

        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Sacar turno");
        System.out.println("2. Atender turno");
        System.out.println("3. Cancelar turno");
        System.out.println("4. Deshacer última cancelación");
        System.out.println("0. Salir");

    };

    public void agregarEspecialidad(GestorTurnos gestor, Scanner sc) {
        System.out.println("¿Cuál es el nombre de la nueva especialidad que desea crear?");
        String nombreEspecialidad = sc.nextLine();
        Especialidad nuevaEspecialidad = new Especialidad(nombreEspecialidad);
        if(gestor.registrarEspecialidad(nuevaEspecialidad)) {
            System.out.println("¡La especialidad ha sido agregada con éxito!");
        } else {
            System.out.println("Ocurrió un error al agregar el especialidad.");
        }
    }

    public void eliminarEspecialidad(GestorTurnos gestor, Scanner sc) {
        List<String> keysEspecialidades = gestor.getEspecialidadesIds();
        Map<String, Especialidad> mapEspecialidades = gestor.getEspecialidades();
        int opcion;

        do {
            System.out.println("Elija la especialidad que desea eliminar:");
            for (int i = 0; i < keysEspecialidades.size(); i++) {
                String key = keysEspecialidades.get(i);
                System.out.println((i+1) + ") " + mapEspecialidades.get(key).getNombre());
            }
            System.out.println("-1) Salir");
            opcion = sc.nextInt();

            if(opcion == -1) {
                System.out.println("Adios");
            } else {
                if (gestor.eliminarEspecialidad(keysEspecialidades.get(opcion-1))) {
                    System.out.println("¡La especialidad ha sido eliminada con éxito!");
                } else {
                    System.out.println("Ocurrió un error al eliminar el especialidad.");
                }
            }
        } while (opcion != -1);
    }

    public static void main(String[] args) {
        GestorTurnos gestor = new GestorTurnos();
        Scanner scanner = new Scanner(System.in);

        Medico nuevoMedico = new Medico("Juan Pérez", "Clínica Médica", "M001")

        // gestor.registrarMedico(nuevoMedico);
        gestor.registrarPaciente("Ana López", "12345678");

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
