package com.koreait.surl_project_11.domain.surl.surl.service;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.surl.surl.entity.Surl;
import com.koreait.surl_project_11.domain.surl.surl.repository.SurlRepository;
import com.koreait.surl_project_11.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurlService { // 비지니스 로직 처리 담당
    private final SurlRepository surlRepository;
    public List<Surl> findAll() {
        return surlRepository.findAll();
    }
    @Transactional
    public RsData<Surl> add(Member author, String body, String url) {
        Surl surl = Surl.builder()
                .author(author)
                .body(body)
                .url(url).build();
        surlRepository.save(surl);
        return RsData.of("%d번 URL이 등록됨".formatted(surl.getId()), surl);
    }
    public Optional<Surl> findById(long id) {
        return surlRepository.findById(id);
    }
    @Transactional
    public void increaseCount(Surl surl) {
        surl.increaseCount();
    }
}
