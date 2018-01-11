/*
 * Copyright 2014 - 2018 Blazebit.
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

package com.blazebit.persistence.deltaspike.data.impl.builder.postprocessor;

import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.deltaspike.data.impl.handler.CriteriaBuilderPostProcessor;

/**
 * @author Moritz Becker
 * @since 1.2.0
 */
public class PaginationCriteriaBuilderPostProcessor implements CriteriaBuilderPostProcessor {

    private final int firstResult;
    private final int maxResults;

    public PaginationCriteriaBuilderPostProcessor(int firstResult, int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }

    @Override
    public FullQueryBuilder<?, ?> postProcess(FullQueryBuilder<?, ?> queryBuilder) {
        return queryBuilder.page(firstResult, maxResults);
    }
}