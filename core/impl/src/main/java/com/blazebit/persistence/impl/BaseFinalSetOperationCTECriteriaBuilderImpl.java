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

import com.blazebit.persistence.BaseFinalSetOperationBuilder;
import com.blazebit.persistence.BaseOngoingFinalSetOperationBuilder;
import com.blazebit.persistence.spi.SetOperationType;

/**
 *
 * @param <T> The query result type
 * @author Christian Beikov
 * @since 1.1.0
 */
public class BaseFinalSetOperationCTECriteriaBuilderImpl<T, X extends BaseFinalSetOperationBuilder<T, X>> extends BaseFinalSetOperationBuilderImpl<T, X, BaseFinalSetOperationCTECriteriaBuilderImpl<T, X>> implements BaseOngoingFinalSetOperationBuilder<T, X>, CTEInfoBuilder {

    protected final T result;
    protected final CTEBuilderListener listener;
    protected final FullSelectCTECriteriaBuilderImpl<?> initiator;
    protected final CTEBuilderListenerImpl subListener;
    
    public BaseFinalSetOperationCTECriteriaBuilderImpl(MainQuery mainQuery, Class<T> clazz, T result, SetOperationType operator, boolean nested, CTEBuilderListener listener, FullSelectCTECriteriaBuilderImpl<?> initiator) {
        super(mainQuery, false, clazz, operator, nested, result);
        this.result = result;
        this.listener = listener;
        this.initiator = initiator;
        this.subListener = new CTEBuilderListenerImpl();
    }
    
    public FullSelectCTECriteriaBuilderImpl<?> getInitiator() {
        return initiator;
    }
    
    public T getResult() {
        return result;
    }

    public CTEBuilderListener getListener() {
        return listener;
    }

    public CTEBuilderListenerImpl getSubListener() {
        return subListener;
    }

    @Override
    public CTEInfo createCTEInfo() {
        return createCTEInfo(this, this);
    }
    
    private static CTEInfo createCTEInfo(AbstractCommonQueryBuilder<?, ?, ?, ?, ?> queryBuilder, AbstractCommonQueryBuilder<?, ?, ?, ?, ?> target) {
        if (queryBuilder instanceof BaseFinalSetOperationCTECriteriaBuilderImpl<?, ?>) {
            BaseFinalSetOperationCTECriteriaBuilderImpl<?, ?> setOperationBuilder = (BaseFinalSetOperationCTECriteriaBuilderImpl<?, ?>) queryBuilder;
            
            if (setOperationBuilder.initiator == null) {
                return createCTEInfo(setOperationBuilder.setOperationManager.getStartQueryBuilder(), target);
            } else {
                List<String> attributes = setOperationBuilder.initiator.prepareAndGetAttributes();
                CTEInfo info = new CTEInfo(setOperationBuilder.initiator.cteName, setOperationBuilder.initiator.cteType, attributes, false, false, target, null);
                return info;
            }
        } else if (queryBuilder instanceof AbstractCTECriteriaBuilder<?, ?, ?, ?>) {
            AbstractCTECriteriaBuilder<?, ?, ?, ?> cteBuilder = (AbstractCTECriteriaBuilder<?, ?, ?, ?>) queryBuilder;
            List<String> attributes = cteBuilder.prepareAndGetAttributes();
            CTEInfo info = new CTEInfo(cteBuilder.cteName, cteBuilder.cteType, attributes, false, false, target, null);
            return info;
        }
        
        throw new IllegalArgumentException("Unsupported query builder type for creating a CTE info: " + queryBuilder);
    }

}
