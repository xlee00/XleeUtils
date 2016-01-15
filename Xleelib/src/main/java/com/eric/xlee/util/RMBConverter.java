/**
 * RMBConverter.java [V 1.0.0]
 * Classes : com.eric.xlee.util.RMBConverter
 * Xlee Create at 15/01/2016 11:18
 */
package com.eric.xlee.util;

import java.util.ArrayList;
import java.util.List;

/**
 * com.eric.xlee.util.RMBConverter
 *
 * @author Xlee <br/>
 *         Create at 15/01/2016 11:18
 *         from http://www.codeweblog.com/java%E5%AE%9E%E7%8E%B0%E6%B5%AE%E7%82%B9%E6%95%B0%E8%BD%AC%E4%BA%BA%E6%B0%91%E5%B8%81%E7%9A%84%E5%B0%8F%E4%BE%8B%E5%AD%90/
 *         原版Bug：角与分发生错误，并且角和分数字位置有误。 结果：已经修复该Bug
 */
public class RMBConverter {
    private static final String TAG = RMBConverter.class.getSimpleName();

    private static final String[] RMB_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String[] RMB_ATTR = {"元", "十", "佰", "仟", "万", "十", "百", "仟", "亿", "十", "佰", "仟", "万", "十",
            "佰", "仟"};
    private static final String[] RMB_FEN = {"角", "分"};

    public String change2RMB(double counts) {
        StringBuffer rsBuf = new StringBuffer();

        if (counts == 0L) {
            return "零";
        }

        if (counts < 0)
            rsBuf.append("负");

        String zheng = strConverse(String.valueOf((long) counts));
        String xiao = strConverse(String.valueOf(Math.round((counts - (long) counts) * 100)));

        char[] zhengArray = zheng.toCharArray();
        char[] xiaoArray = xiao.toCharArray();

        int zLength = zhengArray.length;
        int xLength = xiaoArray.length;

        List<String> zhengList = new ArrayList<String>();

        for (int i = 0; i < zLength; i++) {
            int value = Integer.parseInt("" + zhengArray[i]);
            if (value == 0 && (i != 4) && (i != 8) && i != 0) {
                zhengList.add(RMB_NUMBER[value]);
            } else {
                zhengList.add(RMB_NUMBER[value] + RMB_ATTR[i]);
            }
        }

        for (int i = zhengList.size() - 1; i >= 0; i--) {
            rsBuf.append(zhengList.get(i));
        }

        for (int i = xLength - 1; i >= 0; i--) {
            int value = Integer.parseInt("" + xiaoArray[i]);
            if (value > 0) {
                rsBuf.append(RMB_NUMBER[value]).append(RMB_FEN[xLength - i - 1]);
            }
        }

        String rs = rsBuf.toString();
        rs = rs.replaceAll("零+", "零");
        rs = rs.replaceAll("零元", "元");
        rs = rs.replaceAll("零万", "万");
        rs = rs.replaceAll("零亿", "亿");
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }

    private String strConverse(String str) {
        StringBuffer rs = new StringBuffer();
        char[] strArray = str.toCharArray();
        for (int i = str.length() - 1; i >= 0; i--) {
            rs.append(strArray[i]);
        }
        return rs.toString();
    }

    public static void main(String args[]) {
        RMBConverter c2 = new RMBConverter();
        System.out.println(c2.change2RMB(2001004.10));
        System.out.println(c2.change2RMB(2000000.10));
        System.out.println(c2.change2RMB(0L));
        System.out.println(c2.change2RMB(0.00));
        System.out.println(c2.change2RMB(1121000010000000L));
        System.out.println(c2.change2RMB(101.1));
        System.out.println(c2.change2RMB(100.21));
    }
}
