import 'dart:convert';
import 'package:http/http.dart' as http;
import 'auth_service.dart';
import '../models/submission.dart';

class SubmissionService {
  static const String baseUrl = 'http://localhost:8080';
  final AuthService _authService = AuthService();

  Future<List<Submission>> getSubmissions() async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.get(
      Uri.parse('$baseUrl/submissions'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body) as List;
      return data.map((json) => Submission.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load submissions');
    }
  }

  Future<void> deleteSubmission(int id) async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.delete(
      Uri.parse('$baseUrl/submissions/$id'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode != 204) {
      throw Exception('Failed to delete submission');
    }
  }

  Future<Submission> createSubmission(Submission submission) async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.post(
      Uri.parse('$baseUrl/submissions'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'titreArticle': submission.titreArticle,
        'resume': submission.resume,
        'documentPdf': submission.documentPdf,
        'auteurs': submission.auteurs,
        'conferenceId': submission.conferenceId,
      }),
    );

    if (response.statusCode == 201) {
      return Submission.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create submission');
    }
  }
}
