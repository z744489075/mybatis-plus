/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.core.conditions.update;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.joining;

/**
 * <p>
 * Update 条件封装
 * </p>
 *
 * @author hubin miemie HCL
 * @since 2018-05-30
 */
public class UpdateWrapper<T> extends AbstractWrapper<T, String, UpdateWrapper<T>> implements Serializable {

    /**
     * SQL 更新字段内容，例如：name='1',age=2
     */
    private List<String> sqlSet = new ArrayList<>();

    public UpdateWrapper() {
        this(null);
    }

    public UpdateWrapper(T entity) {
        this.entity = entity;
        this.initNeed();
    }

    private UpdateWrapper(T entity, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs) {
        this.entity = entity;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
    }

    public LambdaUpdateWrapper<T> stream() {
        return new LambdaUpdateWrapper<>(entity, paramNameSeq, paramNameValuePairs);
    }

    @Override
    public String getSqlSet() {
        if (CollectionUtils.isEmpty(sqlSet)) {
            return null;
        }
        return SqlUtils.stripSqlInjection(sqlSet.stream().collect(joining(",")));
    }

    /**
     * <p>
     * SQL SET 字段
     * </p>
     *
     * @param column 字段
     * @param val    值
     * @return
     */
    public UpdateWrapper<T> set(String column, Object val) {
        return this.set(true, column, val);
    }

    /**
     * <p>
     * SQL SET 字段
     * </p>
     *
     * @param condition 操作条件
     * @param column    字段
     * @param val       值
     * @return
     */
    public UpdateWrapper<T> set(boolean condition, String column, Object val) {
        if (condition) {
            sqlSet.add(String.format("%s=%s", column, formatSql("{0}", val)));
        }
        return typedThis;
    }

    /**
     * <p>
     * SET 部分 SQL
     * </p>
     *
     * @param sql SET 部分内容
     * @return
     */
    public UpdateWrapper<T> setSql(String sql) {
        sqlSet.add(sql);
        return typedThis;
    }

    @Override
    protected String columnToString(String column) {
        return column;
    }

    @Override
    protected UpdateWrapper<T> instance(AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs) {
        return new UpdateWrapper<>(entity, paramNameSeq, paramNameValuePairs);
    }
}