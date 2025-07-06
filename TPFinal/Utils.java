package TPFinal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddHHmmssSSS");

    public static String generateId() {
        String timestamp = LocalDateTime.now().format(formatter);
        return timestamp + String.format("%04d", counter.getAndIncrement() % 10000); // Add a counter to ensure uniqueness within the same millisecond
    }

    public enum Sintoma {
        FIEBRE("Fiebre alta"),
        TOS("Tos"),
        CONGESTION("Congestión nasal"),
        DOLOR_CABEZA("Dolor de cabeza"),
        DOLOR_PECHO("Dolor pecho"),
        FALTA_RESPIRACION("Falta de respiración"),
        ESTORNUDOS("Estornudos"),
        PICAZON("Picazón"),
        PERDIDA_OLFATO("Pérdida del olfato"),
        PERDIDA_OIDO("Pérdida del oido"),
        MAREO("Mareo"),
        SANGRADO_NARIZ("Sangrado nariz"),
        SANGRADO_TOS("Tos con sangrado"),
        PERDIDA_CONOCIMIENTO("Pérdida del conocimiento"),
        FATIGA("Cansancio o fatiga");


        private final String descripcion;

        Sintoma(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }

    }
}
