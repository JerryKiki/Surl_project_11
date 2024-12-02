package com.koreait.surl_project_11.domain.auth.auth.service;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.surl.surl.entity.Surl;
import com.koreait.surl_project_11.global.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    //Surl 관련
    public void checkCanGetSurl(Member actor, Surl surl) {
        if (!canGetSurl(actor, surl)) {
            throw new GlobalException("403-1", "권한이 없습니다.");
        }
    }

    private boolean canGetSurl(Member actor, Surl surl) {
        if (actor == null) return false;
        if (surl == null) return false;
        return actor.equals(surl.getAuthor());
    }

    public void checkCanDeleteSurl(Member actor, Surl surl) {
        if (!canDeleteSurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canDeleteSurl(Member actor, Surl surl) {
        return canGetSurl(actor, surl);
    }

    public void checkCanModifySurl(Member actor, Surl surl) {
        if (!canModifySurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canModifySurl(Member actor, Surl surl) {
        return canGetSurl(actor, surl);
    }
    //Surl 관련
}
