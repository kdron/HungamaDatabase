package com.kdron.utils;

import junit.framework.TestCase;

public class MovieSeachFilterTest extends TestCase {

    public void testIsMovieMatch() {
        String inputSearchKeyWord = "i";
        String inputMovieTitle = "I.T.";
        boolean expected = true;

        boolean output  = MovieSeachFilter.isMovieMatch(inputSearchKeyWord,inputMovieTitle);
        assertEquals(expected,output);
    }


    public void testIsMovieMatch1() {
        String inputSearchKeyWord = "i";
        String inputMovieTitle = "Dil To Paagal Hai";
        boolean expected = false;

        boolean output  = MovieSeachFilter.isMovieMatch(inputSearchKeyWord,inputMovieTitle);
        assertEquals(expected,output);
    }


    public void testIsMovieMatch2() {
        String inputSearchKeyWord = "paag";
        String inputMovieTitle = "Dil To Paagal Hai";
        boolean expected = true;

        boolean output  = MovieSeachFilter.isMovieMatch(inputSearchKeyWord,inputMovieTitle);
        assertEquals(expected,output);
    }
}