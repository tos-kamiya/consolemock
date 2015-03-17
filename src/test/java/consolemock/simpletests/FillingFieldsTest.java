package consolemock.simpletests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

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
            "$ abc",
        });
        
        FillingFields sut = new FillingFields();
        sut.console = console;
        sut.run();
        assertTrue(console.isScenarioDone());
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
        try {
            sut.run();
            fail();
        } catch (Abort e) {
            // ok
        }
    }
}
