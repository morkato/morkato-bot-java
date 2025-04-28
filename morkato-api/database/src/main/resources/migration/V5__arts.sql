-- V3 CREATE TABLE ARTS
CREATE DOMAIN art_type AS TEXT CHECK (VALUE ~ '^RESPIRATION|KEKKIJUTSU|FIGHTING_STYLE$');
CREATE FUNCTION mcisidv1_artid_gen() RETURNS mcisidv1 AS $$
  SELECT mcisidv1_gen(15::smallint);
$$ LANGUAGE SQL STABLE STRICT;
CREATE TABLE "arts" (
  "name" name_type NOT NULL,
  "key" key_type NOT NULL,
  "guild_id" discord_id_type NOT NULL,
  "id" mcisidv1 NOT NULL DEFAULT mcisidv1_artid_gen(),
  "type" art_type NOT NULL,
  "description" description_type DEFAULT NULL,
  "banner" banner_type DEFAULT NULL
);
ALTER TABLE "arts"
  ADD CONSTRAINT "art.pkey" PRIMARY KEY ("id");
ALTER TABLE "arts"
  ADD CONSTRAINT "art.key" UNIQUE ("guild_id","key");
ALTER TABLE "arts"
  ADD CONSTRAINT "art.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
CREATE INDEX "art_index.pkey" ON "arts" USING BTREE ("id" mcisidv1_ops);