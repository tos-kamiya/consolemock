package consolemock.simpletests;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import consolemock.*;

class Hello {
    public AbstractConsole console = new SystemInOutConsole();
    
    public void run() {
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
        SenarioConsole console = new SenarioConsole(new String[] {
            "> What's your name: ",
            "$ John",
            "> Hello, John!\n"
        });
        
        Hello sut = new Hello();
        sut.console = console;
        sut.run();
        assertTrue(console.isDone());
    }

    @Test
    public void emptyInput() {
        SenarioConsole console = new SenarioConsole(new String[] {
            "> What's your name: ",
            "$ ",
            "> Hello, Mr.Nobody!\n"
        });
        
        Hello sut = new Hello();
        sut.console = console;
        sut.run();
        assertTrue(console.isDone());
    }
}
