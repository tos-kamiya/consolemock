package consolemock;

import consolemock.SenarioConsole.Format;
import consolemock.SenarioConsole.ReadLine;

import static consolemock.Repr.toPrintableRepresentation;

public class SenarioConsoleException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public SenarioConsoleException() {}
    
    public SenarioConsoleException(String message) {
        super(message);
    }

    public SenarioConsoleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SenarioConsoleException(Throwable cause) {
        super(cause);
    }
    
    public static class ExhaustdButFormat extends SenarioConsoleException {
        private static final long serialVersionUID = 1L;

        public final String actualWriteText;

        public ExhaustdButFormat(String actualWriteText) {
            this.actualWriteText = actualWriteText;
        }
        
        public String toString() {
            return String.format("ExhaustdButFormat(%s)", toPrintableRepresentation(actualWriteText));
        }
    }
    
    public static class ExhaustdButReadLine extends SenarioConsoleException {
        private static final long serialVersionUID = 1L;

        public ExhaustdButReadLine() {
        }
    }
    
    public static class ReadLineExpectedButFormat extends SenarioConsoleException {
        private static final long serialVersionUID = 1L;

        public final int progress;
        public final ReadLine expectedRead;
        public final String actualWriteText;

        public ReadLineExpectedButFormat(int progress, ReadLine expectedRead, String actualWriteText) {
            this.progress = progress;
            this.expectedRead = expectedRead;
            this.actualWriteText = actualWriteText;
        }

        public String toString() {
            return String.format("ReadLineExpectedButFormat(%d, %s, %s)", progress, 
                    toPrintableRepresentation(expectedRead.text), toPrintableRepresentation(actualWriteText));
        }
    }

    public static class FormatExpectedButReadLine extends SenarioConsoleException {
        private static final long serialVersionUID = 1L;

        public final int progress;
        public final Format expectedWrite;

        public FormatExpectedButReadLine(int progress, Format expectedWrite) {
            this.progress = progress;
            this.expectedWrite = expectedWrite;
        }

        public String toString() {
            return String.format("FormatExpectedButReadLine(%d, %s)", progress, 
                    toPrintableRepresentation(expectedWrite.text));
        }
    }

    public static class FormatWrongText extends SenarioConsoleException {
        private static final long serialVersionUID = 1L;

        public final int progress;
        public final Format expectedWrite;
        public final String actualWriteText;

        public FormatWrongText(int progress, Format expectedWrite, String actualWriteText) {
            this.progress = progress;
            this.expectedWrite = expectedWrite;
            this.actualWriteText = actualWriteText;
        }

        public String toString() {
            return String.format("FormatWrongText(%d, %s, %s)", progress, 
                    toPrintableRepresentation(expectedWrite.text),
                    toPrintableRepresentation(actualWriteText));
        }
    }


}
