CREATE VIEW universidad_con_pais AS 
SELECT idUniversidad, universidad.nombre AS universidad, idPais, iso, paises.nombre AS pais FROM universidad LEFT JOIN paises ON universidad.paisOrigen = paises.idPais;
CREATE VIEW facultad_con_region AS
SELECT idFacultad, facultad.nombre AS facultad, idRegion, region.nombre AS region FROM facultad LEFT JOIN region ON facultad.region = region.idRegion;