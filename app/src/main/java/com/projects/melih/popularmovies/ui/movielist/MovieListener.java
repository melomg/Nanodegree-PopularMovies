package com.projects.melih.popularmovies.ui.movielist;

import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.ui.base.ItemClickListener;

public interface MovieListener extends ItemClickListener<Movie> {
    void onFavoriteDelete(Movie movie);
}