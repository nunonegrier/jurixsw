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
@Table(name = "tb_tipo_acao", schema = "jurix")
@SequenceGenerator(name = "seq_tipo_acao", sequenceName = "jurix.seq_tipo_acao")
public class TipoAcao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tipo_acao")
    @Column(name = "ta_id", nullable = false)
    private Long id;

    @Column(name = "ta_descricao")
    private String descricao;

    @Column(name = "ta_removido")
    private Boolean removido = Boolean.FALSE;
}
