package br.com.jurix.filter;

import br.com.jurix.filter.enumeration.TipoIntervaloDataFiltroEnum;
import com.google.common.base.Strings;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static br.com.jurix.datetime.business.JurixDateTime.getLasDayOfMonth;
import static br.com.jurix.datetime.business.JurixDateTime.subtrairDias;

@Setter
@Getter
public class SortPaginatorFilter {

    private Integer page = 1;

    private Integer size = 100;

    private String direction;

    private String sort;

    private Boolean ignoreCase;

    private BooleanExpression mainBooleanExpression;

    private String dataInicio;
    private String dataFim;

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public PageRequest getPageRequest() {

        if (Strings.isNullOrEmpty(sort)) {
            return new PageRequest(page - 1, size);
        }

        return new PageRequest(page -1, size, createSort(sort, direction));
    }

    public void addToMainBooleanExpression(BooleanExpression booleanExpression) {

        if (Objects.isNull(mainBooleanExpression)) {
            mainBooleanExpression = booleanExpression;
        } else {
            mainBooleanExpression = mainBooleanExpression.and(booleanExpression);
        }
    }

    private Sort createSort(String sortField, String direction){
        return new Sort(createSortOrder(sortField, direction));
    }

    private Sort.Order createSortOrder(String sortField, String direction) {
        Sort.Order order = new Sort.Order(getSortDirection(direction), sortField);//.ignoreCase();

        if(Objects.nonNull(ignoreCase) && ignoreCase) {
            order = order.ignoreCase();
        }

        return order;
    }

    private static Sort.Direction getSortDirection(String direction) {
        if (Objects.isNull(direction))
            return Sort.Direction.ASC;

        return Sort.Direction.fromString(direction);
    }

    public void criarFiltroParaIntervaloData(DateTimePath<Date> propriedadeDate, Date dataAtual, TipoIntervaloDataFiltroEnum tipoIntervaloDataFiltroEnum) {

        if(tipoIntervaloDataFiltroEnum.equals(TipoIntervaloDataFiltroEnum.HOJE)){
            criarFiltroParaIntervaloData(propriedadeDate, getDateAsString(dataAtual), getDateAsString(dataAtual));
        }else if(tipoIntervaloDataFiltroEnum.equals(TipoIntervaloDataFiltroEnum.ULTIMA_SEMANA)){
            criarFiltroParaIntervaloData(propriedadeDate, getDateAsString(subtrairDias(dataAtual, 7)), getDateAsString(dataAtual));
        }else if(tipoIntervaloDataFiltroEnum.equals(TipoIntervaloDataFiltroEnum.ESTE_MES)){
            criarFiltroParaIntervaloData(propriedadeDate, getDateAsFirstDayOfMonth(getDateAsString(dataAtual)), getDateAsString(getLasDayOfMonth(dataAtual)));
        }
    }

    public void criarFiltroParaIntervaloData(DateTimePath<Date> propriedadeDate) {

        criarFiltroParaIntervaloData(propriedadeDate, this.dataInicio, this.dataFim);
    }

    public void criarFiltroParaIntervaloData(DateTimePath<Date> propriedadeDate, String inicioStr, String fimStr) {

        if(Objects.isNull(inicioStr) &&  Objects.isNull(fimStr)){
            return;
        }

        Date[] intervalo = converterFiltroIntervaloData(inicioStr, fimStr);
        Date dataInicio = intervalo[0];
        Date dataFim = intervalo[1];

        if (Objects.nonNull(dataInicio) && Objects.nonNull(dataFim)) {
            addToMainBooleanExpression(propriedadeDate.between(dataInicio, dataFim));
        } else if (Objects.nonNull(dataInicio)) {
            addToMainBooleanExpression(propriedadeDate.eq(dataInicio).or(propriedadeDate.gt(dataInicio)));
        } else if (Objects.nonNull(dataFim)) {
            addToMainBooleanExpression(propriedadeDate.eq(dataFim).or(propriedadeDate.lt(dataFim)));
        }
    }

    public static Date[] converterFiltroIntervaloData(String inicioStr, String fimStr) {

        Date[] intervalo = new Date[2];

        if (!Objects.isNull(inicioStr) && !inicioStr.isEmpty()) {
            Date inicio = converterFiltroData(inicioStr);
            intervalo[0] = atStartOfDay(inicio);
        }

        if (!Objects.isNull(fimStr) && !fimStr.isEmpty()) {
            Date fim = converterFiltroData(fimStr);
            intervalo[1] = atEndOfDay(fim);
        }

        return intervalo;
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    public static String getDateAsFirstDayOfMonth(String dateString){
        String[] dates = dateString.split("-");
        return dates[0] + "-" + dates[1] + "-01";
    }

    public static String getDateAsString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date converterFiltroData(String dataStr) {

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(dataStr);
        } catch (ParseException e) {
            throw new RuntimeException("Erro ao converter filtro de data.");
        }

    }

}
