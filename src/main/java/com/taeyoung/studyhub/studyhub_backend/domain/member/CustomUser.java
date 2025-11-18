package com.taeyoung.studyhub.studyhub_backend.domain.member;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {
    public Long id;
    private String email;

    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    public CustomUser(Long id, String username, String password, String email, ProviderType provider, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.email = email;
        this.provider = provider;
    }
}
