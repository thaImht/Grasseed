package com.hetao.grasseed.common.util;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.lshift.diffa.snowflake.IdProvider;
import net.lshift.diffa.snowflake.InvalidSystemClockException;
import net.lshift.diffa.snowflake.SequenceExhaustedException;
import net.lshift.diffa.snowflake.SnowflakeIdProvider;

@Component
@Slf4j
public class IdGenerator implements Configurable,IdentifierGenerator{

    private IdProvider idProvider;

    public synchronized Long getNextId() throws RuntimeException {
        try {
            return  idProvider.getId();
        } catch (InvalidSystemClockException | SequenceExhaustedException e) {
            log.error("IdGenerator_exception:",e);

            throw new RuntimeException(e);
        }
    }

	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		try{
            return getNextId();

        }catch (Exception ex){
            log.error("idGenerator_orderId_Exception",ex);
        }

        return 0L;
	}

	@Override
	public void configure(Type arg0, Properties arg1, ServiceRegistry arg2) throws MappingException {
		IdProvider idProvider = new SnowflakeIdProvider(127);
        ((SnowflakeIdProvider) idProvider).setTimeFn(() -> System.currentTimeMillis() / 1000);
        this.idProvider = idProvider;
	}




}
