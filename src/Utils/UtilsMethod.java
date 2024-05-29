package Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UtilsMethod {

    public String formatTime(long startTime, long endTime){
        long durationSeconds = (endTime - startTime) / 1_000_000_000;
        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        long seconds = durationSeconds % 60;
        return String.format("%02d horas: %02d minutos: %02d segundos", hours, minutes, seconds);
    }

    public String getFomattedDate(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy 'a las' hh:mm a", new Locale("es", "ES"));
        return now.format(formatter);
    }
}
