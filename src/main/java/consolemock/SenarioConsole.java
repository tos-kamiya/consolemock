package consolemock;

import static consolemock.Repr.toPrintableRepresentation;

import java.util.ArrayList;

import consolemock.AbstractConsole;

public class SenarioConsole implements AbstractConsole {
    private Item[] senario;
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
    
    private String readLine_i() {
        if (progress >= senario.length)
            throw new SenarioConsoleException.ExhaustdButReadLine();
        int pos = progress++;
        Item item = senario[pos];
        try {
            ReadLine r = (ReadLine)item;
            return r.text;
        } catch (ClassCastException e) {
            Format w = (Format)item;
            throw new SenarioConsoleException.FormatExpectedButReadLine(pos, w);
        }
    }
    
    private void format_i(String actualWriteText) {
        if (progress >= senario.length)
            throw new SenarioConsoleException.ExhaustdButFormat(actualWriteText);
        int pos = progress++;
        Item item = senario[pos];
        try {
            Format w = (Format)item;
            if (! actualWriteText.equals(w.text))
                throw new SenarioConsoleException.FormatWrongText(pos, w, actualWriteText);
        } catch (ClassCastException e) {
            ReadLine r = (ReadLine)item;
            throw new SenarioConsoleException.ReadLineExpectedButFormat(pos, r, actualWriteText);
        }
    }
    
    public boolean isDone() {
        return progress >= senario.length;
    }

    public SenarioConsole(String[] senario) {
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
            default:
                assert message.substring(0, 2).equals("$ ") || message.substring(0, 2).equals("> ");
                assert false; // never reach here
            }
        }
        this.senario = items.toArray(new Item[0]);
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
        return senario.length;
    }
}
