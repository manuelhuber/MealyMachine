package manuel_huber.InputStrategy;

import manuel_huber.model.Symbol;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SystemInput implements InputStrategy {
    @Override
    public Symbol putIn(List<Symbol> allowedAlphabet) {
        Scanner sc = new Scanner(System.in);
        Optional<Symbol> inputSymbol = Optional.empty();

        while (!inputSymbol.isPresent()) {
            String input = sc.nextLine();
            inputSymbol = allowedAlphabet.stream().filter(symbol -> symbol.getSymbol().equals(input)).findFirst();
            if (!inputSymbol.isPresent()) {
                System.out.println("Unknown input. Please use one of the following strings:");
                System.out.println(allowedAlphabet.stream().map(Symbol::getSymbol).collect(Collectors.joining(", ")));
            }
        }

        return inputSymbol.get();
    }
}
