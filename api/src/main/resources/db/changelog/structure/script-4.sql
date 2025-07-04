alter table jurix.tb_cliente add cl_removido  boolean not null default false;
alter table jurix.tb_colaborador add co_removido  boolean not null default false;
alter table jurix.tb_contrato add con_removido  boolean not null default false;