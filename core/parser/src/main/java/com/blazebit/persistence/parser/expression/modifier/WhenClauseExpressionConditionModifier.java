/*
 * Copyright 2014 - 2020 Blazebit.
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

package com.blazebit.persistence.parser.expression.modifier;

import com.blazebit.persistence.parser.expression.Expression;
import com.blazebit.persistence.parser.expression.WhenClauseExpression;

/**
 *
 * @author Moritz Becker
 * @author Christian Beikov
 * @since 1.2.0
 */
public class WhenClauseExpressionConditionModifier extends AbstractExpressionModifier<WhenClauseExpressionConditionModifier, WhenClauseExpression> {

    public WhenClauseExpressionConditionModifier(WhenClauseExpression target) {
        super(target);
    }

    @Override
    public void set(Expression expression) {
        target.setCondition(expression);
    }

    @Override
    public Expression get() {
        return target.getCondition();
    }

}
