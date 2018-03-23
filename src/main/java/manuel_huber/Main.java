package manuel_huber;

import manuel_huber.InputStrategy.SystemInput;
import manuel_huber.OutputStrategy.SystemOutput;
import manuel_huber.loader.XmlMealyLoader;

public class Main {

    public static void main(String[] args) {
        XmlMealyLoader loader = new XmlMealyLoader();
        loader
                .load()
                .setInputStrategy(new SystemInput())
                .setOutputStrategy(new SystemOutput())
                .start();
    }
}
