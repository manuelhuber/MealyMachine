package manuel_huber.InputStrategy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import manuel_huber.model.Message;
import manuel_huber.model.Symbol;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

/**
 * Reads {@link Message}s from JSON files in the resources/input directory.
 * Every file needs to contain 1 valid JSON string of a {@link Message} object
 */
public class JsonFileInput implements InputStrategy {

    private static final Path BASE_PATH = FileSystems.getDefault().getPath("src", "main", "resources", "input");

    @Override
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet) {
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(BASE_PATH);
            // This is a iterator over every filepath in the directory
            Iterator<Path> pathIterator = directoryStream.iterator();

            // Return a new iterator that returns the symbols of the files
            return new Iterator<Symbol>() {

                // The iterator has a next symbol, if there is a next file
                // We assume that every path actually leads to a valid file
                @Override
                public boolean hasNext() {
                    return pathIterator.hasNext();
                }

                @Override
                public Symbol next() {
                    // First get the next path and create a file
                    File file = pathIterator.next().toFile();
                    Message message;
                    try {
                        message = readFile(file);
                        // Find the symbol in the alphabet
                        Symbol symbol = allowedAlphabet.stream().filter(symbol1 -> symbol1.getSymbol().equals(message.getType())).findAny().get();
                        // KILL! MAIM! BURN! KILL! MAIM! BURN! KILL! MAIM! BURN!
                        file.delete();
                        return symbol;
                    } catch (IOException e) {
                        throw new RuntimeException("The file " + file.getAbsolutePath() + " contained invalid data");
                    }
                }
            };

        } catch (IOException e) {
            throw new RuntimeException("No input files found ind the directory " + BASE_PATH.toAbsolutePath());
        }

    }

    /**
     * Transforms a file (which contains a valid JSON string of a Message) to a message
     *
     * @param file nees to contain exactly one string which needs to be valid JSON
     * @return the message
     * @throws IOException welp, not much you can do about that ¯\_(ツ)_/¯
     */
    private Message readFile(File file) throws IOException {
        // We use the GSON library to work with JSON - and they need a token to know what class the data has
        Type messageType = new TypeToken<Message>() {
        }.getType();
        Gson gson = new Gson();
        FileReader in = new FileReader(file);
        JsonReader reader = new JsonReader(in);
        Message message = gson.fromJson(reader, messageType);
        // Close the stream or we won't be able to delete the file
        in.close();
        return message;
    }
}
