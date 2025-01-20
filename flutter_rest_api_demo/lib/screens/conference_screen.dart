import 'package:flutter/material.dart';
import '../models/conference.dart';
import '../services/conference_service.dart';

class ConferenceScreen extends StatefulWidget {
  @override
  _ConferenceScreenState createState() => _ConferenceScreenState();
}

class _ConferenceScreenState extends State<ConferenceScreen> {
  final _conferenceService = ConferenceService();
  late Future<List<Conference>> _conferences;

  @override
  void initState() {
    super.initState();
    _conferences = _conferenceService.getConferences();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Conferences')),
      body: FutureBuilder<List<Conference>>(
        future: _conferences,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Failed to load conferences'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(child: Text('No conferences found'));
          } else {
            final conferences = snapshot.data!;
            return ListView.builder(
              itemCount: conferences.length,
              itemBuilder: (context, index) {
                final conference = conferences[index];
                return Card(
                  child: ListTile(
                    title: Text(conference.titre),
                    subtitle: Text('Thematique: ${conference.thematique}'),
                    trailing: Text('Etat: ${conference.etat}'),
                    onLongPress: () {
                      // Handle delete
                      _deleteConference(conference.id);
                    },
                  ),
                );
              },
            );
          }
        },
      ),
    );
  }

  Future<void> _deleteConference(int id) async {
    try {
      await _conferenceService.deleteConference(id);
      setState(() {
        _conferences = _conferenceService.getConferences(); // Refresh list after deletion
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to delete conference')),
      );
    }
  }
}
