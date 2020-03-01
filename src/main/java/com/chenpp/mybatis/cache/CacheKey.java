package com.chenpp.mybatis.cache;

import java.util.ArrayList;
import java.util.List;


/**
 * 2020/2/29
 * created by chenpp
 */
public class CacheKey {

    private static final int DEFAULT_MULTIPLYER = 37;
    private static final int DEFAULT_HASHCODE = 17;
    private final int multiplier;
    private int hashcode;
    private int count;
    private List<Object> updateList;

    public CacheKey() {
        this.hashcode = DEFAULT_HASHCODE;
        this.multiplier = DEFAULT_MULTIPLYER;
        this.count = 0;
        this.updateList = new ArrayList();
    }


    public void update(Object object) {
        int baseHashCode = object == null ? 1 : object.hashCode();
        this.count++;
        baseHashCode *= this.count;
        this.hashcode = this.multiplier * this.hashcode + baseHashCode;
        this.updateList.add(object);
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof CacheKey)) {
            return false;
        } else {
            CacheKey cacheKey = (CacheKey)object;
            if (this.hashcode != cacheKey.hashcode) {
                return false;
            }  else if (this.count != cacheKey.count) {
                return false;
            } else {
                for(int i = 0; i < this.updateList.size(); ++i) {
                    Object thisObject = this.updateList.get(i);
                    Object thatObject = cacheKey.updateList.get(i);
                    if (thisObject.equals(thatObject)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int hashCode() {
        return this.hashcode;
    }

}
