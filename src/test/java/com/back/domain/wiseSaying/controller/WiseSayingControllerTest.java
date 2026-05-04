package com.back.domain.wiseSaying.controller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {
    @Test
    @DisplayName("앱 시작 메시지")
    void t1() {
        String out = AppTestRunner.run("");

        assertThat(out)
                .contains("== 명언 앱 ==")
                .contains("명령)");
    }

    @Test
    @DisplayName("등록")
    void t2() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);

        assertThat(out)
                .contains("명언 :")
                .contains("작가 :")
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 - 두 건이면 id가 1, 2 순서로 부여된다")
    void t3() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 말라.
                홍길동
                """);

        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록 - 최신 등록 순으로 출력된다")
    void t4() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 말라.
                홍길동
                목록
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 홍길동 / 과거에 집착하지 말라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");

        // 최신순 검증: 2번이 1번보다 먼저 등장해야 함
        int idx2 = out.indexOf("2 / 홍길동");
        int idx1 = out.indexOf("1 / 작자미상");
        assertThat(idx2).isGreaterThan(0).isLessThan(idx1);
    }

    @Test
    @DisplayName("삭제")
    void t5() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                삭제?id=1
                """);

        assertThat(out).contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("삭제 - 존재하지 않는 id")
    void t6() {
        String out = AppTestRunner.run("""
                삭제?id=1
                """);

        assertThat(out).contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정")
    void t7() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=1
                과거를 사랑하라.
                홍길동
                목록
                """);

        assertThat(out)
                .contains("명언(기존) : 현재를 사랑하라.")
                .contains("작가(기존) : 작자미상")
                .contains("1 / 홍길동 / 과거를 사랑하라.");
    }

    @Test
    @DisplayName("수정 - 존재하지 않는 id")
    void t8() {
        String out = AppTestRunner.run("""
                수정?id=1
                """);

        assertThat(out).contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("종료")
    void t9() {
        String out = AppTestRunner.run("""
                종료
                """);

        // 종료가 정상적으로 처리되어 시작 메시지가 출력된 뒤 루프가 끝나야 한다
        assertThat(out).contains("== 명언 앱 ==");
    }
}
