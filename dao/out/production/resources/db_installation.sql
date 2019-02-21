CREATE TABLE IF NOT EXISTS preservation (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  map_name VARCHAR(128) NOT NULL,
  focus_x INTEGER NOT NULL,
  focus_y INTEGER NOT NULL
);

INSERT INTO preservation (map_name, focus_x, focus_y) SELECT 'test', 1, 1 WHERE (SELECT count(*) FROM preservation) = 0;