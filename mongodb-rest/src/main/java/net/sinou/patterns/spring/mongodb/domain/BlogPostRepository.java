package net.sinou.patterns.spring.mongodb.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "posts", path = "posts")
public interface BlogPostRepository extends MongoRepository<BlogPost, String> {

	List<BlogPost> findByTitle(@Param("title") String title);

}