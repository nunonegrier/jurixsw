package br.com.jurix.financeiro.contasreceber.entity;

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
@Table(name = "tb_conta_receber_incidencia", schema = "jurix")
@SequenceGenerator(name = "seq_conta_receber_incidencia", sequenceName = "jurix.seq_conta_receber_incidencia")
@DynamicUpdate
public class ContasReceberIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_conta_receber_incidencia")
    @Column(name = "cri_id", nullable = false)
    private Long id;

    @Column(name = "cri_incidencia")
    private BigDecimal incidencia;

    @Column(name = "cri_valor")
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "cc_id")
    private CentroCusto centroCusto;

    @Column(name = "cr_id")
    private Long contaReceberId;

}