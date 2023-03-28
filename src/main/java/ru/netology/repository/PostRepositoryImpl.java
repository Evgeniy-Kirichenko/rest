package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepositoryImpl implements PostRepository {
    private final ConcurrentHashMap<Long, Post> posts;
    private final AtomicLong isCount = new AtomicLong(0L);

    public PostRepositoryImpl() {
        posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        if (posts.isEmpty()) return Collections.emptyList();
        else
            return posts.values().stream().filter(x -> !x.isRemove()).collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        if (!posts.get(id).isRemove()) return Optional.ofNullable(posts.get(id));
        else return Optional.empty();
    }

    public Post save(Post post) {
        if (posts.containsKey(post.getId())) {
            if (!posts.isEmpty()) posts.put(post.getId(), post);
            else throw new NotFoundException("Элемент не найден");
        } else {
            var newId = isCount.incrementAndGet();
            post.setId(newId);
            posts.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        if (!posts.get(id).isRemove()) {
            posts.get(id).setRemove(true);
        } else throw new NotFoundException("Элемент удален");
    }
}
