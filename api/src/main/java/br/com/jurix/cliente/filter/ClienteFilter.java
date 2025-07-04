package br.com.jurix.cliente.filter;

import br.com.jurix.cliente.entity.QCliente;
import br.com.jurix.cliente.enumeration.EnumEstadoCivil;
import br.com.jurix.cliente.enumeration.EnumSexo;
import br.com.jurix.cliente.enumeration.EnumTipoEstadoCivil;
import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.pautaevento.entity.QPautaEvento;
import com.google.common.base.Strings;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import java.util.Date;
import java.util.Objects;

public class ClienteFilter extends SortPaginatorFilter {

    public void setNome(String nome) {
        addToMainBooleanExpression(QCliente.cliente.nome.containsIgnoreCase(nome));
    }

    public void setNaoExibirRemovidos(){
        addToMainBooleanExpression(QCliente.cliente.removido.isFalse());
    }

    public void setClienteComProcessPossuiPauta(String clienteComProcessPossuiPauta){

        if(!Strings.isNullOrEmpty(clienteComProcessPossuiPauta)) {
            JPQLQuery idClienteComProcessoPossuiPauta = JPAExpressions.select(QPautaEvento.pautaEvento.processo.contrato.cliente.id).from(QPautaEvento.pautaEvento);
            addToMainBooleanExpression(QCliente.cliente.id.in(idClienteComProcessoPossuiPauta));
        }
    }

