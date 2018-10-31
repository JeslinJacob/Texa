package com.texaconnect.texa.ui.intro_pages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.texaconnect.texa.R;
import com.texaconnect.texa.ui.account.LoginActivity;

public class WelcomePage extends AppCompatActivity {

    ViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPager = findViewById(R.id.pager_view);
        mPager.setAdapter(new ViewPagerAdapter());
    }

    public void loginClick(View view){
        Intent i= new Intent(getApplicationContext(),LoginActivity.class);
        i.putExtra("isLogin",true);
        startActivity(i);
    }

    public void signUpClick(View view){
        Intent i= new Intent(getApplicationContext(),LoginActivity.class);
        i.putExtra("isLogin",false);
        startActivity(i);
    }

    class ViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {


            View view=null;
            switch (position){
                case 0:
                    view=view.inflate(WelcomePage.this,R.layout.intro_page_one,null);

                    break;
                case 1:
                    view=view.inflate(WelcomePage.this,R.layout.intro_page_two,null);
//
                    break;
                case 2:
                    view=view.inflate(WelcomePage.this,R.layout.intro_page_three,null);
//                    ImageView next = view.findViewById(R.id.next_icon);
//                    next.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
                    break;
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView((View) object);
        }


    }
}
