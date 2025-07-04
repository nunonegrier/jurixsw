
alter table jurix.tb_pauta_evento add pe_data_finalizacao timestamp(6) without time zone null;
alter table jurix.tb_pauta_evento add pe_finalizado_automaticamente boolean not null default false ;

alter table jurix.tb_historico_finalizacao_pauta_evento add hpe_data_finalizacao timestamp(6) without time zone null;


update jurix.tb_pauta_evento set pe_data_finalizacao = pe_data_limite where pe_situacao IN ('FINALIZADO', 'FALHOU');