package br.com.jurix.security.entity;

import br.com.jurix.security.enumeration.EnumSituacaoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_usuario", schema = "jurix")
@SequenceGenerator(name = "seq_usuario", sequenceName = "jurix.seq_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_usuario")
    @Column(name = "us_id", nullable = false)
    private Long id;

    @Column(name = "us_email")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "us_senha")
    private String senha;

    @Column(name = "us_nome")
    private String nome;

    @Column(name = "us_token_recuperacao")
    private String tokenRecuperacao;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "us_situacao")
    private EnumSituacaoUsuario situacao = EnumSituacaoUsuario.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pf_id")
    private Perfil perfil;

    @Transient
    private List<String> permissoes;

}