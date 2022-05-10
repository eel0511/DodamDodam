package com.ssafy.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //User
    USER_DOESNT_EXIST(404, "U101", "유저를 찾을 수 없습니다."),
    DUPLICATE_USER_ID(409, "U102", "이미 사용중인 아이디입니다."),

    //Profile
    DUPLICATE_ROLE(409, "P101", "이미 다른 가족 구성원이 설정한 역할입니다."),
    DUPLICATE_NICKNAME(409, "P102", "이미 다른 가족 구성원이 사용중인 닉네임입니다."),

    //Family
    NOT_FOUND_FAMILY(404, "F101", "가입된 가족 그룹이 없습니다.."),
    NOT_BELONG_FAMILY(405, "F102", "해당 그룹에 권한이 없습니다."),

    //Album
    FILE_SIZE_EXCEED(400, "A101", "파일 크기가 20MB를 초과합니다."),
    FILE_DOES_NOT_EXIST(400, "A102", "파일이 존재하지 않습니다."),
    FILE_DOWNLOAD_FAIL(400, "A103", "파일 다운로드에 실패했습니다."),
    FILE_UPLOAD_FAIL(400, "A104", "파일 업로드에 실패했습니다."),
    FILE_COUNT_EXCEED(400, "A105", "업로드 가능한 파일의 갯수를 초과했습니다."),
    WRONG_FILE_EXTENSION(400, "A106", "잘못된 파일 확장자명입니다.."),

    //Common
    INTERVAL_SERVER_ERROR(500, "C101", "서버에 오류가 발생했습니다."),
    INVALID_REQUEST(400, "C102", "잘못된 요청입니다."),
    INVALID_TOKEN(400, "C103", "유효하지 않은 토큰입니다.");


    private final int status;
    private final String code;
    private final String message;
}
