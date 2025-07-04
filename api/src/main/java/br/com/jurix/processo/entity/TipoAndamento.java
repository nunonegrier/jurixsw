package br.com.jurix.processo.entity;

import br.com.jurix.processo.enumeration.EnumFinalidadeTipoAndamento;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_tipo_andamento_processo", schema = "jurix")
@SequenceGenerator(name = "seq_tipo_andamento_processo", sequenceName = "jurix.seq_tipo_andamento_processo")
public class TipoAndamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tipo_andamento_processo")
    @Column(name = "tap_id", nullable = false)
    private Long id;

    @Column(name = "tap_descricao")
    private String descricao;

    @Column(name = "tap_removido")
    private Boolean removido = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tap_finalidade")
    private EnumFinalidadeTipoAndamento finalidade = EnumFinalidadeTipoAndamento.ANDAMENTO;

}
