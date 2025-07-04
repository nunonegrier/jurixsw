create sequence jurix.seq_usuario
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

-------------- Usuário --------------
create table jurix.tb_usuario(
 us_id integer not null,
 us_email character varying(64) not null,
 us_nome character varying(64) not null,
 us_senha character varying(64) not null
);

create index in_us_id on jurix.tb_usuario (us_id)
;

alter table jurix.tb_usuario add constraint pk_us_id primary key (us_id)
;

-------------- Roles --------------

create sequence jurix.seq_roles
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_roles(
 rl_id integer not null,
 rl_nome character varying(64) not null,
 us_id integer not null
);

create index in_rl_id on jurix.tb_roles (rl_id)
;

alter table jurix.tb_roles add constraint pk_rl_id primary key (rl_id)
;

alter table jurix.tb_roles add constraint fk_roles_us_id foreign key (us_id) references jurix.tb_usuario (us_id) on delete no action on update no action
;


-- Criação da tabela de Estados

create sequence jurix.seq_estado
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_estado
(
  es_id integer not null,
  es_nome character varying(50) not null,
  es_uf char 	 (2)  not null
);

create index in_es_es_id on jurix.tb_estado (es_id)
;

alter table jurix.tb_estado add constraint pk_es_id primary key (es_id)
;

-- Criação da tabela de Municipio

create sequence jurix.seq_municipio
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_municipio
(
  mu_id integer not null,
  mu_nome character varying(50) not null,
  es_id integer not null
);

create index in_mu_mu_id on jurix.tb_municipio (mu_id)
;

alter table jurix.tb_municipio add constraint pk_mu_id primary key (mu_id)
;

alter table jurix.tb_municipio add constraint fk_municipio_estado foreign key (es_id) references jurix.tb_estado (es_id) on delete no action on update no action
;

--- Criação da tabela de file metadata.

create sequence jurix.seq_file_metadata
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_file_metadata
(
 fm_id integer not null,
 fm_name character varying(256) not null,
 fm_extension character varying(16) null,
 fm_description character varying(512) null,
 fm_uri character varying(512) not null,
 fm_dest_folder character varying(256) null,
 fm_state character varying(16) not null,
 fm_is_folder boolean not null,
 fm_created_at timestamp(6) null,
 fm_size numeric(14,2) null,
 fm_parent_id integer null
);


alter table jurix.tb_file_metadata add constraint pk_fm_id primary key (fm_id)
;

create index in_fm_fm_id on jurix.tb_file_metadata (fm_id)
;


--- Criação da tabela de cliente fisico

create sequence jurix.seq_cliente_fisico
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;


create table jurix.tb_cliente_fisico
(
 clf_id integer not null,
 clf_tratamento character varying(8) null,
 clf_nome_solteiro character varying(256) null,
 clf_rg_rne character varying(32) null,
 clf_orgao_expeditor character varying(16) null,
 clf_carteira_trabalho character varying(32) null,
 clf_serie character varying(32) null,
 clf_titulo_eleitor character varying(32) null,
 clf_zona character varying(32) null,
 clf_secao character varying(32) null,
 clf_sexo character varying(16) null,
 clf_data_nascimento date null,
 clf_cpf character varying(32) null,
 clf_pis character varying(32) null,
 clf_nacionalidade character varying(64) null,
 clf_estado_civil character varying(32) null,
 clf_tipo_estado_civil character varying(32) null,
 clf_profissao character varying(32) null,
 clf_nome_conjugue character varying(254) null,
 clf_rg_rne_conjugue character varying(32) null,
 clf_cpf_conjugue character varying(32) null,
 clf_nome_pai character varying(32) null,
 clf_data_nascimento_pai date null,
 clf_nome_mae character varying(32) null,
 clf_data_nascimento_mae date null,
 clf_telefone_2 character varying(32) null,
 clf_telefone_3 character varying(32) null,
 clf_email_profissional character varying(32) null
);

create index in_clf_clf_id on jurix.tb_cliente_fisico (clf_id)
;

alter table jurix.tb_cliente_fisico add constraint pk_clf_id primary key (clf_id)
;

--- Criação da tabela de cliente juridico

create sequence jurix.seq_cliente_juridico
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;


create table jurix.tb_cliente_juridico
(
 clj_id integer not null,
 clj_nome_fantasia character varying(254) null,
 clj_cnpj character varying(32) null,
 clj_inscricao_estadual character varying(32) null,
 clj_inscricao_municipal character varying(32) null,
 clj_cpf character varying(32) null,
 clj_fundacao character varying(64) null,
 clj_tipo_fundacao character varying(64) null,
 clj_nome_responsavel character varying(254) null,
 clj_telefone_responsavel character varying(32) null,
 clj_email_responsavel	character varying(254) null,
 clj_nome_responsavel_alt character varying(254) null,
 clj_telefone_responsavel_alt character varying(32) null,
 clj_email_responsavel_alt	character varying(254) null,
 clj_website	character varying(254) null
);

create index in_clj_clj_id on jurix.tb_cliente_juridico (clj_id)
;

alter table jurix.tb_cliente_juridico add constraint pk_clj_id primary key (clj_id)
;

--- Criação da tabela de cliente.

create sequence jurix.seq_cliente
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_cliente
(
 cl_id integer not null,
 cl_nome character varying(256) not null,
 cl_email character varying(256) null,
 cl_telefone character varying(32) not null,
 cl_endereco	character varying(254) null,
 cl_cep	character varying(254) null,
 cl_indicacao character varying(254) null,
 cl_observacoes character varying(254) null,
 clf_id integer null,
 clj_id integer null,
 mu_id integer null
);

create index in_cl_cl_id on jurix.tb_cliente (cl_id)
;

alter table jurix.tb_cliente add constraint pk_cl_id primary key (cl_id)
;

alter table jurix.tb_cliente add constraint fk_cliente_cl_fisico foreign key (clf_id) references jurix.tb_cliente_fisico (clf_id) on delete no action on update no action
;
alter table jurix.tb_cliente add constraint fk_cliente_cl_juridico foreign key (clj_id) references jurix.tb_cliente_juridico (clj_id) on delete no action on update no action
;
alter table jurix.tb_cliente add constraint fk_cliente_municipio foreign key (mu_id) references jurix.tb_municipio (mu_id) on delete no action on update no action
;

--- Criação da tabela de contrato.

create sequence jurix.seq_contrato
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_contrato
(
 con_id integer not null,
 con_descricao character varying(256) not null,
 con_tipo character varying(64) null,
 con_data_inicio date null,
 con_indexador character varying(32) null,
 con_quantidade	numeric(18,8) null,
 con_foro character varying(254) null,
 con_observacoes character varying(254) null,
 cl_id integer null
);

create index in_con_con_id on jurix.tb_contrato (con_id)
;

alter table jurix.tb_contrato add constraint pk_con_id primary key (con_id)
;

alter table jurix.tb_contrato add constraint fk_contrato_cliente foreign key (cl_id) references jurix.tb_cliente (cl_id) on delete no action on update no action
;


