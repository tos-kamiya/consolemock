package consolemock;

import static consolemock.Repr.toPrintableRepresentation;

import java.util.ArrayList;

import consolemock.AbstractConsole;

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
            return String.format("Abort()");
        }
    }
    
    private String readLine_i() {
        if (progress >= scenario.length)
            throw new ScenarioConsoleException.ExhaustdButReadLine();
        int pos = progress++;
        Item item = scenario[pos];
        if (item instanceof Abort)
            throw new ScenarioConsoleException.Abort();
        try {
            ReadLine r = (ReadLine)item;
            return r.text;
        } catch (ClassCastException e) {
            Format w = (Format)item;
            throw new ScenarioConsoleException.FormatExpectedButReadLine(pos, w);
        }
    }
    
    private void format_i(String actualWriteText) {
        if (progress >= scenario.length)
            throw new ScenarioConsoleException.ExhaustdButFormat(actualWriteText);
        int pos = progress++;
        Item item = scenario[pos];
        if (item instanceof Abort)
            throw new ScenarioConsoleException.Abort();
        try {
            Format w = (Format)item;
            if (! actualWriteText.equals(w.text))
                throw new ScenarioConsoleException.FormatWrongText(pos, w, actualWriteText);
        } catch (ClassCastException e) {
            ReadLine r = (ReadLine)item;
            throw new ScenarioConsoleException.ReadLineExpectedButFormat(pos, r, actualWriteText);
        }
    }
    
    public boolean isScenarioDone() {
        return progress >= scenario.length;
    }

    public ScenarioConsole(String[] senario) {
        ArrayList<Item> items = new ArrayList<Item>();
        for (String message : senario) {
            assert message != null;
            assert message.length() >= 2;
            String head = message.substring(0, 2);
            String tail = message.substring(2);
            switch (head) {
            case "$ ":
                items.add(new ReadLine(tail));
                break;
            case "> ":
                items.add(new Format(tail));
                break;
            case "! ":
                items.add(new Abort());
                break;
            default:
                System.err.println("head = " + head);
                assert head.equals("$ ") || head.equals("> ") || head.equals("! ");
                assert false; // never reach here
            }
        }
        this.scenario = items.toArray(new Item[0]);
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
    
    public int getSenarioLength() {
        return scenario.length;
    }
}
