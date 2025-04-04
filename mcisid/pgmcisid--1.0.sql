CREATE TYPE mcisidv1;

CREATE DOMAIN mcisidv1_model AS INTEGER CHECK (VALUE >= 0 AND VALUE < 64);
CREATE DOMAIN mcisidv1_seq AS INTEGER CHECK (VALUE >= 0 AND VALUE <= 262143);

CREATE TABLE mcisidv1seq_store (
  "model" mcisidv1_model NOT NULL,
  "current" mcisidv1_seq NOT NULL,
  "lastime" BIGINT NOT NULL
);

CREATE FUNCTION mcisidv1_type_in(CSTRING) RETURNS mcisidv1 AS 'MODULE_PATHNAME', 'pgmcisidTypeInput' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_type_out(mcisidv1) RETURNS CSTRING AS 'MODULE_PATHNAME', 'pgmcisidTypeOutput' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_type_cmp(mcisidv1, mcisidv1) RETURNS INTEGER AS 'MODULE_PATHNAME', 'pgmcisidv1cmp' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_type_lt(mcisidv1, mcisidv1) RETURNS BOOLEAN AS 'MODULE_PATHNAME', 'pgmcisidv1lt' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_type_eq(mcisidv1, mcisidv1) RETURNS BOOLEAN AS 'MODULE_PATHNAME', 'pgmcisidv1eq' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_type_gt(mcisidv1, mcisidv1) RETURNS BOOLEAN AS 'MODULE_PATHNAME', 'pgmcisidv1gt' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_gen(SMALLINT) RETURNS mcisidv1 AS 'MODULE_PATHNAME', 'pgmcisidv1Generate' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_get_instant(mcisidv1) RETURNS BIGINT AS 'MODULE_PATHNAME', 'pgmcisidv1GetInstant' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_created_at(mcisidv1) RETURNS TIMESTAMP AS 'MODULE_PATHNAME', 'pgmcisidv1CreatedAt' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_origin_model(mcisidv1) RETURNS SMALLINT AS 'MODULE_PATHNAME', 'pgmcisidv1OriginModel' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_instant_sequence(mcisidv1) RETURNS BIGINT AS 'MODULE_PATHNAME', 'pgmcisidv1InstantSequence' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_get_epoch() RETURNS TIMESTAMP AS 'MODULE_PATHNAME', 'pgmcisidv1GetEpoch' LANGUAGE C STRICT;
CREATE FUNCTION mcisidv1_curval(regclass) RETURNS TIMESTAMP AS 'MODULE_PATHNAME', 'pgmcisidv1CurrentValue' LANGUAGE C STRICT;

CREATE TYPE mcisidv1 (
  INPUT = mcisidv1_type_in,
  OUTPUT = mcisidv1_type_out,
  INTERNALLENGTH = 12,
  ALIGNMENT = char,
  STORAGE = plain
);

CREATE OPERATOR < (
  LEFTARG = mcisidv1,
  RIGHTARG = mcisidv1,
  PROCEDURE = mcisidv1_type_lt
);

CREATE OPERATOR = (
  LEFTARG = mcisidv1,
  RIGHTARG = mcisidv1,
  PROCEDURE = mcisidv1_type_eq
);

CREATE OPERATOR > (
  LEFTARG = mcisidv1,
  RIGHTARG = mcisidv1,
  PROCEDURE = mcisidv1_type_gt
);

CREATE OPERATOR CLASS mcisidv1_ops
  DEFAULT FOR TYPE mcisidv1 USING BTREE AS
  OPERATOR 1 < (mcisidv1, mcisidv1),
  OPERATOR 2 = (mcisidv1, mcisidv1),
  OPERATOR 3 > (mcisidv1, mcisidv1),
  FUNCTION 1 mcisidv1_type_cmp(mcisidv1, mcisidv1);