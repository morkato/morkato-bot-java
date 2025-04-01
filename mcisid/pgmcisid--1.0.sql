CREATE TYPE mcisidv1;

CREATE FUNCTION mcisidv1_type_in(CSTRING) RETURNS mcisidv1 AS 'MODULE_PATHNAME', 'pgmcisidTypeInput' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_type_out(mcisidv1) RETURNS CSTRING AS 'MODULE_PATHNAME', 'pgmcisidTypeOutput' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_gen(SMALLINT) RETURNS mcisidv1 AS 'MODULE_PATHNAME', 'pgmcisidv1Generate' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_get_instant(mcisidv1) RETURNS BIGINT AS 'MODULE_PATHNAME', 'pgmcisidv1GetInstant' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_created_at(mcisidv1) RETURNS TIMESTAMP AS 'MODULE_PATHNAME', 'pgmcisidv1CreatedAt' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_origin_model(mcisidv1) RETURNS SMALLINT AS 'MODULE_PATHNAME', 'pgmcisidv1OriginModel' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_instant_sequence(mcisidv1) RETURNS BIGINT AS 'MODULE_PATHNAME', 'pgmcisidv1InstantSequence' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_get_epoch() RETURNS TIMESTAMP AS 'MODULE_PATHNAME', 'pgmcisidv1GetEpoch' LANGUAGE C STRICT;

CREATE TYPE mcisidv1 (
  INPUT = mcisidv1_type_in,
  OUTPUT = mcisidv1_type_out,
  INTERNALLENGTH = 12,
  ALIGNMENT = char,
  STORAGE = plain
)