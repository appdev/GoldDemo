/*
 * Copyright (C) 2012 www.apkdv.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lengyue.apkdv.golddemo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import lengyue.apkdv.golddemo.flake.FlakeView;


public class MainActivity extends Activity {
    //金币掉落动画的主体动画
    private FlakeView flakeView;
    private Button btnAll, btnthree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flakeView = new FlakeView(this);
        btnAll = (Button) findViewById(R.id.btn_all_time);
        btnthree = (Button) findViewById(R.id.btn_amin);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindows(btnAll, "20", true);
            }
        });
        btnthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindows(btnAll, "20", false);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private PopupWindow pop;

    private PopupWindow showPopWindows(View v, String moneyStr, boolean show) {
        View view = this.getLayoutInflater().inflate(R.layout.view_login_reward, null);
        TextView tvTips = (TextView) view.findViewById(R.id.tv_tip);
        TextView money = (TextView) view.findViewById(R.id.tv_money);
        tvTips.setText("连续登陆3天，送您" + moneyStr + "个爱心币");
        money.setText(moneyStr);
        final LinearLayout container = (LinearLayout) view.findViewById(R.id.container);
        //将flakeView 添加到布局中
        container.addView(flakeView);
        //设置背景
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        //设置同时出现在屏幕上的金币数量  建议64以内 过多会引起卡顿
        flakeView.addFlakes(8);
        /**
         * 绘制的类型
         * @see View.LAYER_TYPE_HARDWARE
         * @see View.LAYER_TYPE_SOFTWARE
         * @see View.LAYER_TYPE_NONE
         */
        flakeView.setLayerType(View.LAYER_TYPE_NONE, null);
        view.findViewById(R.id.btn_ikow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (container!=null){
                    container.removeAllViews();
                }
                pop.dismiss();
            }
        });
        pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.half_color));
        pop.setBackgroundDrawable(dw);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.showAtLocation(v, Gravity.CENTER, 0, 0);

        /**
         * 移除动画
         */
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置2秒后
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            container.removeAllViews();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        if (!show)
            thread.start();
        MediaPlayer player = MediaPlayer.create(this, R.raw.shake);
        player.start();
        return pop;
    }
}
