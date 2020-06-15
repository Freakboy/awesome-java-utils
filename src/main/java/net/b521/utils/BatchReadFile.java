package net.b521.utils;

import net.b521.listener.ReaderFileListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Allen
 * @Date: 2020/05/06 17:20
 * @Description: 批量读取文件
 * @Version 1.0
 **/
public class BatchReadFile {
    private final Logger LOGGER = LoggerFactory.getLogger(BatchReadFile.class);
    /**
     * 字符集UTF-8
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    /**
     * 字符集GBK
     */
    public static final String CHARSET_GBK = "GBK";
    /**
     * 字符集gb2312
     */
    public static final String CHARSET_GB2312 = "gb2312";
    /**
     * 文件内容分割符－逗号
     */
    public static final String SEPARATOR_COMMA = ",";

    private int bufSize = 1024;
    // 换行符
    private byte key = "\n".getBytes()[0];
    // 当前行数
    private long lineNum = 0;
    // 文件编码,默认为gb2312
    private String encode = CHARSET_GB2312;
    // 具体业务逻辑监听器
    private ReaderFileListener readerListener;

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setReaderListener(ReaderFileListener readerListener) {
        this.readerListener = readerListener;
    }

    /**
     * 获取准确开始位置
     * @param file
     * @param position
     * @return
     * @throws Exception
     */
    public long getStartNum(File file, long position) throws Exception {
        long startNum = position;
        FileChannel fcin = new RandomAccessFile(file, "r").getChannel();
        fcin.position(position);
        try {
            int cache = 1024;
            ByteBuffer rBuffer = ByteBuffer.allocate(cache);
            // 每次读取的内容
            byte[] bs = new byte[cache];
            // 缓存
            byte[] tempBs = new byte[0];
            while (fcin.read(rBuffer) != -1) {
                int rSize = rBuffer.position();
                rBuffer.rewind();
                rBuffer.get(bs);
                rBuffer.clear();
                byte[] newStrByte = bs;
                // 如果发现有上次未读完的缓存,则将它加到当前读取的内容前面
                if (null != tempBs) {
                    int tL = tempBs.length;
                    newStrByte = new byte[rSize + tL];
                    System.arraycopy(tempBs, 0, newStrByte, 0, tL);
                    System.arraycopy(bs, 0, newStrByte, tL, rSize);
                }
                // 获取开始位置之后的第一个换行符
                int endIndex = indexOf(newStrByte, 0);
                if (endIndex != -1) {
                    return startNum + endIndex;
                }
                tempBs = substring(newStrByte, 0, newStrByte.length);
                startNum += 1024;
            }
        } finally {
            fcin.close();
        }
        return position;
    }

    /**
     * 从设置的开始位置读取文件，一直到结束为止。如果 end设置为负数,刚读取到文件末尾
     * @param fullPath
     * @param start
     * @param end
     * @throws Exception
     */
    public void readFileByLine(String fullPath, long start, long end) throws Exception {
        File fin = new File(fullPath);
        if (!fin.exists()) {
            throw new FileNotFoundException("没有找到文件：" + fullPath);
        }
        FileChannel fileChannel = new RandomAccessFile(fin, "r").getChannel();
        fileChannel.position(start);
        try {
            ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);
            // 每次读取的内容
            byte[] bs = new byte[bufSize];
            // 缓存
            byte[] tempBs = new byte[0];
            String line;
            // 当前读取文件位置
            long nowCur = start;
            while (fileChannel.read(rBuffer) != -1) {
                int rSize = rBuffer.position();
                rBuffer.rewind();
                rBuffer.get(bs);
                rBuffer.clear();
                byte[] newStrByte;
                //去掉表头
                if(nowCur == start){
                    int firstLineIndex = indexOf(bs, 0);
                    int newByteLenth = bs.length-firstLineIndex-1;
                    newStrByte = new byte[newByteLenth];
                    System.arraycopy(bs, firstLineIndex+1, newStrByte, 0, newByteLenth);
                } else {
                    newStrByte = bs;
                }

                // 如果发现有上次未读完的缓存,则将它加到当前读取的内容前面
                if (null != tempBs && tempBs.length != 0) {
                    int tL = tempBs.length;
                    newStrByte = new byte[rSize + tL];
                    System.arraycopy(tempBs, 0, newStrByte, 0, tL);
                    System.arraycopy(bs, 0, newStrByte, tL, rSize);
                }
                // 是否已经读到最后一位
                boolean isEnd = false;
                nowCur += bufSize;
                // 如果当前读取的位数已经比设置的结束位置大的时候，将读取的内容截取到设置的结束位置
                if (end > 0 && nowCur > end) {
                    // 缓存长度 - 当前已经读取位数 - 最后位数
                    int l = newStrByte.length - (int) (nowCur - end);
                    newStrByte = substring(newStrByte, 0, l);
                    isEnd = true;
                }
                int fromIndex = 0;
                int endIndex = 0;
                // 每次读一行内容，以 key（默认为\n） 作为结束符
                while ((endIndex = indexOf(newStrByte, fromIndex)) != -1) {
                    byte[] bLine = substring(newStrByte, fromIndex, endIndex);
                    line = new String(bLine, 0, bLine.length, encode);
                    lineNum++;
                    // 输出一行内容，处理方式由调用方提供
                    readerListener.outLine(line.trim(), lineNum, false);
                    fromIndex = endIndex + 1;
                }
                // 将未读取完成的内容放到缓存中
                tempBs = substring(newStrByte, fromIndex, newStrByte.length);
                if (isEnd) {
                    break;
                }
            }
            // 将剩下的最后内容作为一行，输出，并指明这是最后一行
            String lineStr = new String(tempBs, 0, tempBs.length, encode);
            readerListener.outLine(lineStr.trim(), lineNum, true);
        } finally {
            fileChannel.close();
            fin.deleteOnExit();
        }
    }

    /**
     * 查找一个byte[]从指定位置之后的一个换行符位置
     *
     * @param src
     * @param fromIndex
     * @return
     * @throws Exception
     */
    private int indexOf(byte[] src, int fromIndex) throws Exception {
        for (int i = fromIndex; i < src.length; i++) {
            if (src[i] == key) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从指定开始位置读取一个byte[]直到指定结束位置为止生成一个全新的byte[]
     *
     * @param src
     * @param fromIndex
     * @param endIndex
     * @return
     * @throws Exception
     */
    private byte[] substring(byte[] src, int fromIndex, int endIndex) throws Exception {
        int size = endIndex - fromIndex;
        byte[] ret = new byte[size];
        System.arraycopy(src, fromIndex, ret, 0, size);
        return ret;
    }
}
