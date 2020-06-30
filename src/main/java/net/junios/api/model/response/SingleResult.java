package net.junios.api.model.response;

import lombok.Data;

@Data
public class SingleResult<T> extends CommonResult {
    private T Data;
}


