create sequence jurix.seq_grupo_cliente
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_grupo_cliente(
 gr_id integer not null,
 gr_nome character varying(254) not null,
 gr_removido boolean not null
);

create index in_gr_id on jurix.tb_grupo_cliente (gr_id)
;
alter table jurix.tb_grupo_cliente add constraint pk_gr_id primary key (gr_id)
;

alter table jurix.tb_cliente add gr_id integer
;
alter table jurix.tb_cliente add constraint fk_cliente_grupo foreign key (gr_id) references jurix.tb_grupo_cliente (gr_id) on delete no action on update no action
;