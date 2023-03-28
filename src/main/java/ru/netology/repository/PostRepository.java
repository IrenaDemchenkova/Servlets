package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub

public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong postsCounter = new AtomicLong(1L);

    public List<Post> all() {
        return (List<Post>) this.posts.values();
    }

    public Optional<Post> getById(long id) {
        return posts.values()
                .stream()
                .filter(post -> post.getId() == id)
                .findFirst();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            searchingNextCounter();
            post.setId(postsCounter.longValue());
            posts.put(post.getId(), post);
            return post;
        }
        Post postUpdated = null; //if we have such ID
        for (Post pst : this.posts.values()) {
            if (Objects.equals(pst.getId(), post.getId())) {
                pst.setContent(post.getContent());
                postUpdated = pst;
            }
        }
        posts.put(post.getId(), post);
        if (postUpdated != null) {
            return postUpdated;
        }
        return post;
    }

    public void removeById(long id) {
        if (this.posts.containsKey(id)) {
            throw new NotFoundException("No post found");
        }

        this.posts.remove(id);
    }

    public void searchingNextCounter() {
        for (Post p : this.posts.values()) {
            if (p.getId() == postsCounter.longValue())
                postsCounter.addAndGet(1);
        }
    }
}
