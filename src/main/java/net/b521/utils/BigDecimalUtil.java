package net.b521.utils;

import java.math.BigDecimal;

/**
 * Created by Allen
 * Date: 2019/03/08 16:33
 *
 * @Description: 科学运算浮点数精确度工具类
 **/
public class BigDecimalUtil {

    private BigDecimalUtil() {
    }

    /**
     * 加法操作
     * @param v1 加数
     * @param v2 被加数
     * @return 和
     */
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * 减法操作
     * @param v1 减数
     * @param v2 被减数
     * @return 差
     */
    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 乘法操作
     * @param v1 乘数
     * @param v2 被乘数
     * @return 积
     */
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 除法操作
     * @param v1 除数
     * @param v2 被除数
     * @return 商
     */
    public static BigDecimal div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//四舍五入,保留2位小数

        //除不尽的情况

    }

}
