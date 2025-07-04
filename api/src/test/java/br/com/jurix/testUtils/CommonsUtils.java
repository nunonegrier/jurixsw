package br.com.jurix.testUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonsUtils {

    public static BigDecimal bigDecimalValue(String value){
        return new BigDecimal(value);
    }

    public static Date dateFromString(String date) throws ParseException {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.parse(date);
    }

    public static Date dateTimeFromString(String date) throws ParseException {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfDate.parse(date);
    }
}
