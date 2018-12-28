package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.notifications.KeyStorage;
import com.tablefootbal.server.notifications.dto.Push;
import com.tablefootbal.server.notifications.entity.FirebaseResponse;
import com.tablefootbal.server.notifications.filters.HeaderRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class NotificationSender implements NotificationService {

    private String fcmKey = KeyStorage.FCM_key;

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";

    public FirebaseResponse sendNotification(Push push){

        HttpEntity<Push> request = new HttpEntity<>(push);
        CompletableFuture<FirebaseResponse> pushNotification = this.send(request);
        CompletableFuture.allOf(pushNotification).join();

        FirebaseResponse firebaseResponse = null;

        try{
            firebaseResponse = pushNotification.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }


        return firebaseResponse;
    }

    @Async
    CompletableFuture<FirebaseResponse> send(HttpEntity<Push> entity){

        RestTemplate restTemplate = new RestTemplate();
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", fcmKey));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);
        FirebaseResponse firebaseResponse = restTemplate.postForObject(FCM_API, entity, FirebaseResponse.class);
        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
