package com.clientui.exceptions;


import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder{
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoqueur, Response reponse) {

        return defaultErrorDecoder.decode(invoqueur, reponse);
    }

}