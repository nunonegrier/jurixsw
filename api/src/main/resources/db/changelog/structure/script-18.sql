
alter table jurix.tb_contrato add cc_id integer;

alter table jurix.tb_contrato add constraint fk_contrato_centro_custo foreign key (cc_id) references jurix.tb_centro_custo (cc_id) on delete no action on update no action
;
