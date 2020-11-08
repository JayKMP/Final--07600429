package th.ac.su.cp.afinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;

    public class AddDataBase {

    public class AddUserActivity extends AppCompatActivity {
        @Database(entities = {User.class}, exportSchema = false, version = 1)
        public abstract class AppDatabase<RoomDatabase> extends RoomDatabase {
            private static final String TAG = "AppDatabase";
            private static final String DB_NAME = "user.db";

            private static AppDatabase sInstance;

            public abstract UserDao userDao();
            public static synchronized AppDatabase getInstance(final context context) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME
                    ).addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            insertInitialData(context); /*เพิ่มข้อมูลตั้งจ้นไปด้วย จะมีหรือไม่ก็แล้วแต่*/
                        }
                    }).build();
                }
                return sInstance;
            }

            private static void insertInitialData(final Context context) {
                AppExecutors executors = new AppExecutors();
                executors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() { // worker thread
                        AppDatabase db = getInstance(context);
                        db.userDao().addUser(
                                new User(

                                        31,0.6
                                ),
                                new User(

                                        65,0.66
                                )
                        );
                    }
                });
            }
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_user);



            Button saveButton = findViewById(R.id.button2);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id =0;
                    int distance =0  ;
                    double duration =0;

                    final User user = new User(id,distance,duration);
                    EditText Enterdistance;
                    EditText Entertime  ;
                    TextView result ;
                    Enterdistance = findViewById(R.id.distance);
                    String dis = Enterdistance.getText().toString();
                    Entertime = findViewById(R.id.duration);
                    String dura = Entertime.getText().toString();

                    double speed = ((Double.parseDouble(dis) / Double.parseDouble(dura)) * 3600 / 1000);
                    String str = String.format(Locale.getDefault(), "%.2f", speed);
                    result = findViewById(R.id.result);
                    result.setText(str);

                    if(speed > 80){

                    }
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getInstance(AddUserActivity.this);
                            db.userDao().addUser(user);
                            finish();
                        }
                    });
                }
            });
        }
    }

}
