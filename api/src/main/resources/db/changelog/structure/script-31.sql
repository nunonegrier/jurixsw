ALTER TABLE jurix.tb_cliente ADD cc_id integer;

alter table jurix.tb_cliente add constraint fk_cliente_centro_custo foreign key (cc_id) references jurix.tb_centro_custo (cc_id) on delete no action on update no action;