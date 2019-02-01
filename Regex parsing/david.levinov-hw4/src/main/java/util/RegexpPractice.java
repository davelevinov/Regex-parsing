package util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is only used to practice regular expressions.
 *
 * @author talm
 */
public class RegexpPractice implements RegexpPracticeInterface {
    /**
     * Search for the first occurrence of text between single quotes, return the text (without the quotes).
     * Allow an empty string. If no quoted text is found, return null.
     * Some examples :
     * <ul>
     * <li>On input "this is some 'text' and some 'additional text'" the method should return "text".
     * <li>On input "this is an empty string '' and another 'string'" it should return "".
     * </ul>
     *
     * @param input
     * @return the first occurrence of text between single quotes
     */
    public String findSingleQuotedTextSimple(String input) {
        String result = "";
        Pattern p = Pattern.compile("'([^']*)'");
        Matcher m = p.matcher(input);
        if (m.find() == false) {
            return null;
        }
        result = m.group(1);
        return result;
    }


    /**
     * Search for the first occurrence of text between double quotes, return the text (without the quotes).
     * (should work exactly like {@link #findSingleQuotedTextSimple(String)}), except with double instead
     * of single quotes.
     *
     * @param input
     * @return the first occurrence of text between double quotes
     */
    public String findDoubleQuotedTextSimple(String input) {
        String result = "";
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(input);
        if (m.find() == false) {
            return null;
        }
        result = m.group(1);
        return result;
    }

    /**
     * Search for the all occurrences of text between single quotes <i>or</i> double quotes.
     * Return a list containing all the quoted text found (without the quotes). Note that a double-quote inside
     * a single-quoted string counts as a regular character (e.g, on the string [quote '"this"'] ["this"] should be returned).
     * Allow empty strings. If no quoted text is found, return an empty list.
     *
     * @param input
     * @return
     */
    public List<String> findDoubleOrSingleQuoted(String input) {
        ArrayList<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("\"([^\"]*)\"|'([^']*)'");
        Matcher m = p.matcher(input);
        while (m.find()) {
            if (m.group(1) != null) {
                result.add(m.group(1));
            } else if (m.group(2) != null) {
                result.add(m.group(2));
            }
        }
        return result;
    }

    /**
     * Parse a date string with the following general format:<br>
     * Wdy, DD-Mon-YYYY HH:MM:SS GMT<br>
     * Where:
     * <i>Wdy</i> is the day of the week,
     * <i>DD</i> is the day of the month,
     * <i>Mon</i> is the month,
     * <i>YYYY</i> is the year, <i>HH:MM:SS</i> is the time in 24-hour format,
     * and <i>GMT</i> is a the constant timezone string "GMT".
     * <p>
     * You should also accept variants of the format:
     * <ul>
     * <li>a date without the weekday,
     * <li>spaces instead of dashes (i.e., "DD Mon YYYY"),
     * <li>case-insensitive month (e.g., allow "Jan", "JAN" and "jAn"),
     * <li>a two-digit year (assume it's between 1970 and 2069 in that case)
     * <li>a missing timezone
     * <li>allow multiple spaces wherever a single space is allowed.
     * </ul>
     * <p>
     * The method should return a java {@link Calendar} object with fields
     * set to the corresponding date and time. Return null if the input is not a valid date string.
     *
     * @param input
     * @return
     */
    public Calendar parseDate(String input) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int date = 0, month = 0, year = 0, hourOfDay = 0, minute = 0, second = 0;
        input = input.toLowerCase();
        List<String> months = Arrays.asList("jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec");
        Pattern p = Pattern.compile("(?:[a-zA-Z]{3}, +)?([\\d]{2})(?:-| +)"
                + "(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(?:-| +)" +
                "(\\d{2})?(\\d{2}) +(\\d{2}):(\\d{2}):(\\d{2})( +gmt)?");
        Matcher m = p.matcher(input);
        if (!m.matches()) {
            return null;
        } else {
            date = Integer.parseInt(m.group(1));
            month = months.indexOf(m.group(2));
            if (m.group(3) != null)
                year = Integer.parseInt(m.group(3) + m.group(4));
            else {
                year = Integer.parseInt(m.group(4));
                year += (year < 70) ? 2000 : 1900;
            }
            hourOfDay = Integer.parseInt(m.group(5));
            if (hourOfDay > 12) {
                cal.set(Calendar.AM_PM, Calendar.PM);
            }
            minute = Integer.parseInt(m.group(6));
            second = Integer.parseInt(m.group(7));
        }

