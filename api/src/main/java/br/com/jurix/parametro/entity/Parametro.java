package br.com.jurix.parametro.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_parametro", schema = "jurix")
@SequenceGenerator(name = "seq_parametro", sequenceName = "jurix.seq_parametro")
public class Parametro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_parametro")
    @Column(name = "pa_id", nullable = false)
    private Long id;

    @Column(name = "pa_chave")
    private String chave;

    @Column(name = "pa_valor")
    private String valor;

}
