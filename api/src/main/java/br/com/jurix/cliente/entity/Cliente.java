package br.com.jurix.cliente.entity;

import br.com.jurix.centrocusto.entity.CentroCusto;
import br.com.jurix.localidade.entity.Municipio;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_cliente", schema = "jurix")
@SequenceGenerator(name = "seq_cliente", sequenceName = "jurix.seq_cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_cliente")
    @Column(name = "cl_id", nullable = false)
    private Long id;

    @Column(name = "cl_nome")
    private String nome;

    @Column(name = "cl_email")
    private String email;

    @Column(name = "cl_telefone")
    private String telefone;

    @Column(name = "cl_endereco")
    private String endereco;

    @Column(name = "cl_cep")
    private String cep;

    @Column(name = "cl_indicacao")
    private String indicacao;

    @Column(name = "cl_observacoes")
    private String observacoes;

    @Column(name = "cl_removido")
    private Boolean removido = Boolean.FALSE;

    @Column(name = "cl_possui_contrato")
    private Boolean possuiContrato = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mu_id", nullable = false)
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cc_id")
    private CentroCusto centroCusto;

    @OneToOne
    @JoinColumn(name = "clf_id")
    private ClienteFisico clienteFisico;

    @OneToOne
    @JoinColumn(name = "clj_id")
    private ClienteJuridico clienteJuridico;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gr_id")
    private GrupoCliente grupo;

    public Boolean isClienteFisico(){
        return Objects.nonNull(clienteFisico);
    }

    public Boolean isClienteJuridico(){
        return Objects.nonNull(clienteJuridico);
    }
}
