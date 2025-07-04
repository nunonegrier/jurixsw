package br.com.jurix.security.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_perfil", schema = "jurix")
@SequenceGenerator(name = "seq_perfil", sequenceName = "jurix.seq_perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_perfil")
    @Column(name = "pf_id", nullable = false)
    private Long id;

    @Column(name = "pf_descricao")
    private String descricao;

    @Column(name = "pf_removido")
    private Boolean removido = Boolean.FALSE;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "tb_permissao_perfil", schema = "jurix",
            joinColumns = { @JoinColumn(name = "pf_id") },
            inverseJoinColumns = { @JoinColumn(name = "pe_id") })
    private Set<Permissao> permissoes = new HashSet<>();

}
