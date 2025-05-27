package cz.mendelu.ea;

import cz.mendelu.ea.utils.data.DataImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataImportRunner {

    @Bean
    public CommandLineRunner runImporter(DataImporter importer) {
        return args -> {
            importer.importData();
        };
    }
}
