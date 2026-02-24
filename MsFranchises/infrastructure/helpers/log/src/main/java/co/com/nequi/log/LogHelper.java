package co.com.nequi.log;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogHelper {

    private static final Pattern NEWLINE_PATTERN =
            Pattern.compile("(?<=[:,\\[{])\\s+|\\s+(?=[]}:,])|\\r\\n|\\n|\\r");
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern(LogConstants.TIME_PATTERN.getName());

    public static String getLogTimestamp() {
        return ZonedDateTime.now().format(DATETIME_FORMATTER);
    }

}