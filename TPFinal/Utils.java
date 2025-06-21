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
}
