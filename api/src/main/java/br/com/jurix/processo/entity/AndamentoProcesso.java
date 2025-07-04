package br.com.jurix.processo.entity;

import br.com.jurix.pautaevento.entity.PautaEvento;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tb_andamento_processo", schema = "jurix")
@SequenceGenerator(name = "seq_andamento_processo", sequenceName = "jurix.seq_andamento_processo")
public class AndamentoProcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_andamento_processo")
    @Column(name = "ap_id", nullable = false)
    private Long id;

    @Column(name = "ap_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Column(name = "ap_descricao")
    private String descricao;

    @Column(name = "ap_cria_evento_pauta")
    private Boolean criaEventoPauta = Boolean.FALSE;

    @Column(name = "ap_removido")
    private Boolean removido = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pe_id")
    private PautaEvento pautaEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tap_id", nullable = false)
    private TipoAndamento tipoAndamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id", nullable = false)
    private Processo processo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rpr_id", nullable = false)
    private ResultadoProcesso resultadoProcesso;

}
