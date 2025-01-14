package com.devfiles.enterprise.abstraction;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;

public interface UseCase<I, O> {
    ResponseDto<O> execute(I input);
}
