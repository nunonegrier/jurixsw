package br.com.jurix.localidade.entity;

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
@Table(name = "tb_estado", schema = "jurix")
@SequenceGenerator(name = "seq_estado", sequenceName = "jurix.seq_estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_estado")
    @Column(name = "es_id", nullable = false)
    private Long id;

    @Column(name = "es_nome")
    private String nome;

    @Column(name = "es_uf")
    private String uf;
}
