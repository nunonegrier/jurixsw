package br.com.jurix.pautaevento.entity;

import br.com.jurix.pautaevento.enumeration.EnumSituacaoEvento;
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
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_historico_finalizacao_pauta_evento", schema = "jurix")
@SequenceGenerator(name = "seq_historico_finalizacao_pauta_evento", sequenceName = "jurix.seq_historico_finalizacao_pauta_evento")
public class HistoricoFinalizacaoPauta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_historico_finalizacao_pauta_evento")
    @Column(name = "hpe_id", nullable = false)
    private Long id;

    @Column(name = "hpe_situacao")
    @Enumerated(EnumType.STRING)
    private EnumSituacaoEvento situacao;

    @Column(name = "hpe_observacao_finalizacao")
    private String observacaoFinalizacao;

    @Column(name = "hpe_data_finalizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFinalizacao;

    @Column(name = "hpe_data_alteracao_evento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;

    @Column(name = "hpe_data_alteracao_limite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracaoDataLimite;


    @Column(name = "hpe_data_limite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pe_id")
    private PautaEvento pautaEvento;
}
