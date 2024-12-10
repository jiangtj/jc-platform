package com.jiangtj.platform.system;

import com.jiangtj.platform.spring.cloud.JwkHolder;
import com.jiangtj.platform.spring.cloud.core.RegisterPublicKey;
import com.jiangtj.platform.system.jooq.tables.records.SystemKeyShareRecord;
import com.jiangtj.platform.web.BaseExceptionUtils;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_KEY_SHARE;

@Service
public class KeyService {

    @Resource
    private DSLContext create;

    @SuppressWarnings("unchecked")
    public void registerPublishKey(RegisterPublicKey key) {
        PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>) Jwks.parser().build().parse(key.getJwk());
        String id = publicJwk.getId();
        if (!key.getKid().equals(id)) {
            throw new IllegalArgumentException("kid and jwk kid not match");
        }
        registerPublishKey(key.getApplication(), publicJwk);
    }

    public void registerPublishKey(String application, PublicJwk<PublicKey> jwk) {
        String kid = jwk.getId();

        SystemKeyShareRecord record = fetchKeyRecord(kid)
            .orElseGet(() -> create.newRecord(SYSTEM_KEY_SHARE));

        record.setKid(kid);
        record.setApplication(application);
        record.setJwk(Jwks.json(jwk));
        record.setPublishTime(LocalDateTime.now());
        record.store();
    }

    @SuppressWarnings("unchecked")
    public PublicJwk<PublicKey> getPublishKey(String kid) {
        PublicJwk<PublicKey> publicJwk = JwkHolder.getPublicJwk();
        if (kid.equals(publicJwk.getId())) {
            return publicJwk;
        }

        SystemKeyShareRecord record = fetchKeyRecord(kid)
            .orElseThrow(() -> BaseExceptionUtils.badRequest("未找到公钥!"));

        record.setReadTime(LocalDateTime.now());
        record.store();
        String jwk = record.getJwk();

        return (PublicJwk<PublicKey>) Jwks.parser().build().parse(jwk);
    }

    public Optional<SystemKeyShareRecord> fetchKeyRecord(String kid) {
        return create.selectFrom(SYSTEM_KEY_SHARE)
            .where(SYSTEM_KEY_SHARE.KID.eq(kid))
            .fetchOptional();
    }

}
