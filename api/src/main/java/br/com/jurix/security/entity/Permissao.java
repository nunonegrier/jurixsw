package br.com.jurix.security.entity;

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
@Table(name = "tb_permissao", schema = "jurix")
@SequenceGenerator(name = "seq_permissao", sequenceName = "jurix.seq_permissao")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_permissao")
    @Column(name = "pe_id", nullable = false)
    private Long id;

    @Column(name = "pe_nome")
    private String nome;

    @Column(name = "pe_descricao")
    private String descricao;

}
