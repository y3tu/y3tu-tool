package com.y3tu.tool.core.date;

/**
 * 季度枚举<br>
 *
 * @author Looly
 * @see #SPRING
 * @see #SUMMER
 * @see #AUTUMN
 * @see #WINTER
 * @deprecated 请使用{@link Quarter}代替
 */
@Deprecated
public enum Season {

    /**
     * 春季（第一季度）
     */
    SPRING(1),
    /**
     * 夏季（第二季度）
     */
    SUMMER(2),
    /**
     * 秋季（第三季度）
     */
    AUTUMN(3),
    /**
     * 冬季（第四季度）
     */
    WINTER(4);

    // ---------------------------------------------------------------
    private int value;

    private Season(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    /**
     * 将 季度int转换为Season枚举对象<br>
     *
     * @param intValue 季度int表示
     * @return {@link Season}
     * @see #SPRING
     * @see #SUMMER
     * @see #AUTUMN
     * @see #WINTER
     * @deprecated 使用@{@link Quarter} 替代
     */
    @Deprecated
    public static Season of(int intValue) {
        switch (intValue) {
            case 1:
                return SPRING;
            case 2:
                return SUMMER;
            case 3:
                return AUTUMN;
            case 4:
                return WINTER;
            default:
                return null;
        }
    }
}
