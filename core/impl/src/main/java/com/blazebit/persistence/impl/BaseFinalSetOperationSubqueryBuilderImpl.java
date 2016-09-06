/*
 * Copyright 2015 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.persistence.impl;

import java.util.List;

import javax.persistence.Tuple;

import com.blazebit.persistence.BaseFinalSetOperationBuilder;
import com.blazebit.persistence.BaseOngoingFinalSetOperationBuilder;
import com.blazebit.persistence.impl.expression.Expression;
import com.blazebit.persistence.spi.SetOperationType;

/**
 *
 * @param <T> The query result type
 * @author Christian Beikov
 * @since 1.1.0
 */
public class BaseFinalSetOperationSubqueryBuilderImpl<T, X extends BaseFinalSetOperationBuilder<T, X>> extends BaseFinalSetOperationBuilderImpl<T, X, BaseFinalSetOperationSubqueryBuilderImpl<T, X>> implements BaseOngoingFinalSetOperationBuilder<T, X>, SubqueryInternalBuilder<T> {

    protected final T result;
    protected final SubqueryBuilderListener<T> listener;
    protected final SubqueryBuilderImpl<?> initiator;

    protected final SubqueryBuilderListenerImpl<T> subListener;
    
    @SuppressWarnings("unchecked")
    public BaseFinalSetOperationSubqueryBuilderImpl(MainQuery mainQuery, T result, SetOperationType operator, boolean nested, SubqueryBuilderListener<T> listener, SubqueryBuilderImpl<?> initiator) {
        super(mainQuery, false, (Class<T>) Tuple.class, operator, nested, result);
        this.result = result;
        this.listener = listener;
        this.initiator = initiator;
        this.subListener = new SubqueryBuilderListenerImpl<T>();
    }

    public SubqueryBuilderListener<T> getListener() {
        return listener;
    }
    
    public SubqueryBuilderListener<T> getSubListener() {
        return subListener;
    }

    public SubqueryBuilderImpl<?> getInitiator() {
        return initiator;
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public List<Expression> getSelectExpressions() {
        return getSelectExpressions(this);
    }

    private static List<Expression> getSelectExpressions(AbstractCommonQueryBuilder<?, ?, ?, ?, ?> queryBuilder) {
        if (queryBuilder instanceof BaseFinalSetOperationSubqueryBuilderImpl<?, ?>) {
            BaseFinalSetOperationSubqueryBuilderImpl<?, ?> setOperationBuilder = (BaseFinalSetOperationSubqueryBuilderImpl<?, ?>) queryBuilder;
            
            if (setOperationBuilder.initiator == null) {
                return getSelectExpressions(setOperationBuilder.setOperationManager.getStartQueryBuilder());
            } else {
                return setOperationBuilder.initiator.getSelectExpressions();
            }
        } else if (queryBuilder instanceof BaseSubqueryBuilderImpl<?, ?, ?, ?>) {
            BaseSubqueryBuilderImpl<?, ?, ?, ?> subqueryBuilder = (BaseSubqueryBuilderImpl<?, ?, ?, ?>) queryBuilder;
            return subqueryBuilder.getSelectExpressions();
        }
        
        throw new IllegalArgumentException("Unsupported query builder type for creating select expressions: " + queryBuilder);
    }
    
}
