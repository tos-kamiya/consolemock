package consolemock;

import consolemock.ScenarioConsole.Format;
import consolemock.ScenarioConsole.ReadLine;

import static consolemock.Repr.toPrintableRepresentation;

public class ScenarioConsoleException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ScenarioConsoleException() {}
    
    public ScenarioConsoleException(String message) {
        super(message);
    }

    public ScenarioConsoleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ScenarioConsoleException(Throwable cause) {
        super(cause);
    }
    
    public static class ExhaustedButFormat extends ScenarioConsoleException {
        private static final long serialVersionUID = 1L;

        public final String actualWriteText;

        public ExhaustedButFormat(String actualWriteText) {
            this.actualWriteText = actualWriteText;
        }
        
        public String toString() {
            return String.format("ExhaustedButFormat(%s)", toPrintableRepresentation(actualWriteText));
        }
    }
    
    public static class ExhaustedButReadLine extends ScenarioConsoleException {
        private static final long serialVersionUID = 1L;

        public ExhaustedButReadLine() {
        }
    }
    
    public static class ReadLineExpectedButFormat extends ScenarioConsoleException {
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

    public static class FormatExpectedButReadLine extends ScenarioConsoleException {
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

    public static class FormatWrongText extends ScenarioConsoleException {
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

    public static class Abort extends ScenarioConsoleException {
        private static final long serialVersionUID = 1L;
        public String toString() {
            return "Abort()";
        }
    }

}
