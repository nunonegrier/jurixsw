alter table jurix.tb_contrato add con_data_inativacao timestamp(6) without time zone;
alter table jurix.tb_contrato add con_ativo boolean not null default true;