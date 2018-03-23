package manuel_huber.InputStrategy;

import manuel_huber.model.Symbol;

import java.util.Iterator;
import java.util.List;

public interface InputStrategy {
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet);
}
