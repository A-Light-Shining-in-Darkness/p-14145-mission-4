package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WiseSayingRepository {
    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;

    public WiseSaying save(String content, String author) {
        int id = ++lastId;
        WiseSaying wiseSaying = new WiseSaying(id, content, author);
        wiseSayings.add(wiseSaying);
        return wiseSaying;
    }

    public List<WiseSaying> findAll(String keywordType, String keyword, int page, int pageSize) {
        List<WiseSaying> filtered = filter(keywordType, keyword);
        List<WiseSaying> reversed = new ArrayList<>(filtered);
        Collections.reverse(reversed);
        int offset = (page - 1) * pageSize;
        if (offset >= reversed.size()) return new ArrayList<>();
        return new ArrayList<>(reversed.subList(offset, Math.min(offset + pageSize, reversed.size())));
    }

    public int count(String keywordType, String keyword) {
        return filter(keywordType, keyword).size();
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayings.stream()
                .filter(w -> w.getId() == id)
                .findFirst();
    }

    public boolean deleteById(int id) {
        return wiseSayings.removeIf(w -> w.getId() == id);
    }

    private List<WiseSaying> filter(String keywordType, String keyword) {
        if (keyword == null || keyword.isEmpty()) return new ArrayList<>(wiseSayings);
        return wiseSayings.stream()
                .filter(w -> {
                    if ("content".equals(keywordType)) return w.getContent().contains(keyword);
                    if ("author".equals(keywordType)) return w.getAuthor().contains(keyword);
                    return true;
                })
                .collect(Collectors.toList());
    }
}