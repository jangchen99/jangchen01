package com.example.hongsapp0229

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hongsapp0229.model.NoticeDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_form.*
import java.text.SimpleDateFormat
import java.util.*

class FormActivity : AppCompatActivity() {

    var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        firestore = FirebaseFirestore.getInstance()


        form_addNotice_btn.setOnClickListener {
            val title = form_title_et.text.toString()

            if(title.length >= 2) {
                val noticeData = NoticeDTO()

                val timeStamp = SimpleDateFormat("yyMMddHHmmss").format(Date())
                noticeData.date = timeStamp.toLong()
                noticeData.writer = form_writer_et.text.toString()
                noticeData.title = form_title_et.text.toString()

                // Firebase 데이터베이스로 보내는 코드
                firestore?.collection("NOTICE")?.document()?.set(noticeData)?.addOnSuccessListener {
                    Toast.makeText(this, "글을 생성하였습니다,", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            else{
                Toast.makeText(this, "2글자 이상 입력해주세요.", Toast.LENGTH_LONG).show()

            }
        }



    }
}
