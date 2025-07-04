package br.com.jurix.centrocusto.entity;

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
@Table(name = "tb_centro_custo", schema = "jurix")
@SequenceGenerator(name = "seq_centro_custo", sequenceName = "jurix.seq_centro_custo")
public class CentroCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_centro_custo")
    @Column(name = "cc_id", nullable = false)
    private Long id;

    @Column(name = "cc_descricao")
    private String descricao;

    @Column(name = "cc_removido")
    private Boolean removido = Boolean.FALSE;
}
