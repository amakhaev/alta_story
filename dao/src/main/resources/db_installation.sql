CREATE TABLE IF NOT EXISTS preservations (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  chapter_indicator INTEGER NOT NULL
);

INSERT INTO preservations (id, chapter_indicator) SELECT 1, 1 WHERE (SELECT count(*) FROM preservations) = 0;


CREATE TABLE IF NOT EXISTS character_preservations (
  id INTEGER PRIMARY KEY NOT NULL,
  map_name VARCHAR(128) NOT NULL,
  focus_x INTEGER NOT NULL,
  focus_y INTEGER NOT NULL,
  main_character_skin VARCHAR(512) NOT NULL,
  FOREIGN KEY (id) REFERENCES preservations (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO character_preservations (map_name, focus_x, focus_y, main_character_skin)
    SELECT 'test', 1, 1, 'person1.dscr' WHERE (SELECT count(*) FROM character_preservations) = 0;