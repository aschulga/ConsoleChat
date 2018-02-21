package controller;

import java.util.regex.Pattern;

public class Validator {

    private final static String REGEX_REGISTRATION = "(^\\/register\\s(client|agent)\\s[^\\s]+)";
    private final static String REGEX_LEAF = "(^\\/leave\\s*)";
    private final static String REGEX_EXIT = "(^\\/exit\\s*)";

    public static boolean isValidateRequest(String request, int number) {

        Pattern pattern = null;

        switch (number){
            case 1:{
                pattern = Pattern.compile(REGEX_REGISTRATION);
                break;
            }
            case 2:{
                pattern = Pattern.compile(REGEX_LEAF);
                break;
            }
            case 3:{
                pattern = Pattern.compile(REGEX_EXIT);
                break;
            }
        }

        return pattern.matcher(request).matches();
    }
}
