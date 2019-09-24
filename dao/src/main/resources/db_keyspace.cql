CREATE KEYSPACE alta_game WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

// Types definition
CREATE TYPE IF NOT EXISTS alta_game.acting_character (map_name text, focus_x int, focus_y int, skin text);
CREATE TYPE IF NOT EXISTS alta_game.interaction (interaction_uuid uuid, map_name text, completed boolean);
CREATE TYPE IF NOT EXISTS alta_game.map (participant_uuid uuid, map_name text, visible boolean);
CREATE TYPE IF NOT EXISTS alta_game.quest (quest_name text, current_step_number int, completed boolean);

// Tables definition
CREATE TABLE IF NOT EXISTS alta_game.interaction_preservations(preservation_id int, interaction_uuid uuid, map_name text, completed boolean, PRIMARY KEY (preservation_id, interaction_uuid));
CREATE TABLE IF NOT EXISTS alta_game.map_preservations(preservation_id int, participant_uuid uuid, map_name text, visible boolean, PRIMARY KEY (preservation_id, participant_uuid, map_name));
CREATE TABLE IF NOT EXISTS alta_game.quest_preservations(preservation_id int, quest_name text, current_step_number int, completed boolean, , PRIMARY KEY (preservation_id, quest_name));
CREATE TABLE IF NOT EXISTS alta_game.preservations(id int, chapter_indicator int, acting_character FROZEN<alta_game.acting_character>, PRIMARY KEY (id));
CREATE TABLE IF NOT EXISTS alta_game.preservation_snapshots(id int, chapter_indicator int, acting_character FROZEN<alta_game.acting_character>, interactions list<frozen <alta_game.interaction>>, maps list<frozen <alta_game.map>>, quests list<frozen <alta_game.quest>>, PRIMARY KEY (id));

// Insert of initial values
INSERT INTO alta_game.preservation_snapshots (id, chapter_indicator, acting_character, quests) values (1, 0, {map_name: 'village_yutta', focus_x: 58, focus_y: 72, skin: 'person12'}, [{quest_name: 'main', current_step_number: 0, completed: false}]);

