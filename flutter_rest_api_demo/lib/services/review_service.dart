import 'dart:convert';
import 'package:http/http.dart' as http;
import 'auth_service.dart';
import '../models/review.dart';

class ReviewService {
  static const String baseUrl = 'http://localhost:8080';
  final AuthService _authService = AuthService();

  Future<List<Review>> getReviews() async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.get(
      Uri.parse('$baseUrl/reviews'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body) as List;
      return data.map((json) => Review.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load reviews');
    }
  }

  Future<void> deleteReview(int id) async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.delete(
      Uri.parse('$baseUrl/reviews/$id'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode != 204) {
      throw Exception('Failed to delete review');
    }
  }

  Future<Review> createReview(Review review) async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.post(
      Uri.parse('$baseUrl/reviews'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'note': review.note,
        'commentaires': review.commentaires,
        'etat': review.etat,
        'submissionId': review.submissionId,
        'reviewer': review.reviewer,
      }),
    );

    if (response.statusCode == 201) {
      return Review.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create review');
    }
  }
}
