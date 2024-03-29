package com.ll.traveler.global.exceptions;

import com.ll.traveler.global.rsData.RsData;
import lombok.Getter;

public class GlobalException extends RuntimeException {

    @Getter
    private RsData<?> rsData;

    public GlobalException(String resultCode, String msg) {
        super(resultCode + " " + msg);
        this.rsData = RsData.of(resultCode, msg);
    }
}
