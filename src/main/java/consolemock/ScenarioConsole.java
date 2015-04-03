package consolemock;

import static consolemock.Repr.toPrintableRepresentation;

import java.util.ArrayList;

public class ScenarioConsole implements AbstractConsole {
    private Item[] scenario;
    private int progress;

    public static class Item {
        public final String text;
        public Item(String text) {
            this.text = text;
        }
    }
    public static class Format extends Item {
        public Format(String text) {
            super(text);
        }
        public String toString() {
            return String.format("Format(%s)", toPrintableRepresentation(text));
        }
    }
    public static class ReadLine extends Item {
        public ReadLine(String text) {
            super(text);
        }
        public String toString() {
            return String.format("ReadLine(%s)", toPrintableRepresentation(text));
        }
    }
    public static class Abort extends Item {
        public Abort() {
            super("");
        }
        public String toString() {
            return "Abort()";
        }
    }
    public static class SkipUntilReadLine extends Item {
        public SkipUntilReadLine(String text) {
            super(text);
        }
        public String toString() {
            return String.format("SkipUntilReadLine(%s)", toPrintableRepresentation(text));
        }
    }

    private String readLine_i() {
        if (progress >= scenario.length)
            throw new ScenarioConsoleException.ExhaustedButReadLine();
        int pos = progress;
        Item item = scenario[pos];
        if (item instanceof Abort)
            throw new ScenarioConsoleException.Abort();
        else if (item instanceof Format) {
            Format w = (Format) item;
            throw new ScenarioConsoleException.FormatExpectedButReadLine(pos, w);
        } else if (item instanceof SkipUntilReadLine || item instanceof ReadLine) {
            ++progress;
            return item.text;
        } else
            throw new AssertionError("invalid type of scenario item");
    }
    
    private void format_i(String actualWriteText) {
        if (progress >= scenario.length)
            throw new ScenarioConsoleException.ExhaustedButFormat(actualWriteText);
        int pos = progress;
        Item item = scenario[pos];
        if (item instanceof Abort)
            throw new ScenarioConsoleException.Abort();
        else if (item instanceof Format) {
            Format w = (Format) item;
            if (!actualWriteText.equals(w.text))
                throw new ScenarioConsoleException.FormatWrongText(pos, w, actualWriteText);
            ++progress;
        } else if (item instanceof SkipUntilReadLine)
            ;
        else if (item instanceof ReadLine) {
            ReadLine r = (ReadLine)item;
            throw new ScenarioConsoleException.ReadLineExpectedButFormat(pos, r, actualWriteText);
        } else
            throw new AssertionError("invalid type of scenario item");
    }
    
    public boolean isScenarioDone() {
        return progress >= scenario.length;
    }

    public ScenarioConsole(String[] scenario) throws WrongScenarioItemException {
        ArrayList<Item> items = new ArrayList<Item>();
        int pos = 0;
        for (String text : scenario) {
            assert text != null;
            String head;
            String tail;
            int s = text.indexOf(' ');
            if (s < 0) {
                head = text;
                tail = "";
            } else {
                head = text.substring(0, s);
                tail = text.substring(s + 1);
            }
            switch (head) {
                case ":$":
                    items.add(new SkipUntilReadLine(tail));
                    break;
                case "$":
                    items.add(new ReadLine(tail));
                    break;
                case ">":
                    items.add(new Format(tail));
                    break;
                case "!":
                    if (pos == 0)
                        throw new WrongScenarioItemException.AbortAtBeginning();
                    items.add(new Abort());
                    break;
                default:
                    throw new WrongScenarioItemException.IllegalText(text);
            }
            ++pos;
        }
        this.scenario = items.toArray(new Item[items.size()]);
        this.progress = 0;
    }
    
    public void format(String format, Object... args) {
        String s = String.format(format, args);
        this.format_i(s);
    }

    public String readLine() {
        return this.readLine_i();
    }

    public String readLine(String format, Object... args) {
        String s = String.format(format, args);
        this.format_i(s);
        return this.readLine_i();
    }

    public char[] readPassword() {
        return readLine_i().toCharArray();
    }

    public char[] readPassword(String format, Object... args) {
        String s = String.format(format, args);
        this.format_i(s);
        return readLine_i().toCharArray();
    }
    
    public int getProgress() {
        return progress;
    }
    
    public int getScenarioLength() {
        return scenario.length;
    }
}
