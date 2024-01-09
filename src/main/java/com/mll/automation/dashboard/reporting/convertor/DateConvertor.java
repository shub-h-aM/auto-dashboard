package com.mll.automation.dashboard.reporting.convertor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import java.text.SimpleDateFormat;
import java.util.Date;
@Slf4j
public class DateConvertor implements Converter<Long, Date> {
    @Override
    public Date convert(Long source) {
        if (source == null) {
            return null;
        }
        Date date = new Date(source);
        log.info("Getting date {} ",date);
        return date;
    }
    public String formatDateToDdMmYy(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yy");
        return dateFormat.format(date);
    }
}
