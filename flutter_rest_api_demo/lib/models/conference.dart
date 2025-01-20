class Conference {
  final int id;
  final String titre;
  final DateTime dateDebut;
  final DateTime dateFin;
  final String thematique;
  final String etat;

  Conference({
    required this.id,
    required this.titre,
    required this.dateDebut,
    required this.dateFin,
    required this.thematique,
    required this.etat,
  });

  factory Conference.fromJson(Map<String, dynamic> json) {
    return Conference(
      id: json['id'],
      titre: json['titre'],
      dateDebut: DateTime.parse(json['dateDebut']),
      dateFin: DateTime.parse(json['dateFin']),
      thematique: json['thematique'],
      etat: json['etat'],
    );
  }
}
