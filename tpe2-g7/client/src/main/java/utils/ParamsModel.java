package utils;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ParamsModel {
    private final String aux;
    private final String inPath;
    private final String outPath;

    public ParamsModel() {
        try{
            aux = ParsingUtils.getSystemProperty(PropertyNames.ADDRESSES).orElseThrow();
            inPath = ParsingUtils.getSystemProperty(PropertyNames.IN_PATH).orElseThrow();
            outPath = ParsingUtils.getSystemProperty(PropertyNames.OUT_PATH).orElseThrow();
        }
        catch(NoSuchElementException | NumberFormatException f) {
            throw new InvalidParameterException("Invalid parameters. Now exiting");
        }
    }

    public List<String> getAddresses() {
        return Arrays.asList(aux.split(";"));
    }

    public String getTripsPath() {
        return inPath + "/bikes.csv";
    }

    public String getStationsPath() {
        return inPath + "/stations.csv";
    }

    public String getOutPath() {
        return outPath;
    }
}