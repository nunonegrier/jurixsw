package br.com.jurix.financeiro.contaspagar.entity;

import br.com.jurix.centrocusto.entity.CentroCusto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_conta_pagar_incidencia", schema = "jurix")
@SequenceGenerator(name = "seq_conta_pagar_incidencia", sequenceName = "jurix.seq_conta_pagar_incidencia")
@DynamicUpdate
public class ContasPagarIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_conta_pagar_incidencia")
    @Column(name = "cpi_id", nullable = false)
    private Long id;

    @Column(name = "cpi_incidencia")
    private BigDecimal incidencia;

    @Column(name = "cpi_valor")
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "cc_id")
    private CentroCusto centroCusto;

    @Column(name = "cp_id")
    private Long contaPagarId;

}