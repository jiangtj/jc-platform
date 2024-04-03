package com.jiangtj.platform.common;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatetimeFormattersTest {

    @Test
    public void testFormatters() {
        LocalDate date = LocalDate.of(2024, 2, 3);
        LocalTime time = LocalTime.of(8, 30, 40);
        LocalDateTime datetime = LocalDateTime.of(date, time);
        ZonedDateTime zonedDatetime = ZonedDateTime.of(datetime, ZoneId.of("Asia/Shanghai"));

        assertEquals("2024-02-03", date.format(DatetimeFormatters.Date));
        assertEquals("2024-02-03", datetime.format(DatetimeFormatters.Date));
        assertEquals("2024-02-03", zonedDatetime.format(DatetimeFormatters.Date));
        assertEquals("08:30:40", time.format(DatetimeFormatters.Time));
        assertEquals("08:30:40", datetime.format(DatetimeFormatters.Time));
        assertEquals("08:30:40", zonedDatetime.format(DatetimeFormatters.Time));
        assertEquals("2024-02-03 08:30:40", datetime.format(DatetimeFormatters.DateTime));
        assertEquals("2024-02-03 08:30:40", zonedDatetime.format(DatetimeFormatters.DateTime));
        assertEquals("2024-02-03T08:30:40+08:00", zonedDatetime.format(DatetimeFormatters.RFC3339));
    }

}