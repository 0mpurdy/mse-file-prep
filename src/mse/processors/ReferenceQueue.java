package mse.processors;

import mse.data.author.Author;
import mse.common.config.Config;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mj_pu_000 on 11/09/2015.
 */
public class ReferenceQueue extends ConcurrentLinkedQueue<ReferenceQueueItem> {

    private Author author;
    private Config cfg;

    public ReferenceQueue(Author author, Config cfg) {
        this.author = author;
        this.cfg = cfg;
    }

    public Author getAuthor() {
        return author;
    }

    public Config getConfig() {
        return cfg;
    }
}
