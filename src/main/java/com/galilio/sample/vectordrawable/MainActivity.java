package com.galilio.sample.vectordrawable;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Animatable;


public class MainActivity extends Activity
{    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.do_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView text = (TextView)findViewById(R.id.text);
                Drawable[] ds = text.getCompoundDrawables();
                if (ds[3] instanceof Animatable) {
                    Animatable a = (Animatable)ds[3];
                    a.start();
                }
            } 
        });
    }
}
