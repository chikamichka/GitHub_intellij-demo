class Review {
  final int id;
  final int note;
  final String commentaires;
  final String etat;
  final int submissionId;
  final String reviewer; // Use username for simplicity

  Review({
    required this.id,
    required this.note,
    required this.commentaires,
    required this.etat,
    required this.submissionId,
    required this.reviewer,
  });

  factory Review.fromJson(Map<String, dynamic> json) {
    return Review(
      id: json['id'],
      note: json['note'],
      commentaires: json['commentaires'],
      etat: json['etat'],
      submissionId: json['submission']['id'],
      reviewer: json['reviewer']['username'],
    );
  }
}
