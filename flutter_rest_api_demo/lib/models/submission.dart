import 'package:flutter_rest_api_demo/models/review.dart';

class Submission {
  final int id;
  final String titreArticle;
  final String resume;
  final String documentPdf;
  final List<String> auteurs; // Use List instead of Set for simplicity
  final int conferenceId;
  final List<Review> reviews; // Use List instead of Set for simplicity

  Submission({
    required this.id,
    required this.titreArticle,
    required this.resume,
    required this.documentPdf,
    required this.auteurs,
    required this.conferenceId,
    required this.reviews,
  });

  factory Submission.fromJson(Map<String, dynamic> json) {
    return Submission(
      id: json['id'],
      titreArticle: json['titreArticle'],
      resume: json['resume'],
      documentPdf: json['documentPdf'],
      auteurs: List<String>.from(json['auteurs'].map((author) => author['username'])), // Assuming auteur usernames are in the JSON
      conferenceId: json['conference']['id'],
      reviews: List<Review>.from(json['reviews'].map((review) => Review.fromJson(review))),
    );
  }
}
