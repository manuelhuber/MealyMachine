package manuel_huber.OutputStrategy;

import manuel_huber.model.Symbol;

public class JsonFileOutput implements OutputStrategy {
    @Override
    public void putOut(Symbol symbol) {
        System.out.println("Pretend to output JSON to a file or like whatever");
    }
}
