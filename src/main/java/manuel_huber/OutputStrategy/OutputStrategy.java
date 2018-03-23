package manuel_huber.OutputStrategy;

import manuel_huber.model.Symbol;

/**
 * A strategy to output the output symbols of the machine
 */
public interface OutputStrategy {

    public void putOut(Symbol symbol);
}
