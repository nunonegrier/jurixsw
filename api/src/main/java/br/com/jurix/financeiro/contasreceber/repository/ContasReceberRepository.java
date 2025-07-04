package br.com.jurix.financeiro.contasreceber.repository;

import br.com.jurix.cliente.enumeration.EnumTipoContrato;
import br.com.jurix.financeiro.contasreceber.entity.ContasReceber;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ContasReceberRepository extends JoinCapableRepository<ContasReceber, Long> {

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "JOIN FETCH cr.pessoaConta pc " +
            "JOIN FETCH cr.cliente cl " +
            "LEFT JOIN FETCH cr.contrato con " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cr.removido IS FALSE " +
            "AND month(cr.dataReceber) = :mes " +
            "AND year(cr.dataReceber) = :ano " +
            "AND  cr.id = :centroCustoId " +
            "ORDER BY cr.dataReceber")
    public List<ContasReceber> buscarcontasReceber(@Param("mes") Integer mes, @Param("ano") Integer ano, @Param("centroCustoId") Long centroCustoId);

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "JOIN FETCH cr.pessoaConta pc " +
            "JOIN FETCH cr.contrato con " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cr.removido IS FALSE " +
            "AND cr.dataReceber >= :dataInicio " +
            "AND cr.dataReceber <= :dataFim " +
            "AND con.tipoContrato = :tipoContratos " +
            "AND con.ativo IS TRUE ")
    List<ContasReceber> buscarcontasComRecorrenciaMesAnterior(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim, @Param("tipoContratos") EnumTipoContrato tipoContrato);

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "JOIN FETCH cr.pessoaConta pc " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cr.removido IS FALSE " +
            "AND cr.pagamentoParcelado IS TRUE " +
            "AND cr.quantidadeParcelas > cr.numeroParcela " +
            "AND cr.dataReceber >= :dataInicio " +
            "AND cr.dataReceber <= :dataFim ")
    List<ContasReceber> buscarcontasComParceladasMesAnterior(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "JOIN FETCH cr.pessoaConta pc " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cr.removido IS FALSE " +
            "AND cr.pagamentoParcelado IS TRUE " +
            "AND cr.numeroParcela IS NULL " +
            "AND cr.dataPrimeiraParcela >= :dataInicio " +
            "AND cr.dataPrimeiraParcela <= :dataFim ")
    List<ContasReceber> buscarcontasPrimeiraParcela(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "JOIN cr.contrato con " +
            "JOIN cr.incidencias incidencias " +
            "WHERE cr.removido IS FALSE " +
            "AND cr.dataReceber >= :dataInicio " +
            "AND cr.dataReceber <= :dataFim " +
            "AND con.tipoContrato = :tipoContrato " +
            "AND cr.descricao = :descricao " +
            "AND incidencias.centroCusto.id IN (SELECT cc.id FROM ContasReceberIncidencia cpi JOIN cpi.centroCusto cc WHERE cpi.contaReceberId = :contaCentrosCustoId)")
    List<ContasReceber> buscarcontasPeriodoPorDescricaoECentroCusto(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim, @Param("tipoContrato") EnumTipoContrato tipoContrato, @Param("descricao") String descricao, @Param("contaCentrosCustoId") Long contaCentrosCustoId);

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "JOIN cr.incidencias incidencias " +
            "WHERE cr.removido IS FALSE " +
            "AND cr.dataReceber >= :dataInicio " +
            "AND cr.dataReceber <= :dataFim " +
            "AND cr.numeroParcela = :numeroParcela " +
            "AND cr.descricao = :descricao " +
            "AND incidencias.centroCusto.id IN (SELECT cc.id FROM ContasReceberIncidencia cpi JOIN cpi.centroCusto cc WHERE cpi.contaReceberId = :contaCentrosCustoId)")
    List<ContasReceber> buscarcontasParceladasPeriodoPorDescricaoECentroCusto(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim, @Param("numeroParcela") Integer numeroParcela, @Param("descricao") String descricao, @Param("contaCentrosCustoId") Long contaCentrosCustoId);

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "JOIN FETCH cr.incidencias " +
            "JOIN FETCH cr.pessoaConta pc " +
            "JOIN FETCH cr.cliente cl " +
            "LEFT JOIN FETCH cr.contrato con " +
            "LEFT JOIN FETCH pc.municipio mu " +
            "LEFT JOIN FETCH mu.estado es " +
            "WHERE cr.id = :id")
    ContasReceber buscarcontasReceber(@Param("id") Long id);

    @Query("SELECT cr " +
            "FROM ContasReceber cr " +
            "WHERE cr.contrato.id = :contratoId " +
            "AND cr.dataReceber = (" +
            "   SELECT MAX(crsub.dataReceber) " +
            "   FROM ContasReceber crsub " +
            "   WHERE crsub.contrato.id = :contratoId" +
            ") ")
    ContasReceber buscarContaAtualContrato(@Param("contratoId")Long contratoId);

}
