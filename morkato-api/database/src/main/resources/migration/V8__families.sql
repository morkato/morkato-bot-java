CREATE FUNCTION mcisidv1_familyid_gen() RETURNS mcisidv1 AS $$
  SELECT mcisidv1_gen(18::smallint);
$$ LANGUAGE SQL STABLE STRICT;
CREATE TABLE "families" (
  "name" name_type NOT NULL,
  "key" key_type NOT NULL,
  "id" mcisidv1 NOT NULL DEFAULT mcisidv1_familyid_gen(),
  "rpg_id" mcisidv1 NOT NULL,
  "percent" percent_type NOT NULL DEFAULT 0,
  "user_type" INTEGER NOT NULL DEFAULT 0,
  "description" description_type DEFAULT NULL,
  "banner" banner_type DEFAULT NULL
);

ALTER TABLE "families"
  ADD CONSTRAINT "family.pkey" PRIMARY KEY ("id");
ALTER TABLE "families"
  ADD CONSTRAINT "family.key" UNIQUE ("rpg_id","key");
ALTER TABLE "families"
  ADD CONSTRAINT "family.rpg" FOREIGN KEY ("rpg_id") REFERENCES "mcrpgs"("id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;