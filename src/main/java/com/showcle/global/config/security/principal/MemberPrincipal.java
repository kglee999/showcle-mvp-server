package com.showcle.global.config.security.principal;

import com.showcle.api.auth.dto.Member;
import com.showcle.global.enums.Role;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@ToString
public class MemberPrincipal implements UserDetails {

    private long idx;
    private String username;
    private String passwd;
    private String name;
    private int grade;
    private String countryCode;
    private String phone;
    private String device;
    private String pushToken;
    private long profileImg;
    private int pushYn;

    public MemberPrincipal(Member member) {
        this.idx = member.getIdx();
        this.username = member.getEmail();
        this.name = member.getName();
        this.grade = member.getGrade();
        this.countryCode = member.getCountryCode();
        this.phone = member.getPhone();
        this.device = member.getDevice();
        this.pushToken = member.getPushToken();
        this.profileImg = member.getProfileImg();
        this.pushYn = member.getPushYn();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(int grade) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roles = Role.getRoleNames(grade);

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthorities(this.grade);
    }

    @Override
    public String getPassword() {
        return this.passwd;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
