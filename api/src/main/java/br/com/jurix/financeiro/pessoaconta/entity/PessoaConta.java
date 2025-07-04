package br.com.jurix.financeiro.pessoaconta.entity;

import br.com.jurix.financeiro.pessoaconta.enumeration.EnumTipoPessoaConta;
import br.com.jurix.localidade.entity.Municipio;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

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

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_pessoa_conta", schema = "jurix")
@SequenceGenerator(name = "seq_pessoa_conta", sequenceName = "jurix.seq_pessoa_conta")
@DynamicUpdate
public class PessoaConta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_pessoa_conta")
    @Column(name = "pc_id", nullable = false)
    private Long id;

    @Column(name = "pc_tipo")
    @Enumerated(EnumType.STRING)
    private EnumTipoPessoaConta tipoPessoa;

    @Column(name = "pc_nome")
    private String nome;

    @Column(name = "pc_documento")
    private String documento;

    @Column(name = "pc_endereco")
    private String endereco;

    @Column(name = "pc_cep")
    private String cep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mu_id")
    private Municipio municipio;

}
