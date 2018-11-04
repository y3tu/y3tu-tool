package com.y3tu.tool.captcha;

/**
 * 图形验证码工具
 *
 * @author looly
 */
public class CaptchaUtil {

    /**
     * 创建线干扰的验证码，默认5位验证码，150条干扰线
     *
     * @param width  图片宽
     * @param height 图片高
     * @return {@link LineCaptcha}
     */
    public static LineCaptcha createLineCaptcha(int width, int height) {
        return new LineCaptcha(width, height);
    }

    /**
     * 创建线干扰的验证码
     *
     * @param width     图片宽
     * @param height    图片高
     * @param codeCount 字符个数
     * @param lineCount 干扰线条数
     * @return {@link LineCaptcha}
     */
    public static LineCaptcha createLineCaptcha(int width, int height, int codeCount, int lineCount) {
        return new LineCaptcha(width, height, codeCount, lineCount);
    }

    /**
     * 创建圆圈干扰的验证码，默认5位验证码，15个干扰圈
     *
     * @param width  图片宽
     * @param height 图片高
     * @return {@link CircleCaptcha}
     */
    public static CircleCaptcha createCircleCaptcha(int width, int height) {
        return new CircleCaptcha(width, height);
    }

    /**
     * 创建圆圈干扰的验证码
     *
     * @param width       图片宽
     * @param height      图片高
     * @param codeCount   字符个数
     * @param circleCount 干扰圆圈条数
     * @return {@link CircleCaptcha}
     */
    public static CircleCaptcha createCircleCaptcha(int width, int height, int codeCount, int circleCount) {
        return new CircleCaptcha(width, height, codeCount, circleCount);
    }

    /**
     * 创建扭曲干扰的验证码，默认5位验证码
     *
     * @param width  图片宽
     * @param height 图片高
     * @return {@link ShearCaptcha}
     */
    public static ShearCaptcha createShearCaptcha(int width, int height) {
        return new ShearCaptcha(width, height);
    }

    /**
     * 创建扭曲干扰的验证码，默认5位验证码
     *
     * @param width     图片宽
     * @param height    图片高
     * @param codeCount 字符个数
     * @param thickness 干扰线宽度
     * @return {@link ShearCaptcha}
     */
    public static ShearCaptcha createShearCaptcha(int width, int height, int codeCount, int thickness) {
        return new ShearCaptcha(width, height, codeCount, thickness);
    }
}
