package com.example.cab302prac4.model;

public interface IRatingDAO {
    public void addRating(int userid, int documentid);
    public void removeRating(int userid, int documentid);
    public boolean checkIfRated(int userid, int documentid);
    public int getRatingScoreForDocument(int documentid);
    public void removeAllDocumentRatings(int documentid);
}
