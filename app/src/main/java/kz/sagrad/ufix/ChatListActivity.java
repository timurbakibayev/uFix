package kz.sagrad.ufix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    private static String TAG = "ChatListActivity";
    ArrayList<WChat> chatList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dummyChats();
        RecyclerView r = (RecyclerView)findViewById(R.id.chatListView);
        ChatListAdapter chatListAdapter = new ChatListAdapter(chatList);
        r.setAdapter(chatListAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        r.setLayoutManager(mLayoutManager);
        r.setItemAnimator(new DefaultItemAnimator());
    }

    private void dummyChats() {
        chatList.add(new WChat("Плвыоафдлыдл вы аод выоадл выодла ыв"));
        chatList.add(new WChat("Плвыоафдлыдл алоы рлоа ывы аод выоадл выодла ыв"));
        chatList.add(new WChat("Плвыоафдлыдл вдла лофда ф вы аод выоадл выодла ыв"));
        Log.w(TAG, "dummyChats: added dummy " + chatList.size() + " chats");
    }

}
