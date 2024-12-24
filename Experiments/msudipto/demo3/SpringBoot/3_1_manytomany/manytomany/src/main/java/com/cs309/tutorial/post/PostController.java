package com.cs309.tutorial.post;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/post")
public class PostController {

	private final TagRepository tagRepo;
	private final PostRepository postRepo;

	// Constructor-based injection
	@Autowired
	public PostController(TagRepository tagRepo, PostRepository postRepo) {
		this.tagRepo = tagRepo;
		this.postRepo = postRepo;
	}

	@PostMapping("/create")
	public void createPost(@RequestParam String title, @RequestParam String description,
						   @RequestParam String content, @RequestParam String tags) {
		Post post = new Post(title, description, content);

		String[] tagArr = tags.split(",");

		for (String tagName : tagArr) {
			Tag temp = new Tag(tagName);
			post.getTags().add(temp);
			temp.getPosts().add(post);
		}
		postRepo.save(post);
	}

	@GetMapping("/getAll")
	public List<Post> getAllPosts() {
		return postRepo.findAll();
	}
}