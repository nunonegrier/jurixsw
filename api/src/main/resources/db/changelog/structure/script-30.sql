ALTER TABLE jurix.tb_usuario ADD us_situacao varchar(16) not null default 'ATIVO';

ALTER TABLE jurix.tb_usuario ADD us_token_recuperacao varchar(256);
