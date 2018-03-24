package manuel_huber;

import manuel_huber.InputStrategy.InputStrategy;
import manuel_huber.InputStrategy.JsonFileInput;
import manuel_huber.InputStrategy.JsonFileWatchInput;
import manuel_huber.InputStrategy.SystemInput;
import manuel_huber.OutputStrategy.JsonFileOutput;
import manuel_huber.OutputStrategy.OutputStrategy;
import manuel_huber.OutputStrategy.SystemOutput;
import manuel_huber.loader.XmlMealyLoader;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        JsonFileWatchInput fo = new JsonFileWatchInput();
        fo.init();

        XmlMealyLoader loader = new XmlMealyLoader();
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose input strategy 'JSON', 'JSON:WATCH' or 'CMD'");
        String input = sc.nextLine();
        InputStrategy inputStrategy;
        switch (input) {
            case "JSON":
                inputStrategy = new JsonFileInput();
                break;
            case "JSON:WATCH":
                inputStrategy = new JsonFileWatchInput();
                break;
            default:
                inputStrategy = new SystemInput();
        }

        System.out.println("If you want to use JSON files as output type 'JSON' otherwise the command line will be used");
        String output = sc.nextLine();
        OutputStrategy outputStrategy = output.equals("JSON") ? new JsonFileOutput() : new SystemOutput();

        loader
                .load()
                .setInputStrategy(inputStrategy)
                .setOutputStrategy(outputStrategy)
                .start();
    }
}
