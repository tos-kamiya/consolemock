package consolemock;

import static consolemock.Repr.toPrintableRepresentation;

public class WrongScenarioItemException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public WrongScenarioItemException(String message) {
        super(message);
    }
    
    public WrongScenarioItemException() {
    }
    
    public static class IllegalText extends WrongScenarioItemException {
        private static final long serialVersionUID = 1L;

        public IllegalText(String text) {
            super(toPrintableRepresentation(text));
        }
    }
    
    public static class AbortAtBeginning extends WrongScenarioItemException {
        private static final long serialVersionUID = 1L;
    }
}
