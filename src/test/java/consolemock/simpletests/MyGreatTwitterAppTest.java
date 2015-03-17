package consolemock.simpletests;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import consolemock.*;

class MyGreatTwitterApp {
    public static AbstractConsole console = new SystemInOutConsole();

    public static void main(String[] args) {
        while (true) {
            String command = console.readLine("command: ");
            switch (command) {
            case "h":
                console.format("[q] exit. [l] show time line. [t] tweet.\n");
                break;
            case "q":
                console.format("bye!\n");
                return;
            default:
                console.format("no such command: %s\n", command);
                System.exit(1);
            }
        }
    }
}

public class MyGreatTwitterAppTest {
    @Test
    public void testHelpCommand() {
        ScenarioConsole console = new ScenarioConsole(new String[] {
            "> command: ",
            "$ h",
            "> [q] exit. [l] show time line. [t] tweet.\n",
            "> command: ",
            "$ q",
            "> bye!\n"
        });
        
        MyGreatTwitterApp.console = console;
        MyGreatTwitterApp.main(new String[0]);

        assertTrue(console.isScenarioDone());
    }
}