    public void setNomeSolteiro(String nomeSolteiro){
        if(!Strings.isNullOrEmpty(nomeSolteiro)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.nomeSolteiro.containsIgnoreCase(nomeSolteiro));
        }
    }

    public void setRgRne(String rgRne){
        if(!Strings.isNullOrEmpty(rgRne)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.rgRne.containsIgnoreCase(rgRne));
        }
    }

    public void setSexo(String sexo){
        if(!Strings.isNullOrEmpty(sexo)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.sexo.eq(EnumSexo.valueOf(sexo)));
        }
    }

    public void setNomeFantasia(String nomeFantasia){
        if(!Strings.isNullOrEmpty(nomeFantasia)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.nomeFantasia.containsIgnoreCase(nomeFantasia));
        }
    }

    public void setCnpj(String cnpj){
        if(!Strings.isNullOrEmpty(cnpj)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.cnpj.containsIgnoreCase(cnpj));
        }
    }

    public void setInscricaoEstadual(String inscricaoEstadual){
        if(!Strings.isNullOrEmpty(inscricaoEstadual)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.inscricaoEstadual.containsIgnoreCase(inscricaoEstadual));
        }
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal){
        if(!Strings.isNullOrEmpty(inscricaoMunicipal)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.inscricaoMunicipal.containsIgnoreCase(inscricaoMunicipal));
        }
    }

    public void setNomeResponsavel(String nomeResponsavel){
        if(!Strings.isNullOrEmpty(nomeResponsavel)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.nomeResponsavel.containsIgnoreCase(nomeResponsavel));
        }
    }

    public void setTelefoneResponsavel(String telefoneResponsavel){
        if(!Strings.isNullOrEmpty(telefoneResponsavel)) {
            addToMainBooleanExpression(QCliente.cliente.telefone.containsIgnoreCase(telefoneResponsavel));
        }
    }

    public void setEmailResponsavel(String emailResponsavel){
        if(!Strings.isNullOrEmpty(emailResponsavel)) {
            addToMainBooleanExpression(QCliente.cliente.email.containsIgnoreCase(emailResponsavel));
        }
    }

    public void setNomeResponsavelAlternativo(String nomeResponsavelAlternativo){
        if(!Strings.isNullOrEmpty(nomeResponsavelAlternativo)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.nomeResposavelAlternativo.containsIgnoreCase(nomeResponsavelAlternativo));
        }
    }

    public void setTelefoneResponsavelAlternativo(String telefoneResponsavelAlternativo){
        if(!Strings.isNullOrEmpty(telefoneResponsavelAlternativo)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.telefoneResponsavelAlternativo.containsIgnoreCase(telefoneResponsavelAlternativo));
        }
    }

    public void setEmailResponsavelAlternativo(String emailResponsavelAlternativo){
        if(!Strings.isNullOrEmpty(emailResponsavelAlternativo)) {
            addToMainBooleanExpression(QCliente.cliente.clienteJuridico.emailResponsavelAlternativo.containsIgnoreCase(emailResponsavelAlternativo));
        }
    }

    public void setDataNascimentoParam(Date dataNascimento){
        if(Objects.nonNull(dataNascimento)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.dataNascimento.eq(dataNascimento));
        }
    }

    public void setNacionalidade(String nacionalidade){
        if(!Strings.isNullOrEmpty(nacionalidade)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.nacionalidade.containsIgnoreCase(nacionalidade));
        }
    }

    public void setNomePai(String nomePai){
        if(!Strings.isNullOrEmpty(nomePai)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.nomePai.containsIgnoreCase(nomePai));
        }
    }

    public void setNomeMae(String nomeMae){
        if(!Strings.isNullOrEmpty(nomeMae)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.nomeMae.containsIgnoreCase(nomeMae));
        }
    }

    public void setCpf(String cpf){
        if(!Strings.isNullOrEmpty(cpf)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.cpf.containsIgnoreCase(cpf));
        }
    }

    public void setCarteiraTrabalho(String carteiraTrabalho){
        if(!Strings.isNullOrEmpty(carteiraTrabalho)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.carteiraTrabalho.containsIgnoreCase(carteiraTrabalho));
        }
    }

    public void setSerie(String serie){
        if(!Strings.isNullOrEmpty(serie)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.serie.containsIgnoreCase(serie));
        }
    }

    public void setPis(String pis){
        if(!Strings.isNullOrEmpty(pis)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.pis.containsIgnoreCase(pis));
        }
    }

    public void setProfissao(String profissao){
        if(!Strings.isNullOrEmpty(profissao)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.profissao.containsIgnoreCase(profissao));
        }
    }

    public void setTituloEleitor(String tituloEleitor){
        if(!Strings.isNullOrEmpty(tituloEleitor)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.tituloEleitor.containsIgnoreCase(tituloEleitor));
        }
    }

    public void setZona(String zona){
        if(!Strings.isNullOrEmpty(zona)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.zona.containsIgnoreCase(zona));
        }
    }

    public void setSecao(String secao){
        if(!Strings.isNullOrEmpty(secao)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.secao.containsIgnoreCase(secao));
        }
    }

    public void setEstadoCivil(String estadoCivil){
        if(!Strings.isNullOrEmpty(estadoCivil)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.estadoCivil.eq(EnumEstadoCivil.valueOf(estadoCivil)));
        }
    }

    public void setTipoEstadoCivil(String tipoEstadoCivil){
        if(!Strings.isNullOrEmpty(tipoEstadoCivil)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.tipoEstadoCivil.eq(EnumTipoEstadoCivil.valueOf(tipoEstadoCivil)));
        }
    }

    public void setNomeConjuge(String nomeConjuge){
        if(!Strings.isNullOrEmpty(nomeConjuge)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.nomeConjugue.containsIgnoreCase(nomeConjuge));
        }
    }

    public void setRgRneConjugue(String rgRneConjugue){
        if(!Strings.isNullOrEmpty(rgRneConjugue)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.rgRneConjugue.containsIgnoreCase(rgRneConjugue));
        }
    }

    public void setCpfConjuge(String cpfConjuge){
        if(!Strings.isNullOrEmpty(cpfConjuge)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.cpfConjugue.containsIgnoreCase(cpfConjuge));
        }
    }

    public void setTelefoneFisico1(String telefoneFisico1){
        if(!Strings.isNullOrEmpty(telefoneFisico1)) {
            addToMainBooleanExpression(QCliente.cliente.telefone.containsIgnoreCase(telefoneFisico1));
        }
    }

    public void setTelefoneFisico2(String telefoneFisico2){
        if(!Strings.isNullOrEmpty(telefoneFisico2)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.telefone2.containsIgnoreCase(telefoneFisico2));
        }
    }


    public void setEndereco(String endereco){
        if(!Strings.isNullOrEmpty(endereco)) {
            addToMainBooleanExpression(QCliente.cliente.endereco.containsIgnoreCase(endereco));
        }
    }

    public void setMunicipio(Long municipioId){
        if(Objects.nonNull(municipioId)) {
            addToMainBooleanExpression(QCliente.cliente.municipio.id.eq(municipioId));
        }
    }

    public void setEstado(Long estadoId){
        if(Objects.nonNull(estadoId)) {
            addToMainBooleanExpression(QCliente.cliente.municipio.estado.id.eq(estadoId));
        }
    }

    public void setCep(String cep){
        if(!Strings.isNullOrEmpty(cep)) {
            addToMainBooleanExpression(QCliente.cliente.cep.containsIgnoreCase(cep));
        }
    }

    public void setEmailProfissional(String emailProfissional){
        if(!Strings.isNullOrEmpty(emailProfissional)) {
            addToMainBooleanExpression(QCliente.cliente.clienteFisico.emailProfissional.containsIgnoreCase(emailProfissional));
        }
    }

    public void setEmailPessoal(String emailPessoal){
        if(!Strings.isNullOrEmpty(emailPessoal)) {
            addToMainBooleanExpression(QCliente.cliente.email.containsIgnoreCase(emailPessoal));
        }
    }

    public void setIndicadoPor(String indicadoPor){
        if(!Strings.isNullOrEmpty(indicadoPor)) {
            addToMainBooleanExpression(QCliente.cliente.indicacao.containsIgnoreCase(indicadoPor));
        }
    }

    public void setGrupo(Long grupo){
        if(Objects.nonNull(grupo)){
            addToMainBooleanExpression(QCliente.cliente.grupo.id.eq(grupo));
        }
    }

}
