import 'package:flutter/material.dart';
import '../models/submission.dart';
import '../services/submission_service.dart';

class SubmissionScreen extends StatefulWidget {
  @override
  _SubmissionScreenState createState() => _SubmissionScreenState();
}

class _SubmissionScreenState extends State<SubmissionScreen> {
  final _submissionService = SubmissionService();
  late Future<List<Submission>> _submissions;

  @override
  void initState() {
    super.initState();
    _submissions = _submissionService.getSubmissions();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Submissions')),
      body: FutureBuilder<List<Submission>>(
        future: _submissions,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Failed to load submissions'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(child: Text('No submissions found'));
          } else {
            final submissions = snapshot.data!;
            return ListView.builder(
              itemCount: submissions.length,
              itemBuilder: (context, index) {
                final submission = submissions[index];
                return Card(
                  child: ListTile(
                    title: Text(submission.titreArticle),
                    subtitle: Text('Authors: ${submission.auteurs.join(", ")}'),
                    onLongPress: () {
                      // Handle delete
                      _deleteSubmission(submission.id);
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

  Future<void> _deleteSubmission(int id) async {
    try {
      await _submissionService.deleteSubmission(id);
      setState(() {
        _submissions = _submissionService.getSubmissions(); // Refresh list after deletion
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to delete submission')),
      );
    }
  }
}
