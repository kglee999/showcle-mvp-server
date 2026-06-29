package com.showcle.api.auth.mapper;

import com.showcle.api.auth.dto.EmailFindRequest;
import com.showcle.api.auth.dto.Member;
import com.showcle.api.auth.dto.MemberAuth;
import com.showcle.api.auth.dto.MemberRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 이메일로 회원 조회
    Member findByEmail(@Param("email") String email);

    // 인증 메일 발송 이력 저장
    int insertMemberMailAuth(@Param("email") String email, @Param("code") String code);

    // 인증 메일 발송 최종 이력 조회 - 만료시간 체크해야 됨
    MemberAuth findLatestMemberMailCode(@Param("email") String email);

    // 인증 완료 업데이트
    int updateMemberMailAuth(@Param("idx") long idx);

    // 이메일 인증 완료 여부 확인
    int isEmailVerified(@Param("email") String email);

    // 회원정보 저장
    int insert(MemberRequest param);

    // 이름으로 회원정보 검색
    List<Member> findMemberByName(@Param("name") String name);

    // 이름, 이메일로 회원정보 검색
    List<Member> findMemberByNameOrEmail(@Param("email") String email, @Param("name") String name);

    // 최종 로그인 시간 업데이트
    int updateLastLoginDt(@Param("idx") long idx);
}
