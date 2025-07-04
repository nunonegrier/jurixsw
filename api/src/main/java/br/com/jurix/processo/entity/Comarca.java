package br.com.jurix.processo.entity;

import br.com.jurix.localidade.entity.Estado;
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

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_comarca", schema = "jurix")
@SequenceGenerator(name = "seq_comarca", sequenceName = "jurix.seq_comarca")
public class Comarca {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_comarca")
    @Column(name = "co_id", nullable = false)
    private Long id;

    @Column(name = "co_descricao")
    private String descricao;

    @Column(name = "co_removido")
    private Boolean removido = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "es_id", nullable = false)
    private Estado estado;
}
