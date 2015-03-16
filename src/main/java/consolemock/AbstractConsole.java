package consolemock;

/**
 * Console functions as part of java.io.Console
 */
public interface AbstractConsole {
    void format(String format, Object... args);
    String readLine();
    String readLine(String format, Object... args);
    char[] readPassword();
    char[] readPassword(String format, Object... args);
}
