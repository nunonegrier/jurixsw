package br.com.jurix.cliente.entity;

import br.com.jurix.cliente.enumeration.EnumEstadoCivil;
import br.com.jurix.cliente.enumeration.EnumSexo;
import br.com.jurix.cliente.enumeration.EnumTipoEstadoCivil;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_cliente_fisico", schema = "jurix")
@SequenceGenerator(name = "seq_cliente_fisico", sequenceName = "jurix.seq_cliente_fisico")
public class ClienteFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_cliente_fisico")
    @Column(name = "clf_id", nullable = false)
    private Long id;

    @Column(name = "clf_tratamento")
    private String tratamento;

    @Column(name = "clf_nome_solteiro")
    private String nomeSolteiro;

    @Column(name = "clf_rg_rne")
    private String rgRne;

    @Column(name = "clf_orgao_expeditor")
    private String orgaoExpeditor;

    @Column(name = "clf_carteira_trabalho")
    private String carteiraTrabalho;

    @Column(name = "clf_serie")
    private String serie;

    @Column(name = "clf_titulo_eleitor")
    private String tituloEleitor;

    @Column(name = "clf_zona")
    private String zona;

    @Column(name = "clf_secao")
    private String secao;

    @Column(name = "clf_sexo")
    @Enumerated(EnumType.STRING)
    private EnumSexo sexo;

    @Column(name = "clf_data_nascimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNascimento;

    @Column(name = "clf_cpf")
    private String cpf;

    @Column(name = "clf_pis")
    private String pis;

    @Column(name = "clf_nacionalidade")
    private String nacionalidade;

    @Column(name = "clf_estado_civil")
    @Enumerated(EnumType.STRING)
    private EnumEstadoCivil estadoCivil;

    @Column(name = "clf_tipo_estado_civil")
    @Enumerated(EnumType.STRING)
    private EnumTipoEstadoCivil tipoEstadoCivil;

    @Column(name = "clf_profissao")
    private String profissao;

    @Column(name = "clf_nome_conjugue")
    private String nomeConjugue;

    @Column(name = "clf_rg_rne_conjugue")
    private String rgRneConjugue;

    @Column(name = "clf_cpf_conjugue")
    private String cpfConjugue;

    @Column(name = "clf_nome_pai")
    private String nomePai;

    @Column(name = "clf_data_nascimento_pai")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNascimentoPai;

    @Column(name = "clf_nome_mae")
    private String nomeMae;

    @Column(name = "clf_data_nascimento_mae")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNascimentoMae;

    @Column(name = "clf_telefone_2")
    private String telefone2;

    @Column(name = "clf_telefone_3")
    private String telefone3;

    @Column(name = "clf_email_profissional")
    private String emailProfissional;


}
