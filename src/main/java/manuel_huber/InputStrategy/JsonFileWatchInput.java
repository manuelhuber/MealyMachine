package manuel_huber.InputStrategy;

import manuel_huber.model.Constants;
import manuel_huber.model.Symbol;

import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class JsonFileWatchInput implements InputStrategy {

    private static final Path BASE_PATH = Constants.RESOURCE_PATH.resolve("watch");
    private WatchService watchService;
    private BlockingQueue<Symbol> queue = new ArrayBlockingQueue<>(100);
    /**
     * The next input symbol
     */
    private Symbol next;
    private Thread watcherThread;
    private Thread mainThread;

    @Override
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet) {
        this.init(allowedAlphabet);

        return new Iterator<Symbol>() {

            @Override
            public boolean hasNext() {
                try {
                    if (next == null) {
                        next = queue.take(); // .take() is a blocking function call - the main thread will wait here
                    }
                    return true;
                } catch (InterruptedException e) {
                    return false;
                }
            }

            @Override
            public Symbol next() {
                Symbol symbol = next;
                next = null;
                return symbol;
            }
        };
    }

    /**
     * Initialises a watcher thread if necessary
     */
    private void init(List<Symbol> allowedAlphabet) {
        mainThread = Thread.currentThread();
        if (watcherThread != null) return;
        watcherThread = new Thread(() -> {
            try {
                watchService = FileSystems.getDefault().newWatchService();
                BASE_PATH.register(watchService, ENTRY_CREATE);
                WatchKey key;
                // .take() is a blocking function - the watcher thread will wait here
                while ((key = watchService.take()) != null) {
                    fillQueueFromKey(key, allowedAlphabet);
                }
                mainThread.interrupt();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        watcherThread.start();
    }

    /**
     * Takes the CREATE events from the key, makes Symbols out of the created files and fills the queue with them
     *
     * @param key             a WatchKey with CREATE events
     * @param allowedAlphabet all allowed symbols
     */
    private void fillQueueFromKey(WatchKey key, List<Symbol> allowedAlphabet) {
        try {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                // This key is registered only for ENTRY_CREATE events, but an OVERFLOW event can
                // occur if events are lost or discarded.
                if (kind != ENTRY_CREATE) {
                    continue;
                }

                // Append the file path to the base path
                Path filename = BASE_PATH.resolve(((WatchEvent<Path>) event).context());

                /*
                Ok, here's the deal:
                WatchService implementations are very platform dependant and depending on how the OS works you might
                get different events for what seems to the user like the same thing.
                When copying a file it might be created empty (and firing a CREATED event) and then be locked by the
                OS while it copies the content (and firing a MODIFIED event).
                Or it might just fire a CREATED event once everything is done.
                We don't know.
                So we just wait until the file is readable since that's what works on my machine.
                 */
                while (!Files.isReadable(filename)) {
                    Thread.sleep(10);
                }

                queue.put(JsonReaderUtil.getSymbolFromFile(filename, allowedAlphabet, false));
            }

            // We need to reset the key to access other events in the same key
            key.reset();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
