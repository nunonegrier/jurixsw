
alter table jurix.tb_usuario add pf_id integer;
alter table jurix.tb_usuario add constraint fk_usuario_perfil foreign key (pf_id) references jurix.tb_perfil (pf_id) on update no action on delete no action;

-----------

create sequence jurix.seq_permissao
 increment by 1
 start with 1
 no maxvalue
 no minvalue
;

create table jurix.tb_permissao
(
 pe_id integer not null,
 pe_nome character varying(255) not null,
 pe_descricao character varying(255)null
);


alter table jurix.tb_permissao add constraint pk_pe_pe_id primary key (pe_id)
;

------------

create table jurix.tb_permissao_perfil
(
 pe_id integer not null,
 pf_id integer not null
);

alter table jurix.tb_permissao_perfil add constraint fk_pp_perfil foreign key (pf_id) references jurix.tb_perfil (pf_id) on update no action on delete no action;
;
alter table jurix.tb_permissao_perfil add constraint fk_pp_permissao foreign key (pe_id) references jurix.tb_permissao (pe_id) on update no action on delete no action;
;