package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFileUtils {

    private final String filePath;
    private final String method;
    private final String className;
    private BufferedWriter bw;

    public LogFileUtils(String file, String outPath, String method, String className) {
        this.filePath = outPath + file;
        this.method = method;
        this.className = className;
        File outFile = new File(outPath);
        try {
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            FileWriter fw = new FileWriter(filePath);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while creating the output file.");
        }
    }

    public void writeTimestampsLogger(String line, String msg) {
        StringBuilder output = new StringBuilder();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSSS");
        output.append(LocalDateTime.now().format(f)).append(" INFO [").append(this.method)
                .append("] " ).append(this.className).append(" (").append(this.className)
                .append(".java:").append(line).append(") - ").append(msg)
                .append('\n');
        try {
            bw.write(output.toString());
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
