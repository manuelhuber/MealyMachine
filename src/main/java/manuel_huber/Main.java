package manuel_huber;

import manuel_huber.InputStrategy.SystemInput;
import manuel_huber.OutputStrategy.SystemOutput;
import manuel_huber.loader.XmlMealyLoader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        XmlMealyLoader loader = new XmlMealyLoader();
        loader
                .load()
                .setInputStrategy(new SystemInput())
//                .setInputStrategy(new JsonFileInput())
                .setOutputStrategy(new SystemOutput())
//                .setOutputStrategy(new JsonFileOutput())
                .start();
    }
}
