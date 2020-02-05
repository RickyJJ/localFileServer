package org.jiong.filetree.common.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * @author Mr.Jiong
 */
public class DateKit {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String timestamp(Instant instant) {
        return TIMESTAMP_FORMATTER.format(instant);
    }
}
