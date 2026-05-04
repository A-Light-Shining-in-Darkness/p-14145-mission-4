package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner sc) {
        this.sc = sc;
        this.wiseSayingService = new WiseSayingService();
    }

    public void write() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying ws = wiseSayingService.write(content, author);
        System.out.println(ws.getId() + "번 명언이 등록되었습니다.");
    }

    public void list(String keywordType, String keyword, int page) {
        int pageSize = 5;

        if (keyword != null && !keyword.isEmpty()) {
            System.out.println("----------------------");
            System.out.println("검색타입 : " + keywordType);
            System.out.println("검색어 : " + keyword);
            System.out.println("----------------------");
        }

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayings = wiseSayingService.findAll(keywordType, keyword, page, pageSize);
        for (WiseSaying ws : wiseSayings) {
            System.out.println(ws.getId() + " / " + ws.getAuthor() + " / " + ws.getContent());
        }

        System.out.println("----------------------");

        int totalCount = wiseSayingService.count(keywordType, keyword);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

        StringBuilder pageIndicator = new StringBuilder("페이지 : ");
        for (int i = 1; i <= totalPages; i++) {
            if (i > 1) pageIndicator.append(" / ");
            if (i == page) pageIndicator.append("[").append(i).append("]");
            else pageIndicator.append(i);
        }
        System.out.println(pageIndicator);
    }

    public void delete(int id) {
        boolean removed = wiseSayingService.delete(id);
        if (removed) {
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public void modify(int id) {
        Optional<WiseSaying> opt = wiseSayingService.findById(id);
        if (opt.isEmpty()) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
            return;
        }

        WiseSaying ws = opt.get();

        System.out.println("명언(기존) : " + ws.getContent());
        System.out.print("명언 : ");
        String content = sc.nextLine();

        System.out.println("작가(기존) : " + ws.getAuthor());
        System.out.print("작가 : ");
        String author = sc.nextLine();

        wiseSayingService.modify(id, content, author);
    }

    public void initSampleDataIfEmpty() {
        wiseSayingService.initSampleDataIfEmpty();
    }
}