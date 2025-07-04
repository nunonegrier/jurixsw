package br.com.jurix.pautaevento.filter;

import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.filter.enumeration.TipoIntervaloDataFiltroEnum;
import br.com.jurix.pautaevento.entity.QPautaEvento;
import br.com.jurix.pautaevento.enumeration.DataFiltroEnum;
import br.com.jurix.pautaevento.enumeration.EnumSituacaoEvento;
import br.com.jurix.pautaevento.enumeration.EnumTipoPauta;
import br.com.jurix.processo.entity.QParteProcesso;
import br.com.jurix.security.entity.Usuario;
import com.google.common.base.Strings;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PautaEventoFilter extends SortPaginatorFilter {

    private DataFiltroEnum tipoData;
    private TipoIntervaloDataFiltroEnum intervaloData;
    private Map<DataFiltroEnum, DateTimePath> pathDatasPauta;
    private Boolean filtrarAtrasados = Boolean.FALSE;

    public PautaEventoFilter(){

        pathDatasPauta = new HashMap<>();
        pathDatasPauta.put(DataFiltroEnum.DATA_CRIACAO, QPautaEvento.pautaEvento.dataCriacao);
        pathDatasPauta.put(DataFiltroEnum.DATA_FINALIZACAO, QPautaEvento.pautaEvento.dataFinalizacao);
        pathDatasPauta.put(DataFiltroEnum.DATA_LIMITE, QPautaEvento.pautaEvento.dataLimite);
    }

    public void setTipoData(DataFiltroEnum tipoData) {
        this.tipoData = tipoData;
    }

    public void setIntervaloData(TipoIntervaloDataFiltroEnum intervaloData) {
        this.intervaloData = intervaloData;
    }

    public void setSituacao(String situacaoString){
        if(!Strings.isNullOrEmpty(situacaoString)){

            EnumSituacaoEvento situacao = EnumSituacaoEvento.valueOf(situacaoString);
            if(situacao.equals(EnumSituacaoEvento.ATRASADO)) {
                filtrarAtrasados = Boolean.TRUE;
            }else {
                addToMainBooleanExpression(QPautaEvento.pautaEvento.situacao.eq(situacao));
            }
        }
    }

    public void setExibirFinalizados(Boolean exibirFinalizados){
        if(Objects.nonNull(exibirFinalizados) && !exibirFinalizados){
            addToMainBooleanExpression(QPautaEvento.pautaEvento.situacao.eq(EnumSituacaoEvento.PENDENTE));
        }
    }

    public void setDataPublicacao(String dataPublicacao){
        if(!Strings.isNullOrEmpty(dataPublicacao)) {
            criarFiltroParaIntervaloData(QPautaEvento.pautaEvento.dataPublicacao, dataPublicacao, dataPublicacao);
        }
    }

    public void criarFiltroIntervaloData(Date dataAtual){

        if(Objects.nonNull(tipoData)) {
            if (!intervaloData.equals(TipoIntervaloDataFiltroEnum.FIXO)) {
                criarFiltroParaIntervaloData(pathDatasPauta.get(tipoData), dataAtual, intervaloData);
            } else {
                criarFiltroParaIntervaloData(pathDatasPauta.get(tipoData));
            }
        }

        if(filtrarAtrasados){
            addToMainBooleanExpression(QPautaEvento.pautaEvento.situacao.eq(EnumSituacaoEvento.PENDENTE).and(QPautaEvento.pautaEvento.dataLimite.before(JurixDateTime.getStartOfDay(dataAtual))));
        }
    }

    public void setUsuario(Usuario usuario) {
        if (Objects.nonNull(usuario)) {
            addToMainBooleanExpression(QPautaEvento.pautaEvento.usuarioCriacao.id.eq(usuario.getId())
                    .or(QPautaEvento.pautaEvento.colaborador.usuario.id.eq(usuario.getId())));
        }
    }

    public void setNaoRemovido(){
        addToMainBooleanExpression(QPautaEvento.pautaEvento.removido.isFalse());
    }

    public void setTipo(String tipoString){
        if(!Strings.isNullOrEmpty(tipoString)) {
            addToMainBooleanExpression(QPautaEvento.pautaEvento.tipo.eq(EnumTipoPauta.valueOf(tipoString)));
        }
    }

    public void setIdResponsavel(String idResponsavel){
        if(!Strings.isNullOrEmpty(idResponsavel)){
            addToMainBooleanExpression(QPautaEvento.pautaEvento.colaborador.id.eq(Long.valueOf(idResponsavel)));
        }
    }

    public void setIdCliente(String idCliente){
        if(!Strings.isNullOrEmpty(idCliente)){
            addToMainBooleanExpression(QPautaEvento.pautaEvento.processo.contrato.cliente.id.eq(Long.valueOf(idCliente)));
        }
    }

    public void setIdParte(String idParte){
        if(!Strings.isNullOrEmpty(idParte)){

            JPQLQuery idProcessoComParte = JPAExpressions.select(QParteProcesso.parteProcesso.processo.id).from(QParteProcesso.parteProcesso).where(QParteProcesso.parteProcesso.id.eq(Long.valueOf(idParte)));
            addToMainBooleanExpression(QPautaEvento.pautaEvento.processo.id.in(idProcessoComParte));
        }
    }

    public void setIdProcesso(String idProcesso){
        if(!Strings.isNullOrEmpty(idProcesso)){
            addToMainBooleanExpression(QPautaEvento.pautaEvento.processo.id.eq(Long.valueOf(idProcesso)));
        }
    }

    public void setDescricaoPauta(String descricaoPauta){
        if(!Strings.isNullOrEmpty(descricaoPauta)){
            addToMainBooleanExpression(QPautaEvento.pautaEvento.descricao.containsIgnoreCase(descricaoPauta));
        }
    }

    public void setObservacaoPauta(String observacaoPauta){
        if(!Strings.isNullOrEmpty(observacaoPauta)){
            addToMainBooleanExpression(QPautaEvento.pautaEvento.observacaoCriacao.containsIgnoreCase(observacaoPauta));
        }
    }

}
