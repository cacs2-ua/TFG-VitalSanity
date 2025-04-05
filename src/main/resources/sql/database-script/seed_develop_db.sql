DELETE FROM pacientes;
DELETE FROM usuarios;
DELETE FROM tipos_usuario;


INSERT INTO public.tipos_usuario (id, tipo) VALUES (1, 'administrador');
INSERT INTO public.tipos_usuario (id, tipo) VALUES (2, 'centro_medico');
INSERT INTO public.tipos_usuario (id, tipo) VALUES (3, 'profesional_medico');
INSERT INTO public.tipos_usuario (id, tipo) VALUES (4, 'paciente');

INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, provincia, telefono, tipo_id) VALUES (1, 'b7947100-9226-41c0-af83-2ba6c0c71962', true, '03690', 'fb001dfcffd1c899f3297871406242f097aecf1a5342ccf3ebcd116146188e4b', 'admin@gmail.com', 'San Vicente del Raspeig', '54083179J', 'admin', 'España', 'Alicante', '679153147', 1);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, provincia, telefono, tipo_id) VALUES (2, 'cab22d62-4ae4-4281-ba98-241cb957cc73', true, '03690', 'dd9c62623e886a38406cd5845880bccd4462b5dc733cee418e61e36f868d02ac', 'centro-medico-default@gmail.com', 'San Vicente del Raspeig', '17205375H', 'centro-medico-default', 'España', 'Alicante', '691826817', 2);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, provincia, telefono, tipo_id) VALUES (3, '107cdfc6-c371-4a96-baa0-67eadcd5581e', true, '03690', '66b7b2fb2817602544bb3fd64e5f191dc8444931f69db7be0f33fa789eb43195', 'profesional-medico-default@gmail.com', 'San Vicente del Raspeig', 'Y4991437Z', 'profesional-medico-default', 'España', 'Alicante', '691836271', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, provincia, telefono, tipo_id) VALUES (4, '73c0a524-f567-460b-9294-1936d57d05db', true, '03690', '65b0d3dde9bc1dfd609b3fc65c26ab04da8e11791574902f2a6a6d269ba5d5e1', 'paciente-default@gmail.com', 'San Vicente del Raspeig', '60704841K', 'paciente-default', 'España', 'Alicante', '691825671', 4);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, provincia, telefono, tipo_id) VALUES (5, 'b8eb426c-46b3-427e-882b-eb03f186563b', true, '03690', 'fa68ec85b87520f6c662bc49554edc4f99a42b4645c0f99035106fa2f3504273', 'cacs2@alu.ua.es', 'San Vicente del Raspeig', '05988721G', 'Cristian Andrés Córdoba Silvestre', 'España', 'Alicante', '691996241', 3);



SELECT setval('public.usuarios_id_seq', COALESCE((SELECT MAX(id) FROM public.usuarios), 0) + 1, false);
SELECT setval('public.tipos_usuario_id_seq', COALESCE((SELECT MAX(id) FROM public.tipos_usuario), 0) + 1, false);
