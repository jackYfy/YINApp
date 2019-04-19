package cn.yin.com.ui;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import butterknife.Bind;
import cn.yin.com.R;
import cn.yin.com.base.BaseActivity;

public class BootMusicdActivity extends BaseActivity implements View.OnClickListener {

@Bind(R.id.music_path)
    AppCompatEditText music_path;
@Bind(R.id.seach_btn)
    AppCompatButton seach_btn;
    @Override
    public int bindLayout() {
        return R.layout.activity_boot_musicd;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("开机音乐");
        init();
    }

    private void init() {
        seach_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.seach_btn:
                startActivity(new Intent(this, MusicPathActivity.class));
                break;
        }
    }
}
