package consolemock;

import java.io.Console;

public class JavaIoConsole implements AbstractConsole {
    private final Console console;

    public JavaIoConsole() {
        console = System.console();
        if (console == null)
            throw new RuntimeException("Can not grab a console with System.console()");
    }

    public void format(String format, Object... args) {
        console.format(format, args);
    }

    public String readLine() {
        return console.readLine();
    }

    public String readLine(String format, Object... args) {
        return console.readLine(format, args);
    }

    public char[] readPassword() {
        return console.readPassword();
    }

    public char[] readPassword(String format, Object... args) {
        return console.readPassword(format, args);
    }
}
