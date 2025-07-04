package br.com.jurix.processo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_resultado_processo", schema = "jurix")
@SequenceGenerator(name = "seq_resultado_processo", sequenceName = "jurix.seq_resultado_processo")
public class ResultadoProcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_resultado_processo")
    @Column(name = "rpr_id", nullable = false)
    private Long id;

    @Column(name = "rpr_descricao")
    private String descricao;

    @Column(name = "rpr_removido")
    private Boolean removido = Boolean.FALSE;
}
