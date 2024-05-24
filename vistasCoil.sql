CREATE VIEW iF not exists universidad_con_pais AS 
SELECT idUniversidad, universidad.nombre AS universidad, idPais, pais.nombre AS pais FROM universidad LEFT JOIN pais ON universidad.paisOrigen = pais.idPais;

CREATE VIEW if not exists facultad_con_region AS
SELECT idFacultad, facultad.nombre AS facultad, idRegion, region.nombre AS region FROM facultad LEFT JOIN region ON facultad.region = region.idRegion;

DROP VIEW if exists vista_academico;
CREATE VIEW if not exists vista_academico AS
SELECT 
    p.idPersona AS idPersona,
    p.nombre AS nombre,
    p.apellidoPaterno AS apellidoPaterno,
    p.apellidoMaterno AS apellidoMaterno,
    p.universidad AS idUniversidad,
    u.nombre AS nombreUniversidad,
    u.paisOrigen AS paisOrigen,
    a.cedulaProfesional AS cedulaProfesional,
    a.numeroDePersonal AS numeroDePersonal,
    a.areaEstudios AS areaEstudios,
    a.correoElectronico AS correoElectronico,
    a.numeroTelefonico AS numeroTelefonico,
    a.categoriaContratacion AS categoriaContratacion,
    a.facultad AS idFacultad,
    f.nombre AS nombreFacultad,
    r.idRegion AS idRegion,
    r.nombre AS nombreRegion
FROM 
    persona p
    JOIN academico a ON p.idPersona = a.idPersona
    LEFT JOIN facultad f ON f.idFacultad = a.facultad
    LEFT JOIN region r ON r.idRegion = f.region
    JOIN universidad u ON u.idUniversidad = p.universidad;



CREATE VIEW if not exists vista_estudiante AS
SELECT 
    p.idPersona AS idPersona,
    p.nombre AS nombre,
    p.apellidoPaterno AS apellidoPaterno,
    p.apellidoMaterno AS apellidoMaterno,
    p.universidad AS universidad,
    e.idEstudiante AS idEstudiante,
    e.matricula AS matricula
FROM 
    persona p
    JOIN estudiante e ON p.idPersona = e.idPersona;


CREATE VIEW if not exists vista_cuenta AS
SELECT
    c.idCuenta AS idCuenta,
    c.idPersona AS idPersona,
    c.nombreUsuario AS nombreUsuario,
    c.tipo AS tipo,
    c.contrasena AS contrasena,
    c.estado AS estado,
    p.nombre AS nombre,
    p.apellidoPaterno AS apellidoPaterno,
    p.apellidoMaterno AS apellidoMaterno,
    p.universidad AS idUniversidad,
    u.nombre AS nombreUniversidad,
    u.paisOrigen AS idPais,
    pa.nombre AS nombrePais
FROM
    cuenta c
    JOIN persona p ON p.idPersona = c.idPersona
    JOIN universidad u ON u.idUniversidad = p.universidad
    JOIN pais pa ON pa.idPais = u.paisOrigen;


CREATE VIEW if NOT EXISTS numeralia AS 
SELECT 
	ad.idColaboracion, 
	areaEstudios AS areaAcademica, 
	nombreFacultad AS facultad, 
	nombreRegion AS region, 
	fechaFin, 
	COUNT(idEstudiante) AS alumnosUvTotales
FROM
	 academicoDesarrolla AS ad 
	LEFT JOIN vista_academico AS va
	ON ad.idAcademico = va.cedulaProfesional
	LEFT JOIN colaboracion AS c
	ON ad.idColaboracion = c.idColaboracion
	LEFT JOIN estudiantescolaboracion AS ec
	ON ad.idColaboracion = ec.idColaboracion
WHERE 
	va.nombreUniversidad = 'Universidad Veracruzana' 
	AND (ad.estado = 'anfitrion' OR ad.estado = 'aceptado')
	AND idEstudiante IN 
	(SELECT idEstudiante FROM vista_estudiante WHERE universidad IN (SELECT universidad.idUniversidad FROM universidad WHERE nombre = 'Universidad Veracruzana'))
	AND ad.idColaboracion IN (SELECT colaboracion.idColaboracion FROM colaboracion WHERE estado = 'finalizada') 
	GROUP BY idColaboracion;



CREATE VIEW if NOT EXISTS colaboraciones_uv_finalizadas AS 
SELECT 
	ad.idColaboracion, 
	cedulaProfesional, 
	ad.estado, 
	nombre, 
	nombreUniversidad, 
	areaEstudios, 
	nombreFacultad, 
	nombreRegion, 
	fechaFin, 
	COUNT(idEstudiante) AS alumnosUvTotales
FROM 
	academicodesarrolla AS ad 
	LEFT JOIN vista_academico AS va
	ON ad.idAcademico = va.cedulaProfesional
	LEFT JOIN colaboracion AS c
	ON ad.idColaboracion = c.idColaboracion
	LEFT JOIN estudiantescolaboracion AS ec
	ON ad.idColaboracion = ec.idColaboracion
WHERE 
	va.nombreUniversidad = 'Universidad Veracruzana' 
	AND (ad.estado = 'anfitrion' OR ad.estado = 'aceptado')
	AND idEstudiante IN (SELECT idEstudiante FROM vista_estudiante WHERE universidad IN (SELECT universidad.idUniversidad FROM universidad WHERE nombre = 'Universidad Veracruzana'))
	AND ad.idColaboracion IN (SELECT colaboracion.idColaboracion FROM colaboracion WHERE estado = 'finalizada') 
	GROUP BY idColaboracion;

CREATE VIEW if not exists vista_colaboracion_con_academico AS
SELECT 
    c.*,
    va.*,
    ad.estado AS estadoAcademico
FROM 
    colaboracion c
    INNER JOIN academicoDesarrolla ad ON c.idColaboracion = ad.idColaboracion
    INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional;
