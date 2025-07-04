create sequence jurix.seq_tipo_acao
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_tipo_acao
(
 ta_id integer not null,
 ta_descricao character varying(255) not null,
 ta_removido  boolean not null
);


alter table jurix.tb_tipo_acao add constraint pk_ta_id primary key (ta_id)
;

----------------------------------

create sequence jurix.seq_comarca
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_comarca
(
 co_id integer not null,
 co_descricao character varying(255) not null,
 co_removido  boolean not null,
 es_id integer not null
);

alter table jurix.tb_comarca add constraint pk_com_id primary key (co_id)
;

create index in_co_es_id on jurix.tb_comarca (es_id)
;

alter table jurix.tb_comarca add constraint fk_comarca_estado foreign key (es_id) references jurix.tb_estado (es_id) on delete no action on update no action
;


---------------------------------

create sequence jurix.seq_centro_custo
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_centro_custo
(
 cc_id integer not null,
 cc_descricao character varying(255) not null,
 cc_removido  boolean not null
);

alter table jurix.tb_centro_custo add constraint pk_cc_id primary key (cc_id)
;

----------------------------------

create sequence jurix.seq_processo
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_processo
(
 pr_id integer not null,
 pr_tipo character varying(32) not null,
 pr_numero character varying(128) not null,
 pr_descricao character varying(255) not null,
 pr_area character varying(32) not null,
 pr_data_distribuicao timestamp(6) not null,
 pr_foro character varying(255) not null,
 pr_vara character varying(255) not null,
 pr_instancia character varying(32) not null,
 pr_valor_acao numeric(18,8) not null,
 pr_resumo character varying(255) not null,
 pr_observacoes character varying(255) not null,
 pr_removido  boolean not null,
 ta_id integer not null,
 co_id integer not null,
 cc_id integer not null,
 pr_id_vinculado integer null,
 con_id integer not null
);

alter table jurix.tb_processo add constraint pk_pr_id primary key (pr_id)
;

create index in_pr_ta_id on jurix.tb_processo(ta_id)
;
create index in_pr_cc_id on jurix.tb_processo (cc_id)
;
create index in_pr_co_id on jurix.tb_processo (co_id)
;
create index in_pr_pr_id_vinculado on jurix.tb_processo (pr_id_vinculado)
;
create index in_pr_con_id on jurix.tb_processo (con_id)
;

alter table jurix.tb_processo add constraint fk_processo_centro_custo foreign key (cc_id) references jurix.tb_centro_custo (cc_id) on delete no action on update no action
;
alter table jurix.tb_processo add constraint fk_processo_tipo_acao foreign key (ta_id) references jurix.tb_tipo_acao (ta_id) on delete no action on update no action
;
alter table jurix.tb_processo add constraint fk_processo_comarca foreign key (co_id) references jurix.tb_comarca (co_id) on delete no action on update no action
;
alter table jurix.tb_processo add constraint fk_processo_processo_vinculado foreign key (pr_id_vinculado) references jurix.tb_processo (pr_id) on delete no action on update no action
;
alter table jurix.tb_processo add constraint fk_processo_contrato foreign key (con_id) references jurix.tb_contrato (con_id) on delete no action on update no action
;

----------------------------------

create sequence jurix.seq_parte_processo
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_parte_processo
(
 ppr_id integer not null,
 ppr_nome character varying(128) not null,
 ppr_contato character varying(128) not null,
 ppr_tipo character varying(32) not null,
 ppr_removido  boolean not null,
 pr_id integer not null
);

alter table jurix.tb_parte_processo add constraint pk_ppr_id primary key (ppr_id)
;

create index in_pr_ppr_id on jurix.tb_parte_processo (pr_id)
;

alter table jurix.tb_parte_processo add constraint fk_parte_proc_processo foreign key (pr_id) references jurix.tb_processo (pr_id) on delete no action on update no action
;

----------------------------------

create sequence jurix.seq_tipo_andamento_processo
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_tipo_andamento_processo
(
 tap_id integer not null,
 tap_descricao character varying(255) not null,
 tap_removido  boolean not null
);

alter table jurix.tb_tipo_andamento_processo add constraint pk_tap_id primary key (tap_id)
;

----------------------------------

create sequence jurix.seq_andamento_processo
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_andamento_processo
(
 ap_id integer not null,
 ap_data timestamp(6) not null,
 ap_descricao character varying(254) not null,
 ap_cria_evento_pauta  boolean not null,
 ap_removido  boolean not null,
 tap_id integer not null,
 pe_id integer,
 pr_id integer not null
);

alter table jurix.tb_andamento_processo add constraint pk_ap_id primary key (ap_id)
;

create index in_ap_tap_id on jurix.tb_andamento_processo (tap_id)
;
create index in_ap_pe_id on jurix.tb_andamento_processo (pe_id)
;
create index in_ap_pr_id on jurix.tb_andamento_processo (pr_id)
;

alter table jurix.tb_andamento_processo add constraint fk_tipo_andam_andamento_processo foreign key (tap_id) references jurix.tb_tipo_andamento_processo (tap_id) on delete no action on update no action
;
alter table jurix.tb_andamento_processo add constraint fk_pauta_evento_processo foreign key (pe_id) references jurix.tb_pauta_evento (pe_id) on delete no action on update no action
;
alter table jurix.tb_andamento_processo add constraint fk_processo_andamento_processo foreign key (pr_id) references jurix.tb_processo (pr_id) on delete no action on update no action
;


alter table jurix.tb_pauta_evento add pr_id integer null
;

create index in_pe_pr_id on jurix.tb_pauta_evento (pr_id)
;

alter table jurix.tb_pauta_evento add constraint fk_pauta_evento_processo_rel foreign key (pr_id) references jurix.tb_processo (pr_id) on delete no action on update no action
;