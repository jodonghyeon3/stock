package org.stock.exception.imple;

import org.aspectj.bridge.AbortException;
import org.springframework.http.HttpStatus;
import org.stock.exception.AbstractException;

public class AlreadyExistUserException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 사용자 명입니다.";
    }
}
