package consolemock;

import java.io.*;

public class SystemInOutConsole implements AbstractConsole {
    private BufferedReader reader;

    public SystemInOutConsole() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void format(String format, Object... args) {
        System.out.format(format, args);
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    public String readLine(String format, Object... args) {
        this.format(format, args);
        return this.readLine();
    }

    public char[] readPassword() {
        return readLine().toCharArray();
    }

    public char[] readPassword(String format, Object... args) {
        return readLine(format, args).toCharArray();
    }
}
