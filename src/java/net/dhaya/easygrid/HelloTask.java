/*
 * GRIDGAIN - OPEN CLOUD PLATFORM.
 * COPYRIGHT (C) 2005-2008 GRIDGAIN SYSTEMS. ALL RIGHTS RESERVED.
 *
 * THIS IS FREE SOFTWARE; YOU CAN REDISTRIBUTE IT AND/OR
 * MODIFY IT UNDER THE TERMS OF THE GNU LESSER GENERAL PUBLIC
 * LICENSE AS PUBLISHED BY THE FREE SOFTWARE FOUNDATION; EITHER
 * VERSION 2.1 OF THE LICENSE, OR (AT YOUR OPTION) ANY LATER
 * VERSION.
 *
 * THIS LIBRARY IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY; WITHOUT EVEN THE IMPLIED WARRANTY OF
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  SEE THE
 * GNU LESSER GENERAL PUBLIC LICENSE FOR MORE DETAILS.
 *
 * YOU SHOULD HAVE RECEIVED A COPY OF THE GNU LESSER GENERAL PUBLIC
 * LICENSE ALONG WITH THIS LIBRARY; IF NOT, WRITE TO THE FREE
 * SOFTWARE FOUNDATION, INC., 51 FRANKLIN ST, FIFTH FLOOR, BOSTON, MA
 * 02110-1301 USA
 */

package net.dhaya.easygrid;

import java.io.*;
import java.util.*;
import org.gridgain.grid.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;

/**
 * This class defines grid task for this example. Grid task is responsible for
 * splitting the task into jobs. This particular implementation splits given
 * string into individual words and creates grid jobs for each word. Every job
 * will print the word passed into it and return the number of letters in that 
 * word.
 *
 * @author 2005-2009 Copyright (C) GridGain Systems. All Rights Reserved.
 * @version 2.1.1
 */
public class HelloTask extends GridTaskSplitAdapter<String, Integer> {
    /** Grid logger. */
    @GridLoggerResource
    private GridLogger log = null;

    /**
     * Splits the passed in phrase into words and creates a job for every 
     * word. Every job will print out the word and return number of letters in that
     * word.
     * 
     * @param gridSize Number of nodes in the grid.
     * @param phrase Any phrase (for this example we pass in <tt>"Hello World"</tt>). 
     * @return Created grid jobs for remote execution.
     * @throws GridException If split failed. 
     */
    @Override
    public Collection<? extends GridJob> split(int gridSize, String phrase) throws GridException {
        // Split the passed in phrase into multiple words separated by spaces.
        String[] words = phrase.split(" ");

        List<GridJob> jobs = new ArrayList<GridJob>(words.length);

        for (String word : words) {
            // Every job gets its own word as an argument.
            jobs.add(new GridJobAdapter<String>(word) {
                /*
                 * Simply prints the word passed into the job and 
                 * returns number of letters in that word.
                 */
                public Serializable execute() {
                    String word = getArgument();
                    
                    if (log.isInfoEnabled() == true) {
                        log.info(">>>");
                        log.info(">>> Printing '" + word + "' on this node from grid job.");
                        log.info(">>>");
                    }

                    // Return number of letters in the word.
                    return word.length();
                }
            });
        }

        return jobs;
    }

    /**
     * Sums up all characters returns from all jobs and returns a 
     * total number of characters in the phrase.
     * 
     * @param results Job results.
     * @return Number of characters for the phrase passed into 
     *      <tt>split(gridSize, phrase)</tt> method above.
     * @throws GridException If reduce failed.
     */
    public Integer reduce(List<GridJobResult> results) throws GridException {
        int totalCharCnt = 0;
        
        for (GridJobResult res : results) {
            // Every job returned a number of letters
            // for the word it was responsible for.
            Integer charCnt = res.getData();
            
            totalCharCnt += charCnt;
        }
        
        // Account for spaces. For simplicity we assume one space between words.
        totalCharCnt += results.size() - 1;
        
        // Total number of characters in the phrase
        // passed into task execution.
        return totalCharCnt;
    }
}
