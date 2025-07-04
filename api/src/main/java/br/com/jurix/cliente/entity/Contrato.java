package br.com.jurix.cliente.entity;

import br.com.jurix.centrocusto.entity.CentroCusto;
import br.com.jurix.cliente.enumeration.EnumIndexadorContrato;
import br.com.jurix.cliente.enumeration.EnumTipoContrato;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_contrato", schema = "jurix")
@SequenceGenerator(name = "seq_contrato", sequenceName = "jurix.seq_contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_contrato")
    @Column(name = "con_id", nullable = false)
    private Long id;

    @Column(name = "con_descricao")
    private String descricao;

    @Column(name = "con_tipo")
    @Enumerated(EnumType.STRING)
    private EnumTipoContrato tipoContrato;

    @Column(name = "con_data_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Column(name = "con_data_vencimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimento;

    @Column(name = "con_indexador")
    @Enumerated(EnumType.STRING)
    private EnumIndexadorContrato indexador;

    @Column(name = "con_quantidade")
    private BigDecimal quantidade;

    @Column(name = "con_foro")
    private String foro;

    @Column(name = "con_observacoes")
    private String observacoes;

    @Column(name = "con_removido")
    private Boolean removido = Boolean.FALSE;

    @Column(name = "con_ativo")
    private Boolean ativo = Boolean.TRUE;

    @Column(name = "con_informar_data_vencimento")
    private Boolean informarDataVencimento = Boolean.FALSE;

    @Column(name = "con_data_inativacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInativacao;

    @ManyToOne
    @JoinColumn(name = "cl_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "cc_id")
    private CentroCusto centroCusto;
}
