
insert into jurix.tb_permissao (pe_id,  pe_descricao, pe_nome) values
(nextval('jurix.seq_permissao'), 'Permite Criar evento de pauta para outros colaboradores', 'Pauta.Criar.ParaColaboradores'),
(nextval('jurix.seq_permissao'), 'Permite finalizar processo', 'Processo.Finalizar');