package br.com.jurix.math.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class JurixMathUtils {

    public static final int CASAS_DECIMAIS_VALOR = 2;

    public static BigDecimal getValorPercentual(BigDecimal valorTotal, BigDecimal percentual){
        BigDecimal valorPercentual = valorTotal.multiply(percentual);
        valorPercentual = valorPercentual.divide(BigDecimal.valueOf(100l));
        return arredondar(valorPercentual);
    }

    public static BigDecimal arredondar(BigDecimal valor){
        return valor.setScale(CASAS_DECIMAIS_VALOR, RoundingMode.HALF_UP);
    }
}
