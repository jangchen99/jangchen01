package com.example.hongsapp0229

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.hongsapp0229.model.UserDTO
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null // auth는 login을 관리해주는 것
    var googleSignInClient : GoogleSignInClient? = null

    var firestore : FirebaseFirestore? = null
    var GOOGLE_LOGIN_CODE = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        login_googleSignIn_btn.setOnClickListener {
            googleLogin()
        }
    }

    fun googleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE) // intent 창이 나옴
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // 화면(intent?)이 나왔다가 꺼지고 난 뒤 데이터를 그 모체?에 넘기는 함수
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_LOGIN_CODE) {

            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data) /// 문제
            if(result.isSuccess){

                var account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            }
        }
    }


    fun firebaseAuthWithGoogle(account : GoogleSignInAccount){  //Google에서 firebase로 넘겨주는 코드
        var credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)?.addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                Toast.makeText(this, "ㅁㅁㅁㅁ.", Toast.LENGTH_LONG).show()

                moveMainPage(auth?.currentUser) // 로그인에 성공하면 auth에 회원 정보가 담김
            }else{

                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    fun highlightText(mText: String, hText: String, tv: TextView){
        val ss = SpannableString(mText)
        if(mText.indexOf(hText) > -1) {
            try {
                val start = mText.indexOf(hText)
                val end = start + hText.length
                ss.setSpan(
                    ForegroundColorSpan(Color.parseColor("#ff8c00")),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            catch(e: Exception){}
            tv.text = ss
        }
    }


    fun moveMainPage(user : FirebaseUser?){
        if(user != null){

            Toast.makeText(this, "로그인 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_LONG).show()

            login_black_lo.visibility = View.VISIBLE
            login_black_lo.bringToFront()

            firestore?.collection("USER_INFO")?.get()?.addOnSuccessListener {
                    querySnapshot ->

                var uidArray = ArrayList<String>()
                if(querySnapshot != null){
                    for(snapshot in querySnapshot){
                        try{
                            val uid = snapshot.id
                            uidArray.add(uid)
                        }
                        catch(e: Exception){ }
                    }

                    if(uidArray.contains(user.uid)){

                        login_black_lo.visibility = View.GONE

                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // 뒤로가기 했을 때, 전 페이지로 돌아가지 못 하게 막는 것
                    }
                    else{
                        userData(user)
                    }

                }
            }
        }
    }


    override fun onResume() { // 자동로그인
        super.onResume()

        if(auth?.currentUser != null){
            Toast.makeText(this, "먼데", Toast.LENGTH_LONG).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish() // 뒤로가기 했을 때, 전 페이지로 돌아가지 못 하게 막는 것
        }

    }


    fun userData(user :FirebaseUser?){

        val userEmail = user?.email!!
        val uid = user?.uid!!
        val userData = UserDTO()
        userData.userEmail = userEmail
        firestore?.collection("USER_INFO")?.document(uid)?.set(userData)?.addOnSuccessListener {
            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()

        }
    }
}
