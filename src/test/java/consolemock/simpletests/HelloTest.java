package consolemock.simpletests;

import org.junit.Test;
import static org.junit.Assert.*;

import consolemock.*;

class Hello {
    public static AbstractConsole console = new SystemInOutConsole();
    
    public static void main(String[] args) {
        String name = console.readLine("What's your name: ");
        if (name.length() == 0)
            console.format("Hello, Mr.Nobody!\n");
        else
            console.format("Hello, %s!\n", name);
    }
}

public class HelloTest {
    @Test
    public void helloJohn() {
        ScenarioConsole console = new ScenarioConsole(new String[] {
            "> What's your name: ",
            "$ John",
            "> Hello, John!\n"
        });
        
        Hello.console = console;
        Hello.main(new String[0]);
        assertTrue(console.isScenarioDone());
    }

    @Test
    public void emptyInput() {
        ScenarioConsole console = new ScenarioConsole(new String[] {
            "> What's your name: ",
            "$ ",
            "> Hello, Mr.Nobody!\n"
        });
        
        Hello.console = console;
        Hello.main(new String[0]);
        assertTrue(console.isScenarioDone());
    }
}
