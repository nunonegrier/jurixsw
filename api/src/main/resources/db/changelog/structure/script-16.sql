create sequence jurix.seq_pessoa_conta
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_pessoa_conta(
 pc_id integer not null,
 pc_tipo character varying(16) not null,
 pc_nome character varying(254) not null,
 pc_documento character varying(64) not null,
 pc_endereco character varying(64),
 pc_cep character varying(64),
 mu_id integer null
);

create index in_pc_pc_id on jurix.tb_pessoa_conta (pc_id)
;

alter table jurix.tb_pessoa_conta add constraint pk_pc_id primary key (pc_id)
;

alter table jurix.tb_pessoa_conta add constraint fk_pessoa_conta_munici foreign key (mu_id) references jurix.tb_municipio (mu_id) on delete no action on update no action
;

---

create sequence jurix.seq_contas_pagar
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_contas_pagar(
 cp_id integer not null,
 cp_tipo character varying(20) not null,
 cp_situacao character varying(64) not null,
 cp_valor numeric(18,8),
 cp_valor_pago numeric(18,8),
 cp_pagamento_final boolean,
 cp_descricao character varying(512) not null,
 cp_data_vencimento timestamp(6) without time zone not null,
 cp_data_pagamento timestamp(6) without time zone,
 cp_frequencia character varying(128) not null,
 cp_repetir_valor boolean not null,
 pc_id integer not null,
 cc_id integer not null,
 cp_data_criacao timestamp(6) without time zone not null,
 cp_removido boolean not null
);

create index in_cp_cp_id on jurix.tb_contas_pagar (cp_id)
;

alter table jurix.tb_contas_pagar add constraint pk_cp_id primary key (cp_id)
;

alter table jurix.tb_contas_pagar add constraint fk_cpagar_pesso_conta foreign key (pc_id) references jurix.tb_pessoa_conta (pc_id) on delete no action on update no action
;

alter table jurix.tb_contas_pagar add constraint fk_cpagar_centro_custo foreign key (cc_id) references jurix.tb_centro_custo (cc_id) on delete no action on update no action
;