package com.y3tu.tool.mail;

import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.setting.Setting;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MailAccountTest {
    @Test
    public void parseSettingTest() {

        Setting setting = new Setting("mail.setting",true);
        Console.log(setting.getByGroup("host",""));
        MailAccount account = GlobalMailAccount.INSTANCE.getAccount();
        Assert.assertNotNull(account.getCharset());
    }
}