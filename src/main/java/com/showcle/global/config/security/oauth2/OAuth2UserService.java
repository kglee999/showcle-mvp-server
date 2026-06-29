package com.showcle.global.config.security.oauth2;

import com.showcle.api.auth.dto.MemberSocial;
import com.showcle.global.enums.SnsType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    // /login/oauth2/code/{provider} Security 에서 자동 처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String vendor = userRequest.getClientRegistration().getRegistrationId();
        SnsType snsType = SnsType.getByName(vendor);
        OAuth2User user = super.loadUser(userRequest);

        snsType = null;

        // KAKAO, APPLE, GOOGLE 가 아닐 때
        if(snsType == null) {
            throw new OAuth2AuthenticationException("Provider not supported : " + vendor);
        }

        MemberSocial social = new MemberSocial();
        social.setProvider(snsType.getName());

        switch (snsType) {
            case GOOGLE:
                Object sub = user.getAttribute("sub");
                if (sub == null) throw new OAuth2AuthenticationException("Google sub is null");

                social.setProviderUserId(sub.toString());
                social.setEmail(user.getAttribute("email"));
                break;
            case KAKAO:
                Object id = user.getAttribute("id");
                if (id == null) throw new OAuth2AuthenticationException("Kakao id is null");

                social.setProviderUserId(id.toString());
                Map<String, Object> kakaoAccount = user.getAttribute("kakao_account");
                if (kakaoAccount != null) {
                    social.setEmail((String) kakaoAccount.get("email"));
                }
                break;
            case APPLE:
                break;
        }

        // 소셜 정보 업데이트 INSERT, UPDATE

        // PROVIDER, PROVIDER USER ID 으로 조회 - 연동된 맴버 정보가 있다면 조회
        // 로그인 처리


        // 정보가 없다면 INSERT
        // 연동된 정보가 없습니다. 에러 띄우기
        // 디비에 id 가 없다면 insert 후 회원 가입 로직 진행

        log.info("user : {}", user);
        return super.loadUser(userRequest);
    }
}
