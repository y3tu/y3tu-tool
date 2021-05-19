package com.y3tu.tool.lowcode.common.util;

import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * @author y3tu
 */
public class Banner {

    private static final String name = "Tomato";
    private static final String defaultBanner = "" +
            "\\ \\   /___ /__ __| |   |   __ __| _ \\   _ \\  |     \n" +
            " \\   /   _ \\   |   |   |      |  |   | |   | |     \n" +
            "    |     ) |  |   |   |      |  |   | |   | |     \n" +
            "   _|  ____/  _|  \\___/      _| \\___/ \\___/ _____| ";

    public static void print() {
        printVersion();
    }

    private static void printVersion() {
        PrintStream printStream = System.out;
        String version = Banner.getVersion();
        version = version != null ? " (v" + version + ")" : "";
        StringBuilder padding = new StringBuilder();

        while (padding.length() < 42 - (version.length() + name.length())) {
            padding.append(" ");
        }

        printStream.println(AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, defaultBanner, AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT}));
        printStream.println();
        printStream.println(AnsiOutput.toString(new Object[]{AnsiColor.GREEN, " :: Y3tu-Tool-Low-Code :: ", AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, version}));
        printStream.println();
    }

    private static String getVersion() {
        String implementationVersion = Banner.class.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            return implementationVersion;
        } else {
            CodeSource codeSource = Banner.class.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return null;
            } else {
                URL codeSourceLocation = codeSource.getLocation();

                try {
                    URLConnection connection = codeSourceLocation.openConnection();
                    if (connection instanceof JarURLConnection) {
                        return getImplementationVersion(((JarURLConnection) connection).getJarFile());
                    } else {
                        JarFile jarFile = new JarFile(new File(codeSourceLocation.toURI()));
                        Throwable var5 = null;

                        String var6;
                        try {
                            var6 = getImplementationVersion(jarFile);
                        } catch (Throwable var16) {
                            throw var16;
                        } finally {
                            if (jarFile != null) {
                                if (var5 != null) {
                                    try {
                                        jarFile.close();
                                    } catch (Throwable var15) {
                                        var5.addSuppressed(var15);
                                    }
                                } else {
                                    jarFile.close();
                                }
                            }

                        }

                        return var6;
                    }
                } catch (Exception var18) {
                    return null;
                }
            }
        }
    }

    private static String getImplementationVersion(JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }
}
