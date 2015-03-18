[![Build Status](https://travis-ci.org/tos-kamiya/consolemock.svg)](https://travis-ci.org/tos-kamiya/consolemock)

# Console Mock
A Java's mock unit-testing kit for java.io.Console or System.in/out.
With console mock, you can write interactions between a CLI app and its user as a scenario,
which contains print-outs by the app and input lines by the user in a time line.

## How to use

1) Modify the target app for mocking, to replace a `java.io.Console` obj with `consolemock.SenarioConsole` obj.

Modify this kind of code:

```
public class MyGreatTwitterApp {
    private static java.io.Console console = System.console();
    ...
}
```

to:

```
import consolemock.*;

class MyGreatTwitterApp {
    public static AbstractConsole console = JavaIoConsole(); // java.io.Console ('s wrapper) by default.
    ...
}
```

2) Write a unit test as a scenario.

In a scenario, a string starts with "> " represents an output by the app.
A string starts with "$ " represents an input line by a user.

```
    @Test
    public void testHelpCommand() {
        SenarioConsole console = new SenarioConsole(new String[] {
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
```

3) Run the unit test.

A file `src/test/java/MyGreatTwitterAppTest.java` contains the almost same sample to above.

## License

Public Domain except for a class `Repr`, which has been taken from http://stackoverflow.com/a/1351973 .

## Acknowledgements

Thanks to [dirk](http://stackoverflow.com/users/141081/dirk) for a very useful class `Repr` .
