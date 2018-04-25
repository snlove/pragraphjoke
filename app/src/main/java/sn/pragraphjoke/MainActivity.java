package sn.pragraphjoke;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ioc.OnClick;
import ioc.ViewById;
import ioc.ViewUtils;

/**
 * Created by pc on 2018/4/25.
 */

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tv_Test)
    private TextView textView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        textView.setText("what the ioc word");
    }

    @OnClick(values = R.id.tv_Test)
    private void onClick(View view){
        Toast.makeText(getBaseContext(),"weclome to come to annotion workd",Toast.LENGTH_SHORT).show();
    }
}
