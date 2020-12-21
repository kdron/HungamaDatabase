package com.kdron.utils;

public class MovieSeachFilter {



    public static boolean isMovieMatch(String search, String title) {
        search= search.toLowerCase();
        title= title.toLowerCase();

        boolean isMovieMatch = false;
        String[] arr = title.split(" ");

        for (int i = 0 ; i<arr.length;i++)
        {
            if(arr[i].startsWith(search))
            {
                isMovieMatch = true;
                break;
            }
        }
        return isMovieMatch;

    }
}
