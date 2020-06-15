package net.b521.pojo.helper;

import java.util.Comparator;

/**
 * @author Allen
 * @Date: 2019/03/21 17:41
 * @Description: 关键字排序比较器
 * @Version 1.0
 **/
public class PairKeywordComparator implements Comparator<PairKeyword> {

    @Override
    public int compare(PairKeyword keyword0, PairKeyword keyword1) {
        return keyword0.getIndex() > keyword1.getIndex() ? 1 : -1;
    }
}
