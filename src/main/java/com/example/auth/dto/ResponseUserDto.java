package com.example.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseUserDto {
    private String userName;
    private String password;
    private String name;
    private String surName;
}
