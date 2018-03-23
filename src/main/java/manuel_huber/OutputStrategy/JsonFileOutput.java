package manuel_huber.OutputStrategy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manuel_huber.model.Symbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class JsonFileOutput implements OutputStrategy {
    private static final Path BASE_PATH = FileSystems.getDefault().getPath("src", "main", "resources", "output");
    private static int COUNTER = 0;

    @Override
    public void putOut(Symbol symbol) {
        File file = new File(BASE_PATH.resolve(COUNTER++ + ".msg").toString());
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
}
