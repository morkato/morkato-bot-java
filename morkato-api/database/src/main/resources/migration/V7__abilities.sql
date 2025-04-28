CREATE FUNCTION mcisidv1_abilityid_gen() RETURNS mcisidv1 AS $$
  SELECT mcisidv1_gen(17::smallint);
$$ LANGUAGE SQL STABLE STRICT;
CREATE TABLE "abilities" (
  "name" name_type NOT NULL,
  "key" key_type NOT NULL,
  "id" mcisidv1 NOT NULL DEFAULT mcisidv1_abilityid_gen(),
  "rpg_id" mcisidv1 NOT NULL,
  "percent" percent_type NOT NULL DEFAULT 0,
  "user_type" INTEGER NOT NULL DEFAULT 0,
  "description" description_type DEFAULT NULL,
  "banner" banner_type DEFAULT NULL
);

ALTER TABLE "abilities"
  ADD CONSTRAINT "ability.pkey" PRIMARY KEY ("id");
ALTER TABLE "abilities"
  ADD CONSTRAINT "ability.key" UNIQUE ("rpg_id","key");
ALTER TABLE "abilities"
  ADD CONSTRAINT "ability.rpg" FOREIGN KEY ("rpg_id") REFERENCES "mcrpgs"("id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;