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
import java.util.List;
import java.util.Optional;

class JsonReaderUtil {
    static Symbol readFile(File file, List<Symbol> allowedAlphabet, boolean deleteAfterReading) throws IOException {
        Message message = fileToMessage(file);
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
        if (deleteAfterReading && !file.delete()) {
            System.err.println("Couldn't delete file " + file.getAbsolutePath());
        }

        return symbolOptional.get();

    }

    /**
     * Transforms a file (which contains a valid JSON string of a Message) to a message
     *
     * @param file nees to contain exactly one string which needs to be valid JSON
     * @return the message
     * @throws IOException welp, not much you can do about that ¯\_(ツ)_/¯
     */
    private static Message fileToMessage(File file) throws IOException {
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
