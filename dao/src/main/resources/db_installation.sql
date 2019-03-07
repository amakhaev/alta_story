CREATE TABLE IF NOT EXISTS preservation (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  map_name VARCHAR(128) NOT NULL,
  focus_x INTEGER NOT NULL,
  focus_y INTEGER NOT NULL,
  main_character_skin VARCHAR(512) NOT NULL
);

INSERT INTO preservation (map_name, focus_x, focus_y, main_character_skin)
    SELECT 'test', 1, 1, 'person1.dscr' WHERE (SELECT count(*) FROM preservation) = 0;