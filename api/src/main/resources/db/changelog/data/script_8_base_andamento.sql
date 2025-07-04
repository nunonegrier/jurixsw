
insert into jurix.tb_tipo_andamento_processo (tap_id, tap_descricao, tap_removido, tap_finalidade) values
(nextval('jurix.seq_tipo_andamento_processo'), 'Decisão', false, 'FINALIZAR_PROCESSO'),
(nextval('jurix.seq_tipo_andamento_processo'), 'Acordo', false, 'FINALIZAR_PROCESSO'),
(nextval('jurix.seq_tipo_andamento_processo'), 'Reativação Processo', false, 'REATIVAR_PROCESSO');

insert into jurix.tb_resultado_processo(rpr_id, rpr_descricao, rpr_removido) VALUES
(nextval('jurix.seq_resultado_processo'), 'Procedente', false),
(nextval('jurix.seq_resultado_processo'), 'Parcialmente Procedente', false),
(nextval('jurix.seq_resultado_processo'), 'Improcedente', false);
