define(['../module', 'text!./view.html'], function (module, view) {
    'use strict';

    function novaContaPagarFn(ctrl, $rootScope) {
        return function () {
            $rootScope.$broadcast('NOVA_CONTA_PAGAR', ctrl.centroCusto);
        };
    }

    function visulizarContaPagarFn($scope) {
        return function (contasPagar) {
            $scope.$root.$broadcast('VISUALIZAR_CONTA_PAGAR', contasPagar);
        }
    }

    function salvarValorFn($rootScope, contasPagarService){
        return function (contaPagarModel) {

            contaPagarModel.valor = contaPagarModel.novoValor;

            contasPagarService.save(contaPagarModel)
                .then(function () {
                    $rootScope.$broadcast('CONTA_SALVA');
                })
                .catch(function () {
                    alert('erro')
                });
        };
    }

    function removerContaFn($rootScope){
        return function(contaPagarModel){
            $rootScope.$broadcast('REMOVER_CONTA', contaPagarModel, 'CONTA_PAGAR');
        };
    }

    function exibirContaFn(ctrl){
        return function(contaPagarModel){

            if(!ctrl.filters.mostrarFinalizados && contaPagarModel.situacao === 'PAGA'){
                return false;
            }

            return true;
        };
    }

    function selecionarDiasVencimentoFn($rootScope){
        return function(diasVencimento){
            $rootScope.$broadcast('BUSCAR_CONTAS_ULTIMOS_DIAS', diasVencimento);
        };
    }

    function lastday (y,m){
        return new Date(y, m, 0).getDate();
    }

    function pesquisarDatasFn(ctrl, $rootScope){
        return function(){

            if(ctrl.filters.dataInicio && ctrl.filters.dataFinal){
                $rootScope.$broadcast('BUSCAR_CONTAS_ENTRE_DIAS', ctrl.filters.dataInicio, ctrl.filters.dataFinal);
            }
        };
    }

    function getValorPercentualFn(ctrl){
        return function(contaPagar){
            var incidencia = getIncidenciaControCusto(contaPagar, ctrl.centroCusto);
            return incidencia ? incidencia.valor : 0;
        };
    }

    function getIncidenciaControCusto(contaPagar, centroCusto){
        return contaPagar.incidencias.filter(function(incidencia){
            return  incidencia.centroCusto.id == centroCusto.id;
        })[0];
    }

    function getPercentualFn(ctrl, $filter){
        return function(contaPagar){
            var incidencia = getIncidenciaControCusto(contaPagar, ctrl.centroCusto);
            return incidencia ? $filter('number')(incidencia.incidencia, 2).replace(',00', '') : 0;
        };
    }

    module.component('contasPagarGrid', {
        template: view,
        bindings: {
            contasPagar: '=',
            centroCusto: '=',
            centrosCusto: '=',
            mes: '<',
            ano: '<'
        },
        controller: ['$rootScope', 'contasPagarService', '$filter', function ($rootScope, contasPagarService, $filter) {

            var ctrl = this;

            ctrl.filters = {
                mostrarFinalizados:true,
                dataInicio:null,
                dataFinal: null
            };

            ctrl.$onChanges = function(){
                ctrl.dateOptionsPagar = {
                    minDate: '01/' + ctrl.mes + '/' + ctrl.ano,
                    maxDate: lastday(ctrl.ano, ctrl.mes) + '/' + ctrl.mes + '/' + ctrl.ano,
                    hideIfNoPrevNext: true
                };
                ctrl.filters.dataInicio = null;
                ctrl.filters.dataFinal = null;
             };

            ctrl.novaContaPagar = novaContaPagarFn(ctrl, $rootScope);

            ctrl.salvarValor = salvarValorFn($rootScope, contasPagarService);

            ctrl.visulizarContaPagar = visulizarContaPagarFn($rootScope);

            ctrl.removerConta = removerContaFn($rootScope);

            ctrl.exibirConta = exibirContaFn(ctrl);

            ctrl.selecionarDiasVencimento = selecionarDiasVencimentoFn($rootScope);

            ctrl.pesquisarDatas = pesquisarDatasFn(ctrl, $rootScope);

            ctrl.getValorPercentual = getValorPercentualFn(ctrl);

            ctrl.getPercentual = getPercentualFn(ctrl, $filter);
        }]
    });
});