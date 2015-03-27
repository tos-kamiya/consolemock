package consolemock.simpletests;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import consolemock.*;
import consolemock.ScenarioConsoleException.*;

class FillingFields {
    public AbstractConsole console = new SystemInOutConsole();
    
    public void run() {
        String productId = console.readLine("product id: ");
        for (char c : productId.toCharArray()) {
            if (! Character.isLetterOrDigit(c)) {
                console.format("Error. invalid product id.\n");
                console.format("Order canceld.\n");
                return;
            }
        }
        String amountStr = console.readLine("qty: ");
        int amount = Integer.valueOf(amountStr);
        console.format("Order: product %s, qty %d.\n", productId, amount);
    }
    
    public static void main(String[] args) {
        new FillingFields().run();
    }
}

public class FillingFieldsTest {
    @Test
    public void testOrdering() {
        ScenarioConsole console = new ScenarioConsole(new String[] {
            "> product id: ",
            "$ shaver001",
            "> qty: ",
            "$ 2",
            "> Order: product shaver001, qty 2.\n"
        });
        
        FillingFields sut = new FillingFields();
        sut.console = console;
        sut.run();
        
        assertTrue(console.isScenarioDone());
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidNumber() {
        ScenarioConsole console = new ScenarioConsole(new String[] {
            "> product id: ",
            "$ shaver002",
            "> qty: ",
            "$ abc", // causes NumberFormatException. should be a number.
        });
        
        FillingFields sut = new FillingFields();
        sut.console = console;
        sut.run();
    }

    @Test
    public void testExtraInput() {
        String[] senario = new String[] {
            "> product id: ",
            "$ shaver003",
            "> qty: ",
            "$ 4",
            "> Order: product shaver003, qty 4.\n",
            "$ extra input line" // extra input
        };
        ScenarioConsole console = new ScenarioConsole(senario);
        
        FillingFields sut = new FillingFields();
        sut.console = console;
        sut.run();

        assertThat(console.isScenarioDone(), is(false));
        assertThat(senario[console.getProgress()], is(senario[senario.length - 1]));
    }
    
    @Test
    public void testInvalidProductId() {
        String[] senario = new String[] {
            "> product id: ",
            "$ $%#",
            "> Error. invalid product id.\n",
            "! " // abort. no need to check the following.
        };
        ScenarioConsole console = new ScenarioConsole(senario);
        
        FillingFields sut = new FillingFields();
        sut.console = console;

        // ** Java 1.7 **
        try {
            sut.run();
            assertTrue(console.isScenarioDone());
            // ok in either case of done ...
        } catch (Abort e) {
            // ... or abort.
        }
        // ** Java 1.8  **
        // import static consolemock.ScenarioAssert.*;
        // assertDoneOrAbort(console, () -> sut.run());
    }
}
