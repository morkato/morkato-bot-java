-- V4 CREATE TABLE ATTACKS
--  // Intents (attacks.flags): Inteções do ataque.

--  // 0: Nenhuma
--  // (1 << 1): Indesviável
--  // (1 << 2): Indefensável
--  // (1 << 3): Em área
--  // (1 << 4): Não contra atacável
--  // (1 << 5): Contra Ataque
--  // (1 << 6): Usável para defesa
--  // (1 << 7): Atributo Técnica
--  // (1 << 8): Glicínia
--  // (1 << 9): Atributo Exclusivo da Arte
CREATE DOMAIN name_prefix_art_type AS VARCHAR(32) CHECK (LENGTH(VALUE) >= 2 AND LENGTH(VALUE) <= 32 AND VALUE ~ '^[^\s].{1,31}$');
CREATE FUNCTION mcisidv1_attackid_gen() RETURNS mcisidv1 AS $$
  SELECT mcisidv1_gen(16::smallint);
$$ LANGUAGE SQL STABLE STRICT;
CREATE TABLE "attacks" (
  "name" name_type NOT NULL,
  "key" key_type NOT NULL,
  "id" mcisidv1 NOT NULL DEFAULT mcisidv1_attackid_gen(),
  "art_id" mcisidv1 NOT NULL,
  "name_prefix_art" name_prefix_art_type DEFAULT NULL,
  "description" description_type DEFAULT NULL,
  "banner" banner_type DEFAULT NULL,
  "damage" attr_type NOT NULL DEFAULT 0,
  "breath" attr_type NOT NULL DEFAULT 0,
  "blood" attr_type NOT NULL DEFAULT 0,
  "stun" attr_type NOT NULL DEFAULT 0,
  "flags" INTEGER NOT NULL DEFAULT 0
);
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.pkey" PRIMARY KEY ("id");
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.key" UNIQUE ("art_id","key");
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.art" FOREIGN KEY ("art_id") REFERENCES "arts"("id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
CREATE INDEX "attack_index_pkey" ON "attacks" USING BTREE ("id" mcisidv1_ops);