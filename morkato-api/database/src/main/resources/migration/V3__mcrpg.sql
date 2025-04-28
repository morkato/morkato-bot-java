CREATE FUNCTION mcisidv1_mcrpgid_gen() RETURNS mcisidv1 AS $$
  SELECT mcisidv1_gen(1::smallint);
$$ LANGUAGE SQL STABLE STRICT;
CREATE TABLE "mcrpgs" (
  "id" mcisidv1 NOT NULL DEFAULT mcisidv1_mcrpgid_gen(),
  "human_initial_life" attr_type NOT NULL DEFAULT 1000,
  "oni_initial_life" attr_type NOT NULL DEFAULT 500,
  "hybrid_initial_life" attr_type NOT NULL DEFAULT 1500,
  "breath_initial" attr_type NOT NULL DEFAULT 500,
  "blood_initial" attr_type NOT NULL DEFAULT 1000,
  "ability_roll" roll_type NOT NULL DEFAULT 3,
  "family_roll" roll_type NOT NULL DEFAULT 3
);
ALTER TABLE "mcrpgs"
  ADD CONSTRAINT "mcrpg.pkey" PRIMARY KEY ("id");
CREATE INDEX "mcrpg_index.pkey" ON "mcrpgs" USING BTREE ("id" mcisidv1_ops);