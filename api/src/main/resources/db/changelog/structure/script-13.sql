
alter table jurix.tb_pauta_evento add pe_data_criacao timestamp(6) without time zone;
update jurix.tb_pauta_evento set pe_data_criacao = pe_data_publicacao;
alter table jurix.tb_pauta_evento alter column pe_data_criacao set not null;

alter table jurix.tb_historico_finalizacao_pauta_evento add hpe_data_limite timestamp(6) without time zone;
update jurix.tb_historico_finalizacao_pauta_evento hpe set hpe_data_limite = pe.pe_data_limite from jurix.tb_pauta_evento pe where pe.pe_id = hpe.pe_id;
alter table jurix.tb_historico_finalizacao_pauta_evento alter column hpe_data_limite set not null;


