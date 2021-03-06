/*
 * Copyright (c) 2012, 2018, Werner Keil, Anatole Tresch and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Contributors: @atsticks, @keilw
 */
package org.javamoney.calc.common;

import org.javamoney.calc.CalculationContext;

import javax.money.MonetaryAmount;
import static org.javamoney.calc.CalculationContext.one;

/**
 * The future value of an annuity formula is used to calculate what the value at a future date would
 * be for a series of periodic payments. The future value of an annuity formula assumes that
 *
 * <ul>
 * <li>The rate does not change
 * <li>The first payment is one period away
 * <li>The periodic payment does not change
 * </ul>
 *
 * If the rate or periodic payment does change, then the sum of the future value of each individual
 * cash flow would need to be calculated to determine the future value of the annuity. If the first
 * cash flow, or payment, is made immediately, the {@link org.javamoney.calc.common.FutureValue} formula would be used.
 *
 * @author Anatole
 * @author Werner
 * @see <a href="http://www.financeformulas.net/Future_Value_of_Annuity.html">http://www.financeformulas.net/Future_Value_of_Annuity.html</a>
 */
public final class FutureValueOfAnnuity extends AbstractRateAndPeriodBasedOperator {

    /**
     * Private constructor.
     *
     * @param rateAndPeriods    the target rate an periods, not null.
     */
    private FutureValueOfAnnuity(RateAndPeriods rateAndPeriods) {
        super(rateAndPeriods);
    }

    /**
     * Access a MonetaryOperator for calculation.
     *
     * @param rateAndPeriods the rate and periods, not null.
     * @return the operator, never null.
     */
    public static FutureValueOfAnnuity of(RateAndPeriods rateAndPeriods) {
        return new FutureValueOfAnnuity(rateAndPeriods);
    }

    /**
     * Performs the calculation.
     *
     * @param amount         the first payment
     * @param rateAndPeriods The rate and periods, not null.
     * @return the resulting amount, never null.
     */
    public static MonetaryAmount calculate(MonetaryAmount amount, RateAndPeriods rateAndPeriods) {
        // Am * (((1 + r).pow(n))-1/rate)
        Rate rate = rateAndPeriods.getRate();
        int periods = rateAndPeriods.getPeriods();
        return amount.multiply(one().add(rate.get()).pow(periods).subtract(one()).divide(
                rate.get(),CalculationContext.mathContext()));
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount amount) {
        return calculate(amount, rateAndPeriods);
    }

    @Override
    public String toString() {
        return "FutureValueOfAnnuity{" +
                "\n " + rateAndPeriods +
                '}';
    }
}
