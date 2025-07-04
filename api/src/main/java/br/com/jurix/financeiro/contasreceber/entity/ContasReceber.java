package br.com.jurix.financeiro.contasreceber.entity;

import br.com.jurix.cliente.entity.Cliente;
import br.com.jurix.cliente.entity.Contrato;
import br.com.jurix.financeiro.contasreceber.enumeration.EnumSituacaoContaReceber;
import br.com.jurix.financeiro.pessoaconta.entity.PessoaConta;
import br.com.jurix.processo.entity.ParteProcesso;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_contas_receber", schema = "jurix")
@SequenceGenerator(name = "seq_contas_receber", sequenceName = "jurix.seq_contas_receber")
@DynamicUpdate
public class ContasReceber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_contas_receber")
    @Column(name = "cr_id", nullable = false)
    private Long id;

    @Column(name = "cr_descricao", nullable = false)
    private String descricao;

    @Column(name = "cr_situacao")
    @Enumerated(EnumType.STRING)
    private EnumSituacaoContaReceber situacao;

    @Column(name = "cr_valor_receber")
    private BigDecimal valorReceber;

    @Column(name = "cr_valor_entrada")
    private BigDecimal valorEntrada;

    @Column(name = "cr_valor_parcela")
    private BigDecimal valorParcela;

    @Column(name = "cr_valor_recebido")
    private BigDecimal valorRecebido;

    @Column(name = "cr_valor_extraordinario")
    private BigDecimal valorExtraordinario;

    @Column(name = "cr_descricao_valor_extraordinario", nullable = false)
    private String descricaoValorExtraordinario;

    @Column(name = "cr_quantidade_parcelas")
    private Integer quantidadeParcelas;

    @Column(name = "cr_numero_parcela")
    private Integer numeroParcela;

    @Column(name = "cr_data_receber")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataReceber;

    @Column(name = "cr_data_recebimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRecebimento;

    @Column(name = "cr_data_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;

    @Column(name = "cr_data_primeira_parcela")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPrimeiraParcela;

    @Column(name = "cr_pagamento_parcelado")
    private Boolean pagamentoParcelado = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PessoaConta pessoaConta;

    @ManyToOne
    @JoinColumn(name = "ppr_id")
    private ParteProcesso parteProcesso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cl_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
    private Contrato contrato;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_id_origem")
    private ContasReceber contasReceberField;

    @Column(name = "cr_data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Column(name = "cr_removido")
    private Boolean removido = Boolean.FALSE;

    @OneToMany(mappedBy = "contaReceberId", cascade = CascadeType.PERSIST)
    private List<ContasReceberIncidencia> incidencias;

    @Transient
    private ContasReceber contasReceberOrigem;

    @Transient
    private Boolean isAtrasado;

    @Transient
    private Boolean aVencer;

}
