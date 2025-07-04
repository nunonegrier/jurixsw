ALTER TABLE jurix.tb_perfil ADD pf_removido boolean not null default false;


create sequence jurix.seq_parametros
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_parametro(
 pa_id integer not null,
 pa_chave character varying(64) not null,
 pa_valor character varying(128) not null
);

create index in_pa_id on jurix.tb_parametro(pa_id)
;

alter table jurix.tb_parametro add constraint pk_pa_id primary key (pa_id)
;