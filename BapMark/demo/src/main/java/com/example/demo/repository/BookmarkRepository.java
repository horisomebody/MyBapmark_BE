package com.example.demo.repository;

import com.example.demo.domain.Bookmark;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
    Optional<Bookmark> findByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);

    @Query("""
        SELECT b FROM Bookmark b
        WHERE b.user = :user AND
              b.createdAt IN (
                  SELECT MAX(b2.createdAt)
                  FROM Bookmark b2
                  WHERE b2.user = :user
                  GROUP BY b2.post.address
              )
    """)
    List<Bookmark> findLatestBookmarksByUserGroupedByLocation(@Param("user") User user); // 가장 최신 저장한 게시글만 북마크

    List<Bookmark> findByUserIdAndVisited(Long userId, Boolean visited); // 방문 여부 확인


    Optional<Bookmark> findByUserAndPlaceNameAndLatitudeAndLongitude(User user, String placeName, Double lat, Double lng);

    List<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId);
}
