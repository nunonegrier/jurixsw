-- Usuário

insert into jurix.tb_usuario (us_id, us_email, us_nome, us_senha)
values (nextval('jurix.seq_usuario'), 'admin@jurix.com', 'Administrador', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.');

insert into jurix.tb_roles (rl_id, rl_nome, us_id) values (nextval('jurix.seq_roles'), 'jurix', (select us_id from jurix.tb_usuario where us_email = 'admin@jurix.com'));
