package at.binter.gcd.util;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Method;
import java.util.*;

public class Tools {
    private static final Logger log = LoggerFactory.getLogger(Tools.class);

    public static final String[] greekLetters = new String[]{
            "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta",
            "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho",
            "sigmaf", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"};

    public static final Map<String, String> greekLettersUnicodeMap = new HashMap<>();

    public static Converter<String> greekLetterConverter = new Converter<>() {
        @Override
        public boolean showDeAndEncoded() {
            return false;
        }

        @Override
        public String encode(String input) {
            if (StringUtils.isBlank(input)) return input;
            return Tools.transformMathematicaGreekToUnicodeLetters(input);
        }

        @Override
        public String decode(String input) {
            if (StringUtils.isBlank(input)) return input;
            return Tools.transformUnicodeToMathematicaGreekLetters(input);
        }
    };

    public static Converter<String> greekLetterShowAllConverter = new Converter<>() {
        @Override
        public boolean showDeAndEncoded() {
            return true;
        }

        @Override
        public String encode(String input) {
            if (StringUtils.isBlank(input)) return input;
            return Tools.transformMathematicaGreekToUnicodeLetters(input);
        }

        @Override
        public String decode(String input) {
            if (StringUtils.isBlank(input)) return input;
            return Tools.transformUnicodeToMathematicaGreekLetters(input);
        }
    };

    public static Converter<Collection> greekLetterCollectionConverter = new Converter<>() {
        @Override
        public boolean showDeAndEncoded() {
            return false;
        }

        @Override
        public Collection encode(Collection input) {
            List<String> list = new ArrayList<>();
            for (Object s : input) {
                list.add(greekLetterConverter.encode(s.toString()));
            }
            return list;
        }

        @Override
        public Collection decode(Collection input) {
            List<String> list = new ArrayList<>();
            for (Object s : input) {
                list.add(greekLetterConverter.decode(s.toString()));
            }
            return list;
        }
    };

    static {
        for (int i = 0; i < greekLetters.length; i++) {
            int unicode = 0x03B1 + i;
            greekLettersUnicodeMap.put(greekLetters[i], "" + (char) unicode);
        }
    }

    public static String getParameterTypesAsString(Method m) {
        return StringUtils.remove(StringUtils.join(m.getParameterTypes(), ','), "class ");
    }

    public static String format(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        } else if (args.length == 1) {
            return MessageFormatter.format(message, args[0]).getMessage();
        } else if (args.length == 2) {
            return MessageFormatter.format(message, args[0], args[1]).getMessage();
        }
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    public static String[] split(String string, String... separatorStrings) {
        List<String> result = new ArrayList<>();
        char[] chars = string.toCharArray();
        List<Separator> separators = new ArrayList<>();
        for (String s : separatorStrings) {
            separators.add(new Separator(s));
        }
        int i = 0;
        int startIndex = 0;
        int endIndex = 0;
        boolean found = false;
        for (char c : chars) {
            i++;
            for (Separator s : separators) {
                s.checkCharacter(c);
                found = s.foundSeparator();
                if (found) {
                    s.reset();
                    endIndex = i - s.length();
                    String subString = new String(ArrayUtils.subarray(chars, startIndex, endIndex)).trim();
                    if (StringUtils.isNotBlank(subString)) {
                        result.add(subString);
                    }
                    startIndex = i;
                    break;
                }
            }
            if (found) {
                for (Separator s : separators) {
                    s.reset();
                }
            }
        }
        if (endIndex != chars.length) {
            endIndex = chars.length;
        }
        result.add(new String(ArrayUtils.subarray(chars, startIndex, endIndex)).trim());
        return result.toArray(new String[0]);
    }

    public static String transformMathematicaGreekToUnicodeLetters(String text) {
        for (Map.Entry<String, String> entry : greekLettersUnicodeMap.entrySet()) {
            text = text.replace("\\[" + entry.getKey() + "]", entry.getValue());
        }
        return text;
    }

    public static String transformUnicodeToMathematicaGreekLetters(String text) {
        for (Map.Entry<String, String> entry : greekLettersUnicodeMap.entrySet()) {
            text = text.replace(entry.getValue(), "\\[" + entry.getKey() + "]");
        }
        return text;
    }

    public static String transformMathematicaGreekToGreekHtmlLetters(String text) {
        for (String l : greekLetters) {
            text = text.replace("\\[" + l + "]", "&" + l.toLowerCase() + ";");
        }
        return text;
    }

    public static String transformGreekHtmlToMathematicaGreekLetters(String text) {
        for (String l : greekLetters) {
            text = text.replace("&" + l.toLowerCase() + ";", "\\[" + l + "]");
        }
        return text;
    }

    public static String[] getNumberArray(int count) {
        String[] array = new String[count];
        for (int i = 0; i < count; i++) {
            array[i] = Integer.toString(i + 1);
        }
        return array;
    }

    public static void setLabelTextFormatted(Label label, List<String> list) {
        label.setText(Tools.transformMathematicaGreekToUnicodeLetters(String.join(",", list)));
    }

    public static void setLabelTextFormatted(Label label, String text) {
        label.setText(Tools.transformMathematicaGreekToUnicodeLetters(text));
    }

    public static boolean isMousePrimaryDoubleClicked(MouseEvent event) {
        return event != null && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2;
    }
}