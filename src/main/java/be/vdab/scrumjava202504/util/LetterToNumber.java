package be.vdab.scrumjava202504.util;

public class LetterToNumber {
    public static int getNumberOfChar(char letter) {
        return Character.toLowerCase(letter) - 'a' + 1;
    }
}
