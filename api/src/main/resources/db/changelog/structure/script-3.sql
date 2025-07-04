create table jurix.tb_pauta_evento(
 pe_id integer not null,
 pe_tipo character varying(20),
 pe_data_publicacao timestamp(6) without time zone,
 pe_data_limite timestamp(6) without time zone,
 pe_descricao character varying(100),
 pe_observacao_criacao character varying(255),
 pe_observacao_finalizacao character varying(255),
 pe_situacao character varying(30),
 co_id integer,
 us_id integer,
 CONSTRAINT pk_pe_id PRIMARY KEY (pe_id),
 CONSTRAINT fk_pauta_evento_colaborador FOREIGN KEY (co_id)
      REFERENCES jurix.tb_colaborador (co_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fk_pauta_evento_usuario FOREIGN KEY (us_id)
      REFERENCES jurix.tb_usuario (us_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX in_pe_pe_id
  ON jurix.tb_pauta_evento
  USING btree
  (pe_id);

create sequence jurix.seq_pauta_evento
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;
