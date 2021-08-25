本仓库一共两个组件
城市列表组件和loading组件

# 1.城市列表组件

```
    mCitylistlayout = findViewById(R.id.cityListLayout);

    mCitylistlayout.addCurrLocation(cityBean, R.layout.item_city);
    mCitylistlayout.addCitySpecialData("热门", hotlist);
    mCitylistlayout.addCityList(allList);
    mCitylistlayout.setItemClickListener(new CityListLayout.ItemClickListener() {
        @Override
        public void headerViewClick(CityBean cityBean) {
            Toast.makeText(MainActivity2.this, "" + cityBean.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void flowItemClick(CityBean cityBean) {

            Toast.makeText(MainActivity2.this, "" + cityBean.toString(), Toast.LENGTH_SHORT).show();
        }
    });

```
# 2.loading组件

Activity里使用
```
    //oncreate
   noBoLoadingManager = new NoBoLoadingManager(this);

    //加载数据前
   noBoLoadingManager.showLoading();

   //加载失败
   noBoLoadingManager.showLoadFailed(new LoadingView.IRetryClickListener() {
                               @Override
                               public void retryClick() {
                                   initViews();
                               }
                           });

    //加载成功
     noBoLoadingManager.showLoadSuccess();

     //回收资源
     noBoLoadingManager.clear();

```

Fragment和view使用 类似

```
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, null);
        //传入根布局
        noBoLoadingManager = new NoBoLoadingManager(root);

        mImage = root.findViewById(R.id.image);

        initViews();

        //返回parent对象
        return (View) root.getParent();
    }



   noBoLoadingManager.showLoading();

   noBoLoadingManager.showLoadFailed(new LoadingView.IRetryClickListener() {
                             @Override
                             public void retryClick() {
                                 initViews();
                             }
                         });
    noBoLoadingManager.showLoadSuccess();


```
