package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private ConcurrentHashMap postsMap = new ConcurrentHashMap<>();
    Set<Post> posts = postsMap.newKeySet();
    private AtomicLong postsCounter = new AtomicLong(1L);

    public List<Post> all() {
        return new ArrayList<>(posts);
    }

    public Optional<Post> getById(long id) {
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            searchingNextCounter();
            post.setId(postsCounter.longValue());
            posts.add(post);
            return post;
        }
        Post postUpdated = null; //if we have such ID
        for (Post pst : posts) {
            if (pst.getId() == post.getId()) {
                pst.setContent(post.getContent());
                postUpdated = pst;
            }
        }
        posts.add(post);
        if (postUpdated != null) {
            return postUpdated;
        }
        return post;
    }

    public void removeById(long id) {
        for (Post post :
                posts) {
            if (post.getId() == id) {
                posts.remove(post);
                return;
            }
        }
        throw new NotFoundException("No post found");
    }

    public AtomicLong searchingNextCounter() {
        for (Post p :
                posts) {
            if (p.getId() == postsCounter.longValue())
                postsCounter.addAndGet(1);
        }
        return postsCounter;
    }
}
