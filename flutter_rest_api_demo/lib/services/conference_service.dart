import 'dart:convert';
import 'package:http/http.dart' as http;
import 'auth_service.dart';
import '../models/conference.dart';

class ConferenceService {
  static const String baseUrl = 'http://localhost:8080';
  final AuthService _authService = AuthService();

  Future<List<Conference>> getConferences() async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.get(
      Uri.parse('$baseUrl/conferences'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body) as List;
      return data.map((json) => Conference.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load conferences');
    }
  }

  Future<void> deleteConference(int id) async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.delete(
      Uri.parse('$baseUrl/conferences/$id'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode != 204) {
      throw Exception('Failed to delete conference');
    }
  }

  Future<Conference> createConference(Conference conference) async {
    final token = await _authService.getToken();

    if (token == null) {
      throw Exception('User is not authenticated');
    }

    final response = await http.post(
      Uri.parse('$baseUrl/conferences'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'titre': conference.titre,
        'dateDebut': conference.dateDebut.toIso8601String(),
        'dateFin': conference.dateFin.toIso8601String(),
        'thematique': conference.thematique,
        'etat': conference.etat,
      }),
    );

    if (response.statusCode == 201) {
      return Conference.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create conference');
    }
  }
}
