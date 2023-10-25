package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParsingUtils {

    private static final Logger logger = LoggerFactory.getLogger(ParsingUtils.class);

    public static Optional<String> getSystemProperty(String name){
        final String prop = System.getProperty(name);
        if(prop == null){
            return Optional.empty();
        }
        return Optional.of(prop);
    }

    public static List<String[]> parseCsv(String path){
        FileReader file;
        try{
            file = new FileReader(path);
        }
        catch(FileNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }

        CSVParser parser;
        try {
            parser = new CSVParser(file, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean skipFirstLine = true;
        List<String[]> lines = new ArrayList<>();

        for (CSVRecord record : parser) {
            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }
            String[] tokenizedArray = record.get(0).split(";");
            lines.add(tokenizedArray);
        }
        return lines;
    }

}
