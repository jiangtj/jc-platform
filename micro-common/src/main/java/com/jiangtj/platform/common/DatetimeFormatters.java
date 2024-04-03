package com.jiangtj.platform.common;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public interface DatetimeFormatters {
    DateTimeFormatter DateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter Date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter Time = DateTimeFormatter.ofPattern("HH:mm:ss");
    // RFC3339 for wechat pay v3
    DateTimeFormatter RFC3339 = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(Date)
        .appendLiteral("T")
        .append(Time)
        .parseLenient()
        .appendOffsetId()
        .parseStrict()
        .toFormatter();
}
