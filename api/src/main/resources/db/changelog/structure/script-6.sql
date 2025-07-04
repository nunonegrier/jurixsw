
alter table jurix.tb_pauta_evento add pe_removido boolean not null default false;

alter table jurix.tb_parte_processo add cl_id integer null;

-------------------

create sequence jurix.seq_posicao_parte
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_posicao_parte
(
 pap_id integer not null,
 pap_descricao character varying(255) not null,
 pap_removido  boolean not null
);


alter table jurix.tb_posicao_parte add constraint pk_pap_id primary key (pap_id)
;

----------

alter table jurix.tb_parte_processo add pap_id integer null
;

create index in_ppr_pap_id on jurix.tb_parte_processo (pap_id)
;

alter table jurix.tb_parte_processo add constraint fk_parte_posicao_parte foreign key (pap_id) references jurix.tb_posicao_parte (pap_id) on delete no action on update no action
;

