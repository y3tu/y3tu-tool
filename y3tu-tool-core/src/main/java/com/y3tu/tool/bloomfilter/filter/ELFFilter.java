package com.y3tu.tool.bloomfilter.filter;

import com.y3tu.tool.core.util.HashUtil;

public class ELFFilter extends AbstractFilter {

    public ELFFilter(long maxValue, int MACHINENUM) {
        super(maxValue, MACHINENUM);
    }

    public ELFFilter(long maxValue) {
        super(maxValue);
    }

    @Override
    public long hash(String str) {
        return HashUtil.elfHash(str) % size;
    }

}
