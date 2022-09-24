package larx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Misc {
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("hh:ss");

    public static String timestamp() {
        return DTF.format(LocalDateTime.now());
    }
}
