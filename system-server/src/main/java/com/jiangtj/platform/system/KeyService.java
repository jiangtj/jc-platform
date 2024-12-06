package com.jiangtj.platform.system;

import com.jiangtj.platform.sql.jooq.PageUtils;
import com.jiangtj.platform.system.entity.SharePublicKey;
import com.jiangtj.platform.system.entity.SystemUser;
import com.jiangtj.platform.system.jooq.tables.daos.SystemKeyShareDao;
import com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare;
import com.jiangtj.platform.system.jooq.tables.records.SystemKeyShareRecord;
import com.jiangtj.platform.system.jooq.tables.records.SystemUserRecord;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.jooq.DSLContext;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_KEY_SHARE;
import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_USER;
import static org.jooq.impl.DSL.noCondition;

@Service
public class KeyService {

    @Resource
    private DSLContext create;
    @Resource
    private SystemKeyShareDao systemKeyShareDao;


    public void publishKey(SharePublicKey key) {
        SystemKeyShare systemKeyShare = new SystemKeyShare(key.getKid(), Jwks.json(key.getJwk()), LocalDateTime.now());
        SystemKeyShareRecord record = create.newRecord(SYSTEM_KEY_SHARE, systemKeyShare);
        record.store();
        record.into(SystemKeyShare.class);
    }

    @SuppressWarnings("unchecked")
    public PublicJwk<PublicKey> getPublishKey(String kid) {
        /*return create.selectFrom(SYSTEM_KEY_SHARE)
            .where(SYSTEM_KEY_SHARE.KID.eq(kid))
            .fetchOptionalInto(SharePublicKey.class)
            .map(SharePublicKey::getJwk)
            .orElseThrow();*/
        return (PublicJwk<PublicKey>) systemKeyShareDao.fetchOptionalByKid(kid)
            .map(SystemKeyShare::jwk)
            .map(jwk -> Jwks.parser().build().parse(jwk))
            .orElseThrow();
    }

}
