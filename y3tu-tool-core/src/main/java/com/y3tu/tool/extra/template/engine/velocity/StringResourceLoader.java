package com.y3tu.tool.extra.template.engine.velocity;

import com.y3tu.tool.core.util.CharsetUtil;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringResourceLoader extends ResourceLoader {

    @Override
    public void init(ExtendedProperties configuration) {

    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        return new ByteArrayInputStream(source.getBytes(CharsetUtil.CHARSET_UTF_8));
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }

}