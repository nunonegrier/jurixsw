alter table jurix.tb_pauta_evento add pe_data_alteracao timestamp(6) without time zone;
alter table jurix.tb_pauta_evento add pe_data_alteracao_limite timestamp(6) without time zone;

update jurix.tb_pauta_evento set pe_data_alteracao = pe_data_criacao;
update jurix.tb_pauta_evento set pe_data_alteracao_limite = pe_data_criacao;

alter table jurix.tb_historico_finalizacao_pauta_evento add hpe_data_alteracao_evento timestamp(6) without time zone;
alter table jurix.tb_historico_finalizacao_pauta_evento add hpe_data_alteracao_limite timestamp(6) without time zone;

update jurix.tb_historico_finalizacao_pauta_evento hpe set hpe_data_alteracao_evento = pe.pe_data_alteracao from jurix.tb_pauta_evento pe where pe.pe_id = hpe.pe_id;
update jurix.tb_historico_finalizacao_pauta_evento hpe set hpe_data_alteracao_limite = pe.pe_data_alteracao_limite from jurix.tb_pauta_evento pe where pe.pe_id = hpe.pe_id;