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

    /**
     * Reads a JSON file that contains a single message and returns the corresponding Symbol from the allowed alphabet
     *
     * @param deleteAfterReading delete the file after reading
     * @return a symbol (from the allowedAlphabet)
     * @throws IOException
     */
    static Symbol getSymbolFromFile(Path path, List<Symbol> allowedAlphabet, boolean deleteAfterReading) throws IOException {
        Message message = readMessageFile(path);
        if (message == null) throw new IOException("no message found");

        /*
        We assume the requirement is that only the type of the message is relevant
        So we ignore the payload and only use the type to look for the input symbol
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
     * @param path to a JSON file
     * @return the message (might be null if the content of the file is not valid)
     */
    private static Message readMessageFile(Path path) {
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
