package lk.ijse.dep11.edupanel.advice;

import com.google.cloud.storage.Bucket;
import lk.ijse.dep11.edupanel.service.ServiceFactory;
import lk.ijse.dep11.edupanel.store.AppStore;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;

@Aspect
@Component
public class RequestAdvice {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Bucket bucket;

    @Before("within(lk.ijse.dep11.edupanel.api.*) &&" +
            " @target(org.springframework.web.bind.annotation.RestController)")
    public void interceptHandlerMethods(JoinPoint jp) throws NoSuchFieldException, IllegalAccessException {
        AppStore.setBucket(bucket);
        AppStore.setEntityManager(entityManager);
        Field field = jp.getTarget().getClass().getDeclaredField("lecturerService");
        field.setAccessible(true);
        field.set(jp.getTarget(), ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER));
    }
}
