package com.example.hongsapp0229

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hongsapp0229.model.ChatDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat.*
import java.text.SimpleDateFormat
import java.util.*

class ChatgoActivity : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatgo)

        firestore = FirebaseFirestore.getInstance()

        chat_send_btn.setOnClickListener {

            val contents = chat_content_et.text.toString()

                val chatData = ChatDTO()

                val timeStamp = SimpleDateFormat("yyMMddHHmmss").format(Date())
                chatData.date = timeStamp.toLong()
                //chatData.writer = chat_writer_et.text.toString()
                chatData.content = chat_content_et.text.toString()

                // Firebase 데이터베이스로 보내는 코드
                firestore?.collection("CHAT")?.document()?.set(chatData)?.addOnSuccessListener {
                    Toast.makeText(this, "채팅을 생성하였습니다,", Toast.LENGTH_LONG).show()
                    finish()

                }

            }
        }
    }

