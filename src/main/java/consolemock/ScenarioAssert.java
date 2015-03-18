package consolemock;

import static org.junit.Assert.assertTrue;

import consolemock.ScenarioConsole;
import consolemock.ScenarioConsoleException.Abort;

public class ScenarioAssert {
    public interface ScenarioRun {
        public abstract void run();
    }

    public static void assertDoneOrAbort(ScenarioConsole console, ScenarioRun scenarioRun) {
        try {
            scenarioRun.run();
            assertTrue(console.isScenarioDone());
            // ok in either case of done ...
        } catch (Abort e) {
            // ... or abort.
        }
    }

    public static void assertDone(ScenarioConsole console, ScenarioRun scenarioRun) {
        scenarioRun.run();
        assertTrue(console.isScenarioDone());
    }
}
