package br.com.jurix.cliente.entity;

import br.com.jurix.cliente.enumeration.EnumTipoFundacao;
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
@Table(name = "tb_cliente_juridico", schema = "jurix")
@SequenceGenerator(name = "seq_cliente_juridico", sequenceName = "jurix.seq_cliente_juridico")
public class ClienteJuridico {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_cliente_juridico")
    @Column(name = "clj_id", nullable = false)
    private Long id;

    @Column(name = "clj_nome_fantasia")
    private String nomeFantasia;

    @Column(name = "clj_cnpj")
    private String cnpj;

    @Column(name = "clj_inscricao_estadual")
    private String inscricaoEstadual;

    @Column(name = "clj_inscricao_municipal")
    private String inscricaoMunicipal;

    @Column(name = "clj_cpf")
    private String cpf;

    @Column(name = "clj_fundacao")
    private String fundacao;

    @Column(name = "clj_tipo_fundacao")
    private EnumTipoFundacao tipoFundacao;

    @Column(name = "clj_nome_responsavel")
    private String nomeResponsavel;

    @Column(name = "clj_telefone_responsavel")
    private String telefoneResponsavel;

    @Column(name = "clj_email_responsavel")
    private String emailResponsavel;

    @Column(name = "clj_nome_responsavel_alt")
    private String nomeResposavelAlternativo;

    @Column(name = "clj_email_responsavel_alt")
    private String emailResponsavelAlternativo;

    @Column(name = "clj_telefone_responsavel_alt")
    private String telefoneResponsavelAlternativo;

    @Column(name = "clj_website")
    private String website;

}
