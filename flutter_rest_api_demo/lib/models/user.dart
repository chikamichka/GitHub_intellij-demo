class User {
  final String nom;
  final String prenom;
  final String username;
  final String infos;
  final List<String> roles;

  User({
    required this.nom,
    required this.prenom,
    required this.username,
    required this.infos,
    required this.roles,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      nom: json['nom'],
      prenom: json['prenom'],
      username: json['username'],
      infos: json['infos'],
      roles: List<String>.from(json['roles']),
    );
  }
}
