package net.b521.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen
 * @Date: 2020/05/06 17:20
 * @Description: 读文件监听父类
 * @version 1.0
 **/
public abstract class ReaderFileListener<T> {

    // 一次读取行数，默认为1000
    private int readColNum = 1000;

    /**
     * 文件编码
     */
    private String encode;

    /**
     * 分批读取行列表
     */
    private List<String> rowList = new ArrayList<>();

    /**
     *其他参数
     */
    private T otherParams;

    /**
     * 每读取到一行数据，添加到缓存中
     * @param lineStr 读取到的数据
     * @param lineNum 行号
     * @param over 是否读取完成
     * @throws Exception
     */
    public void outLine(String lineStr, long lineNum, boolean over) throws Exception {
        if(null != lineStr && !lineStr.trim().equals("")){
            rowList.add(lineStr);
        }

        if (!over && (lineNum % readColNum == 0)) {
            output(rowList);
            rowList = new ArrayList<>();
        } else if (over) {
            output(rowList);
            rowList = new ArrayList<>();
        }
    }

    /**
     * 批量输出
     * @param stringList
     * @throws Exception
     */
    public abstract void output(List<String> stringList) throws Exception;

    /**
     * 设置一次读取行数
     * @param readColNum
     */
    protected void setReadColNum(int readColNum) {
        this.readColNum = readColNum;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public T getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(T otherParams) {
        this.otherParams = otherParams;
    }

    public List<String> getRowList() {
        return rowList;
    }

    public void setRowList(List<String> rowList) {
        this.rowList = rowList;
    }
}
