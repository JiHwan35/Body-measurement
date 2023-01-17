# 사용자 신체 치수 측정 서비스
   
   Automatic Estimation of Anthropometric Human Body Measurements
   
## 프로젝트 목표

     1. 사용자가 직접 촬영한 사진을 기반으로 Body-Pix의 Body segmentation을 통해서 얻은 픽셀 정보를 이용하여 신체 치수 정보를 사용자에게 제공
     
     2. 사용자가 직접 촬영한 사진을 기반으로 딥러닝 알고리즘으로 측정된 신체 치수 정보를 사용자에게 제공
     
     3. 어깨 너비, 가슴 둘레, 총장 (상체 길이), 허리너비, 총장 (하체 길이)등 여러 치수를 사용자에게 출력
     

![image](https://user-images.githubusercontent.com/93183216/212838042-69b17bad-2b12-4ac5-8bc2-0bb73165626f.png)


## 1. 모델 기반 치수 측정 - Body-pix API 활용
![image](https://user-images.githubusercontent.com/93183216/212838245-a5f90809-7172-4207-8b64-5826488d9aff.png)
```
   1. Human segmentation(Full–Body segmentation)
   
   - 각 픽셀이 사람과 배경 중 어떤 것인지 0과 1사이의 확률 값으로 표현
   - Threshold (0.5) 이상의 값은 사람의 일부로 가정하여 바이너리 값 1로 변환
   
   2. Body part segmentation
   
   - 각 픽셀을 24개의 Body part에 대한 0과 1 사이의 확률 값으로 표현
   - 확률 값이 가장 큰 채널을 픽셀의 Body part ID값으로 채택  
```
### 활용 Dataset
```
1. Human segmentation

Dataset : COCO 2017 Train/Val Images (66000 images)
Model : MovileNetV1*
Labels: Person instances labeled with 2D key points
```
![image](https://user-images.githubusercontent.com/93183216/212838926-59a02102-d97a-4392-9a42-b5c19691b2c2.png)

```
2. Body part segmentation

Dataset : 이미 분할된 신체 부분을 컴퓨터 그래픽으로 Rendering한 뒤, 합쳐 제작된 가상의 image
Model : ResNet50
Labels: Segmented Body-parts
```
######*Howard, Andrew G., et al. "Mobilenets: Efficient convolutional neural networks for mobile vision applications." arXiv preprint arXiv:1704.04861 (2017).

### Algorithm

![image](https://user-images.githubusercontent.com/93183216/212843178-3f15de45-2b4b-4ff3-9c16-0ff6d648a203.png)
```
Body part 각각의 픽셀 배열 정보를 이용하여 사용자의 신체 치수를 측정 
```

![image](https://user-images.githubusercontent.com/93183216/212843348-3dd619da-d253-47db-becc-56515eebd9e7.png)
```
사용자가 측정하려는 대상의 Height 실제 값으로 Height의 픽셀 값(픽셀 수)를 구한 뒤 
픽셀 당 실제 길이(cm)를 이용하여 다른 part의 치수를 계산
```

1) 강연우, et al. 2D 이미지에서 기계학습 기법을 활용한 특징점 찾기 및 신체 치수 추출. 한국정보기술학회논문지, 2018, 16.4: 29-36.
2) 무신사 스토어 사이즈 가이드 [웹사이트]. (2022.06.10). URL:https://www.musinsa.com/app/product/real_size


## 치수 측정 - 어플리케이션 구현

1. 모델 기반 치수 측정

https://user-images.githubusercontent.com/93183216/212837490-37dde234-9b14-4f10-b1ad-e5b0c2086436.mp4

2. 딥러닝 기반 치수 측정

https://user-images.githubusercontent.com/93183216/212832973-2b76d05c-a0d5-4136-9e16-5d2a93e35658.mp4

