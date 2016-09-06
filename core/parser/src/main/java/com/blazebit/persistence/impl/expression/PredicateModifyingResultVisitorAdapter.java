/*
 * Copyright 2014 Blazebit.
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
package com.blazebit.persistence.impl.expression;

import java.util.ArrayList;
import java.util.List;

import com.blazebit.persistence.impl.predicate.*;

/**
 *
 * @author Moritz Becker
 * @since 1.0
 */
public abstract class PredicateModifyingResultVisitorAdapter implements Expression.ResultVisitor<Expression> {

    @Override
    public Expression visit(PathExpression expression) {
        List<PathElementExpression> expressions = expression.getExpressions();
        int size = expressions.size();
        for (int i = 0; i < size; i++) {
            expressions.get(i).accept(this);
        }
        return expression;
    }

    @Override
    public Expression visit(ArrayExpression expression) {
        expression.getBase().accept(this);
        expression.getIndex().accept(this);
        return expression;
    }

    @Override
    public Expression visit(TreatExpression expression) {
        expression.getExpression().accept(this);
        return expression;
    }

    @Override
    public Expression visit(PropertyExpression expression) {
        return expression;
    }

    @Override
    public Expression visit(ParameterExpression expression) {
        return expression;
    }

    @Override
    public Expression visit(NullExpression expression) {
        return expression;
    }

    @Override
    public Expression visit(SubqueryExpression expression) {
        return expression;
    }

    @Override
    public Expression visit(FunctionExpression expression) {
        List<Expression> expressions = expression.getExpressions();
        int size = expressions.size();
        for (int i = 0; i < size; i++) {
            expressions.get(i).accept(this);
        }
        return expression;
    }

    @Override
    public Expression visit(TypeFunctionExpression expression) {
        return visit((FunctionExpression) expression);
    }

    @Override
    public Expression visit(TrimExpression expression) {
        expression.getTrimSource().accept(this);
        return expression;
    }

    @Override
    public Expression visit(GeneralCaseExpression expression) {
        List<WhenClauseExpression> expressions = expression.getWhenClauses();
        int size = expressions.size();
        for (int i = 0; i < size; i++) {
            expressions.get(i).accept(this);
        }
        expression.getDefaultExpr().accept(this);
        return expression;
    }

    @Override
    public Expression visit(SimpleCaseExpression expression) {
        expression.getCaseOperand().accept(this);
        visit((GeneralCaseExpression) expression);
        return expression;
    }

    @Override
    public Expression visit(WhenClauseExpression expression) {
        expression.getCondition().accept(this);
        expression.getResult().accept(this);
        return expression;
    }

    @Override
    public Expression visit(ArithmeticExpression expression) {
        return expression;
    }

    @Override
    public Expression visit(ArithmeticFactor expression) {
        return expression;
    }

    @Override
    public Expression visit(NumericLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(BooleanLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(StringLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(DateLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(TimeLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(TimestampLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(EnumLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(EntityLiteral expression) {
        return expression;
    }

    @Override
    public Expression visit(CompoundPredicate predicate) {
        for (int i = 0; i < predicate.getChildren().size(); i++) {
            Predicate p = predicate.getChildren().get(i);
            predicate.getChildren().set(i, (Predicate) p.accept(this));
        }
        return predicate;
    }

    @Override
    public Expression visit(EqPredicate predicate) {
        return visit((BinaryExpressionPredicate) predicate);
    }

    @Override
    public Expression visit(IsNullPredicate predicate) {
        predicate.setExpression(predicate.getExpression().accept(this));
        return predicate;
    }

    @Override
    public Expression visit(IsEmptyPredicate predicate) {
        predicate.setExpression(predicate.getExpression().accept(this));
        return predicate;
    }

    @Override
    public Expression visit(MemberOfPredicate predicate) {
        return visit((BinaryExpressionPredicate) predicate);
    }

    @Override
    public Expression visit(LikePredicate predicate) {
        return visit((BinaryExpressionPredicate) predicate);
    }

    @Override
    public Expression visit(BetweenPredicate predicate) {
        predicate.setLeft(predicate.getLeft().accept(this));
        predicate.setStart(predicate.getStart().accept(this));
        predicate.setEnd(predicate.getEnd().accept(this));
        return predicate;
    }

    @Override
    public Expression visit(InPredicate predicate) {
        predicate.setLeft(predicate.getLeft().accept(this));
        List<Expression> newRight = new ArrayList<Expression>();
        for (Expression right : predicate.getRight()) {
            newRight.add(right.accept(this));
        }
        predicate.setRight(newRight);
        return predicate;
    }

    @Override
    public Expression visit(GtPredicate predicate) {
        return visit((BinaryExpressionPredicate) predicate);
    }

    @Override
    public Expression visit(GePredicate predicate) {
        return visit((BinaryExpressionPredicate) predicate);
    }

    @Override
    public Expression visit(LtPredicate predicate) {
        return visit((BinaryExpressionPredicate) predicate);
    }

    @Override
    public Expression visit(LePredicate predicate) {
        return visit((BinaryExpressionPredicate) predicate);
    }

    @Override
    public Expression visit(ExistsPredicate predicate) {
        predicate.setExpression(predicate.getExpression().accept(this));
        return predicate;
    }

    private BinaryExpressionPredicate visit(BinaryExpressionPredicate predicate) {
        predicate.setLeft(predicate.getLeft().accept(this));
        predicate.setRight(predicate.getRight().accept(this));
        return predicate;
    }

}
