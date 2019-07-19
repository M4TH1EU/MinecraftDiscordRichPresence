package ch.m4th1eu.richpresence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Utils {

    /**
     * @author Nathanaël#4314
     */
    public static String readFileToString(final File file) {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            final String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
