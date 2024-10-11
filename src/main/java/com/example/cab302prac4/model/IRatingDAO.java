package com.example.cab302prac4.model;

import java.util.List;

public interface IRatingDAO {
    public void addRating(int userid, int documentid);
    public void removeRating(int userid, int documentid);
    public boolean checkIfRated(int userid, int documentid);
    public int getRatingScoreForDocument(int documentid);
    public void removeAllDocumentRatings(int documentid);
    public int getUserTotalRatingScore(int userid);
    public List<Contact> getAllRatedDocuments(int currentuserid);
}
