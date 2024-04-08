
CREATE VIEW if not exists universidad_con_pais AS 
SELECT idUniversidad, universidad.nombre AS universidad, idPais, pais.nombre AS pais FROM universidad LEFT JOIN pais ON universidad.paisOrigen = pais.idPais;
CREATE VIEW if not exists facultad_con_region AS
SELECT idFacultad, facultad.nombre AS facultad, idRegion, region.nombre AS region FROM facultad LEFT JOIN region ON facultad.region = region.idRegion;
