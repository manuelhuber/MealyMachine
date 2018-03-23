package manuel_huber.OutputStrategy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manuel_huber.model.Constants;
import manuel_huber.model.Symbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class JsonFileOutput implements OutputStrategy {
    private static final Path BASE_PATH = Constants.RESOURCE_PATH.resolve("output");
    private static int COUNTER = 0;

    public JsonFileOutput() {
        clearFolder();
    }

    @Override
    public void putOut(Symbol symbol) {
        File file = new File(BASE_PATH.resolve(COUNTER++ + Constants.MESSAGE_SUFFIX).toString());
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            try (Writer writer = new FileWriter(file)) {
                Gson gson = new GsonBuilder().create();
                gson.toJson(symbol, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFolder() {
        for (File file : BASE_PATH.toFile().listFiles())
            if (!file.isDirectory() && file.getPath().endsWith(Constants.MESSAGE_SUFFIX)) file.delete();
    }
}