        cal.set(year, month, date, hourOfDay, minute, second);
        return cal;
    }

    /**
     * Separate the input into <i>tokens</i> and return them in a list.
     * A token is any mixture of consecutive word characters and single-quoted strings (single quoted strings
     * may contain any character except a single quote).
     * The returned tokens should not contain the quote characters.
     * A pair of single quotes is considered an empty token (the empty string).
     * <p>
     * For example, the input "this-string 'has only three tokens'" should return the list
     * {"this", "string", "has only three tokens"}.
     * The input "this*string'has only two@tokens'" should return the list
     * {"this", "stringhas only two@tokens"}
     *
     * @param input
     * @return
     */
    public List<String> wordTokenize(String input) {
        ArrayList<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("((\\w+)|('[^']*'))+");
        Matcher m = p.matcher(input);
        while (m.find()) {
            result.add(m.group(0).replaceAll("'", ""));
        }
        return result;
    }


    /**
     * Search for the all occurrences of text between single quotes, but treating "escaped" quotes ("\'") as
     * normal characters. Return a list containing all the quoted text found (without the quotes, and with the quoted escapes
     * replaced).
     * Allow empty strings. If no quoted text is found, return an empty list.
     * Some examples :
     * <ul>
     * <li>On input "'This is not wrong' and 'this is isn\'t either", the method should return a list containing
     * ("This is not wrong" and "This isn't either").
     * <li>On input "No quoted \'text\' here" the method should return an empty list.
     * </ul>
     *
     * @param input
     * @return all occurrences of text between single quotes, taking escaped quotes into account.
     */
    public List<String> findSingleQuotedTextWithEscapes(String input) {
        ArrayList<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("(^|[^\\\\]|\\G)'(((.*?)[^\\\\])??)'");
        Matcher m = p.matcher(input);
        while (m.find()) {
            result.add(m.group(2).replaceAll("\\\\'", "'"));
        }
        return result;
    }

    /**
     * Search for the all occurrences of text between single quotes, but treating "escaped" quotes ("\'") as
     * normal characters. Return a list containing all the quoted text found (without the quotes, and with the quoted escapes
     * replaced).
     * Allow empty strings. If no quoted text is found, return an empty list.
     * Some examples :
     * <ul>
     * <li>On input "'This is not wrong' and 'this is isn\'t either", the method should return a list containing
     * ("This is not wrong" and "This isn't either").
     * <li>On input "No quoted \'text\' here" the method should return an empty list.
     * </ul>
     *
     * @param input
     * @return all occurrences of text between single quotes, taking escaped quotes into account.
     */
    public List<String> findDoubleQuotedTextWithEscapes(String input) {
        ArrayList<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("(^|[^\\\\]|\\G)\"(((.*?)[^\\\\])??)\"");
        Matcher m = p.matcher(input);
        while (m.find()) {
            result.add(m.group(2).replaceAll("\\\\\"", "\""));
        }
        return result;
    }


    /**
     * Parse the input into a list of attribute-value pairs.
     * The input should be a valid attribute-value pair list: attr=value; attr=value; attr; attr=value...
     * If a value exists, it must be either an HTTP token (see {@link AVPair}) or a double-quoted string.
     *
     * @param input
     * @return
     */
    public List<AVPair> parseAvPairs(String input) {
        // TODO: Implement
        return null;
    }


    /**
     * Parse the input into a list of attribute-value pairs, with input checking.
     * The input should be a valid attribute-value pair list: attr=value; attr=value; attr; attr=value...
     * If a value exists, it must be either an HTTP token (see {@link AVPair}) or a double-quoted string.
     * <p>
     * This  method should return null if the input is not a list of attribute-value pairs with the format
     * specified above.
     *
     * @param input
     * @return
     */
    @Override
    public List<AVPair> parseAvPairs2(String input) {
        // TODO: Implement
        return null;
    }
}
