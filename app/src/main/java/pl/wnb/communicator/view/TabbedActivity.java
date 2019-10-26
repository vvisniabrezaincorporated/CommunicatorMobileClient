package pl.wnb.communicator.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pl.wnb.communicator.R;
import pl.wnb.communicator.view.adapter.HomePagerAdapter;
import pl.wnb.communicator.model.util.GlobalUserUtil;
import pl.wnb.communicator.view.fragment.ExportKeyFragment;
import pl.wnb.communicator.view.fragment.GenerateKeyFragment;
import pl.wnb.communicator.view.fragment.ImportKeyFragment;
import pl.wnb.communicator.view.fragment.UsersFragment;

public class TabbedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        GlobalUserUtil.getInstance().setCurrentActivity(this);

        ViewPager viewPager = findViewById(R.id.pager);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());

        homePagerAdapter.addFragment(new UsersFragment(), "users online");
        homePagerAdapter.addFragment(new GenerateKeyFragment(), "generate key");
        homePagerAdapter.addFragment(new ImportKeyFragment(), "import key");
        homePagerAdapter.addFragment(new ExportKeyFragment(), "export key");
        viewPager.setAdapter(homePagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void onResume() {
        super.onResume();
        GlobalUserUtil.getInstance().setCurrentActivity(this);
    }
}