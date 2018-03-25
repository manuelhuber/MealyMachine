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
    private List<Symbol> allowedAlphabet;
    private Thread watcherThread;

    @Override
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet) {
        this.allowedAlphabet = allowedAlphabet;
        this.init();

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

    public void init() {
        if (watcherThread != null) return;

        watcherThread = new Thread(() -> {
            try {
                watchService = FileSystems.getDefault().newWatchService();
                BASE_PATH.register(watchService, ENTRY_CREATE);
                boolean allIsFine;
                do {
                    // .take() is a blocking function - the watcher thread will wait here
                    allIsFine = fillQueueFromKey(watchService.take(), allowedAlphabet);
                } while (allIsFine);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        watcherThread.start();
    }

    private boolean fillQueueFromKey(WatchKey key, List<Symbol> allowedAlphabet) {
        try {
            // process events
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                Path filename = BASE_PATH.resolve(pathEvent.context());
                queue.put(JsonReaderUtil.readFile(filename.toFile(), allowedAlphabet, true));
            }
            // reset the key
            key.reset();
            return true;
        } catch (InterruptedException | IOException e) {
            return false;
        }
    }
}
