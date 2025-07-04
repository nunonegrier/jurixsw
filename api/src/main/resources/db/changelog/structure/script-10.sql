
alter table jurix.tb_tipo_andamento_processo add tap_finalidade character varying(32) not null default 'ANDAMENTO';
alter table jurix.tb_processo add pr_situacao character varying(32) not null default 'ATIVO';


-------------------

create sequence jurix.seq_resultado_processo
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_resultado_processo
(
 rpr_id integer not null,
 rpr_descricao character varying(255) not null,
 rpr_removido  boolean not null
);


alter table jurix.tb_resultado_processo add constraint pk_rpr_id primary key (rpr_id)
;

-------------------

alter table jurix.tb_andamento_processo add rpr_id integer null;

alter table jurix.tb_andamento_processo add constraint fk_andamento_processo_resultado foreign key (rpr_id) references jurix.tb_resultado_processo (rpr_id) on delete no action on update no action
;