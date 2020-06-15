package net.b521.pojo.helper;

/**
 * @author Allen
 * @Date: 2019/03/21 17:40
 * @Description: 关键字对，用于列表排序
 * @Version 1.0
 **/
public class PairKeyword {

    private final String name;
    private final int index;

    public PairKeyword(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

}
