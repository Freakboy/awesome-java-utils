package net.b521.thread;

import net.b521.listener.ReaderFileListener;
import net.b521.utils.BatchReadFile;

/**
 * @author Allen
 * @Date: 2020/05/06 17:20
 * @Description: 线程读取文件
 * @version 1.0
 **/
public class ReadFileThread extends Thread {

    private ReaderFileListener processDataListeners;
    private String filePath;
    private long start;
    private long end;
    private Thread preThread;

    public ReadFileThread(ReaderFileListener processDataListeners,
                          long start,long end,
                          String file) {
        this(processDataListeners, start, end, file, null);
    }

    public ReadFileThread(ReaderFileListener processDataListeners,
                          long start,long end,
                          String file,
                          Thread preThread) {
        this.setName(this.getName()+"-ReadFileThread");
        this.start = start;
        this.end = end;
        this.filePath = file;
        this.processDataListeners = processDataListeners;
        this.preThread = preThread;
    }

    @Override
    public void run() {
        BatchReadFile readFile = new BatchReadFile();
        readFile.setReaderListener(processDataListeners);
        readFile.setEncode(processDataListeners.getEncode());
        try {
            readFile.readFileByLine(filePath, start, end + 1);
            if(this.preThread != null){
                this.preThread.join();
            }
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
}
