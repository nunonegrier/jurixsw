package br.com.jurix.financeiro.contaspagar.repository;

import br.com.jurix.financeiro.contaspagar.entity.ContasPagar;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumFrequenciaContasPagar;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ContasPagarRepository extends JoinCapableRepository<ContasPagar, Long> {

    @Query("SELECT cp " +
            "FROM ContasPagar cp " +
            "JOIN FETCH cp.pessoaConta pc " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cp.removido IS FALSE " +
            "AND month(cp.dataVencimento) = :mes " +
            "AND year(cp.dataVencimento) = :ano " +
            "AND  cp.id = :centroCustoId " +
            "ORDER BY cp.dataVencimento")
    public List<ContasPagar> buscarcontasPagar(@Param("mes") Integer mes, @Param("ano") Integer ano, @Param("centroCustoId") Long centroCustoId);

    @Query("SELECT cp " +
            "FROM ContasPagar cp " +
            "JOIN FETCH cp.pessoaConta pc " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cp.removido IS FALSE " +
            "AND cp.dataVencimento >= :dataInicio " +
            "AND cp.dataVencimento <= :dataFim " +
            "AND (cp.pagamentoFinal IS FALSE OR cp.pagamentoFinal IS NULL) " +
            "AND cp.frequencia = :frequenciaContasPagar ")
    List<ContasPagar> buscarcontasPagarComRecorrenciaMesAnterior(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim, @Param("frequenciaContasPagar") EnumFrequenciaContasPagar frequenciaContasPagar);

    @Query("SELECT cp " +
            "FROM ContasPagar cp " +
            "JOIN FETCH cp.incidencias incidencias " +
            "WHERE cp.removido IS FALSE " +
            "AND cp.dataVencimento >= :dataInicio " +
            "AND cp.dataVencimento <= :dataFim " +
            "AND cp.frequencia = :frequenciaContasPagar " +
            "AND cp.descricao = :descricao " +
            "AND incidencias.centroCusto.id IN (SELECT cc.id FROM ContasPagarIncidencia cpi JOIN cpi.centroCusto cc WHERE cpi.contaPagarId = :contaPagarId)")
    List<ContasPagar> buscarcontasPagarPeriodoPorDescricaoECentroCustoDaConta(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim, @Param("frequenciaContasPagar") EnumFrequenciaContasPagar frequenciaContasPagar, @Param("descricao") String descricao, @Param("contaPagarId") Long contaPagarId);

    @Query("SELECT cp " +
            "FROM ContasPagar cp " +
            "JOIN FETCH cp.incidencias " +
            "JOIN FETCH cp.pessoaConta pc " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cp.id = :id")
    ContasPagar buscarcontasPagar(@Param("id") Long id);
}
