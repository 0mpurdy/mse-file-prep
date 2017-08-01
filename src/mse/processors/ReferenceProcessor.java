package mse.processors;

import mse.common.log.ILogger;
import mse.data.author.Author;
import mse.data.author.AuthorIndex;
import mse.common.config.Config;
import mse.olddata.PreparePlatform;
import mse.helpers.FileHelper;

import java.io.File;

/**
 * Created by mjp on 10/09/2015.
 */
public class ReferenceProcessor extends Thread {

    private ReferenceQueue tokenQueue;
    private AuthorIndex authorIndex;
    private Config cfg;
    private Author author;
    private long totalTokenCount = 0;
    private PreparePlatform platform;

    public ReferenceProcessor(ReferenceQueue tokenQueue, PreparePlatform platform) {
        this.tokenQueue = tokenQueue;
        this.author = tokenQueue.getAuthor();
        this.authorIndex = new AuthorIndex(author);
        this.cfg = tokenQueue.getConfig();
        this.platform = platform;
    }

    @Override
    public void run() {
        while (!(isInterrupted() && tokenQueue.isEmpty())) {
            if (!tokenQueue.isEmpty()) {
                ReferenceQueueItem nextItem = tokenQueue.remove();
                authorIndex.incrementTokenCount(nextItem.getToken(), nextItem.volumeNumber, nextItem.pageNumber);
                totalTokenCount++;
            }
        }

        // clean up the index arrays
        authorIndex.cleanIndexArrays();

        // output index
        authorIndex.writeIndex(platform.getResDir() + File.separator + FileHelper.getIndexFile(author, File.separator));

        System.out.println("Total token count: " + totalTokenCount);

    }
}
