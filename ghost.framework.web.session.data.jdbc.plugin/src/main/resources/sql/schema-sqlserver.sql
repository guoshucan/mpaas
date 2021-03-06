CREATE TABLE GHOST_SESSION (
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	CONSTRAINT GHOST_SESSION_PK PRIMARY KEY (SESSION_ID)
);

CREATE UNIQUE INDEX GHOST_SESSION_IX1 ON GHOST_SESSION (SESSION_ID);
CREATE INDEX GHOST_SESSION_IX2 ON GHOST_SESSION (EXPIRY_TIME);

CREATE TABLE GHOST_SESSION_ATTRIBUTES (
	SESSION_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES IMAGE NOT NULL,
	CONSTRAINT GHOST_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_ID, ATTRIBUTE_NAME)
);
