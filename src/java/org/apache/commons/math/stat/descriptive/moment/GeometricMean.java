/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.stat.descriptive.moment;

import org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.summary.SumOfLogs;

/**
 * Returns the <a href="http://www.xycoon.com/geometric_mean.htm">
 * geometric mean </a> of the available values.
 * <p>
 * Uses a {@link SumOfLogs} instance to compute sum of logs and returns
 * <code> exp( 1/n  (sum of logs) ).</code>  Therefore, </p>
 * <ul>
 * <li>If any of values are < 0, the result is <code>NaN.</code></li>
 * <li>If all values are non-negative and less than 
 * <code>Double.POSITIVE_INFINITY</code>,  but at least one value is 0, the 
 * result is <code>0.</code></li>
 * <li>If both <code>Double.POSITIVE_INFINITY</code> and 
 * <code>Double.NEGATIVE_INFINITY</code> are among the values, the result is
 * <code>NaN.</code></li>
 * </ul> </p>
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If 
 * multiple threads access an instance of this class concurrently, and at least
 * one of the threads invokes the <code>increment()</code> or 
 * <code>clear()</code> method, it must be synchronized externally.</p>
 * 
 *
 * @version $Revision$ $Date$
 */
public class GeometricMean extends AbstractStorelessUnivariateStatistic {

    /** Serializable version identifier */
    private static final long serialVersionUID = -8178734905303459453L;  
    
    /** Wrapped SumOfLogs instance */
    private StorelessUnivariateStatistic sumOfLogs;

    /**
     * Create a GeometricMean instance
     */
    public GeometricMean() {
        sumOfLogs = new SumOfLogs();
    }
    
    /**
     * Create a GeometricMean instance using the given SumOfLogs instance
     */
    public GeometricMean(SumOfLogs sumOfLogs) {
        this.sumOfLogs = sumOfLogs;
    }
    
    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#increment(double)
     */
    public void increment(final double d) {
        sumOfLogs.increment(d);
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getResult()
     */
    public double getResult() {
        if (sumOfLogs.getN() > 0) {
            return Math.exp(sumOfLogs.getResult() / (double) sumOfLogs.getN());
        } else {
            return Double.NaN;
        }
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#clear()
     */
    public void clear() {
        sumOfLogs.clear();
    }

    /**
     * Returns the geometric mean of the entries in the specified portion
     * of the input array.
     * <p>
     * See {@link GeometricMean} for details on the computing algorithm.</p>
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     * 
     * @param values input array containing the values
     * @param begin first array element to include
     * @param length the number of elements to include
     * @return the geometric mean or Double.NaN if length = 0 or
     * any of the values are &lt;= 0.
     * @throws IllegalArgumentException if the input array is null or the array
     * index parameters are not valid
     */
    public double evaluate(
        final double[] values, final int begin, final int length) {
        return Math.exp(
            sumOfLogs.evaluate(values, begin, length) / (double) length);
    }
    
    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getN()
     */
    public long getN() {
        return sumOfLogs.getN();
    }
    
    /**
     * <p>Sets the implementation for the sum of logs.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #increment(double) increment} has been used to add data; 
     * otherwise an IllegalStateException will be thrown.</p>
     * 
     * @param sumLogImpl the StorelessUnivariateStatistic instance to use
     * for computing the log sum
     * @throws IllegalStateException if data has already been added 
     *  (i.e if n > 0)
     */
    public void setSumLogImpl(
            StorelessUnivariateStatistic sumLogImpl) {
        checkEmpty();
        this.sumOfLogs = sumLogImpl;
    }
    
    /**
     * Returns the currently configured sum of logs implementation
     * 
     * @return the StorelessUnivariateStatistic implementing the log sum
     */
    public StorelessUnivariateStatistic getSumLogImpl() {
        return sumOfLogs;
    }
    
    /**
     * Throws IllegalStateException if n > 0.
     */
    private void checkEmpty() {
        if (getN() > 0) {
            throw new IllegalStateException(
                "Implementation must be configured before values are added.");
        }
    }

}