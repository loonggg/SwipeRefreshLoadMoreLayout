# SwipeRefreshLoadMoreLayout
一个 Material Design 风格的下拉刷新，上拉自动加载更多的库，支持下拉刷新，上拉自动加载更多，还支持点击加载更多，还支持列表为空时显示空状态下的视图。

## 效果图
![](https://raw.githubusercontent.com/loonggg/SwipeRefreshLoadMoreLayout/master/image/ssd.gif)

## 使用方法
### Step 1. Add the JitPack repository to your build file 
Add it in your root build.gradle at the end of repositories:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

### Step 2. Add the dependency
```java
	dependencies {
	        compile 'com.github.loonggg:SwipeRefreshLoadMoreLayout:v1.0'
	}
```

### Example
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.loonggg.swiperefreshloadmorelayout.MainActivity">

    <com.loonggg.refreshloadmorelib.view.SwipeRefreshLoadMoreLayout
        android:id="@+id/swipelayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</RelativeLayout>
```

### MainActivity.java
```java
public class MainActivity extends AppCompatActivity implements SwipeRefreshLoadMoreLayout.OnRefreshListener, SwipeRefreshLoadMoreLayout.OnLoadMoreListener {
    private SwipeRefreshLoadMoreLayout swipeRefreshLoadMoreLayout;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLoadMoreLayout = (SwipeRefreshLoadMoreLayout) findViewById(R.id.swipelayout);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        swipeRefreshLoadMoreLayout.setLayoutManager(mLayoutManager);
        swipeRefreshLoadMoreLayout.setOnLoadMoreListener(this);
        swipeRefreshLoadMoreLayout.setOnRefreshListener(this);
        getDatas();
        final BaseRecyclerAdapter adapter = new BaseRecyclerAdapter<String>(this, list) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_rv;
            }

            @Override
            public void bindData(RecyclerView.ViewHolder holder, int position, String item) {
                RecyclerViewHolder mHolder = (RecyclerViewHolder) holder;
            }
        };
        swipeRefreshLoadMoreLayout.setAdapter(adapter);
        swipeRefreshLoadMoreLayout.setLoadMore(true);
        swipeRefreshLoadMoreLayout.setOnClickLoadMoreListener(new SwipeRefreshLoadMoreLayout.OnClickLoadMoreListener() {
            @Override
            public void onClickLoad() {
                Toast.makeText(MainActivity.this, "被点击了", Toast.LENGTH_SHORT).show();
            }
        });

        list.clear();
        swipeRefreshLoadMoreLayout.setEmptyView(R.layout.empty_layout);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                getDatas();
                swipeRefreshLoadMoreLayout.refreshFinish();
                swipeRefreshLoadMoreLayout.setLoadMore(true);
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        swipeRefreshLoadMoreLayout.startLoadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDatas();
                swipeRefreshLoadMoreLayout.loadMoreFinish();
            }
        }, 2000);
    }

    private List<String> getDatas() {
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
        return list;
    }
}
```