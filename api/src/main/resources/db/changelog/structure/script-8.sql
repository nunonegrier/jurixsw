
alter table jurix.tb_cliente add cl_possui_contrato boolean not null default false;

update jurix.tb_cliente set cl_possui_contrato = true where cl_id in (
  select cl_id from jurix.tb_contrato
);