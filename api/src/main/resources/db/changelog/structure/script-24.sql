create sequence jurix.seq_conta_pagar_incidencia
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_conta_pagar_incidencia
(
 cpi_id integer not null,
 cc_id integer not null,
 cp_id integer not null,
 cpi_incidencia numeric(18,8) not null
);


alter table jurix.tb_conta_pagar_incidencia add constraint pk_cpi_id primary key (cpi_id)
;

alter table jurix.tb_conta_pagar_incidencia add constraint fk_cpi_centro_custo foreign key (cc_id) references jurix.tb_centro_custo (cc_id) on update no action on delete no action;
;
alter table jurix.tb_conta_pagar_incidencia add constraint fk_cpi_contas_pagar foreign key (cp_id) references jurix.tb_contas_pagar (cp_id) on update no action on delete no action;
;


insert into jurix.tb_conta_pagar_incidencia
select nextval('jurix.seq_conta_pagar_incidencia'), cc.cc_id, cp.cp_id, 100.00
from jurix.tb_contas_pagar cp
join jurix.tb_centro_custo cc on cp.cc_id = cc.cc_id;

alter table jurix.tb_contas_pagar drop column cc_id;

--------

create sequence jurix.seq_conta_receber_incidencia
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

create table jurix.tb_conta_receber_incidencia
(
 cri_id integer not null,
 cc_id integer not null,
 cr_id integer not null,
 cri_incidencia numeric(18,8) not null
);

alter table jurix.tb_conta_receber_incidencia add constraint pk_cri_id primary key (cri_id)
;

alter table jurix.tb_conta_receber_incidencia add constraint fk_cri_centro_custo foreign key (cc_id) references jurix.tb_centro_custo (cc_id) on update no action on delete no action;
;
alter table jurix.tb_conta_receber_incidencia add constraint fk_cri_contas_receber foreign key (cr_id) references jurix.tb_contas_receber (cr_id) on update no action on delete no action;
;

insert into jurix.tb_conta_receber_incidencia
select nextval('jurix.seq_conta_receber_incidencia'), cc.cc_id, cr.cr_id, 100.00
from jurix.tb_contas_receber cr
join jurix.tb_centro_custo cc on cr.cc_id = cc.cc_id;

alter table jurix.tb_contas_receber drop column cc_id;