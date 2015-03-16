package consolemock.simpletests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import consolemock.*;
import consolemock.SenarioConsoleException.*;

class FillingFields {
    public AbstractConsole console = new SystemInOutConsole();
    
    public void run() {
        String productId = console.readLine("product id: ");
        String amountStr = console.readLine("qty: ");
        int amount = Integer.valueOf(amountStr);
        console.format("Order: product %s, qty %d.\n", productId, amount);
    }
}



public class FillingFieldsTest {
    @Test
    public void testOrdering() {
        SenarioConsole console = new SenarioConsole(new String[] {
            "> product id: ",
            "$ shaver001",
            "> qty: ",
            "$ 2",
            "> Order: product shaver001, qty 2.\n"
        });
        
        FillingFields sut = new FillingFields();
        sut.console = console;
        sut.run();
        assertTrue(console.isDone());
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidNumber() {
        SenarioConsole console = new SenarioConsole(new String[] {
            "> product id: ",
            "$ shaver002",
            "> qty: ",
            "$ abc",
        });
        
        FillingFields sut = new FillingFields();
        sut.console = console;
        sut.run();
        assertTrue(console.isDone());
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
        SenarioConsole console = new SenarioConsole(senario);
        
        FillingFields sut = new FillingFields();
        sut.console = console;
        sut.run();

        assertThat(console.isDone(), is(false));
        assertThat(senario[console.getProgress()], is(senario[senario.length - 1]));
    }
}
