DELETE FROM documentos;
DELETE FROM solicitudes_autorizacion;
DELETE FROM profesionales_medicos;
DELETE FROM centros_medicos;
DELETE FROM pacientes;
DELETE FROM usuarios;
DELETE FROM tipos_usuario;


INSERT INTO public.tipos_usuario (id, tipo) VALUES (1, 'administrador');
INSERT INTO public.tipos_usuario (id, tipo) VALUES (2, 'centro_medico');
INSERT INTO public.tipos_usuario (id, tipo) VALUES (3, 'profesional_medico');
INSERT INTO public.tipos_usuario (id, tipo) VALUES (4, 'paciente');

INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (1, 'b7947100-9226-41c0-af83-2ba6c0c71962', true, '03690', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'admin@gmail.com', 'San Vicente del Raspeig', '54083179J', 'admin', 'España', false, 'Alicante', '679153147', 1);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (2, 'cab22d62-4ae4-4281-ba98-241cb957cc73', true, '03690', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'centro-medico-default@gmail.com', 'San Vicente del Raspeig', '17205375H', 'centro-medico-default', 'España', false, 'Alicante', '691826817', 2);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (3, '107cdfc6-c371-4a96-baa0-67eadcd5581e', true, '03690', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'profesional-medico-default@gmail.com', 'San Vicente del Raspeig', 'Y4991437Z', 'profesional-medico-default', 'España', false, 'Alicante', '691836271', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (4, '73c0a524-f567-460b-9294-1936d57d05db', true, '03690', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'paciente-default@gmail.com', 'San Vicente del Raspeig', '60704841K', 'paciente-default', 'España', false, 'Alicante', '691825671', 4);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (5, 'b8eb426c-46b3-427e-882b-eb03f186563b', true, '03690', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'cacs2@alu.ua.es', 'San Vicente del Raspeig', '05988721G', 'Cristian Andrés Córdoba Silvestre', 'España', false, 'Alicante', '691996241', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (6, 'ff87e117-a1ee-4821-9837-051f0ba33f92', true, '28001', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'juan.perez@example.com', 'Madrid', '12345678Z', 'Juan Perez', 'Espana', false, 'Madrid', '600111222', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (7, '45848ec1-d104-4f59-83d7-e2ef8e93967b', true, '08001', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'maria.lopez@example.com', 'Barcelona', '87654321X', 'Maria Lopez', 'Espana', false, 'Barcelona', '600333444', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (8, 'bf53f31d-87c7-4319-9bc0-f14296eb2033', true, '41001', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'carlos.garcia@example.com', 'Sevilla', '11223344Y', 'Carlos Garcia', 'Espana', false, 'Sevilla', '600555666', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (9, 'd2289317-ecaa-43f9-b9c5-d75cc3dbdc7f', true, '03764', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'adeslas@example.com', 'Alicante', '23938402W', 'Centro Medico Adeslas', 'Espana', false, 'Alicante', '+34 952137683', 2);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (10, 'f424eca8-8447-404b-a907-48c6fe861eaf', true, '349002', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'monica.garcia@example.com', 'Madrid', '12345678Z', 'Monica Garcia Ripoll', 'Espana', false, 'Madrid', '678146781', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (11, 'eec5903a-d51a-4b93-b58e-4efb97fd0d70', true, '076514', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'manuel.gimenez@example.com', 'Alicante', 'X0655490J', 'Manuel Gimenez Rivilla', 'Espana', false, 'Alicante', '714523481', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (12, 'f682cad0-f40d-4ace-9af0-e044dbef42c0', true, '62009', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'laura.hernandez@example.com', 'Granada', '48733805W', 'Laura Hernandez Diaz', 'Espana', false, 'Granada', '648798176', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (13, 'b4f01091-c2bf-4e06-b7ea-25f020354314', true, '87476', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'pablo.rodriguez@example.com', 'Murcia', '45908922W', 'Pablo Rodriguez Carrion', 'Espana', false, 'Murcia', '751438909', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (14, '9b704e4b-0c85-4de8-a1aa-3562e49cbb20', true, '13500', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'second-paciente-default@gmail.com', 'Puertollano', '87952828T', 'second-paciente-default', 'España', false, 'Ciudad Real', '681965234', 4);


INSERT INTO public.centros_medicos (id, direccion, iban, usuario_id) VALUES (1, 'Calle Pablo Iglesias', 'ES3900168167502018540968', 2);
INSERT INTO public.centros_medicos (id, direccion, iban, usuario_id) VALUES (2, 'Calle Pablo Iglesias Nº27', 'ES1630177794343116396906', 9);


INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (1, 'a', '2003-02-04', 'hombre', 'a', 'a', 1, 5);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (2, 'CCC001', '1980-05-15', 'Masculino', 'ES7620770024003102575766', 'NAF123456', 1, 6);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (3, 'CCC002', '1985-08-22', 'Femenino', 'ES9121000418450200051332', 'NAF654321', 1, 7);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (4, 'CCC003', '1975-12-05', 'Masculino', 'ES7921000813610123456789', 'NAF987654', 1, 8);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (5, 'CCC004', '1983-04-19', 'Femenino', 'ES1702370421294954610030', 'NAF871468', 2, 10);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (6, 'CCC005', '1990-03-12', 'Masculino', 'ES4000563849819906926808', 'NAF381905', 2, 11);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (7, 'CCC006', '1972-11-04', 'Femenino', 'ES4001902194314053342469', 'NAF976142', 2, 12);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, usuario_id) VALUES (8, 'CCC007', '1970-08-09', 'Masculino', 'ES8430242743684355262412', 'NAF471570', 2, 13);


INSERT INTO public.pacientes (id, fecha_nacimiento, genero, usuario_id) VALUES (1, '2000-04-11', 'hombre', 4);
INSERT INTO public.pacientes (id, fecha_nacimiento, genero, usuario_id) VALUES (2, '2001-07-21', 'mujer', 14);



/* Autorizaciones */

SELECT setval('public.documentos_id_seq', COALESCE((SELECT MAX(id) FROM public.documentos), 0) + 1, false);
SELECT setval('public.solicitudes_autorizacion_id_seq', COALESCE((SELECT MAX(id) FROM public.solicitudes_autorizacion), 0) + 1, false);
SELECT setval('public.profesionales_medicos_id_seq', COALESCE((SELECT MAX(id) FROM public.profesionales_medicos), 0) + 1, false);
SELECT setval('public.centros_medicos_id_seq', COALESCE((SELECT MAX(id) FROM public.centros_medicos), 0) + 1, false);
SELECT setval('public.pacientes_id_seq', COALESCE((SELECT MAX(id) FROM public.pacientes), 0) + 1, false);
SELECT setval('public.usuarios_id_seq', COALESCE((SELECT MAX(id) FROM public.usuarios), 0) + 1, false);
SELECT setval('public.tipos_usuario_id_seq', COALESCE((SELECT MAX(id) FROM public.tipos_usuario), 0) + 1, false);

