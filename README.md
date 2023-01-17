# 사용자 신체 치수 측정 서비스

Automatic Estimation of Anthropometric Human Body Measurements


## 프로젝트 목표

1. 사용자가 직접 촬영한 사진을 기반으로 Body-Pix의 Body segmentation을 통해서 얻은 픽셀 정보를 
   이용하여 신체 치수 정보를 사용자에게 제공 

2. 사용자가 직접 촬영한 사진을 기반으로 딥러닝 알고리즘으로 측정된 신체 치수 정보를 사용자에게 제공 

3. 어깨 너비, 가슴 둘레, 총장 (상체 길이), 허리너비, 총장 (하체 길이)등 여러 치수를 사용자에게 출력

![image](https://user-images.githubusercontent.com/93183216/212838042-69b17bad-2b12-4ac5-8bc2-0bb73165626f.png)


## 1. 모델 기반 치수 측정 - Body-pix API 활용

![image](https://user-images.githubusercontent.com/93183216/212838245-a5f90809-7172-4207-8b64-5826488d9aff.png)

1. Human segmentation(Full–Body segmentation)
- 각 픽셀이 사람과 배경 중 어떤 것인지 0과 1사이의 확률 값으로 표현
- Threshold (0.5) 이상의 값은 사람의 일부로 가정하여 바이너리 값 1로 변환

2. Body part segmentation
- 각 픽셀을 24개의 Body part에 대한 0과 1 사이의 확률 값으로 표현
- 확률 값이 가장 큰 채널을 픽셀의 Body part ID값으로 채택  




## 치수 측정 - 어플리케이션 구현

1. 모델 기반 치수 측정

https://user-images.githubusercontent.com/93183216/212837490-37dde234-9b14-4f10-b1ad-e5b0c2086436.mp4

2. 딥러닝 기반 치수 측정

https://user-images.githubusercontent.com/93183216/212832973-2b76d05c-a0d5-4136-9e16-5d2a93e35658.mp4

