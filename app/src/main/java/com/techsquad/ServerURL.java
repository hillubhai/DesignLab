package com.techsquad;

final class ServerURL {
    private static final String BASE_URL = "";
    static final String LOGIN_URL = BASE_URL + "/";
    static final String LOGOUT_URL = BASE_URL + "/";
    static final String PHONE_CHECK_URL = BASE_URL + "/";
    static final String VERIFY_TOKEN_URL = BASE_URL + "/";
    static final String REFRESH_TOKEN_URL = BASE_URL + "/";
    static final String CUSTOMER_REGISTER_URL = BASE_URL + "/";

    static final String SEARCH_URL = BASE_URL + "/";
    static final String DRIVER_BOOK_URL = BASE_URL + "/";
    static final String PAST_RIDES_URL = BASE_URL + "/";
    static final String UPCOMING_RIDES_URL = BASE_URL + "/";
    static final String ONGOING_RIDE = BASE_URL + "/";
    static final String CUSTOMER_CANCEL_BOOKING_URL = BASE_URL + "/";
    static final String CUSTOMER_TRIP_RATE_URL = BASE_URL + "/";

    private static final String REVERSE_GEOCODE_REQUEST = "https://maps.googleapis.com/maps/api/geocode/json?&latlng=";

    static String getMapsAppURL(String lat, String lon) {
        return "http://maps.google.com/maps?daddr=" + lat + "," + lon;
    }

    static String getRevGeoURL(String s) {
        return ServerURL.REVERSE_GEOCODE_REQUEST + s;
    }

}