/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.controller.ui;

/**
 * A counter class.
 */
public class Counter {
    
    static int count;
    
    /**
     * Creates a new Counter.
     */
    public Counter() {
        count = 0;
    }
    
    /**
     * Increment the counter.
     */
    public static void incCounter() {
        count++;
    }
    
    /**
     * Decrease the counter.
     */
    public static void decCounter() {
        count--;
    }
    
    /**
     * Gets the current counter number.
     * @return The counter number.
     */
    public static int getCounter() {      
        return count;
    } 
}
