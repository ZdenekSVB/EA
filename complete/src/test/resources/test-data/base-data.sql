-- Vložení základních států
INSERT INTO country (name) VALUES ('Czech Republic');
INSERT INTO country (name) VALUES ('Slovakia');

-- Happiness pro Czech Republic (2023)
INSERT INTO happiness (
    country_id, year, rank, happiness_score, upper_whisker, lower_whisker, gdp, social_support,
    healthy_life_expectancy, freedom, generosity, corruption, dystopia_residual
) VALUES (
             1, 2023, 10, 7.0, 7.2, 6.8, 1.5, 1.0, 0.9, 0.6, 0.1, 0.02, 2.1
         );

-- Happiness pro Slovakia (2023)
INSERT INTO happiness (
    country_id, year, rank, happiness_score, upper_whisker, lower_whisker, gdp, social_support,
    healthy_life_expectancy, freedom, generosity, corruption, dystopia_residual
) VALUES (
             2, 2023, 20, 6.0, 6.2, 5.8, 1.3, 0.9, 0.8, 0.5, 0.2, 0.03, 1.9
         );

-- Prediction pro Czech Republic (2025)
INSERT INTO prediction (year, predicted_score, country_id) VALUES (2025, 7.5, 1);

-- Prediction pro Slovakia (2025)
INSERT INTO prediction (year, predicted_score, country_id) VALUES (2025, 6.3, 2);
