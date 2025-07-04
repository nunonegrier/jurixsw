package br.com.jurix.cliente.entity;

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
@Table(name = "tb_grupo_cliente", schema = "jurix")
@SequenceGenerator(name = "seq_grupo_cliente", sequenceName = "jurix.seq_grupo_cliente")
public class GrupoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_grupo_cliente")
    @Column(name = "gr_id", nullable = false)
    private Long id;

    @Column(name = "gr_nome")
    private String nome;

    @Column(name = "gr_removido")
    private Boolean removido = Boolean.FALSE;
}
