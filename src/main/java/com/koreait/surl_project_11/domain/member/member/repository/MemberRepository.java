package com.koreait.surl_project_11.domain.member.member.repository;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String Username);

    //    Optional<Member> findByApiKey(String apiKey);
    Optional<Member> findByRefreshToken(String refreshToken);
}
