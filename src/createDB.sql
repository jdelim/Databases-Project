CREATE DATABASE ResultTrackerJADRIELDELIM;

USE ResultTrackerJADRIELDELIM;

CREATE TABLE Players (
	player_id INT NOT NULL,
    tag VARCHAR(45) NOT NULL,
    real_name VARCHAR(45) NOT NULL,
    nationality VARCHAR(2) NOT NULL,
    birthday DATE NOT NULL,
    game_race ENUM('P', 'Z', 'T') NOT NULL,
    PRIMARY KEY (player_id)
    );
    
CREATE TABLE Teams (
	team_id INT NOT NULL,
    team_name VARCHAR(50) NOT NULL,
    founded DATE NOT NULL,
    disbanded DATE,
    PRIMARY KEY (team_id)
);

CREATE TABLE Tournaments (
	tournament_id INT NOT NULL,
    tournament_name VARCHAR(200) NOT NULL,
    region VARCHAR(2) NOT NULL,
    major BOOLEAN NOT NULL,
    PRIMARY KEY (tournament_id)
);

CREATE TABLE Members (
	player_id INT NOT NULL,
    team_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    PRIMARY KEY (player_id, start_date),
    FOREIGN KEY (player_id) REFERENCES Players(player_id),
    FOREIGN KEY (team_id) REFERENCES Teams(team_id)
);

CREATE TABLE Earnings (
	tournament_id INT NOT NULL,
    player_id INT NOT NULL,
    prize_money INT NOT NULL,
    position INT NOT NULL,
    FOREIGN KEY (tournament_id) REFERENCES Tournaments(tournament_id),
    FOREIGN KEY (player_id) REFERENCES Players(player_id)
);

CREATE TABLE Matches (
	match_id INT NOT NULL,
    date_of DATE NOT NULL,
    tournament_id INT NOT NULL,
    playerA_id INT NOT NULL,
    playerB_id INT NOT NULL,
    playerA_score INT NOT NULL,
    playerB_score INT NOT NULL,
    is_offline BOOLEAN NOT NULL,
    PRIMARY KEY (match_id),
    FOREIGN KEY (tournament_id) REFERENCES Tournaments(tournament_id),
    FOREIGN KEY (playerA_id) REFERENCES Players(player_id),
    FOREIGN KEY (playerB_id) REFERENCES Players(player_id)
);
