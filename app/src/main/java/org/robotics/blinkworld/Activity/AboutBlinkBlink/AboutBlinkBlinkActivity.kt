package org.robotics.blinkworld.Activity.AboutBlinkBlink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_about_blink_blink.*
import me.relex.circleindicator.CircleIndicator3
import org.robotics.blinkworld.Activity.MainActivity
import org.robotics.blinkworld.Activity.RegistrationPhone.LoginRegistrationActivity
import org.robotics.blinkworld.Activity.RegistrationPhone.RegistrationPhoneActivity
import org.robotics.blinkworld.Adapter.ViewPagerAdapter.ViewPagerAdapter
import org.robotics.blinkworld.R

class AboutBlinkBlinkActivity : AppCompatActivity() {
    private var imageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_blink_blink)
        //Glide.with(this).load(R.drawable.blinkblink_about).into(about_gif)
        postToList()
        view_pager.adapter = ViewPagerAdapter(imageList)
        view_pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        var circle = findViewById<CircleIndicator3>(R.id.circle_view_pager)
        circle.setViewPager(view_pager)
        button_login_registr.setOnClickListener {

            if(view_pager.getCurrentItem() < 2){
                view_pager.setCurrentItem(view_pager.getCurrentItem() + 1, true)
            }else{
                finish()
                val intent = Intent(this, LoginRegistrationActivity::class.java)
                startActivities(arrayOf(intent))
            }
        }
    }

    private fun postToList() {
        var a = listOf(R.drawable.about_blinkblink_first,R.drawable.about_blinkblink_second,R.drawable.about_blinkblink_third)
        for(i in a){
            addToList(i)
        }
    }

    private fun addToList(i: Int) {
        imageList.add(i)
    }
}