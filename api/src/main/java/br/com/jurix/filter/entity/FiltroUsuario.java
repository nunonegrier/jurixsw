package br.com.jurix.filter.entity;

import br.com.jurix.filter.enumeration.TipoFiltroUsuarioEnum;
import br.com.jurix.security.entity.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_filtro_pesquisa", schema = "jurix")
@SequenceGenerator(name = "seq_filtro_pesquisa", sequenceName = "jurix.seq_filtro_pesquisa")
public class FiltroUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_filtro_pesquisa")
    @Column(name = "fp_id", nullable = false)
    private Long id;

    @Column(name = "fp_nome")
    private String nome;

    @Column(name = "fp_valor")
    private String valor;

    @Column(name = "fp_descricao")
    private String descricao;

    @Column(name = "fp_tipo")
    @Enumerated(EnumType.STRING)
    private TipoFiltroUsuarioEnum tipo;

    @Column(name = "fp_data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Column(name = "fp_removido")
    private Boolean removido;

    @Column(name = "fp_padrao")
    private Boolean padrao = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "us_id")
    private Usuario usuarioCriacao;

}
