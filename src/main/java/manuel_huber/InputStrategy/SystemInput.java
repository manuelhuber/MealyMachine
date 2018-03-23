package manuel_huber.InputStrategy;

import manuel_huber.model.Symbol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reads string input from the user via {@link System}.in and creates an endless iterator
 */
public class SystemInput implements InputStrategy {
    @Override
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // A stream that reads the users command line input
        Stream<String> stream = in.lines();

        // We create an endless iterator from a stream
        return stream.map(s ->
                // Find a symbol that corresponds to the users input
                allowedAlphabet
                        .stream()
                        .filter(symbol -> symbol.getSymbol().equals(s))
                        .findFirst())
                // No symbol found -> filter it away
                .filter(symbolOptional -> {

                    boolean isPresent = symbolOptional.isPresent();
                    if (!isPresent) {
                        System.out.println("Unknown input. Please use one of the following strings:");
                        System.out.println(allowedAlphabet.stream().map(Symbol::getSymbol).collect(Collectors.joining(", ")));
                    }
                    return isPresent;
                }).map(Optional::get).iterator();
    }
}
