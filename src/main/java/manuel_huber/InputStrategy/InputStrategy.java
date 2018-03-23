package manuel_huber.InputStrategy;

import manuel_huber.model.Symbol;

import java.util.List;

public interface InputStrategy {
    public Symbol putIn(List<Symbol> allowedAlphabet);
}
