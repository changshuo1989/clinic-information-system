--<ScriptOptions statementTerminator=";"/>

CREATE TABLE sequence (
		seq_name VARCHAR(50) NOT NULL,
		seq_count NUMERIC(38 , 0)
	);

CREATE UNIQUE INDEX sequence_pkey ON sequence (seq_name ASC);

ALTER TABLE sequence ADD CONSTRAINT sequence_pkey PRIMARY KEY (seq_name);

