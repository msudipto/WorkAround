
package coms309.service;

import coms309.entity.PerformanceReview;
import coms309.repository.PerformanceReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceReviewService {

    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;

    public PerformanceReview savePerformanceReview(PerformanceReview review) {
        return performanceReviewRepository.save(review);
    }

    public List<PerformanceReview> getAllPerformanceReviews() {
        return performanceReviewRepository.findAll();
    }

    public List<PerformanceReview> getPerformanceReviewsByUsername(String username) {
        return performanceReviewRepository.findByUsername(username);
    }
}
