INSERT INTO mcrpgs (
  "human_initial_life",
  "oni_initial_life",
  "hybrid_initial_life",
  "breath_initial",
  "blood_initial",
  "ability_roll",
  "family_roll"
) VALUES (?, ?, ?, ?, ?, ?, ?)
  RETURNING "id";