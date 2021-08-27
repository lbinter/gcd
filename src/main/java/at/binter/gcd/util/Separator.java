package at.binter.gcd.util;

public class Separator {
    private final String separator;
    private final char[] separatorCharArray;
    private int position = 0;

    public Separator(String separator) {
        this.separator = separator;
        separatorCharArray = separator.toCharArray();
    }

    public void checkCharacter(char c) {
        if (c == separatorCharArray[position]) {
            position++;
        } else {
            reset();
        }
    }

    public void reset() {
        position = 0;
    }

    public boolean foundSeparator() {
        return position == length();
    }

    public int length() {
        return separatorCharArray.length;
    }

    public String getSeparator() {
        return separator;
    }
}
