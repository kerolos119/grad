package org.example.model;

public record ApiResponse<T>(
        String status,
        int code,
        String message,
        T data) {
    public static <T> ApiResponse<T> success(T data, String s){
        return new ApiResponse<>("success",200,null , data);
    }
    public static <T> ApiResponse<T> created(T data){
        return new ApiResponse<>("created",201,null,data);
    }
    public static <T> ApiResponse<T> noContent(String message) {
        return new ApiResponse<>("noContent",204,null,null);
    }
}
