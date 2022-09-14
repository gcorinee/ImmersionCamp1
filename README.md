# 만나유 MannaU


## ****Abstraction****

코로나 때문에 얼굴도 못봤던 친구를 만나러 가자. 만날 지역의 날씨를 알아보자!

- 연락처: 기기에 저장된 연락처를 확인하고 정보를 수정할 수 있습니다. 문자와 전화가 가능합니다.
- 갤러리: 기기 내의 모든 사진을 볼 수 있습니다. 사진을 선택하여 연락처의 프로필 사진을 바꿀 수 있습니다.
- 날씨: Google Play Service의 Location과 기상청 api를 활용해 날씨 정보를 받아 확인 할 수 있습니다.

## 개발 환경

- OS: Android (minSdk: 21, targetSdk: 32)
- 언어: Java
- IDE: Android Studio
- Target Device: Galaxy S10e

---

## Contacts

![Contacts.png](https://user-images.githubusercontent.com/69137469/177309514-59fa1df2-7d92-461d-9351-8e5e9a4c5589.png)

- 기기에 있는 연락처를 불러옵니다.
- 사진과 이름, 직장으로 구성된 연락처 목록이 보이며, 각 연락처를 클릭하면 해당 연락처의 상세 페이지로 이동합니다.
- 상세 페이지에서는 이름, 직장, 전화번호, 이메일이 보이며 각각을 클릭하여 수정할 수 있습니다.
- 표시되는 정보는 항상 기기의 연락처와 동기화됩니다.
- 문자 버튼, 전화 버튼을 누르면 기기의 메시지, 전화 기능을 통해 상대에게 연락할 수 있습니다.

### 기술 설명

- 기기로부터 연락처 접근 권한을 받은 후 ContactContract 클래스를 사용해 기기에 저장된 연락처를 불러옵니다.
    - 불러온 연락처 정보는 modal에 저장하고, modal들은 ArrayList에 저장합니다.
- RecylcerView를 이용하여 불러온 연락처 목록을 보여줍니다.
- RecylcerView에서 각 itemView를 클릭하면 해당 연락처의 상세 정보를 보여주는 activity를 시작합니다.
    - Intent를 이용하여 activity를 시작할 때 해당 연락처의 modal을 함께 전달합니다.
- 상세 activity에서는 modal로부터 상세 정보를 불러와 대응하는 view에 띄웁니다.
- 이름, 직장, 전화번호, 이메일을 EditText view에 띄워 사용자가 정보를 수정할 수 있도록 합니다.
    - 사용자가 정보를 수정하고 키보드의 엔터를 클릭하면 해당 정보를 연락처에서도 업데이트합니다.
    - 연락처를 업데이트할 때 modal에 저장된 연락처의 id를 사용합니다.
- 사진을 클릭하면 gallery로 이동하여 gallery로부터 선택된 사진의 주소를 반환 받습니다.
    - 이미지의 uri를 구해 modal과 연락처의 정보를 업데이트합니다.

## Gallery

![KakaoTalk_Photo_2022-07-05-15-44-37 002.png](https://user-images.githubusercontent.com/69137469/177309664-9bebca0b-b38d-4320-bb8d-aa34a5f9fd14.png)

- 연락처의 프로필 사진을 누르면 기기의 모든 사진 폴더와 사진 개수를 보여주는 화면으로 넘어갑니다.
    - 사진이 없는 경우 `No Images on this device`가 뜹니다.
- 폴더의 썸네일을 누르면 해당 폴더로 이동합니다.
- 폴더 안의 사진을 눌러 사진을 자세히 볼 수 있습니다.
    - 화면 좌우 쓸어넘기기 또는 하단의 사진 미리보기를 통해 이웃한 사진을 빠르게 탐색할 수 있습니다.
    - 해당 화면의 우측 상단에 있는 “SELECT” 버튼을 누르면 연락처의 프로필 사진을 변경합니다.

### **기술 설명**

1. 모든 사진 폴더 보기
- MediaStore.Images에 있는 모든 사진 경로에서 사진 폴더 경로를 뽑아 picPaths에 저장합니다.
- 또한 폴더 이름, 폴더 속 사진 개수, 폴더의 첫번째 이미지(폴더의 썸네일)을 picFolders 객체로 저장합니다.
- RecyclerView를 이용해 모든 사진 폴더를 Nx3 형태로 화면에 띄웁니다.
- 각 폴더의 썸네일(ImageView)를  누르면 해당 폴더의 모든 사진이 화면에 뜹니다.
2. 사진 상세보기
- ViewPager를 이용해 사진을 화면에 크게 띄웁니다.
- 사진을 좌우로 쓸어넘기면 폴더의 이전/다음 사진이 뜹니다.
    
    ```java
    imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    		...
        @Override
        public void onPageSelected(int position) {
    
            if (previousSelected != -1) {
                allImages.get(previousSelected).setSelected(false);
            }
            previousSelected = position;
            allImages.get(position).setSelected(true);
            indicatorRecycler.getAdapter().notifyDataSetChanged();
            indicatorRecycler.scrollToPosition(position);
        }
    		...
    });
    ```
    
- RecyclerView로 구현한 하단의 사진 인디케이터로 다른 사진을 탐색할 수 있습니다.
    
    ```java
    @Override
    public void onImageIndicatorClicked(int ImagePosition) {
        if (previousSelected != -1) {
            allImages.get(previousSelected).setSelected(false);
            previousSelected = ImagePosition;
            indicatorRecycler.getAdapter().notifyDataSetChanged();
        } else {
            previousSelected = ImagePosition;
        }
    
        imagePager.setCurrentItem(ImagePosition);
    }
    ```
    
- 화면 우측 상단의 “SELECT” 버튼을 누르면 연락처 자세히 보기 화면의 ViewModel에 해당 사진의 URI를 전달하여 연락처의 사진을 업데이트 합니다.
    
    ```java
    selectThisImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ContactDetailActivity.getImgPathViewModel().setImgPath(allImages.get(position).getPicturePath());
            getActivity().onBackPressed();
        }
    });
    ```
    

참고 [https://github.com/CodeBoy722/Android-Simple-Image-Gallery](https://github.com/CodeBoy722/Android-Simple-Image-Gallery)

## Weather

![Untitled](https://user-images.githubusercontent.com/69137469/177309773-74bf6b6b-90aa-4120-87f2-d7e72d8ed4ab.png)

- 연락처 위에 위젯 형태로 현재 위치의 날씨 정보를 보여줍니다.
- 위젯을 클릭하면 지역별 날씨를 알 수 있는 화면으로 넘어갑니다.
- 지역별 날씨 화면에서 각 아이템을 클릭하면 더 자세한 날씨 정보 메시지가 나옵니다.

### **기술 설명**

- FrameLayout을 활용해 기존 연락처 위에 단일 원소 리스트뷰를 투명도 있게 띄워 위젯 형태로 구현했습니다.
- Google Play Service의 fusedLocationProviderClient를 활용해 현재 위치를 불러왔습니다.
    - 동기화를 위해 Callback() 함수를 사용했습니다.
- 위치 정보와 기상청 api를 활용해 별도의 쓰레드에서 여러 날씨 정보를 받았습니다.
    - 동기화를 위해 정보를 받아오는 중에는 thread.join()을 통해 메인 쓰레드를 정지시켰습니다.
- 지역별 날씨에서 아이템을 클릭하면 해당 지역의 상세한 날씨 정보를 Toast 메시지를 통해 보여줍니다.

---

## 기여자

- 허회준
- 최예빈
- 김우일
