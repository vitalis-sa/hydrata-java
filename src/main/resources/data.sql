-- Inserindo Planos de Assinatura se não existirem
MERGE INTO PLANO p
USING (SELECT 'Básico' as nome, 50.0 as valor_mensalidade, 'Plano básico com recursos essenciais para pequenas propriedades' as descricao, 'ATIVO' as status FROM DUAL) src
ON (p.nome = src.nome)
WHEN NOT MATCHED THEN
  INSERT (id, nome, valor_mensalidade, descricao, status)
  VALUES (SQ_PLANO.NEXTVAL, src.nome, src.valor_mensalidade, src.descricao, src.status);

MERGE INTO PLANO p
USING (SELECT 'Intermediário' as nome, 100.0 as valor_mensalidade, 'Plano intermediário com análise preditiva' as descricao, 'ATIVO' as status FROM DUAL) src
ON (p.nome = src.nome)
WHEN NOT MATCHED THEN
  INSERT (id, nome, valor_mensalidade, descricao, status)
  VALUES (SQ_PLANO.NEXTVAL, src.nome, src.valor_mensalidade, src.descricao, src.status);

MERGE INTO PLANO p
USING (SELECT 'Avançado' as nome, 150.0 as valor_mensalidade, 'Plano avançado com integração IOT e IA completa' as descricao, 'ATIVO' as status FROM DUAL) src
ON (p.nome = src.nome)
WHEN NOT MATCHED THEN
  INSERT (id, nome, valor_mensalidade, descricao, status)
  VALUES (SQ_PLANO.NEXTVAL, src.nome, src.valor_mensalidade, src.descricao, src.status);

-- Inserindo Produtor Inicial se não existir (senha: senha123)
MERGE INTO PRODUTOR p
USING (SELECT 'João Fazendeiro' as nome, '111.222.333-44' as cpf, 'joao@fazenda.com' as email, '11999999999' as telefone, '$2b$12$XwZXvtJkdzQeLPpFsF4wXuo52p1AlauNg1EJQZ1dYS/Come06Aqle' as senha, 'ATIVO' as status, CURRENT_TIMESTAMP as data_cadastro FROM DUAL) src
ON (p.email = src.email)
WHEN NOT MATCHED THEN
  INSERT (id, nome, cpf, email, telefone, senha, status, data_cadastro)
  VALUES (SQ_PRODUTOR.NEXTVAL, src.nome, src.cpf, src.email, src.telefone, src.senha, src.status, src.data_cadastro);

-- Inserindo Cooperativa se não existir
MERGE INTO COOPERATIVA c
USING (SELECT 'Cooperativa Central Agro' as nome, 'contato@coopagro.com' as email, '11988888888' as telefone, 'ATIVO' as status, CURRENT_TIMESTAMP as data_cadastro FROM DUAL) src
ON (c.email = src.email)
WHEN NOT MATCHED THEN
  INSERT (id, nome, email, telefone, status, data_cadastro)
  VALUES (SQ_COOPERATIVA.NEXTVAL, src.nome, src.email, src.telefone, src.status, src.data_cadastro);

-- Vinculando Produtor à Cooperativa
MERGE INTO PRODUTOR_COOPERATIVA pc
USING (SELECT (SELECT id FROM PRODUTOR WHERE email = 'joao@fazenda.com') as produtor_id, (SELECT id FROM COOPERATIVA WHERE email = 'contato@coopagro.com') as cooperativa_id FROM DUAL) src
ON (pc.produtor_id = src.produtor_id AND pc.cooperativa_id = src.cooperativa_id)
WHEN NOT MATCHED THEN
  INSERT (produtor_id, cooperativa_id)
  VALUES (src.produtor_id, src.cooperativa_id);

-- Inserindo Propriedade vinculada ao Produtor e Plano Avançado
MERGE INTO PROPRIEDADE p
USING (
  SELECT 'Fazenda Esperança' as nome, 50.5 as area_hectares, 'ATIVO' as status, CURRENT_TIMESTAMP as data_cadastro,
         -23.5505 as latitude, -46.6333 as longitude, 'São Paulo' as cidade, 'SP' as estado
  FROM DUAL
) src
ON (p.nome = src.nome)
WHEN NOT MATCHED THEN
  INSERT (id, produtor_id, plano_id, nome, area_hectares, status, data_cadastro, latitude, longitude, cidade, estado)
  VALUES (SQ_PROPRIEDADE.NEXTVAL, (SELECT id FROM PRODUTOR WHERE email = 'joao@fazenda.com'), (SELECT id FROM PLANO WHERE nome = 'Avançado'), src.nome, src.area_hectares, src.status, src.data_cadastro, src.latitude, src.longitude, src.cidade, src.estado);

-- Inserindo Dispositivo IOT vinculado à Propriedade (MAC Address do João)
MERGE INTO DISPOSITIVO_IOT d
USING (
  SELECT 'ESP32 Hydrata PRO' as modelo, 'AA:BB:CC:DD:EE:FF' as mac_address, 'ATIVO' as status, CURRENT_TIMESTAMP as data_cadastro
  FROM DUAL
) src
ON (d.mac_address = src.mac_address)
WHEN NOT MATCHED THEN
  INSERT (id, propriedade_id, modelo, mac_address, status, data_cadastro)
  VALUES (SQ_DISPOSITIVO_IOT.NEXTVAL, (SELECT id FROM PROPRIEDADE WHERE nome = 'Fazenda Esperança'), src.modelo, src.mac_address, src.status, src.data_cadastro);
