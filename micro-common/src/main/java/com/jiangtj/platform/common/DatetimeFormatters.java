package com.jiangtj.platform.common;

import java.time.format.DateTimeFormatter;

public interface DatetimeFormatters {
    DateTimeFormatter DateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter Date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter Time = DateTimeFormatter.ofPattern("HH:mm:ss");
}
