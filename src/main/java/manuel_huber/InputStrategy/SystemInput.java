package manuel_huber.InputStrategy;

import manuel_huber.model.Symbol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SystemInput implements InputStrategy {
    @Override
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Stream<String> stream = in.lines();

        return stream.map(s ->
                allowedAlphabet
                        .stream()
                        .filter(symbol -> symbol.getSymbol().equals(s))
                        .findFirst())
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
