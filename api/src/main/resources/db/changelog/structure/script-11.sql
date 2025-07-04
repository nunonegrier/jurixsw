-------------------

create sequence jurix.seq_historico_finalizacao_pauta_evento
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_historico_finalizacao_pauta_evento
(
 hpe_id integer not null,
 hpe_observacao_finalizacao text null,
 hpe_situacao character varying(255) not null,
 pe_id integer not null
);


alter table jurix.tb_historico_finalizacao_pauta_evento add constraint pk_hpe_id primary key (hpe_id)
;
alter table jurix.tb_historico_finalizacao_pauta_evento add constraint fk_historico_finalizacao_pauta foreign key (pe_id) references jurix.tb_pauta_evento (pe_id) on delete no action on update no action
;

-------------------

alter table jurix.tb_pauta_evento alter column pe_observacao_finalizacao type text
;



