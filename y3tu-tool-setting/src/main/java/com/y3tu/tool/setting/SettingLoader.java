package com.y3tu.tool.setting;

import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.IORuntimeException;
import com.y3tu.tool.core.io.IoUtil;
import com.y3tu.tool.core.text.CharUtil;
import com.y3tu.tool.core.text.CharsetUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.core.util.ReUtil;
import com.y3tu.tool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Setting文件加载器
 * 
 * @author Looly
 *
 */
@Slf4j
public class SettingLoader {

	/** 注释符号（当有此符号在行首，表示此行为注释） */
	private final static char COMMENT_FLAG_PRE = '#';
	/** 赋值分隔符（用于分隔键值对） */
	private final static char ASSIGN_FLAG = '=';
	/** 变量名称的正则 */
	private String reg_var = "\\$\\{(.*?)\\}";

	/** 本设置对象的字符集 */
	private Charset charset;
	/** 是否使用变量 */
	private boolean isUseVariable;
	/** GroupedMap */
	private GroupedMap groupedMap;

	/**
	 * 构造
	 * 
	 * @param groupedMap GroupedMap
	 */
	public SettingLoader(GroupedMap groupedMap) {
		this(groupedMap, CharsetUtil.CHARSET_UTF_8, false);
	}

	/**
	 * 构造
	 * 
	 * @param groupedMap GroupedMap
	 * @param charset 编码
	 * @param isUseVariable 是否使用变量
	 */
	public SettingLoader(GroupedMap groupedMap, Charset charset, boolean isUseVariable) {
		this.groupedMap = groupedMap;
		this.charset = charset;
		this.isUseVariable = isUseVariable;
	}

	/**
	 * 加载设置文件
	 * 
	 * @param urlResource 配置文件URL
	 * @return 加载是否成功
	 */
	public boolean load(URL urlResource) {
		if (urlResource == null) {
			throw new NullPointerException("Null setting url define!");
		}
		log.debug("Load setting file [{}]", urlResource);
		InputStream settingStream = null;
		try {
			settingStream = URLUtil.getStream(urlResource);
			load(settingStream);
		} catch (Exception e) {
			log.error("Load setting error!",e);
			return false;
		} finally {
			IoUtil.close(settingStream);
		}
		return true;
	}

	/**
	 * 加载设置文件。 此方法不会关闭流对象
	 * 
	 * @param settingStream 文件流
	 * @return 加载成功与否
	 * @throws IOException IO异常
	 */
	public boolean load(InputStream settingStream) throws IOException {
		groupedMap.clear();
		BufferedReader reader = null;
		try {
			reader = IoUtil.getReader(settingStream, this.charset);
			// 分组
			String group = null;

			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				line = line.trim();
				// 跳过注释行和空行
				if (StringUtils.isBlank(line) || StringUtils.startWith(line, COMMENT_FLAG_PRE)) {
					continue;
				}

				// 记录分组名
				if (StringUtils.isSurround(line, CharUtil.BRACKET_START, CharUtil.BRACKET_END)) {
					group = line.substring(1, line.length() - 1).trim();
					continue;
				}

				final String[] keyValue = StringUtils.split(line, ASSIGN_FLAG);
				// 跳过不符合简直规范的行
				if (keyValue.length < 2) {
					continue;
				}

				String value = keyValue[1].trim();
				// 替换值中的所有变量变量（变量必须是此行之前定义的变量，否则无法找到）
				if (isUseVariable) {
					value = replaceVar(group, value);
				}
				this.groupedMap.put(group, keyValue[0].trim(), value);
			}
		} finally {
			IoUtil.close(reader);
		}
		return true;
	}

	/**
	 * 设置变量的正则<br>
	 * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
	 * 
	 * @param regex 正则
	 */
	public void setVarRegex(String regex) {
		this.reg_var = regex;
	}

	/**
	 * 持久化当前设置，会覆盖掉之前的设置<br>
	 * 持久化会不会保留之前的分组
	 * 
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(FileUtil.asBufferedWriter(absolutePath, charset));
			store(writer);
		} catch (IOException e) {
			throw new IORuntimeException(e, "Store Setting to [{}] error!", absolutePath);
		} finally {
			IoUtil.close(writer);
		}
	}

	/**
	 * 存储到Writer
	 * 
	 * @param writer Writer
	 * @throws IOException IO异常
	 */
	private void store(PrintWriter writer) throws IOException {
		for (Entry<String, LinkedHashMap<String, String>> groupEntry : this.groupedMap.entrySet()) {
			writer.println(StringUtils.format("{}{}{}", CharUtil.BRACKET_START, groupEntry.getKey(), CharUtil.BRACKET_END));
			for (Entry<String, String> entry : groupEntry.getValue().entrySet()) {
				writer.println(StringUtils.format("{} {} {}", entry.getKey(), ASSIGN_FLAG, entry.getValue()));
			}
		}
	}

	// ----------------------------------------------------------------------------------- Private method start
	/**
	 * 替换给定值中的变量标识
	 * 
	 * @param group 所在分组
	 * @param value 值
	 * @return 替换后的字符串
	 */
	private String replaceVar(String group, String value) {
		// 找到所有变量标识
		final Set<String> vars = ReUtil.findAll(reg_var, value, 0, new HashSet<String>());
		String key;
		for (String var : vars) {
			key = ReUtil.get(reg_var, var, 1);
			if (StringUtils.isNotBlank(key)) {
				// 查找变量名对应的值
				String varValue = this.groupedMap.get(group, key);
				if (null != varValue) {
					// 替换标识
					value = value.replace(var, varValue);
				} else {
					// 跨分组查找
					final List<String> groupAndKey = ArrayUtil.asList(StringUtils.split(key, CharUtil.DOT));
					if (groupAndKey.size() > 1) {
						varValue = this.groupedMap.get(groupAndKey.get(0), groupAndKey.get(1));
						if (null != varValue) {
							// 替换标识
							value = value.replace(var, varValue);
						}
					}
				}
			}
		}
		return value;
	}
	// ----------------------------------------------------------------------------------- Private method end
}