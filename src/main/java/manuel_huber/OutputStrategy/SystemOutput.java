package manuel_huber.OutputStrategy;

import manuel_huber.model.Symbol;

public class SystemOutput implements OutputStrategy {
    @Override
    public void putOut(Symbol symbol) {
        System.out.println("Output:");
        System.out.println(symbol.getSymbol());
    }
}
