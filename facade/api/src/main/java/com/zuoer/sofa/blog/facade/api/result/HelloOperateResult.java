/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.facade.api.result;

import com.zuoer.sofa.blog.base.result.ResultBase;

/**
 *
 * @author zuoer
 *
 * @version $Id: HelloOperateResult.java, v 0.1 2019年11月28日 下午5:57:03 zuoer Exp $
 */
public class HelloOperateResult extends ResultBase {

    /**
     * 结果
     */
    private String resultStr;

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }
}
