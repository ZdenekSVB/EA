TRUNCATE TABLE prediction RESTART IDENTITY CASCADE;
TRUNCATE TABLE happiness RESTART IDENTITY CASCADE;
TRUNCATE TABLE country RESTART IDENTITY CASCADE;

SELECT setval('country_id_seq', 1, false);
SELECT setval('happiness_id_seq', 1, false);
SELECT setval('prediction_id_seq', 1, false);
