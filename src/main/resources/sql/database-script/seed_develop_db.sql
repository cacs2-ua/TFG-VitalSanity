DELETE FROM documentos;
DELETE FROM solicitudes_autorizacion;
DELETE FROM profesionales_medicos;
DELETE FROM especialidades_medicas;
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
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (4, '73c0a524-f567-460b-9294-1936d57d05db', true, '03690', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'paciente-default@gmail.com', 'San Vicente del Raspeig', '60704841K', 'paciente-default', 'España', false, 'Alicante', '691825671', 4);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (5, 'b8eb426c-46b3-427e-882b-eb03f186563b', true, '03690', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'cacs2@alu.ua.es', 'San Vicente del Raspeig', '05988721G', 'Cristian Andrés Córdoba Silvestre', 'España', false, 'Alicante', '691996241', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (6, '9b704e4b-0c85-4de8-a1aa-3562e49cbb20', true, '13500', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'second-paciente-default@gmail.com', 'Puertollano', '87952828T', 'second-paciente-default', 'España', false, 'Ciudad Real', '681965234', 4);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (7, 'd2289317-ecaa-43f9-b9c5-d75cc3dbdc7f', true, '03764', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'adeslas@example.com', 'Alicante', '23938402W', 'Centro Medico Adeslas', 'Espana', false, 'Alicante', '+34 952137683', 2);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (8, '1ddfed57-6ce1-4d95-96ec-23c9b88c2ccb', true, '28001', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'juan.perez@example.com', 'Madrid', '63297648S', 'Juan Perez', 'Espana', false, 'Madrid', '600111222', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (9, '048a8262-e330-49b2-a8d6-0ff1ec915203', true, '08001', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'maria.lopez@example.com', 'Barcelona', '87654321X', 'Maria Lopez', 'Espana', false, 'Barcelona', '600333444', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (10, '4f9fffd9-05de-41e9-83ff-9491777062bc', true, '41001', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'carlos.garcia@example.com', 'Sevilla', '11223344Y', 'Carlos Garcia', 'Espana', false, 'Sevilla', '600555666', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (11, 'd5ebe218-fa99-4c8b-9d0a-621c76056b20', true, '349002', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'monica.garcia@example.com', 'Madrid', '12345678Z', 'Monica Garcia Ripoll', 'Espana', false, 'Madrid', '678146781', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (12, 'b75174dd-bba0-456f-9506-8c2d40db41bc', true, '076514', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'manuel.gimenez@example.com', 'Alicante', 'X0655490J', 'Manuel Gimenez Rivilla', 'Espana', false, 'Alicante', '714523481', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (13, 'bfe5a48e-7a60-4378-8e98-40e326a32f2c', true, '62009', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'laura.hernandez@example.com', 'Granada', '48733805W', 'Laura Hernandez Diaz', 'Espana', false, 'Granada', '648798176', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (14, '63cfd087-10d5-4c34-b01c-3192e98505d3', true, '87476', 'f1cfdca558ac0c00464ca0f3e265ec6fb32c57caeb106fbfed9f174f6b814642', 'pablo.rodriguez@example.com', 'Murcia', '45908922W', 'Pablo Rodriguez Carrion', 'Espana', false, 'Murcia', '751438909', 3);
INSERT INTO public.usuarios (id, uuid, activado, codigo_postal, contrasenya, email, municipio, nif_nie, nombre_completo, pais, primer_acceso, provincia, telefono, tipo_id) VALUES (15, 'a8801c7d-3f0d-4c46-8b5d-2896ccc26aad', true, 'a', '82105df9168605b105a2a5b5bc2d19698b9be31fab9cad10c26dc186e9e0af03', 'b@gmail.com', 'a', '60704841K', 'a', 'a', false, 'aa', 'a', 4);



INSERT INTO public.centros_medicos (id, direccion, iban, usuario_id) VALUES (1, 'Calle Pablo Iglesias', 'ES3900168167502018540968', 2);
INSERT INTO public.centros_medicos (id, direccion, iban, usuario_id) VALUES (2, 'Calle Pablo Iglesias Nº27', 'ES1630177794343116396906', 7);


INSERT INTO public.especialidades_medicas (id, nombre) VALUES (1, 'Psicologia');
INSERT INTO public.especialidades_medicas (id, nombre) VALUES (2, 'Dermatologia');
INSERT INTO public.especialidades_medicas (id, nombre) VALUES (3, 'Fisioterapia');
INSERT INTO public.especialidades_medicas (id, nombre) VALUES (4, 'Urologia');
INSERT INTO public.especialidades_medicas (id, nombre) VALUES (5, 'Psiquiatria');
INSERT INTO public.especialidades_medicas (id, nombre) VALUES (6, 'Medicina General');
INSERT INTO public.especialidades_medicas (id, nombre) VALUES (7, 'Reumatologia');
INSERT INTO public.especialidades_medicas (id, nombre) VALUES (8, 'Traumatologia');



INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (1, 'a', '2003-02-04', 'hombre', 'a', 'a', 1, 1, 5);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (2, 'CCC001', '1980-05-15', 'Masculino', 'ES7620770024003102575766', 'NAF123456', 1, 2, 8);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (3, 'CCC002', '1985-08-22', 'Femenino', 'ES9121000418450200051332', 'NAF654321', 1, 3, 9);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (4, 'CCC003', '1975-12-05', 'Masculino', 'ES7921000813610123456789', 'NAF987654', 1, 4, 10);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (5, 'CCC004', '1983-04-19', 'Femenino', 'ES1702370421294954610030', 'NAF871468', 2, 5, 11);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (6, 'CCC005', '1990-03-12', 'Masculino', 'ES4000563849819906926808', 'NAF381905', 2, 2, 12);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (7, 'CCC006', '1972-11-04', 'Femenino', 'ES4001902194314053342469', 'NAF976142', 2, 6, 13);
INSERT INTO public.profesionales_medicos (id, ccc, fecha_nacimiento, genero, iban, naf, centro_medico_id, especialidad_medica_id, usuario_id) VALUES (8, 'CCC007', '1970-08-09', 'Masculino', 'ES8430242743684355262412', 'NAF471570', 2, 7, 14);




INSERT INTO public.pacientes (id, fecha_nacimiento, genero, usuario_id) VALUES (1, '2000-04-11', 'hombre', 4);
INSERT INTO public.pacientes (id, fecha_nacimiento, genero, usuario_id) VALUES (2, '2001-07-21', 'mujer', 6);


INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (1, false, false, '16576', '2025-04-13 01:12:42.575547', true, 'Evaluación psicológica integral para diagnóstico de trastornos emocionales y conductuales', '60704841K', '05988721G', 'centro-medico-default', 'paciente-default', 'Cristian Andrés Córdoba Silvestre', 1, 1);
INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (2, false, false, '16578', '2025-04-13 01:16:26.832760', true, 'Revisión integral del historial en salud mental', '60704841K', '12345678Z', 'Centro Medico Adeslas', 'paciente-default', 'Monica Garcia Ripoll', 1, 5);
INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (3, false, false, '16580', '2025-04-13 01:19:32.713517', true, 'Evaluación integral de afecciones cutáneas', '60704841K', '12345678Z', 'centro-medico-default', 'paciente-default', 'Juan Perez', 1, 2);
INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (4, false, false, '16582', '2025-04-13 01:20:53.111870', true, 'Seguimiento y evaluación de lesiones pigmentadas', '60704841K', 'X0655490J', 'Centro Medico Adeslas', 'paciente-default', 'Manuel Gimenez Rivilla', 1, 6);
INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (5, false, false, '16584', '2025-04-13 01:23:18.558104', true, 'Consulta de rehabilitación y seguimiento post-traumático', '87952828T', '87654321X', 'centro-medico-default', 'second-paciente-default', 'Maria Lopez', 2, 3);
INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (6, false, false, '16586', '2025-04-13 01:24:33.942252', true, 'Revisión de antecedentes reumáticos y evaluaciones complementarias', '87952828T', '45908922W', 'Centro Medico Adeslas', 'second-paciente-default', 'Pablo Rodriguez Carrion', 2, 8);
INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (7, false, false, '16588', '2025-04-13 01:26:01.452516', true, 'Evaluación de historia clínica para atención integral', '87952828T', '48733805W', 'Centro Medico Adeslas', 'second-paciente-default', 'Laura Hernandez Diaz', 2, 7);
INSERT INTO public.solicitudes_autorizacion (id, cofirmada, denegada, descripcion, fecha_creacion, firmada, motivo, nif_nie_paciente, nif_nie_profesional_medico, nombre_centro_medico, nombre_paciente, nombre_profesional_medico, paciente_id, profesional_medico_id) VALUES (8, false, false, '16590', '2025-04-13 01:28:50.666923', true, 'Análisis de antecedentes urológicos y procedimentales', '87952828T', '11223344Y', 'centro-medico-default', 'second-paciente-default', 'Carlos Garcia', 2, 4);


INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (1, '2025-04-13 01:12:52.759884', 'b8eb426c-46b3-427e-882b-eb03f186563b_73c0a524-f567-460b-9294-1936d57d05db_1744499572758.pdf', 'autorizaciones/b8eb426c-46b3-427e-882b-eb03f186563b_73c0a524-f567-460b-9294-1936d57d05db_1744499571472.pdf', 56061, 'application/pdf', null, 1);
INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (2, '2025-04-13 01:16:37.599519', 'd5ebe218-fa99-4c8b-9d0a-621c76056b20_73c0a524-f567-460b-9294-1936d57d05db_1744499797599.pdf', 'autorizaciones/d5ebe218-fa99-4c8b-9d0a-621c76056b20_73c0a524-f567-460b-9294-1936d57d05db_1744499796765.pdf', 56002, 'application/pdf', null, 2);
INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (3, '2025-04-13 01:19:46.541591', '1ddfed57-6ce1-4d95-96ec-23c9b88c2ccb_73c0a524-f567-460b-9294-1936d57d05db_1744499986541.pdf', 'autorizaciones/1ddfed57-6ce1-4d95-96ec-23c9b88c2ccb_73c0a524-f567-460b-9294-1936d57d05db_1744499985885.pdf', 55971, 'application/pdf', null, 3);
INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (4, '2025-04-13 01:21:08.153107', 'b75174dd-bba0-456f-9506-8c2d40db41bc_73c0a524-f567-460b-9294-1936d57d05db_1744500068153.pdf', 'autorizaciones/b75174dd-bba0-456f-9506-8c2d40db41bc_73c0a524-f567-460b-9294-1936d57d05db_1744500067349.pdf', 56022, 'application/pdf', null, 4);
INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (5, '2025-04-13 01:23:29.372329', '048a8262-e330-49b2-a8d6-0ff1ec915203_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500209372.pdf', 'autorizaciones/048a8262-e330-49b2-a8d6-0ff1ec915203_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500208603.pdf', 55969, 'application/pdf', null, 5);
INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (6, '2025-04-13 01:24:50.885793', '63cfd087-10d5-4c34-b01c-3192e98505d3_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500290885.pdf', 'autorizaciones/63cfd087-10d5-4c34-b01c-3192e98505d3_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500290003.pdf', 56040, 'application/pdf', null, 6);
INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (7, '2025-04-13 01:26:17.311610', 'bfe5a48e-7a60-4378-8e98-40e326a32f2c_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500377311.pdf', 'autorizaciones/bfe5a48e-7a60-4378-8e98-40e326a32f2c_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500376614.pdf', 56018, 'application/pdf', null, 7);
INSERT INTO public.documentos (id, fecha_subida, nombre, s3_key, tamanyo, tipo_archivo, informe_id, solicitud_autorizacion_id) VALUES (8, '2025-04-13 01:28:59.851555', '4f9fffd9-05de-41e9-83ff-9491777062bc_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500539851.pdf', 'autorizaciones/4f9fffd9-05de-41e9-83ff-9491777062bc_9b704e4b-0c85-4de8-a1aa-3562e49cbb20_1744500538945.pdf', 55994, 'application/pdf', null, 8);



/* Autorizaciones */

SELECT setval('public.documentos_id_seq', COALESCE((SELECT MAX(id) FROM public.documentos), 0) + 1, false);
SELECT setval('public.solicitudes_autorizacion_id_seq', COALESCE((SELECT MAX(id) FROM public.solicitudes_autorizacion), 0) + 1, false);
SELECT setval('public.profesionales_medicos_id_seq', COALESCE((SELECT MAX(id) FROM public.profesionales_medicos), 0) + 1, false);
SELECT setval('public.especialidades_medicas_id_seq', COALESCE((SELECT MAX(id) FROM public.especialidades_medicas), 0) + 1, false);
SELECT setval('public.centros_medicos_id_seq', COALESCE((SELECT MAX(id) FROM public.centros_medicos), 0) + 1, false);
SELECT setval('public.pacientes_id_seq', COALESCE((SELECT MAX(id) FROM public.pacientes), 0) + 1, false);
SELECT setval('public.usuarios_id_seq', COALESCE((SELECT MAX(id) FROM public.usuarios), 0) + 1, false);
SELECT setval('public.tipos_usuario_id_seq', COALESCE((SELECT MAX(id) FROM public.tipos_usuario), 0) + 1, false);

