ALTER TABLE `RESULT`
	ADD COLUMN `KEY_THESIS` INTEGER  DEFAULT NULL;

ALTER TABLE `THESIS`
	ADD COLUMN `KEY_PUBLICATION` INTEGER  DEFAULT NULL;

ALTER TABLE `THESIS`
	DROP COLUMN `SCIENTIFIC_AREA`,
	DROP COLUMN `LIBRARY_CONFIRMATION`,
	DROP COLUMN `LIBRARY_EXPORTED`;

ALTER TABLE RESULT
	ADD COLUMN SCIENTIFIC_AREA VARCHAR(255),
	ADD COLUMN LIBRARY_CONFIRMATION BOOLEAN DEFAULT 0,
	ADD COLUMN LIBRARY_EXPORTED BOOLEAN DEFAULT 0;
