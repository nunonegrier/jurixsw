package br.com.jurix.processo.entity;

import br.com.jurix.centrocusto.entity.CentroCusto;
import br.com.jurix.cliente.entity.Contrato;
import br.com.jurix.processo.enumeration.EnumAreaProcesso;
import br.com.jurix.processo.enumeration.EnumInstanciaProcesso;
import br.com.jurix.processo.enumeration.EnumSituacaoProcesso;
import br.com.jurix.processo.enumeration.EnumTipoProcesso;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_processo", schema = "jurix")
@SequenceGenerator(name = "seq_processo", sequenceName = "jurix.seq_processo")
@DynamicUpdate
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_processo")
    @Column(name = "pr_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "pr_tipo")
    private EnumTipoProcesso tipo;

    @Column(name = "pr_numero")
    private String numero;

    @Column(name = "pr_descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "pr_area")
    private EnumAreaProcesso area;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pr_data_distribuicao")
    private Date dataDistribuicao;

    @Column(name = "pr_foro")
    private String foro;

    @Column(name = "pr_vara")
    private String vara;

    @Enumerated(EnumType.STRING)
    @Column(name = "pr_instancia")
    private EnumInstanciaProcesso instancia;

    @Column(name = "pr_valor_acao")
    private BigDecimal valorAcao;

    @Column(name = "pr_resumo")
    private String resumo;

    @Column(name = "pr_observacoes")
    private String observacoes;

    @Column(name = "pr_removido")
    private Boolean removido = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "pr_situacao")
    private EnumSituacaoProcesso situacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ta_id", nullable = false)
    private TipoAcao tipoAcao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id", nullable = false)
    private Comarca comarca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cc_id", nullable = false)
    private CentroCusto centroCusto;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id_vinculado")
    private Processo processoVinculadoField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id", nullable = false)
    private Contrato contrato;

    @Transient
    private ParteProcesso parteProcesso;

    @Transient
    private Processo processoVinculado;

    @Transient
    private String ultimoAndamento;

    @Transient
    private String parteInteressada;

    @Transient
    private String parteContraria;
}
