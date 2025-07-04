alter table jurix.tb_contrato add con_data_vencimento date null;
alter table jurix.tb_contrato add con_informar_data_vencimento boolean not null default false;

update jurix.tb_contrato set con_data_vencimento = con_data_inicio;