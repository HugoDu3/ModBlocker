package fr.dev.solari.util;

public class UtilString {
    private UtilString(){}


    public static String combine(String[] args, int startIndex) {
        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }

        return builder.toString();
    }
}
