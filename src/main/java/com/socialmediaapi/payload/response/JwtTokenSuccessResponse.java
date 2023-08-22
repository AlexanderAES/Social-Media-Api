package com.socialmediaapi.payload.response;

public class JwtTokenSuccessResponse {

    private boolean success;
    private String token;

    public JwtTokenSuccessResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "JwtTokenSuccessResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                '}';
    }
}
