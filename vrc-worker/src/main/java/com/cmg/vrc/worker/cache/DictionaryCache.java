package com.cmg.vrc.worker.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cmg on 17/09/15.
 */
public class DictionaryCache implements Serializable {

    private static final long serialVersionUID = -5728863186904758810L;

    public HashMap<String, List<String>> cache = new HashMap<String, List<String>>();
}
