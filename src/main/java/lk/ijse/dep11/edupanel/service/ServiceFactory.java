package lk.ijse.dep11.edupanel.service;

import lk.ijse.dep11.edupanel.service.custom.impl.LecturerServiceImpl;

import javax.transaction.NotSupportedException;

public class ServiceFactory {

    private static ServiceFactory INSTANCE;

    public enum ServiceType{
        LECTURER, USER
    }

    private ServiceFactory(){}

    public static ServiceFactory getInstance() {
        return (INSTANCE == null)? (INSTANCE = new ServiceFactory()): INSTANCE;
    }

    public <T extends SuperService> T getService(ServiceType type){
        switch (type){
            case LECTURER:
                return (T) new LecturerServiceImpl();
            case USER:
                throw new RuntimeException("Not implemented yet");
            default:
                throw new IllegalArgumentException();
        }
    }
}
