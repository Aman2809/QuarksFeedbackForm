package com.feedback.app.controllers;

import com.feedback.app.entities.Feedback;
import com.feedback.app.payloads.ApiResponse;
import com.feedback.app.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // POST --> Create Feedback
    @PostMapping("/create")
    public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback) {
        Feedback createdFeedback = feedbackService.addFeedback(feedback);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    // GET --> Retrieve Feedback by ID
    @GetMapping("/{feedbackId}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long feedbackId) {
        Feedback feedback = feedbackService.findById(feedbackId);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    // GET --> Retrieve Feedback by SessionId
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Feedback> getFeedbackBySessionId(@PathVariable UUID sessionId) {
        Feedback feedback = feedbackService.findBySessionId(sessionId);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    // GET --> Retrieve All Feedback
    @GetMapping("/")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        List<Feedback> feedbackList = feedbackService.findAll();
        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    // PUT --> Update Feedback
    @PutMapping("/update/{feedbackId}")
    public ResponseEntity<Feedback> updateFeedback( @Valid @RequestBody Feedback feedback,@PathVariable Long feedbackId) {
        Feedback updatedFeedback = feedbackService.updateFeedback( feedback,feedbackId);
        return new ResponseEntity<>(updatedFeedback, HttpStatus.OK);
    }

    @GetMapping("/question/{questionId}/ratings")
    public ResponseEntity<Map<String, Object>> getRatingsForQuestion(@PathVariable Long questionId) {
        Map<String, Object> ratingsData = new HashMap<>();
//        ratingsData.put("ratingsCount", feedbackService.getRatingCountForQuestion(questionId));
        ratingsData.put("ratingsPercentage", feedbackService.getRatingPercentagesForQuestion(questionId));
        ratingsData.put("averageRating", feedbackService.getAverageRatingForQuestion(questionId)); // Add average rating
        return new ResponseEntity<>(ratingsData, HttpStatus.OK);
    }



    // GET --> Retrieve Rating Percentages for a Specific Question (with total)
    @GetMapping("/ratings/{questionId}/percentages")
    public ResponseEntity<Map<String, Object>> getRatingPercentagesForQuestion(@PathVariable Long questionId) {
        Map<String, Object> ratingPercentages = feedbackService.getRatingPercentagesForQuestion(questionId);
        return new ResponseEntity<>(ratingPercentages, HttpStatus.OK);
    }



    // DELETE --> Delete Feedback
    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<ApiResponse> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteById(feedbackId);
        ApiResponse response = new ApiResponse("Feedback deleted successfully", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
