package com.koreait.surl_project_11.domain.surl.surl.repository;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.surl.surl.entity.Surl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurlRepository extends JpaRepository<Surl, Long> {
    List<Surl> findByAuthorOrderByIdDesc(Member author);
}
