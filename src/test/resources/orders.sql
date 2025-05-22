DROP TABLE IF EXISTS Bestellijnen;
DROP TABLE IF EXISTS Bestellingen;
DROP TABLE IF EXISTS Artikelen;

CREATE TABLE Artikelen
(
    artikelId     BIGINT       NOT NULL PRIMARY KEY,
    naam          VARCHAR(255) NOT NULL,
    gewichtInGram INT          NOT NULL
);

CREATE TABLE Bestellingen
(
    bestelId    BIGINT  NOT NULL PRIMARY KEY,
    betaald     BOOLEAN NOT NULL,
    besteldatum DATE    NOT NULL,
    bestellingsstatusid INT NOT NULL
);

CREATE TABLE Bestellijnen
(
    bestelId      BIGINT NOT NULL,
    artikelId     BIGINT NOT NULL,
    aantalBesteld INT    NOT NULL,
    FOREIGN KEY (bestelId) REFERENCES Bestellingen (bestelId),
    FOREIGN KEY (artikelId) REFERENCES Artikelen (artikelId)
);

INSERT INTO Artikelen (artikelId, naam, gewichtInGram)
VALUES (1, 'Boormachine', 2000),
       (2, 'Schroevendraaier', 500),
       (3, 'Hamer', 1500);

INSERT INTO Bestellingen (bestelId, betaald, besteldatum,bestellingsstatusid)
VALUES (100, TRUE, '2024-01-01',2),
       (101, TRUE, '2024-01-02',2),
       (102, FALSE, '2024-01-03',1),
       (103, TRUE, '2024-01-04',2),
       (104, TRUE, '2024-01-05',1),
       (105, TRUE, '2024-01-06',2);

INSERT INTO Bestellijnen (bestelId, artikelId, aantalBesteld)
VALUES (100, 1, 1),
       (100, 2, 2),
       (101, 2, 1),
       (101, 3, 1),
       (103, 1, 3),
       (104, 3, 2),
       (105, 1, 1),
       (105, 2, 1);
