package br.com.jurix.processo.entity;

import br.com.jurix.cliente.entity.Cliente;
import br.com.jurix.processo.enumeration.EnumTipoParte;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_parte_processo", schema = "jurix")
@SequenceGenerator(name = "seq_parte_processo", sequenceName = "jurix.seq_parte_processo")
public class ParteProcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_parte_processo")
    @Column(name = "ppr_id", nullable = false)
    private Long id;

    @Column(name = "ppr_nome")
    private String nome;

    @Column(name = "ppr_contato")
    private String contato;

    @Enumerated(EnumType.STRING)
    @Column(name = "ppr_tipo")
    private EnumTipoParte tipo;

    @Column(name = "ppr_removido")
    private Boolean removido = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cl_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pap_id")
    private PosicaoParte posicaoParte;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id", nullable = false)
    private Processo processo;

}
