package com.ciyuanplus.mobile.net;

import java.util.LinkedHashMap;

/**
 * Created by Alen on 2017/2/13.
 */

public class ApiParamMap extends LinkedHashMap<String, ApiParamMap.ParamData> {
    public ApiParamMap() {
        super();
    }

    public static class ParamData {
        public String value;
        public int index;
        public ParamData(String value) {
            this.value = value;
        }

        public ParamData(int index) {
            this.index = index;
        }
    }
}
