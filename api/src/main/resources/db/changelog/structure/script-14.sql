create sequence jurix.seq_filtro_pesquisa
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

-------------- Filtro Pesquisa --------------
create table jurix.tb_filtro_pesquisa(
 fp_id integer not null,
 fp_nome character varying(512) not null,
 fp_tipo character varying(64) not null,
 fp_valor text not null,
 fp_data_criacao timestamp(6) without time zone,
 fp_removido  boolean not null,
 us_id integer not null
);

create index in_fp_id on jurix.tb_filtro_pesquisa (fp_id)
;
alter table jurix.tb_filtro_pesquisa add constraint pk_fp_id primary key (fp_id)
;
alter table jurix.tb_filtro_pesquisa add constraint fk_filtro_usuario foreign key (us_id) references jurix.tb_usuario (us_id) on delete no action on update no action
;

insert into jurix.tb_filtro_pesquisa (fp_id, fp_nome, fp_tipo, fp_valor, fp_data_criacao, fp_removido, us_id)
select
nextval('jurix.seq_filtro_pesquisa') as id,
'Data' as nome,
'PAUTA_EVENTO' as tipo,
'{"idResponsavel":null,"idCliente":null,"idParte":null,"idProcesso":null,"dataInicio":null,"dataFim":null,"intervaloData":null,"tipoData":null,"tipo":null,"dataPublicacao":null,"situacao":null,"sort":"dataLimite","direction":"desc","page":1,"size":10}' as valor,
now()::timestamp as data_criacao,
false as removido,
us_id
from jurix.tb_usuario;

insert into jurix.tb_filtro_pesquisa (fp_id, fp_nome, fp_tipo, fp_valor, fp_data_criacao, fp_removido, us_id)
select
nextval('jurix.seq_filtro_pesquisa') as id,
'Pendentes' as nome,
'PAUTA_EVENTO' as tipo,
'{"idResponsavel":null,"idCliente":null,"idParte":null,"idProcesso":null,"dataInicio":null,"dataFim":null,"intervaloData":null,"tipoData":null,"tipo":null,"dataPublicacao":null,"situacao":"PENDENTE","sort":"dataLimite","direction":"desc","page":1,"size":10}' as valor,
now()::timestamp as data_criacao,
false as removido,
us_id
from jurix.tb_usuario;