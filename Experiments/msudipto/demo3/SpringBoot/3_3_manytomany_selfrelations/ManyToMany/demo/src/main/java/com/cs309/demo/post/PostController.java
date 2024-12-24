package com.cs309.demo.post;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/post")
public class PostController {

	private final TagRepository tagRepo;
	private final PostRepository postRepo;

	@Autowired
	public PostController(TagRepository tagRepo, PostRepository postRepo) {
		this.tagRepo = tagRepo;
		this.postRepo = postRepo;
	}

	/**
	 * Creates a new Post object to be saved in the database.
	 *
	 * @param post The Post object to be created.
	 * @return The ID of the created Post, or null if the title already exists.
	 */
	@PostMapping("/create")
	public Long createPost(@RequestBody Post post) {
		if (postRepo.findByTitle(post.getTitle()) != null) {
			return null;
		}
		postRepo.save(post);
		return post.getId();
	}

	/**
	 * Returns all of the posts stored in the database.
	 *
	 * @return a List of all the posts in the database.
	 */
	@GetMapping("/getAll")
	public List<Post> getAllPosts() {
		return postRepo.findAll();
	}

	/**
	 * Returns the post associated with the given id.
	 *
	 * @param id The ID of the post to retrieve.
	 * @return The Post object if found, null otherwise.
	 */
	@GetMapping("/get")
	public Post getPost(@RequestParam Long id) {
		Optional<Post> postOpt = postRepo.findById(id);
		return postOpt.orElse(null);
	}

	/**
	 * Updates a post with the given title.
	 *
	 * @param post The updated Post object.
	 * @return The ID of the updated Post, or null if the title does not exist.
	 */
	@PutMapping("/update")
	public Long updatePost(@RequestBody Post post) {
		Post existingPost = postRepo.findByTitle(post.getTitle());
		if (existingPost == null) {
			return null;
		}
		post.setPostedAt(existingPost.getPostedAt());
		post.setLastUpdatedAt(new Date());
		postRepo.save(post);
		return post.getId();
	}

	/**
	 * Adds a tag to an already existing post.
	 *
	 * @param tagName The name of the tag to be added.
	 * @param postID The ID of the post to which the tag will be added.
	 */
	@PutMapping("/addTag")
	@Transactional
	public void addTag(@RequestParam String tagName, @RequestParam Long postID) {
		Optional<Post> postOpt = postRepo.findById(postID);
		if (postOpt.isPresent()) {
			Post post = postOpt.get();
			post.getTags().add(new Tag(tagName));
			post.setLastUpdatedAt(new Date());
			postRepo.save(post);
		}
	}
}