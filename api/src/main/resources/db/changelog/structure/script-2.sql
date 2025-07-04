-------------- Perfil --------------

create table jurix.tb_perfil(
 pf_id integer NOT NULL,
 pf_descricao character varying(100),
 CONSTRAINT pk_pf_id PRIMARY KEY (pf_id)
);

CREATE INDEX in_perfil_pf_id ON jurix.tb_perfil USING btree(pf_id);

create sequence jurix.seq_perfil
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;

-------------- Colaborador--------------

create table jurix.tb_colaborador(
 co_id integer NOT NULL,
 co_nacionalidade character varying(100),
 co_sexo character varying(10),
 co_cpf character varying(20),
 co_oab character varying(100),
 co_rgrne character varying(100),
 co_orgao_expedidor character varying(100),
 co_data_nascimento timestamp(6) without time zone,
 co_carteira_trabalho character varying(100),
 co_serie character varying(100),
 co_telefone_1 character varying(100),
 co_telefone_2 character varying(100),
 co_email_profissional character varying(100),
 co_email_pessoal character varying(100),
 us_id integer,
 pf_id integer,
 CONSTRAINT pk_co_id PRIMARY KEY (co_id),
 CONSTRAINT fk_colaborador_usuario FOREIGN KEY (us_id)
      REFERENCES jurix.tb_usuario (us_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fk_colaborador_perfil FOREIGN KEY (pf_id)
      REFERENCES jurix.tb_perfil (pf_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX in_col_co_id ON jurix.tb_colaborador USING btree(co_id);

create sequence jurix.seq_colaborador
 increment by 1
 start with 1
 no maxvalue
 no minvalue
 cache 1
;