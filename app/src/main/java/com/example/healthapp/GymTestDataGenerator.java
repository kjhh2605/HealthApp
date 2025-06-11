package com.example.healthapp;

import com.example.healthapp.model.Gym;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GymTestDataGenerator {
    // 머신 브랜드 목록
    private static final String[] MACHINES = {
            "해머스트렝스", "싸이벡스", "파나타", "테크노짐", "짐80", "프리코어", "라이프 피트니스",
            "왓슨", "아틀란티스", "프라임", "너틸러스", "매트릭스",
            "뉴텍", "디렉스", "개선스포츠", "다이나포스", "렉스코", "포커스", "신코"
    };

    // 트레이너 이름 목록
    private static final String[] TRAINER_NAMES = {
            "김철수", "이영희", "박민수", "최민지", "정우성", "한지민", "이준호", "김태희", "송중기", "전지현", "유재석", "강호동", "박보검", "아이유", "배수지"
    };

    // 자격증 목록
    private static final String[] CERTIFICATES = {
            "스포츠지도사", "필라테스지도사", "요가지도사", "근력운동지도사", "헬스트레이너", "복싱지도사", "수영강사", "재활운동사", "영양사", "퍼스널트레이너"
    };

    // 서울시 구(區) 리스트
    private static final String[] SEOUL_DISTRICTS = {
            "도봉구", "노원구", "강북구", "성북구", "중랑구", "동대문구", "광진구", "성동구", "종로구", "중구",
            "용산구", "마포구", "서대문구", "은평구", "강서구", "양천구", "구로구", "영등포구", "동작구",
            "관악구", "서초구", "강남구", "송파구", "강동구"
    };

    private static final Random random = new Random();

    // 자격증 2개 랜덤 선택 (ArrayList)
    private static ArrayList<String> getRandomCertificates() {
        ArrayList<String> certs = new ArrayList<>();
        List<String> certList = new ArrayList<>(Arrays.asList(CERTIFICATES));
        Collections.shuffle(certList);
        certs.add(certList.get(0));
        certs.add(certList.get(1));
        return certs;
    }

    // 트레이너 2명 랜덤 선택 (HashMap<String, ArrayList<String>>)
    private static HashMap<String, ArrayList<String>> getRandomTrainers() {
        HashMap<String, ArrayList<String>> trainers = new HashMap<>();
        List<String> trainerList = new ArrayList<>(Arrays.asList(TRAINER_NAMES));
        Collections.shuffle(trainerList);
        trainers.put(trainerList.get(0), getRandomCertificates());
        trainers.put(trainerList.get(1), getRandomCertificates());
        return trainers;
    }

    // 머신 5개 랜덤 선택 (ArrayList)
    private static ArrayList<String> getRandomMachines() {
        ArrayList<String> machines = new ArrayList<>();
        List<String> machineList = new ArrayList<>(Arrays.asList(MACHINES));
        Collections.shuffle(machineList);
        for (int i = 0; i < 5; i++) {
            machines.add(machineList.get(i));
        }
        return machines;
    }

    // Gym 테스트 데이터 1개 생성
    private static Gym generateGym(int index) {
        Gym gym = new Gym();
        gym.setName("헬스장 " + (index + 1));
        gym.setRegion("서울시 " + SEOUL_DISTRICTS[random.nextInt(SEOUL_DISTRICTS.length)]);
        gym.setPrice(new int[]{40000, 50000, 60000, 70000, 90000}[random.nextInt(5)]);
        gym.setMachineList(getRandomMachines());
        gym.setTrainerList(getRandomTrainers());
        return gym;
    }

    // Gym 테스트 데이터 10개 생성 및 Firebase 저장
    public static void generateAndSaveGyms(DatabaseReference gymRef, int count) {
        for (int i = 0; i < count; i++) {
            Gym gym = generateGym(i);
            gymRef.child("Gym" + (i + 1)).setValue(gym);
        }
    }

    // 예시: Activity/Fragment에서 호출
    public static void addTestGyms() {
        DatabaseReference gymRef = FirebaseDatabase.getInstance().getReference("GYMPT/Gym");
        generateAndSaveGyms(gymRef, 100);
    }
}
