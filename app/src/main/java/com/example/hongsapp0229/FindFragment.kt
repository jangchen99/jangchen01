package com.example.hongsapp0229

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_find.view.*


class FindFragment : Fragment(){
       override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
        val fView = inflater.inflate(R.layout.fragment_find, container, false)
       fView!!.library_seat_btn.setOnClickListener {
               try {
                   val uri = Uri.parse("http://seat.ansan.go.kr:8093/EZ5500/SEAT/RoomStatus.aspx")
                   val intent = Intent(Intent.ACTION_VIEW, uri)
                   //intent.setPackage("http://seat.ansan.go.kr:8093/EZ5500/SEAT/RoomStatus.aspx")
                   startActivity(intent)
               } catch (e: ActivityNotFoundException) {
                   //Toast.makeText(context, "인스타그램 앱이 설치되어있어야 합니다.", Toast.LENGTH_LONG).show()
               }
           }
        return fView
    }
}
