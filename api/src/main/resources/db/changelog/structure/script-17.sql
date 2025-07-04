create sequence jurix.seq_contas_receber
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_contas_receber(
 cr_id integer not null,
 cr_situacao character varying(64) not null,
 cr_valor_receber numeric(18,8),
 cr_valor_entrada numeric(18,8),
 cr_valor_parcela numeric(18,8),
 cr_valor_recebido numeric(18,8),
 cr_valor_extraordinario numeric(18,8),
 cr_descricao_valor_extraordinario character varying(254),
 cr_quantidade_parcelas integer,
 cr_numero_parcela integer,
 cr_descricao character varying(512) not null,
 cr_data_receber timestamp(6) without time zone not null,
 cr_data_primeira_parcela timestamp(6) without time zone,
 cr_data_entrada timestamp(6) without time zone,
 cr_data_recebimento timestamp(6) without time zone,
 cr_pagamento_parcelado boolean not null not null,
 pc_id integer not null,
 cc_id integer not null,
 ppr_id integer,
 cl_id integer,
 con_id integer,
 cr_id_origem integer,
 cr_data_criacao timestamp(6) without time zone not null,
 cr_removido boolean not null
);

create index in_cr_cr_id on jurix.tb_contas_receber (cr_id)
;

alter table jurix.tb_contas_receber add constraint pk_cr_id primary key (cr_id)
;

alter table jurix.tb_contas_receber add constraint fk_creceber_pesso_conta foreign key (pc_id) references jurix.tb_pessoa_conta (pc_id) on delete no action on update no action
;

alter table jurix.tb_contas_receber add constraint fk_creeber_centro_custo foreign key (cc_id) references jurix.tb_centro_custo (cc_id) on delete no action on update no action
;

alter table jurix.tb_contas_receber add constraint fk_creceber_parte foreign key (ppr_id) references jurix.tb_parte_processo (ppr_id) on delete no action on update no action
;

alter table jurix.tb_contas_receber add constraint fk_creeber_cliente foreign key (cl_id) references jurix.tb_cliente (cl_id) on delete no action on update no action
;

alter table jurix.tb_contas_receber add constraint fk_creeber_contrato foreign key (con_id) references jurix.tb_contrato (con_id) on delete no action on update no action
;