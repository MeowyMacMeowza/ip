package korvus.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeParser {
    private DateTimeFormatter formatter;

    public DateTimeParser (DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public boolean isValid(String sDatetime) {
        try{
            formatter.parse(sDatetime);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public LocalDateTime parseDateString(String sDatetime) {
        try{
            return LocalDateTime.parse(sDatetime, formatter);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    public String convertDateToString(LocalDateTime dateTime) {
        return dateTime == null ? "unknown" : dateTime.format(formatter);
    }
}
