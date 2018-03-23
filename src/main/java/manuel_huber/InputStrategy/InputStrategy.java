package manuel_huber.InputStrategy;

import manuel_huber.model.Symbol;

import java.util.Iterator;
import java.util.List;

/**
 * Strategy to read symbols as input for the machine
 */
public interface InputStrategy {

    /**
     * Returns a (possibly infinite) iterator for input symbols
     *
     * @param allowedAlphabet A list of all valid inpus
     * @return iterator of symbols
     */
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet);
}
