package br.com.jurix.financeiro.contaspagar.entity;

import br.com.jurix.financeiro.contaspagar.enumeration.EnumFrequenciaContasPagar;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumSituacaoConta;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumTipoContasPagar;
import br.com.jurix.financeiro.pessoaconta.entity.PessoaConta;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_contas_pagar", schema = "jurix")
@SequenceGenerator(name = "seq_contas_pagar", sequenceName = "jurix.seq_contas_pagar")
@DynamicUpdate
public class ContasPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_contas_pagar")
    @Column(name = "cp_id", nullable = false)
    private Long id;

    @Column(name = "cp_tipo")
    @Enumerated(EnumType.STRING)
    private EnumTipoContasPagar tipo;

    @Column(name = "cp_situacao")
    @Enumerated(EnumType.STRING)
    private EnumSituacaoConta situacao;

    @Column(name = "cp_valor")
    private BigDecimal valor;

    @Column(name = "cp_descricao")
    private String descricao;

    @Column(name = "cp_data_vencimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimento;

    @Column(name = "cp_valor_pago")
    private BigDecimal valorPago;

    @Column(name = "cp_data_pagamento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;

    @Column(name = "cp_frequencia")
    @Enumerated(EnumType.STRING)
    private EnumFrequenciaContasPagar frequencia;

    @Column(name = "cp_repetir_valor")
    private Boolean repetirValor;

    @Column(name = "cp_removido")
    private Boolean removido = Boolean.FALSE;

    @Column(name = "cp_pagamento_final")
    private Boolean pagamentoFinal;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PessoaConta pessoaConta;

    @Column(name = "cp_data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @OneToMany(mappedBy = "contaPagarId", cascade = CascadeType.PERSIST)
    private List<ContasPagarIncidencia> incidencias;

    @Transient
    private Boolean isAtrasado;

    @Transient
    private Boolean aVencer;

    public BigDecimal getValor() {
        return Objects.nonNull(valor) ? valor : BigDecimal.ZERO;
    }
}