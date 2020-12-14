package dev.esophose.transformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transformer {

    private static Map<String, String[]> transformations;
    private static Pattern pattern = Pattern.compile("\\{\\d}");

    public static void main(String... args) {


        try (Scanner scanner = new Scanner(new File("C:\\Users\\Nicole\\Desktop\\Java Projects\\PlayerParticles\\src\\main\\resources\\lang\\en_US.lang"))) {
            StringBuilder output = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    output.append('\n');
                    continue;
                }

                if (line.startsWith("#")) {
                    String comment = line.substring(line.indexOf('#') + 2);
                    output.append("\t\t\t// ").append(comment).append('\n');
                } else {
                    int colon = line.indexOf(':');
                    String key = line.substring(0, colon);
                    String value = pattern.matcher(line.substring(colon + 2)).results().map(MatchResult::group).map(x -> "\""+ x +"\"").collect(Collectors.joining(", "));

                    if (value.isEmpty()) {
                        output.append("\t\t\tthis.put(\"").append(key).append("\", new String[0]").append(value).append(");").append('\n');
                    } else {
                        output.append("\t\t\tthis.put(\"").append(key).append("\", new String[] { ").append(value).append(" });").append('\n');
                    }
                }
            }

            System.out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(new File("C:\\Users\\Nicole\\Desktop\\Java Projects\\PlayerParticles\\src\\main\\resources\\lang\\fr_FR.lang"))) {
            StringBuilder output = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    output.append('\n');
                    continue;
                }

                if (line.startsWith("#")) {
                    String comment = line.substring(line.indexOf('#') + 2);
                    output.append("\t\t\t// ").append(comment).append('\n');
                } else {
                    int colon = line.indexOf(':');
                    String key = line.substring(0, colon);
                    String value = line.substring(colon + 2);
                    try {
                        int target = 0;
                        for (String replacement : Stream.of(transformations.get(key)).map(x -> "%" + x + "%").collect(Collectors.toList())) {
                            value = value.replaceAll("\\{" + target + "}", replacement);
                            target++;
                        }
                    } catch (Exception ex) {
                        System.out.println(key);
                        throw ex;
                    }

                    output.append("\t\t\tthis.put(\"").append(key).append("\", ").append(value).append(");").append('\n');
                }
            }

            System.out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}
