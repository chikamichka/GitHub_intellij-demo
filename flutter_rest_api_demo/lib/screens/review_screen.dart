import 'package:flutter/material.dart';
import '../models/review.dart';
import '../services/review_service.dart';

class ReviewScreen extends StatefulWidget {
  @override
  _ReviewScreenState createState() => _ReviewScreenState();
}

class _ReviewScreenState extends State<ReviewScreen> {
  final _reviewService = ReviewService();
  late Future<List<Review>> _reviews;

  @override
  void initState() {
    super.initState();
    _reviews = _reviewService.getReviews();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Reviews')),
      body: FutureBuilder<List<Review>>(
        future: _reviews,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Failed to load reviews'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(child: Text('No reviews found'));
          } else {
            final reviews = snapshot.data!;
            return ListView.builder(
              itemCount: reviews.length,
              itemBuilder: (context, index) {
                final review = reviews[index];
                return Card(
                  child: ListTile(
                    title: Text(review.commentaires),
                    subtitle: Text('Reviewer: ${review.reviewer}, Note: ${review.note}'),
                    onLongPress: () {
                      // Handle delete
                      _deleteReview(review.id);
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

  Future<void> _deleteReview(int id) async {
    try {
      await _reviewService.deleteReview(id);
      setState(() {
        _reviews = _reviewService.getReviews(); // Refresh list after deletion
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to delete review')),
      );
    }
  }
}
