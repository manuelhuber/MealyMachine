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

    public JsonFileWatchInput() {
        init();
    }

    @Override
    public Iterator<Symbol> putIn(List<Symbol> allowedAlphabet) {
        return new Iterator<Symbol>() {
            @Override
            public boolean hasNext() {
                try {
                    fillQueueFromKey(watchService.take(), allowedAlphabet);
                    return true;
                } catch (InterruptedException | IOException e) {
                    return false;
                }
            }

            @Override
            public Symbol next() {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    System.err.println("Error taking from queue");
                    return null;
                }
            }
        };
    }

    public void init() {
        new Thread(() -> {
            try {
                watchService = FileSystems.getDefault().newWatchService();
                BASE_PATH.register(watchService, ENTRY_CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fillQueueFromKey(WatchKey key, List<Symbol> allowedAlphabet) throws IOException, InterruptedException {
        // process events
        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent<Path> ev = (WatchEvent<Path>) event;
            Path filename = BASE_PATH.resolve(ev.context());
            queue.put(JsonReaderUtil.readFile(filename.toFile(), allowedAlphabet, true));
        }
        // reset the key
        boolean valid = key.reset();
    }
}
