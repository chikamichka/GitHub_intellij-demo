import 'dart:convert';
import 'package:http/http.dart' as http;
import 'auth_service.dart';
import '../models/user.dart';

class UserService {
  static const String baseUrl = 'http://localhost:8080'; // Use localhost for macOS
  final AuthService _authService = AuthService();

  Future<List<User>> getUsersInfo() async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    print('Fetching users info with token: $token'); // Log token

    final response = await http.get(
      Uri.parse('$baseUrl/users/info'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    print('Users info response status: ${response.statusCode}'); // Log response status code
    print('Users info response body: ${response.body}'); // Log response body

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body) as List;
      return data.map((json) => User.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load users info');
    }
  }
}
