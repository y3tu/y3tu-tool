package com.y3tu.tool.cache;

import com.y3tu.tool.cache.file.LFUFileCache;
import org.junit.Assert;
import org.junit.Test;


/**
 * 文件缓存单元测试
 * @author looly
 *
 */
public class FileCacheTest {
	@Test
	public void lfuFileCacheTest() {
		LFUFileCache cache = new LFUFileCache(1000, 500, 2000);
		Assert.assertNotNull(cache);
	}
}
