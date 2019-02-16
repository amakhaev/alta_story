CREATE TABLE IF NOT EXISTS preservation (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  map_name VARCHAR(128) NOT NULL
);

INSERT INTO preservation (map_name) SELECT 'test' WHERE (SELECT count(*) FROM preservation) = 0;