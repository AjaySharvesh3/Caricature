package com.example.photoblog;

public class ReportUser {

    public String userName;
    public String userReportDetails;

    public ReportUser() {}

    public ReportUser(String userName, String userReportDetails) {
        this.userName = userName;
        this.userReportDetails = userReportDetails;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserReportDetails() {
        return userReportDetails;
    }

    public void setUserReportDetails(String userReportDetails) {
        this.userReportDetails = userReportDetails;
    }
}
