package br.com.jurix.colaborador.entity;

import br.com.jurix.cliente.enumeration.EnumSexo;
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
@Table(name = "tb_colaborador", schema = "jurix")
@SequenceGenerator(name = "seq_colaborador", sequenceName = "jurix.seq_colaborador")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_colaborador")
    @Column(name = "co_id", nullable = false)
    private Long id;

    @Column(name = "co_nacionalidade")
    private String nacionalidade;

    @Column(name = "co_sexo")
    @Enumerated(EnumType.STRING)
    private EnumSexo sexo;

    @Column(name = "co_cpf")
    private String cpf;

    @Column(name = "co_oab")
    private String oab;

    @Column(name = "co_rgrne")
    private String rgRne;

    @Column(name = "co_orgao_expedidor")
    private String orgaoExpedidor;

    @Column(name = "co_data_nascimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNascimento;

    @Column(name = "co_carteira_trabalho")
    private String carteiraTrabalho;

    @Column(name = "co_serie")
    private String serie;

    @Column(name = "co_telefone_1")
    private String telefone1;

    @Column(name = "co_telefone_2")
    private String telefone2;

    @Column(name = "co_email_profissional")
    private String emailProfissional;

    @Column(name = "co_email_pessoal")
    private String emailPessoal;

    @Column(name = "co_removido")
    private Boolean removido = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "us_id")
    private Usuario usuario;

}

