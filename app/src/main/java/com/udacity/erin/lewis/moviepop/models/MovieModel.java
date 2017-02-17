package com.udacity.erin.lewis.moviepop.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erin Lewis on 1/30/2017.
 */

public class MovieModel implements Parcelable{
    public String poster_path;
    public boolean adult;
    public String overview;
    public String release_date;
    public int[] genre_ids;
    public int id;
    public String original_title;
    public String original_language;
    public String title;
    public String backdrop_path;
    public double popularity;
    public int vote_count;
    public boolean video;
    public double vote_average;
    public boolean isFavorite;

    public MovieModel() {
        poster_path = "";
        adult = false;
        overview = "";
        release_date = "";
        genre_ids = new int[0];
        id = 0;
        original_title = "";
        original_language = "";
        title = "";
        backdrop_path = "";
        popularity = 0.0;
        vote_count = 0;
        video = false;
        vote_average = 0.0;
        isFavorite = false;
    }

    public MovieModel(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public MovieModel createFromParcel(Parcel in ) {
            return new MovieModel( in );
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(release_date);
        parcel.writeString(original_title);
        parcel.writeString(original_language);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(overview);
        parcel.writeInt(vote_count);
        parcel.writeDouble(vote_average);
        parcel.writeDouble(popularity);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeInt(genre_ids != null ? genre_ids.length : 0);
        parcel.writeIntArray(genre_ids != null ? genre_ids : new int[0]);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        release_date = in.readString();
        original_title = in.readString();
        original_language = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        vote_count = in.readInt();
        vote_average = in.readDouble();
        popularity = in.readDouble();
        adult = in.readByte() != 0;
        video = in.readByte() != 0;
        genre_ids = new int[in.readInt()];
        in.readIntArray(genre_ids);
        isFavorite = in.readByte() != 0;
    }
}
