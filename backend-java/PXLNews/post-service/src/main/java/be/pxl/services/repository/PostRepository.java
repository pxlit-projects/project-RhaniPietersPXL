package be.pxl.services.repository;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> findByAuthorAndState(String author, State state);
    List<Post> findByState(State state);
}