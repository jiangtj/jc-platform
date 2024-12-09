package com.jiangtj.platform.system;

import com.jiangtj.platform.system.dto.SharePublicKey;
import com.jiangtj.platform.system.jooq.tables.records.SystemKeyShareRecord;
import com.jiangtj.platform.web.BaseExceptionUtils;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDateTime;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_KEY_SHARE;

@Service
public class KeyService {

    @Resource
    private DSLContext create;

    public void publishKey(SharePublicKey key) {
        PublicJwk<PublicKey> jwk = key.getJwk();
        String kid = jwk.getId();
        SystemKeyShareRecord record = create.selectFrom(SYSTEM_KEY_SHARE)
            .where(SYSTEM_KEY_SHARE.KID.eq(kid))
            .fetchOne();
        if (record == null) {
            record = create.newRecord(SYSTEM_KEY_SHARE);
        }
        record.setKid(kid);
        record.setApplication(key.getApplication());
        record.setJwk(Jwks.json(jwk));
        record.setPublishTime(LocalDateTime.now());
        record.store();
    }

    @SuppressWarnings("unchecked")
    public PublicJwk<PublicKey> getPublishKey(String kid) {
        SystemKeyShareRecord record = create.selectFrom(SYSTEM_KEY_SHARE)
            .where(SYSTEM_KEY_SHARE.KID.eq(kid))
            .fetchOne();

        if (record == null) {
            throw BaseExceptionUtils.badRequest("未找到公钥!");
        }

        record.setReadTime(LocalDateTime.now());
        record.store();
        String jwk = record.getJwk();

        return (PublicJwk<PublicKey>) Jwks.parser().build().parse(jwk);
    }

}
