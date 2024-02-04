package com.demo.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CommonResponse {
    private String resultCode;
    private Map playLoad;
}
