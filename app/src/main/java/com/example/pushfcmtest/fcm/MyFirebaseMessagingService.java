package com.example.pushfcmtest.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.pushfcmtest.MainActivity;
import com.example.pushfcmtest.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("토큰", "새 토큰 발급");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        String pushTitle = remoteMessage.getNotification().getTitle();
        String pushBody = remoteMessage.getNotification().getBody();
        String pushName = remoteMessage.getNotification().getTag();

        Log.d("푸시 알림 제목", pushTitle);
        Log.d("푸시 알림 바디", pushBody);
        Log.d("푸시 알림 태그", pushName);

        showNotification(pushTitle, pushBody);
    }

    public void showNotification(String title, String msg){

        //알림을 누르면 어느 화면으로 갈지 설정
        // 메인 페이지 외에도 화면 이동 가능
        Intent intent = new Intent(this, MainActivity.class);

        // 화면에 액티비티가 쌓여있으면 모두 제거하고 새 액티비티로 호출, 꼭 해야하는건 아님
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // 바로 띄우는게 아니라, 누르면 띄우도록 처리하기 위한 겉을 감싸는 인텐트

        // 이 PendingInteent 를 표시될 알림에 첨부
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                                            0,
                                                                        intent,
                                                                        PendingIntent.FLAG_ONE_SHOT);

        //푸시 알림이 왔을때 울릴 소리를 설정 연습 : 폰의 기본 설정으로 적용
        //Uri : 어딘가 향하는 경로를 지정할때 자주 사용
        Uri defaultNotiUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // 알림을 띄우는 역할을 담당하는 매니저를 안드로이드 시스템으로 얻어옴
        // 안드로이드 시스템 서비스는 여러 가지 종류를 내포, 알림을 쓰기 때문에 알림을 강제 케스팅
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // 알림 내용을 구성해주는 Builder 변수 생성
        NotificationCompat.Builder builder = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 안드로이드 버전 코드가 0보다 크다 ! 오레오 이상 버전

            // 알림 체널을 만들어서 해당 채널에 푸시알림을 던져주는 방식



        }else{
            // 오레오 하위 계정 N버전 이하, 누가, 마시멜로, 롤리팝, 아이스크림 샌드위치

            // 알림 채널이라는 기능이 없어서, 기능 통일해서 쓰면 앱 죽어버림
            builder = new NotificationCompat.Builder(this);

            // 푸시 알림 상태에서 띄우는 아이콘 설정
            builder.setSmallIcon(R.mipmap.ic_launcher);
            //알림 제목
            builder.setContentTitle(title);
            //알림 내용
            builder.setContentText(msg);

            // 알림 소리
            builder.setSound(defaultNotiUri);


            // 알림 진동 패턴 ( 1초에 한번 )
            builder.setVibrate(new Long[][{1, 1000}]);

            // 시간 지나면 사라지도록
            builder.setAutoCancel(true);

            // 이 알림을 누르면 어디로 갈지? 아까 만든 pending intent 활용 지정
            builder.setContentIntent(pendingIntent);

            // 실제로 알림을 띄우는 부분

            // id 를 일반 숫자로 고정하면, 항상 같은 id 입력 => 여러번 알림이 오면 기존 알림을 덮어씀
            // 만약 여러개의 알림을 모두 띄우고 싶다면 그때 그때 다른 숫자가 들어오도록 코딩
            notificationManager.notify(1, builder.build());

        }


    }
}
