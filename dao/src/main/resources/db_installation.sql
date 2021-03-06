CREATE TABLE IF NOT EXISTS preservations (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO preservations (id) SELECT 1 WHERE (SELECT count(*) FROM preservations) = 0;


CREATE TABLE IF NOT EXISTS character_preservations (
  id INTEGER PRIMARY KEY NOT NULL,
  map_name VARCHAR(128) NOT NULL,
  focus_x INTEGER NOT NULL,
  focus_y INTEGER NOT NULL,
  main_character_skin VARCHAR(512) NOT NULL,
  FOREIGN KEY (id) REFERENCES preservations (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO character_preservations (map_name, focus_x, focus_y, main_character_skin)
    SELECT 'village_yutta', 58, 72, 'person12' WHERE (SELECT count(*) FROM character_preservations) = 0;

CREATE TABLE IF NOT EXISTS interaction_preservations (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  preservation_id INTEGER NOT NULL,
  uuid VARCHAR(32) NOT NULL,
  map_name VARCHAR(32) NOT NULL,
  is_completed BOOLEAN DEFAULT false,
  is_temporary BOOLEAN NOT NULL,
  UNIQUE (preservation_id, uuid, map_name, is_temporary),
  FOREIGN KEY (preservation_id) REFERENCES preservations (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS map_preservations (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  preservation_id INTEGER NOT NULL,
  uuid VARCHAR(32) NOT NULL,
  map_name VARCHAR(32) NOT NULL,
  is_temporary BOOLEAN NOT NULL,
  is_visible BOOLEAN,
  UNIQUE (preservation_id, uuid, map_name, is_temporary),
  FOREIGN KEY (preservation_id) REFERENCES preservations (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS quest_preservations (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  preservation_id INTEGER NOT NULL,
  name VARCHAR(128) NOT NULL,
  is_temporary BOOLEAN NOT NULL,
  current_step_number INTEGER NOT NULL,
  is_completed BOOLEAN DEFAULT false,
  UNIQUE (preservation_id, name, is_temporary),
  FOREIGN KEY (preservation_id) REFERENCES preservations (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO quest_preservations (preservation_id, name, is_temporary, current_step_number, is_completed)
    SELECT 1, 'main', 0, 0, 0 WHERE (SELECT count(*) FROM quest_preservations) = 0;

CREATE TABLE IF NOT EXISTS global_preservations (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  preservation_id INTEGER NOT NULL,
  is_temporary BOOLEAN NOT NULL,
  chapter_indicator INTEGER NOT NULL,
  UNIQUE (preservation_id, is_temporary),
  FOREIGN KEY (preservation_id) REFERENCES preservations (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO global_preservations (preservation_id, chapter_indicator, is_temporary)
    SELECT 1, 0, 0 WHERE (SELECT count(*) FROM global_preservations) = 0;