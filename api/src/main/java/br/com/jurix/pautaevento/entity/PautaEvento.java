package br.com.jurix.pautaevento.entity;

import br.com.jurix.colaborador.entity.Colaborador;
import br.com.jurix.pautaevento.enumeration.EnumSituacaoEvento;
import br.com.jurix.pautaevento.enumeration.EnumTipoPauta;
import br.com.jurix.processo.entity.Processo;
import br.com.jurix.security.entity.Usuario;
import com.querydsl.core.annotations.QueryInit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_pauta_evento", schema = "jurix")
@SequenceGenerator(name = "seq_pauta_evento", sequenceName = "jurix.seq_pauta_evento")
public class PautaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_pauta_evento")
    @Column(name = "pe_id", nullable = false)
    private Long id;

    @Column(name = "pe_tipo")
    @Enumerated(EnumType.STRING)
    private EnumTipoPauta tipo;

    @Column(name = "pe_situacao")
    @Enumerated(EnumType.STRING)
    private EnumSituacaoEvento situacao;

    @Column(name = "pe_data_publicacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPublicacao;

    @Column(name = "pe_data_limite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLimite;

    @Column(name = "pe_data_finalizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFinalizacao;

    @Column(name = "pe_data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Column(name = "pe_data_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;

    @Column(name = "pe_data_alteracao_limite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracaoDataLimite;

    @Column(name = "pe_descricao")
    private String descricao;

    @Column(name = "pe_observacao_criacao")
    private String observacaoCriacao;

    @Column(name = "pe_observacao_finalizacao")
    private String observacaoFinalizacao;

    @Column(name = "pe_removido")
    private Boolean removido = Boolean.FALSE;

    @Column(name = "pe_finalizado_automaticamente")
    private Boolean finalizadoAutomaticamente = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id")
    @QueryInit("usuario")
    private Colaborador colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "us_id")
    private Usuario usuarioCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id")
    @QueryInit("contrato.cliente")
    private Processo processo;


    @Transient
    private Boolean aVencer;

    @Transient
    private Boolean atrasado;
}
