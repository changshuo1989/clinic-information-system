--<ScriptOptions statementTerminator=";"/>

CREATE TABLE treatment (
		id INT8 NOT NULL,
		ttype VARCHAR(31),
		diagnosis VARCHAR(255),
		patient_id INT8,
		provider_fk INT8
	);

CREATE UNIQUE INDEX treatment_pkey ON treatment (id ASC);

ALTER TABLE treatment ADD CONSTRAINT treatment_pkey PRIMARY KEY (id);

ALTER TABLE treatment ADD CONSTRAINT fk_treatment_provider_fk FOREIGN KEY (provider_fk)
	REFERENCES provider (id);

ALTER TABLE treatment ADD CONSTRAINT fk_treatment_patient_id FOREIGN KEY (patient_id)
	REFERENCES patient (id);

