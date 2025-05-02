import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class ApiResponse<T> {
  final bool success;
  final int status;
  final String message;
  final String timestamp;
  final T? data;
  final Map<String, dynamic>? errors;

  ApiResponse({
    required this.success,
    required this.status,
    required this.message,
    required this.timestamp,
    this.data,
    this.errors,
  });

  factory ApiResponse.fromJson(Map<String, dynamic> json, T Function(dynamic)? fromJson) {
    return ApiResponse(
      success: json['success'] as bool,
      status: json['status'] as int,
      message: json['message'] as String,
      timestamp: json['timestamp'] as String,
      data: json['data'] != null && fromJson != null ? fromJson(json['data']) : null,
      errors: json['errors'] != null ? Map<String, dynamic>.from(json['errors']) : null,
    );
  }
}

class ApiService {
  static const String baseUrl = 'http://your-api-domain.com/api/v1';
  
  final http.Client _client = http.Client();
  
  Future<String?> _getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('auth_token');
  }
  
  Future<void> _saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('auth_token', token);
  }
  
  Future<void> _clearToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('auth_token');
  }
  
  Future<Map<String, String>> _getHeaders() async {
    final token = await _getToken();
    final headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
    
    if (token != null) {
      headers['Authorization'] = 'Bearer $token';
    }
    
    return headers;
  }
  
  // User authentication methods
  Future<ApiResponse<User>> register(User user) async {
    final response = await _client.post(
      Uri.parse('$baseUrl/users'),
      headers: await _getHeaders(),
      body: jsonEncode(user.toJson()),
    );
    
    final responseJson = jsonDecode(response.body);
    return ApiResponse.fromJson(responseJson, (data) => User.fromJson(data));
  }
  
  Future<ApiResponse<AuthData>> login(String email, String password) async {
    final response = await _client.post(
      Uri.parse('$baseUrl/users/login'),
      headers: await _getHeaders(),
      body: jsonEncode({
        'email': email,
        'password': password,
      }),
    );
    
    final responseJson = jsonDecode(response.body);
    final apiResponse = ApiResponse.fromJson(responseJson, (data) => AuthData.fromJson(data));
    
    if (apiResponse.success && apiResponse.data != null) {
      await _saveToken(apiResponse.data!.accessToken);
    }
    
    return apiResponse;
  }
  
  Future<void> logout() async {
    await _clearToken();
  }
  
  // User management methods
  Future<ApiResponse<User>> getUserProfile() async {
    final response = await _client.get(
      Uri.parse('$baseUrl/users/profile'),
      headers: await _getHeaders(),
    );
    
    final responseJson = jsonDecode(response.body);
    return ApiResponse.fromJson(responseJson, (data) => User.fromJson(data));
  }
  
  Future<ApiResponse<User>> updateUserProfile(User user) async {
    final response = await _client.put(
      Uri.parse('$baseUrl/users/${user.userId}'),
      headers: await _getHeaders(),
      body: jsonEncode(user.toJson()),
    );
    
    final responseJson = jsonDecode(response.body);
    return ApiResponse.fromJson(responseJson, (data) => User.fromJson(data));
  }
  
  // Generic method for handling errors
  void handleApiError(ApiResponse response) {
    if (!response.success) {
      // You can use a state management solution to show errors or use a toast/snackbar
      print('API Error: ${response.message}');
      
      if (response.errors != null) {
        // Format validation errors
        response.errors!.forEach((field, error) {
          print('$field: $error');
        });
      }
      
      // Handle specific status codes
      if (response.status == 401) {
        // Unauthorized - need to re-login
        _clearToken();
        // Navigate to login screen
      }
    }
  }
}

// Example model classes
class User {
  final int? userId;
  final String username;
  final String email;
  final String? phoneNumber;
  final String? gender;
  final String? role;

  User({
    this.userId,
    required this.username,
    required this.email,
    this.phoneNumber,
    this.gender,
    this.role,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userId: json['userId'],
      username: json['username'],
      email: json['email'],
      phoneNumber: json['phoneNumber'],
      gender: json['gender'],
      role: json['role'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (userId != null) 'userId': userId,
      'username': username,
      'email': email,
      if (phoneNumber != null) 'phoneNumber': phoneNumber,
      if (gender != null) 'gender': gender,
      if (role != null) 'role': role,
    };
  }
}

class AuthData {
  final String accessToken;
  final String? refreshToken;
  final User user;

  AuthData({
    required this.accessToken,
    this.refreshToken,
    required this.user,
  });

  factory AuthData.fromJson(Map<String, dynamic> json) {
    return AuthData(
      accessToken: json['accessToken'],
      refreshToken: json['refreshToken'],
      user: User.fromJson(json['users']),
    );
  }
} 