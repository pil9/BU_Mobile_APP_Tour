package bu.mobile.app.tour;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static bu.mobile.app.tour.MypageActivity.fruits;
import static bu.mobile.app.tour.MypageActivity.iimg;
import static bu.mobile.app.tour.MypageActivity.price;

public class JoinActivity extends AppCompatActivity {

    private final String dbName = "tourdb";
    private final String tableName = "member";

    bu.mobile.app.tour.LoginActivity.myDBHelper myHelper;
    SQLiteDatabase sqlDB;//쿼리문 수행용

    EditText idText;
    EditText passText;
    String Id;
    String Pass;

    SQLiteDatabase tourdb;
    String id;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idText = (EditText) findViewById(R.id.idText);
        passText = (EditText) findViewById(R.id.editTextTextPassword);

        myHelper=new bu.mobile.app.tour.LoginActivity.myDBHelper(this);

        fruits.add("각원사 스탬프");
        fruits.add("아라리오갤러리 스탬프");
        fruits.add("독립기념관 스탬프");
        fruits.add("유관순 사열지 스탬프");
        fruits.add("보탑사 스탬프");

        price.add("가맹점 20% 할인");
        price.add("티켓 10% 할인");
        price.add("500원 기프트콘");
        price.add("전통시장 5% 할인");
        price.add("적립 +10%");

        iimg.add("stamp1");
        iimg.add("stamp2");
        iimg.add("stamp3");
        iimg.add("stamp4");
        iimg.add("stamp5");

    }

    private static final String TAG = "JoinActivity";//Log사용을 위해서 로그 태그 설정
    public void members(View view)
    {

        sqlDB=myHelper.getReadableDatabase();
        Cursor cursor;
        cursor=sqlDB.rawQuery("SELECT * FROM "+tableName+" WHERE id='"+idText.getText().toString()+"'   ;",null);//select문 실행
        //가입할려는 아이디가 디비에 이미 존재하는지 select문으로 조회, 조회가 되면 이미 존재하는 아이디이고 없을경우 회원가입 가능
        Log.d(TAG,"회원검색 카운트 "+cursor.getCount());
        if(cursor.getCount() == 0) {//getCount가 0이면 일치하는 정보없음 회원가입 가능
            Log.d(TAG, "아이디 없다 ");
            sqlDB.close();
            cursor.close();

            sqlDB = myHelper.getWritableDatabase();//쓰기전용db열기
            sqlDB.execSQL("INSERT INTO " + tableName + " VALUES(null,'" + idText.getText().toString() + "'," + passText.getText().toString() + ");");
            //insert문으로 회원 추가
            Toast.makeText(getApplicationContext(), "회원가입완료 ", Toast.LENGTH_LONG).show();
            sqlDB.close();
            cursor.close();
            /*로그인페이지로 이동 */
            Intent i1;
            i1 = new Intent(this, bu.mobile.app.tour.LoginActivity.class);
            startActivity(i1);
        }else{//중복되는 아이디가 존재 회원가입 실패
            sqlDB.close();
            cursor.close();
            Toast.makeText(getApplicationContext(),"중복된 아이디가 존재합니다. ",Toast.LENGTH_LONG).show();
        }



    }


    public class myDBHelper extends SQLiteOpenHelper {
        //테이블 생성
        public myDBHelper(Context context){
            super(context, "groupDB",null,1);
        }
        //onCreate()-테이블생성
        //onUpgrade()-테이블 초기화 후 생성


        @Override
        public void onCreate(SQLiteDatabase db) {//쿼리문 수행
            db.execSQL("CREATE TABLE IF NOT EXISTS member "
                    + " (idx INTEGER PRIMARY KEY AUTOINCREMENT , id CHAR(20) , pw CHAR(20) );");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+tableName);//groupTBL존재시 테이블 지움
            onCreate(db);//테이블 다시 생성

        }
    }



}