CREATE TABLE "guilds" (
  "id" discord_id_type NOT NULL,
  "rpg_id" mcisidv1 NOT NULL,
  "roll_category_id" discord_id_type DEFAULT NULL,
  "off_category_id" discord_id_type DEFAULT NULL
);
ALTER TABLE "guilds"
  ADD CONSTRAINT "guild.pkey" PRIMARY KEY ("id");
ALTER TABLE "guilds"
  ADD CONSTRAINT "guild.rpg" FOREIGN KEY ("rpg_id") REFERENCES "mcrpgs"("id");