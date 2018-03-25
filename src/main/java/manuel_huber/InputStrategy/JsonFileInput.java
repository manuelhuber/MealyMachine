package manuel_huber.InputStrategy;

import manuel_huber.model.Constants;
import manuel_huber.model.Message;
import manuel_huber.model.Symbol;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import static manuel_huber.InputStrategy.JsonReaderUtil.readFile;

/**
 * Reads {@link Message}s from JSON files in the resources/input directory.
 * Every file needs to contain 1 valid JSON string of a {@link Message} object
 */
public class JsonFileInput implements InputStrategy {

    private static final Path BASE_PATH = Constants.RESOURCE_PATH.resolve("input");

    @Override
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet) {
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(BASE_PATH, "*" + Constants.MESSAGE_SUFFIX);
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
                    Path file = pathIterator.next();
                    try {
                        return readFile(file, allowedAlphabet, true);
                    } catch (IOException e) {
                        throw new RuntimeException("The file " + file.toString() + " contained invalid data");
                    }
                }
            };

        } catch (IOException e) {
            throw new RuntimeException("No input files found ind the directory " + BASE_PATH.toAbsolutePath());
        }

    }

    private Symbol wrongInput(List<Symbol> allowedAlphabet) {
        unknownSymbolMessage(allowedAlphabet);
        throw new RuntimeException("Such is life");
    }

}
