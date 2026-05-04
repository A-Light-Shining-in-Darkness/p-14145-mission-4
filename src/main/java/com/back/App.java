package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
    private final Scanner sc;
    private final boolean withSampleData;
    private final SystemController systemController;
    private final WiseSayingController wiseSayingController;

    public App() {
        this(new Scanner(System.in), true);
    }

    public App(Scanner sc) {
        this(sc, false);
    }

    private App(Scanner sc, boolean withSampleData) {
        this.sc = sc;
        this.withSampleData = withSampleData;
        this.systemController = new SystemController();
        this.wiseSayingController = new WiseSayingController(sc);
    }

    public void run() {
        if (withSampleData) {
            wiseSayingController.initSampleDataIfEmpty();
        }

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");

            String cmd;
            try {
                cmd = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                break;
            }

            if (cmd.equals("종료")) {
                systemController.exit();
                break;
            } else if (cmd.equals("등록")) {
                wiseSayingController.write();
            } else if (cmd.startsWith("목록")) {
                Map<String, String> params = parseQueryParams(cmd);
                String keywordType = params.getOrDefault("keywordType", "");
                String keyword = params.getOrDefault("keyword", "");
                int page = Integer.parseInt(params.getOrDefault("page", "1"));
                wiseSayingController.list(keywordType, keyword, page);
            } else if (cmd.startsWith("삭제?id=")) {
                int id = Integer.parseInt(cmd.split("=")[1]);
                wiseSayingController.delete(id);
            } else if (cmd.startsWith("수정?id=")) {
                int id = Integer.parseInt(cmd.split("=")[1]);
                wiseSayingController.modify(id);
            }
        }
    }

    private Map<String, String> parseQueryParams(String cmd) {
        Map<String, String> params = new HashMap<>();
        int idx = cmd.indexOf('?');
        if (idx == -1) return params;
        for (String pair : cmd.substring(idx + 1).split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) params.put(kv[0], kv[1]);
        }
        return params;
    }
}
