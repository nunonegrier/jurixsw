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
@Table(name = "tb_posicao_parte", schema = "jurix")
@SequenceGenerator(name = "seq_posicao_parte", sequenceName = "jurix.seq_posicao_parte")
public class PosicaoParte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_posicao_parte")
    @Column(name = "pap_id", nullable = false)
    private Long id;

    @Column(name = "pap_descricao")
    private String descricao;

    @Column(name = "pap_removido")
    private Boolean removido = Boolean.FALSE;
}
