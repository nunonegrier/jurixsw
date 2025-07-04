package br.com.jurix.datetime.business;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class JurixDateTime {

    public Date getCurrentDateTime() {
        return new Date();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static Long getQuantidadeDiasEntreData(Date dateInit, Date dateEnd) {
        return DAYS.between(dateToLocalDateTime(getEndOfDay(dateInit)), dateToLocalDateTime(getEndOfDay(dateEnd)));
    }

    public static Integer getDayOfMonth(Date date){
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.getDayOfMonth();
    }


    public static Date setDayOfMonth(Date date, Integer dayOfMonth){
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTimeToDate(localDateTime.withDayOfMonth(dayOfMonth));
    }

    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static Date localDateTimeToDate(LocalDateTime startOfDay) {
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date subtrairDias(Date date, Integer dias) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTimeToDate(localDateTime.minusDays(dias));
    }

    public static Date adicionarDias(Date date, Integer dias) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTimeToDate(localDateTime.plusDays(dias));
    }

    public Date subtrairMeses(Date data, int numeroMeses) {
        LocalDateTime localDateTime = dateToLocalDateTime(data);
        return localDateTimeToDate(localDateTime.minusMonths(numeroMeses));
    }

    public Date subtrairAnos(Date data, int numeroMeses) {
        LocalDateTime localDateTime = dateToLocalDateTime(data);
        return localDateTimeToDate(localDateTime.minusYears(numeroMeses));
    }


    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date getLasDayOfMonth(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime lastDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
        return localDateTimeToDate(lastDay);
    }

    public boolean isDataAtrasa(Date data){
        return getEndOfDay(getCurrentDateTime()).after(getEndOfDay(data));
    }

    public static boolean isMesmoMes(Date date1, Date date2){
        LocalDateTime localDateTime1 = dateToLocalDateTime(date1);
        LocalDateTime localDateTime2 = dateToLocalDateTime(date2);

        return localDateTime1.getYear() == localDateTime2.getYear() && localDateTime1.getMonth() == localDateTime2.getMonth();
    }
}
