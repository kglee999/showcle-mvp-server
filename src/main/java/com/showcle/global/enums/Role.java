package com.showcle.global.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Role {
   USER(1);

   private final int code;

   Role(int code) {
      this.code = code;
   }

   public static List<String> getRoleNames(int grade) {
       List<String> roleList = new ArrayList<>();

       for(Role role : Role.values()) {
           if(role.code <= grade) {
               roleList.add(role.name());
           }
       }
       return roleList;
   }
}
