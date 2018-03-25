package manuel_huber.InputStrategy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import manuel_huber.model.Message;
import manuel_huber.model.Symbol;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

class JsonReaderUtil {
    static Symbol readFile(Path path, List<Symbol> allowedAlphabet, boolean deleteAfterReading) throws IOException {
        Message message = fileToMessage(path);
        if (message == null) throw new IOException("no message found");

                        /*
                         * We assume the requirement is that only the type of the message is relevant
                         * So we ignore the payload and only use the type to look for the input symbol
                         */
        Optional<Symbol> symbolOptional =
                allowedAlphabet.stream().filter(sym ->
                        sym.getSymbol().equals(message.getType())
                ).findAny();

        if (!symbolOptional.isPresent()) {
            throw new IOException("No symbol found");
        }

        // KILL! MAIM! BURN! KILL! MAIM! BURN! KILL! MAIM! BURN!
        if (deleteAfterReading) {
            Files.delete(path);
        }

        return symbolOptional.get();

    }

    /**
     * Transforms a file (which contains a valid JSON string of a Message) to a message
     *
     * @param path nees to contain exactly one string which needs to be valid JSON
     * @return the message (might be null)
     */
    private static Message fileToMessage(Path path) {
        // We use the GSON library to work with JSON - and they need a token to know what class the data has
        Type messageType = new TypeToken<Message>() {
        }.getType();
        Gson gson = new Gson();
        Message message = null;
        try (BufferedReader in = Files.newBufferedReader(path);
             JsonReader reader = new JsonReader(in)) {
            message = gson.fromJson(reader, messageType);
            // Close the stream or we won't be able to delete the file
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
