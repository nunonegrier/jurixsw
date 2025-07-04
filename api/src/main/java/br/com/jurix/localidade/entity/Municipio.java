package br.com.jurix.localidade.entity;

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
@Table(name = "tb_municipio", schema = "jurix")
@SequenceGenerator(name = "seq_municipio", sequenceName = "jurix.seq_municipio")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_municipio")
    @Column(name = "mu_id", nullable = false)
    private Long id;

    @Column(name = "mu_nome")
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "es_id", nullable = false)
    private Estado estado;
}
