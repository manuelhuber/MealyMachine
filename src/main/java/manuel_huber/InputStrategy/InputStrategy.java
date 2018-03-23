package manuel_huber.InputStrategy;

import manuel_huber.model.Symbol;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    Iterator<Symbol> putIn(List<Symbol> allowedAlphabet);

    /**
     * Yeah, just put an implementation in an interface. That's what they are for, right?!
     */
    default void unknownSymbolMessage(List<Symbol> allowedAlphabet) {
        System.out.println("Unknown input. Please use one of the following strings:");
        System.out.println(allowedAlphabet.stream().map(Symbol::getSymbol).collect(Collectors.joining(", ")));

    }
}
